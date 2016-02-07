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

import com.octo.android.robospice.retrofit.RetrofitJackson2SpiceService;

import java.util.HashMap;
import java.util.Map;

import retrofit.RestAdapter;

/**

 */
public abstract class BaseNetworkService extends RetrofitJackson2SpiceService {

    private Map<Class<?>, RestAdapter> mRetrofitInterfaceToRestAdapter;
    private final Map<Class<?>, Object> mRetrofitInterfaceToServiceMap = new HashMap<>();

    private RestAdapter mDefaultRestAdapter;

    @Override
    protected String getServerUrl() {
        //because we create RestAdapter ourself
        return null;
    }

    /**
     * Create default restAdapter
     * @return rest adapter
     */
    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        RestAdapter.Builder builder = createDefaultRestAdapterBuilder();
        mDefaultRestAdapter = builder.build();
        return builder;
    }

    protected abstract RestAdapter.Builder createDefaultRestAdapterBuilder();

    void addRetrofitInterface(Class<?> serviceClass, RestAdapter restAdapter) {
        if(mRetrofitInterfaceToRestAdapter == null)
            mRetrofitInterfaceToRestAdapter = new HashMap<>();
        mRetrofitInterfaceToRestAdapter.put(serviceClass, restAdapter);

    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T getRetrofitService(Class<T> serviceClass) {
        T service = (T) mRetrofitInterfaceToServiceMap.get(serviceClass);
        if (service == null) {
            service = createRetrofitService(serviceClass);
            mRetrofitInterfaceToServiceMap.put(serviceClass, service);
        }
        return service;
    }

    private <T> T createRetrofitService(Class<T> serviceClass) {
        if (mRetrofitInterfaceToRestAdapter != null && mRetrofitInterfaceToRestAdapter.containsKey(serviceClass)) {
            RestAdapter restAdapter = mRetrofitInterfaceToRestAdapter.get(serviceClass);
            return restAdapter.create(serviceClass);
        } else {
            return mDefaultRestAdapter.create(serviceClass);
        }
    }
}
