package com.apchernykh.todo.atleap.sample.network.robospice;

import android.util.Log;

import com.apchernykh.todo.atleap.sample.BuildConfig;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;

import roboguice.util.temp.Ln;

public class DefaultSpiceManager extends SpiceManager {
    public DefaultSpiceManager(Class<? extends SpiceService> spiceServiceClass) {
        super(spiceServiceClass);
        if(BuildConfig.DEBUG) {
            Ln.getConfig().setLoggingLevel(Log.VERBOSE);
        } else {
            Ln.getConfig().setLoggingLevel(Log.ERROR);
        }
    }
}
