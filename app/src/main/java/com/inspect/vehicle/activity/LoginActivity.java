package com.inspect.vehicle.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;

import com.inspect.vehicle.R;
import com.inspect.vehicle.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

    private UserLoginTask mAuthTask = null;

    private View mProgressView;
    @BindView(R.id.account)
    TextInputEditText editAccount;
    @BindView(R.id.password)
    TextInputEditText editPassword;

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public void afterOnCreate() {
        hideActionBar(true);
        mProgressView = findViewById(R.id.login_progress);
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                attemptLogin();
                break;
            case R.id.btn_register:
                startActivityForResultByAct(RegisterActivity.class, IntImpl.REGISTER);
                break;
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        editAccount.setError(null);
        editPassword.setError(null);

        String email = editAccount.getText().toString();
        String password = editPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            editPassword.setError(getString(R.string.error_invalid_password));
            focusView = editPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editAccount.setError(getString(R.string.error_field_required));
            focusView = editAccount;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editAccount.setError(getString(R.string.error_invalid_email));
            focusView = editAccount;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        return email.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                startActivityByAct(MainActivity.class);
                finish();
            } else {
                editPassword.setError(getString(R.string.error_incorrect_password));
                editPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case IntImpl.REGISTER:
                String account = null == data ? "" : data.getStringExtra("account");
                String password = null == data ? "" : data.getStringExtra("password");
                setText(editAccount, replaceNull(account));
                setText(editPassword, replaceNull(password));
        }
    }

    private void setText(TextInputEditText editText, CharSequence sequence) {
        editText.setText(sequence);
        editText.setSelection(sequence.length());
    }
}

