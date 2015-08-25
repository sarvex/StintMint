package com.stintmint.ui;

import android.R.integer;
import android.R.layout;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.Profile;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.stintmint.R;
import com.stintmint.R.id;
import com.stintmint.R.string;
import com.stintmint.depend.PlusBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends PlusBaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  /**
   * A dummy authentication store containing known user names and passwords.
   * TODO: remove after connecting to a real authentication system.
   */
  private static final String[] DUMMY_CREDENTIALS = {
      "foo@example.com:hello", "bar@example.com:world"
  };
  private LoginActivity.UserLoginTask mAuthTask;

  private AutoCompleteTextView mEmailView;
  private EditText mPasswordView;
  private View mProgressView;
  private View mEmailLoginFormView;
  private SignInButton mPlusSignInButton;
  private View mSignOutButtons;
  private View mLoginFormView;

  @Override
  protected void onPlusClientRevokeAccess() {
    // TODO: Access to the user's G+ account has been revoked.  Per the developer terms, delete
    // any stored user data here.
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    // Find the Google+ sign in button.
    mPlusSignInButton = (SignInButton) findViewById(id.plus_sign_in_button);
    if (supportsGooglePlayServices()) {
      // Set a listener to connect the user when the G+ button is clicked.
      mPlusSignInButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          signIn();
        }
      });
    } else {
      // Don't offer G+ sign in if the app's version is too low to support Google Play
      // Services.
      mPlusSignInButton.setVisibility(View.GONE);
      return;
    }

    // Set up the login form.
    mEmailView = (AutoCompleteTextView) findViewById(id.email);
    populateAutoComplete();

    mPasswordView = (EditText) findViewById(id.password);
    mPasswordView.setOnEditorActionListener(new OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == id.login || id == EditorInfo.IME_NULL) {
          attemptLogin();
          return true;
        }
        return false;
      }
    });

    Button mEmailSignInButton = (Button) findViewById(id.email_sign_in_button);
    mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        attemptLogin();
      }
    });

    mLoginFormView = findViewById(id.login_form);
    mProgressView = findViewById(id.login_progress);
    mEmailLoginFormView = findViewById(id.email_login_form);
    mSignOutButtons = findViewById(id.plus_sign_out_buttons);
  }

  @Override
  protected void updateConnectButtonState() {
    //TODO: Update this logic to also handle the user logged in by email.
    boolean connected = getPlusClient().isConnected();

    mSignOutButtons.setVisibility(connected ? View.VISIBLE : View.GONE);
    mPlusSignInButton.setVisibility(connected ? View.GONE : View.VISIBLE);
    mEmailLoginFormView.setVisibility(connected ? View.GONE : View.VISIBLE);
  }

  @Override
  protected void onPlusClientBlockingUI(boolean show) {
    showProgress(show);
  }

  @Override
  protected void onPlusClientSignIn() {
    //Set up sign out and disconnect buttons.
    Button signOutButton = (Button) findViewById(id.plus_sign_out_button);
    signOutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        signOut();
      }
    });
    Button disconnectButton = (Button) findViewById(id.plus_disconnect_button);
    disconnectButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        revokeAccess();
      }
    });
  }

  @Override
  protected void onPlusClientSignOut() {

  }

  /**
   * Check if the device supports Google Play Services.  It's best
   * practice to check first rather than handling this as an error case.
   *
   * @return whether the device supports Google Play Services
   */
  private boolean supportsGooglePlayServices() {
    return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ==
        ConnectionResult.SUCCESS;
  }

  private void populateAutoComplete() {
    getLoaderManager().initLoader(0, null, this);
  }

  /**
   * Attempts to sign in or register the account specified by the login form.
   * If there are form errors (invalid email, missing fields, etc.), the
   * errors are presented and no actual login attempt is made.
   */
  public void attemptLogin() {
    if (mAuthTask != null) {
      return;
    }

    // Reset errors.
    mEmailView.setError(null);
    mPasswordView.setError(null);

    // Store values at the time of the login attempt.
    String email = mEmailView.getText().toString();
    String password = mPasswordView.getText().toString();

    boolean cancel = false;
    View focusView = null;

    // Check for a valid password, if the user entered one.
    if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
      mPasswordView.setError(getString(string.error_invalid_password));
      focusView = mPasswordView;
      cancel = true;
    }

    // Check for a valid email address.
    if (TextUtils.isEmpty(email)) {
      mEmailView.setError(getString(string.error_field_required));
      focusView = mEmailView;
      cancel = true;
    } else if (!isEmailValid(email)) {
      mEmailView.setError(getString(string.error_invalid_email));
      focusView = mEmailView;
      cancel = true;
    }

    if (cancel) {
      // There was an error; don't attempt login and focus the first
      // form field with an error.
      focusView.requestFocus();
    } else {
      // Show a progress spinner, and kick off a background task to
      // perform the user login attempt.
      showProgress(true);
      mAuthTask = new LoginActivity.UserLoginTask(email, password);
      mAuthTask.execute((Void) null);
    }
  }

  private boolean isPasswordValid(String password) {
    //TODO: Replace this with your own logic
    return password.length() > 4;
  }

  private boolean isEmailValid(String email) {
    //TODO: Replace this with your own logic
    return email.contains("@");
  }

  /**
   * Shows the progress UI and hides the login form.
   */
  @TargetApi(VERSION_CODES.HONEYCOMB_MR2)
  public void showProgress(final boolean show) {
    // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
    // for very easy animations. If available, use these APIs to fade-in
    // the progress spinner.
    if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(integer.config_shortAnimTime);

      mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
      mLoginFormView.animate().setDuration(shortAnimTime).alpha(
          show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
      });

      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
      mProgressView.animate().setDuration(shortAnimTime).alpha(
          show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
      });
    } else {
      // The ViewPropertyAnimator APIs are not available, so simply show
      // and hide the relevant UI components.
      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
      mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    return new CursorLoader(this,
        // Retrieve data rows for the device user's 'profile' contact.
        Uri.withAppendedPath(Profile.CONTENT_URI,
            Data.CONTENT_DIRECTORY), LoginActivity.ProfileQuery.PROJECTION,

        // Select only email addresses.
        Data.MIMETYPE +
            " = ?", new String[]{Email
        .CONTENT_ITEM_TYPE},

        // Show primary email addresses first. Note that there won't be
        // a primary email address if the user hasn't specified one.
        Data.IS_PRIMARY + " DESC");
  }

  @Override
  public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    List<String> emails = new ArrayList<String>();
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      emails.add(cursor.getString(LoginActivity.ProfileQuery.ADDRESS));
      cursor.moveToNext();
    }

    addEmailsToAutoComplete(emails);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> cursorLoader) {

  }

  private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
    //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
    ArrayAdapter<String> adapter =
        new ArrayAdapter<String>(this,
            layout.simple_dropdown_item_1line, emailAddressCollection);

    mEmailView.setAdapter(adapter);
  }

  @Override
  public void onConnectionSuspended(int i) {

  }


  private interface ProfileQuery {
    String[] PROJECTION = {
        Email.ADDRESS,
        Email.IS_PRIMARY,
    };

    int ADDRESS = 0;
    int IS_PRIMARY = 1;
  }

  /**
   * Represents an asynchronous login/registration task used to authenticate
   * the user.
   */
  public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;

    UserLoginTask(String email, String password) {
      mEmail = email;
      mPassword = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      // TODO: attempt authentication against a network service.

      try {
        // Simulate network access.
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        return false;
      }

      for (String credential : LoginActivity.DUMMY_CREDENTIALS) {
        String[] pieces = credential.split(":");
        if (pieces[0].equals(mEmail)) {
          // Account exists, return true if the password matches.
          return pieces[1].equals(mPassword);
        }
      }

      // TODO: register the new account here.
      return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
      mAuthTask = null;
      showProgress(false);

      if (success) {
        finish();
      } else {
        mPasswordView.setError(getString(string.error_incorrect_password));
        mPasswordView.requestFocus();
      }
    }

    @Override
    protected void onCancelled() {
      mAuthTask = null;
      showProgress(false);
    }
  }
}

