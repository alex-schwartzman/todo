package com.apchernykh.todo.atleap.sample.auth;

import android.content.Context;

import com.blandware.android.atleap.auth.BaseAuthActivity;
import com.blandware.android.atleap.auth.BaseAuthenticator;

class UserAuthenticator extends BaseAuthenticator {

    public UserAuthenticator(Context context) {
        super(context);
    }

    @Override
    protected Class<? extends BaseAuthActivity> getAuthActivityClass() {
        return AuthActivity.class;
    }
}
