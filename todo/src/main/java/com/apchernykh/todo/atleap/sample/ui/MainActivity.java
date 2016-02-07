/*
 * Copyright (C) 2013 Blandware (http://www.blandware.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apchernykh.todo.atleap.sample.ui;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apchernykh.todo.atleap.sample.Constants;
import com.apchernykh.todo.atleap.sample.auth.AuthActivity;
import com.blandware.android.atleap.auth.AuthHelper;
import com.blandware.android.atleap.util.NavUtil;
import com.squareup.picasso.Picasso;

import roboguice.activity.CustomBaseActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

@SuppressWarnings({"TryWithIdenticalCatches", "SameParameterValue"})
@ContentView(com.apchernykh.todo.atleap.sample.R.layout.activity_main)
public class MainActivity extends CustomBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @SuppressWarnings("unused")
    @InjectView(com.apchernykh.todo.atleap.sample.R.id.nav_view)
    NavigationView mNavigationView;
    @SuppressWarnings("unused")
    @InjectView(com.apchernykh.todo.atleap.sample.R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(com.apchernykh.todo.atleap.sample.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.apchernykh.todo.atleap.sample.R.string.activity_main_drawer_open, com.apchernykh.todo.atleap.sample.R.string.activity_main_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(com.apchernykh.todo.atleap.sample.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sorry, did not implement this!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.apchernykh.todo.atleap.sample.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupNavigationView() {
        Log.d(TAG, "setupNavigationView");

        View view = mNavigationView.getHeaderView(0);

        Account account = AuthHelper.getLastOrFirstAccount(this, Constants.ACCOUNT_TYPE);

        Log.d(TAG, "Account " + account);

        ImageView userAvatarImageView = (ImageView)view.findViewById(com.apchernykh.todo.atleap.sample.R.id.user_avatar);
        Picasso.with(this).load(AuthHelper.getUserData(this, account, AuthActivity.KEY_USER_AVATAR_URL)).into(userAvatarImageView);

        TextView userNameTextView = (TextView)view.findViewById(com.apchernykh.todo.atleap.sample.R.id.user_name);
        userNameTextView.setText(AuthHelper.getUserData(this, account, AuthActivity.KEY_USER_NAME));

        TextView userLoginTextView = (TextView)view.findViewById(com.apchernykh.todo.atleap.sample.R.id.user_login);
        userLoginTextView.setText(account.name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isAuthenticated = AuthHelper.checkLastAccountAndToken(Constants.ACCOUNT_TYPE, Constants.ACCOUNT_TOKEN_TYPE, null, null, this);
        if (isAuthenticated) {
            setupNavigationView();
            replaceFragmentIfNeeded(this, com.apchernykh.todo.atleap.sample.R.id.container, MasterFragment.class);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == com.apchernykh.todo.atleap.sample.R.id.nav_search) {
            replaceFragmentIfNeeded(this, com.apchernykh.todo.atleap.sample.R.id.container, MasterFragment.class);

        } else if (id == com.apchernykh.todo.atleap.sample.R.id.nav_logout) {
            logoutMenuItemClicked();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.apchernykh.todo.atleap.sample.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutMenuItemClicked() {
        AuthHelper.invalidateAuthTokenForLastAccount(Constants.ACCOUNT_TYPE, Constants.ACCOUNT_TOKEN_TYPE, null, null, this);
    }

    private static void replaceFragmentIfNeeded(FragmentActivity activity, int containerId, Class<? extends Fragment> fragmentClazz) {
        if (activity == null) {
            return;
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(containerId);

        if (fragment == null || !fragmentClazz.isAssignableFrom(fragment.getClass())) {
            try {
                fragment = fragmentClazz.newInstance();

                NavUtil.replaceFragment(activity, containerId, fragment);

            } catch (InstantiationException ex) {
                Ln.e(ex, "Cannot create instance of fragment");
            } catch (IllegalAccessException ex) {
                Ln.e(ex, "Cannot create instance of fragment");
            }
        }
    }
}
