package roboguice.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.blandware.android.atleap.BaseApplication;
import com.google.inject.Inject;
import com.google.inject.Key;

import java.util.HashMap;
import java.util.Map;

import roboguice.RoboGuice;
import roboguice.activity.event.OnActivityResultEvent;
import roboguice.activity.event.OnContentChangedEvent;
import roboguice.activity.event.OnNewIntentEvent;
import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnRestartEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.activity.event.OnSaveInstanceStateEvent;
import roboguice.activity.event.OnStopEvent;
import roboguice.context.event.OnConfigurationChangedEvent;
import roboguice.context.event.OnCreateEvent;
import roboguice.context.event.OnDestroyEvent;
import roboguice.context.event.OnStartEvent;
import roboguice.event.EventManager;
import roboguice.inject.ContentViewListener;
import roboguice.inject.RoboInjector;
import roboguice.util.Ln;
import roboguice.util.RoboContext;

/**
 * TODO RoboGuice has some methods as package access, thats why the ugly package
 */
@SuppressLint("Registered")
public class CustomBaseActivity extends AppCompatActivity implements RoboContext {
    private EventManager eventManager;
    private final HashMap<Key<?>, Object> scopedObjects = new HashMap<>();

    @SuppressWarnings("unused")
    @Inject
    ContentViewListener ignored; // BUG find a better place to put this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final RoboInjector injector = RoboGuice.getInjector(this);
        eventManager = injector.getInstance(EventManager.class);
        injector.injectMembersWithoutViews(this);
        super.onCreate(savedInstanceState);
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication) getApplication()).dispatchActivityCreatedSupport(this, savedInstanceState);
        }
        eventManager.fire(new OnCreateEvent<Activity>(this, savedInstanceState));
        Ln.d(this.getClass().getName() + " onCreate");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication) getApplication()).dispatchActivitySaveInstanceStateSupport(this, outState);
        }
        eventManager.fire(new OnSaveInstanceStateEvent(this, outState));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        eventManager.fire(new OnRestartEvent(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication) getApplication()).dispatchActivityStartedSupport(this);
        }
        eventManager.fire(new OnStartEvent<Activity>(this));
        Ln.d(this.getClass().getName() + " onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication) getApplication()).dispatchActivityResumedSupport(this);
        }
        eventManager.fire(new OnResumeEvent(this));
        Ln.d(this.getClass().getName() + " onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getApplication() instanceof BaseApplication) {
            ((BaseApplication) getApplication()).dispatchActivityPausedSupport(this);
        }
        eventManager.fire(new OnPauseEvent(this));
        Ln.d(this.getClass().getName() + " onPause");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        eventManager.fire(new OnNewIntentEvent(this));
    }

    @Override
    protected void onStop() {
        try {
            eventManager.fire(new OnStopEvent(this));
            Ln.d(this.getClass().getName() + " onStop");
        } finally {
            super.onStop();
            if (getApplication() instanceof BaseApplication) {
                ((BaseApplication) getApplication()).dispatchActivityStoppedSupport(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        try {
            eventManager.fire(new OnDestroyEvent<Activity>(this));
            Ln.d(this.getClass().getName() + " onDestroy");
        } finally {
            try {
                RoboGuice.destroyInjector(this);
            } finally {
                super.onDestroy();
                if (getApplication() instanceof BaseApplication) {
                    ((BaseApplication) getApplication()).dispatchActivityDestroyedSupport(this);
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        final Configuration currentConfig = getResources().getConfiguration();
        super.onConfigurationChanged(newConfig);
        eventManager.fire(new OnConfigurationChangedEvent<Activity>(this, currentConfig, newConfig));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onSupportContentChanged() {
        super.onSupportContentChanged();
        RoboGuice.getInjector(this).injectViewMembers(this);
        eventManager.fire(new OnContentChangedEvent(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        eventManager.fire(new OnActivityResultEvent(this, requestCode, resultCode, data));
    }

    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return scopedObjects;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        if (RoboActivity.shouldInjectOnCreateView(name))
            return RoboActivity.injectOnCreateView(name, context, attrs);

        return super.onCreateView(name, context, attrs);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (RoboActivity.shouldInjectOnCreateView(name))
            return RoboActivity.injectOnCreateView(name, context, attrs);

        return super.onCreateView(parent, name, context, attrs);
    }
}

