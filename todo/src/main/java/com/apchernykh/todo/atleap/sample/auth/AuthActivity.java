package com.apchernykh.todo.atleap.sample.auth;

import android.accounts.Account;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apchernykh.todo.atleap.sample.exception.ServerErrorException;
import com.apchernykh.todo.atleap.sample.model.GetAccessTokenResult;
import com.apchernykh.todo.atleap.sample.model.User;
import com.apchernykh.todo.atleap.sample.network.robospice.DefaultSpiceManager;
import com.apchernykh.todo.atleap.sample.network.robospice.GetAccessTokenRequest;
import com.apchernykh.todo.atleap.sample.network.robospice.GetUserRequest;
import com.apchernykh.todo.atleap.sample.network.robospice.NetworkService;
import com.blandware.android.atleap.auth.BaseAuthActivity;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

import org.apache.commons.lang3.exception.ExceptionUtils;

@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class AuthActivity extends BaseAuthActivity {

    private static final String TAG = AuthActivity.class.getSimpleName();

    public static final String KEY_USER_ID = "KEY_USER_ID";
    public static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    public static final String KEY_USER_NAME = "KEY_USER_NAME";
    public static final String KEY_USER_COMPANY = "KEY_USER_COMPANY";
    public static final String KEY_USER_AVATAR_URL = "KEY_USER_AVATAR_URL";

    private final DefaultSpiceManager spiceManager = new DefaultSpiceManager(NetworkService.class);

    private String mAuthToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        super.onCreate(savedInstanceState);

        setContentView(com.apchernykh.todo.atleap.sample.R.layout.activity_auth);

        Log.v(TAG, "Staring authorization page");
    }

    @Override
    public void onBackPressed() {
        //do not allow to go back
        moveTaskToBack(true);
    }

    @Override
    public void onStart() {
        changeProgressBarVisibility(false);
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccessToken("code");
    }

    private void getAccessToken(String code) {
        Log.v(TAG, "Requesting access token");
        GetAccessTokenRequest request = new GetAccessTokenRequest(
                getString(com.apchernykh.todo.atleap.sample.R.string.oauth_client_id),
                getString(com.apchernykh.todo.atleap.sample.R.string.oauth_client_secret),
                code,
                getString(com.apchernykh.todo.atleap.sample.R.string.oauth_client_callback_url)
        );
        executeSpiceRequest(request, null, 0, new GetAccessTokenCallback());
    }

    protected void changeProgressBarVisibility(boolean show) {

        ProgressBar progressBar = (ProgressBar) findViewById(com.apchernykh.todo.atleap.sample.R.id.progress_bar);
        if (progressBar != null) {
            if (show)
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);
        }
    }

    protected DefaultSpiceManager getSpiceManager() {
        return spiceManager;
    }

    protected <T> void executeSpiceRequest(SpiceRequest<T> request, Object requestCacheKey, long cacheExpiryDuration, RequestListener<T> callback) {
        changeProgressBarVisibility(true);
        getSpiceManager().execute(request, requestCacheKey, cacheExpiryDuration, callback);
    }

    private void getCurrentUserProfile(String accessToken) {
        Log.v(TAG, "Requesting user profile information");
        executeSpiceRequest(new GetUserRequest(accessToken), null, 0, new GetCurrentUserProfileCallback());
    }

    private void finishAuth(User user, String authToken) {

        if (TextUtils.isEmpty(authToken)) {
            sendCancelResult();
        } else {
            Log.v(TAG, "Creating user account");
            Bundle options = new Bundle();
            options.putString(KEY_USER_EMAIL, user.email);
            options.putString(KEY_USER_NAME, user.name);
            options.putString(KEY_USER_ID, String.valueOf(user.id));
            options.putString(KEY_USER_COMPANY, user.company);
            options.putString(KEY_USER_AVATAR_URL, user.avatarUrl);

            Account account = updateOrCreateAccount(user.login, null, authToken, options);
            if (account != null) {
                sendSuccessResult(account, authToken);
            }
            Log.v(TAG, "User account created successfully");
        }

    }

    private class GetAccessTokenCallback implements RequestListener<GetAccessTokenResult> {
        @Override
        public void onRequestFailure(SpiceException e) {
            changeProgressBarVisibility(false);
            if (e instanceof NoNetworkException) {
                Toast.makeText(AuthActivity.this, com.apchernykh.todo.atleap.sample.R.string.no_network, Toast.LENGTH_LONG).show();
            } else if (ExceptionUtils.indexOfType(e, ServerErrorException.class) >= 0) {
                Toast.makeText(AuthActivity.this, com.apchernykh.todo.atleap.sample.R.string.activity_auth_cannot_login, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onRequestSuccess(GetAccessTokenResult result) {
            changeProgressBarVisibility(false);

            mAuthToken = result.accessToken;
            Log.v(TAG, "Access token received successfully");
            getCurrentUserProfile(mAuthToken);
        }
    }

    private class GetCurrentUserProfileCallback implements RequestListener<User> {
        @Override
        public void onRequestFailure(SpiceException e) {
            changeProgressBarVisibility(false);
            if (e instanceof NoNetworkException) {
                Toast.makeText(AuthActivity.this, com.apchernykh.todo.atleap.sample.R.string.no_network, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onRequestSuccess(User result) {
            changeProgressBarVisibility(false);
            Log.v(TAG, "User profile received successfully");
            finishAuth(result, mAuthToken);
        }
    }
}
