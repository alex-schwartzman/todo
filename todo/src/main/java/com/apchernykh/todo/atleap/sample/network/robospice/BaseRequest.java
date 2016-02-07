package com.apchernykh.todo.atleap.sample.network.robospice;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

abstract class BaseRequest<T, R> extends RetrofitSpiceRequest<T, R> {
    BaseRequest(Class<T> clazz, Class<R> retrofitedInterfaceClass) {
        super(clazz, retrofitedInterfaceClass);
        this.setRetryPolicy(new NetworkRetryPolicy());
    }
}