package com.apchernykh.todo.atleap.sample.network.retrofit;

import com.apchernykh.todo.atleap.sample.BuildConfig;
import com.apchernykh.todo.atleap.sample.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;

@SuppressWarnings("SameParameterValue")
public class RetrofitHelper {

    public static RestAdapter.Builder createApiGithubRestAdapter(RestAdapter.Builder builder) {
        return createApiTodoRestAdapter(builder)
                .setErrorHandler(new NetworkErrorHandler())
                .setRequestInterceptor(new AuthRequestInterceptor());
    }

    public static RestAdapter.Builder createApiTodoRestAdapter(RestAdapter.Builder builder) {
        return createBaseRestAdapter(builder)
                .setEndpoint(Constants.TODO_API_BASE_URL);
    }

    public static RestAdapter.Builder createTodoRestAdapterForAuth(RestAdapter.Builder builder) {
        return createBaseRestAdapter(builder).setEndpoint(Constants.TODO_API_BASE_URL);
    }

    private static RestAdapter.Builder createBaseRestAdapter(RestAdapter.Builder builder) {
        if (builder == null)
            builder = new RestAdapter.Builder();

        builder.setConverter(new JacksonConverter(new ObjectMapper()));

        if (BuildConfig.DEBUG) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        } else {
            builder.setLogLevel(RestAdapter.LogLevel.NONE);
        }

        return builder;
    }
}
