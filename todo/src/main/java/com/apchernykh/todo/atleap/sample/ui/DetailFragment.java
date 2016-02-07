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

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apchernykh.todo.atleap.sample.provider.DefaultContract;
import com.blandware.android.atleap.loader.LoaderManagerCreator;
import com.blandware.android.atleap.loader.ViewLoadable;
import com.blandware.android.atleap.util.ActivityHelper;
import com.squareup.picasso.Picasso;

public class DetailFragment extends BaseFragment {

    private static final String ARG_ITEM_UUID = "ARG_ITEM_UUID";
    private static final String NONEXISTENT = "NONEXISTENT";

    private String mItemUuid;

    public static DetailFragment newInstance(String mItemUuid) {
        DetailFragment fragment =  new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ITEM_UUID, mItemUuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() == null || getArguments().getString(ARG_ITEM_UUID, NONEXISTENT).equals(NONEXISTENT)) {
            throw new IllegalArgumentException("ARG_ITEM_UUID cannot be null");
        }

        mItemUuid = getArguments().getString(ARG_ITEM_UUID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(com.apchernykh.todo.atleap.sample.R.layout.fragment_todo_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewLoadable viewLoadable = new ViewLoadable(
                getActivity(),
                DefaultContract.Item.getItemUri(mItemUuid),
                getView(),
                new String[] {DefaultContract.Item.TITLE, DefaultContract.Item.DESCRIPTION, DefaultContract.Item._ID, DefaultContract.Item.ICON_URI},
                new int[] {com.apchernykh.todo.atleap.sample.R.id.todoTitle, com.apchernykh.todo.atleap.sample.R.id.todoDescription, com.apchernykh.todo.atleap.sample.R.id.todoId, com.apchernykh.todo.atleap.sample.R.id.item_icon}
        ) {
            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                super.onLoadFinished(loader, data);
                initActionBarTitle(data);
            }
        };
        new LoaderManagerCreator(this, viewLoadable);
        viewLoadable.setViewBinder(new ViewLoadable.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == com.apchernykh.todo.atleap.sample.R.id.item_icon) {
                    ImageView imageView = (ImageView) view;
                    String avatarUrl = cursor.getString(columnIndex);
                    Picasso.with(getActivity()).load(avatarUrl).into(imageView);
                    return true;
                } else {
                    return false;
                }            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(com.apchernykh.todo.atleap.sample.R.menu.fragment_detail_menu, menu);
    }

    private void initActionBarTitle(Cursor data) {
        String name = data.getString(data.getColumnIndex(DefaultContract.Item.TITLE));
        ActivityHelper.changeActionBarTitle(getActivity(), name);
    }

}
