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

package com.apchernykh.todo.atleap.sample.network.robospice;

import android.app.Application;

import com.apchernykh.todo.atleap.sample.network.retrofit.TodoApiService;
import com.apchernykh.todo.atleap.sample.provider.DefaultContract;
import com.apchernykh.todo.atleap.sample.provider.DefaultDatabaseHelper;
import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.apchernykh.todo.atleap.sample.network.retrofit.AuthenticationService;
import com.apchernykh.todo.atleap.sample.network.retrofit.RetrofitHelper;
import com.apchernykh.todo.atleap.sample.provider.DefaultUriMatcher;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;

import retrofit.RestAdapter;

/**

 */
public class NetworkService extends BaseNetworkService {

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(AuthenticationService.class, RetrofitHelper.createTodoRestAdapterForAuth(null).build());
        addRetrofitInterface(TodoApiService.class, RetrofitHelper.createApiTodoRestAdapter(null).build());
    }

    protected RestAdapter.Builder createDefaultRestAdapterBuilder() {
        return RetrofitHelper.createApiGithubRestAdapter(null);
    }


    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        OrmLiteUriMatcher matcher = OrmLiteUriMatcher.getInstance(DefaultUriMatcher.class, DefaultContract.CONTENT_AUTHORITY);

        // init
        RoboSpiceDatabaseHelper databaseHelper = new RoboSpiceDatabaseHelper(application, DefaultDatabaseHelper.DATABASE_NAME, DefaultDatabaseHelper.DATABASE_VERSION);
        @SuppressWarnings("unchecked") InDatabaseObjectPersisterFactory inDatabaseObjectPersisterFactory = new InDatabaseObjectPersisterFactory( application, databaseHelper, matcher.getClassToNotificationUriMap() );
        cacheManager.addPersister(inDatabaseObjectPersisterFactory);
        return cacheManager;
    }


}
