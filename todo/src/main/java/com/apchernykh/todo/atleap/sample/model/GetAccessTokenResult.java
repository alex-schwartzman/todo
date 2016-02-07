package com.apchernykh.todo.atleap.sample.model;

import android.text.TextUtils;

import com.apchernykh.todo.atleap.sample.exception.ServerErrorException;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class GetAccessTokenResult {

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("scope")
    public String scope;

    @JsonProperty("token_type")
    public String tokenType;

    @JsonProperty("error_description")
    private String errorDescription;

    @JsonProperty("error_uri")
    public String errorUri;

    @JsonProperty("error")
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) throws ServerErrorException {
        this.error = error;

        if (!TextUtils.isEmpty(error)) {
            throw new ServerErrorException(error, errorDescription);
        }
    }
}
