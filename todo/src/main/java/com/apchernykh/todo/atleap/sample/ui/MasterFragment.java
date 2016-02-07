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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apchernykh.todo.atleap.sample.provider.DefaultContract;
import com.blandware.android.atleap.loader.LoaderManagerCreator;
import com.blandware.android.atleap.loader.SimpleCursorRecyclerAdapter;
import com.blandware.android.atleap.loader.SimpleCursorRecyclerAdapterLoadable;
import com.apchernykh.todo.atleap.sample.R;
import com.apchernykh.todo.atleap.sample.network.robospice.ListTodoItemsRequest;
import com.blandware.android.atleap.util.NavUtil;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.squareup.picasso.Picasso;

/**

 */
public class MasterFragment extends BaseFragment {

    private static final String CACHE_KEY_TODO = "todo_cache_key";


    private ListTodoItemsRequest listTodoItemsRequest;


    @SuppressWarnings("unused")
    public static MasterFragment newInstance() {
        return new MasterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_master, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SimpleCursorRecyclerAdapterLoadable adapter = new SimpleCursorRecyclerAdapterLoadable(
                getActivity(),
                DefaultContract.CONTENT_URI_ITEMS,
                new String[] {DefaultContract.Item.TABLE+"."+DefaultContract.Item._ID+" AS "+DefaultContract.Item._ID, DefaultContract.Item.TITLE, DefaultContract.Item.DESCRIPTION, DefaultContract.Item.CREATED_AT, DefaultContract.Item.ICON_URI},
                null, //selection
                null, //selectionArgs
                DefaultContract.Item.PRIORITY + " ASC, "+DefaultContract.Item._ID + " ASC", //sortOrder - we sort by ID as well to provide stable sort results
                R.layout.listitem_todo,
                new String[] {DefaultContract.Item.TITLE, DefaultContract.Item.DESCRIPTION, DefaultContract.Item._ID, DefaultContract.Item.ICON_URI},
                new int[] {R.id.todoTitle, R.id.todoDescription, R.id.todoId, R.id.item_icon}
        );
        new LoaderManagerCreator(this, adapter);

        //noinspection ConstantConditions because we want app to fail early
        RecyclerView recyclerView = (RecyclerView)getView().findViewById(R.id.list_repositories);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setViewBinder(new SimpleCursorRecyclerAdapterLoadable.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (view.getId() == R.id.item_icon) {
                    ImageView imageView = (ImageView) view;
                    String avatarUrl = cursor.getString(i);
                    Picasso.with(getActivity()).load(avatarUrl).into(imageView);
                    return true;
                } else {
                    return false;
                }
            }
        });

        adapter.setOnItemClickListener(new SimpleCursorRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SimpleCursorRecyclerAdapter adapter, View view, int position, long id) {
                Cursor cursor = (Cursor)adapter.getItem(position);
                String itemUuId = cursor.getString(cursor.getColumnIndex(DefaultContract.Item._ID));
                NavUtil.replaceFragment(getActivity(), R.id.container, DetailFragment.newInstance(itemUuId));
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listTodoItemsRequest = new ListTodoItemsRequest();
    }

    @Override
    public void onStart() {
        super.onStart();
        //noinspection unchecked
        executeSpiceRequest(listTodoItemsRequest, CACHE_KEY_TODO, DurationInMillis.ONE_MINUTE);
    }
}
