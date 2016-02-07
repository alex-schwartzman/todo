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


import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.apchernykh.todo.atleap.sample.network.robospice.DefaultSpiceManager;
import com.apchernykh.todo.atleap.sample.network.robospice.NetworkService;
import com.blandware.android.atleap.util.ActivityHelper;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

/**

 */
@SuppressWarnings("SameParameterValue")
public abstract class BaseFragment<T> extends Fragment implements RequestListener<T> {

    private final DefaultSpiceManager spiceManager = new DefaultSpiceManager(NetworkService.class);

    @Override
    public void onStart() {
        super.onStart();
        /*
                final ActionBarActivity actionBarActivity = (ActionBarActivity)getActivity();
                if (actionBarActivity != null) {
                    actionBarActivity.setSupportProgressBarIndeterminateVisibility(show);
                }
        */
        spiceManager.start(getActivity());
        ActivityHelper.changeActionBarTitle(getActivity(), null);
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    private SpiceManager getSpiceManager() {
        return spiceManager;
    }

    void executeSpiceRequest(SpiceRequest<T> request, Object requestCacheKey, long cacheExpiryDuration) {
        getSpiceManager().execute(request, requestCacheKey, cacheExpiryDuration, this);
    }

    @Override
    public void onRequestFailure(SpiceException e) {
        /*
                final ActionBarActivity actionBarActivity = (ActionBarActivity)getActivity();
                if (actionBarActivity != null) {
                    actionBarActivity.setSupportProgressBarIndeterminateVisibility(show);
                }
        */
        if (e instanceof NoNetworkException) {
            Toast.makeText(getActivity(), com.apchernykh.todo.atleap.sample.R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestSuccess(Object o) {
    }

}
