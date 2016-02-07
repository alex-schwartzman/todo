package com.apchernykh.todo.atleap.sample.auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UserAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        UserAuthenticator authenticator = new UserAuthenticator(this);
        return authenticator.getIBinder();
    }
}
