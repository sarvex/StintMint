package com.stintmint.depend;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;

/**
 * A base class to wrap communication with the Google Play Services PlusClient.
 */
public abstract class PlusBaseActivity extends AppCompatActivity implements ConnectionCallbacks,
    OnConnectionFailedListener {

  // A magic number we will use to know that our sign-in error resolution activity has completed
  private static final int OUR_REQUEST_CODE = 49404;
  // A flag to track when a connection is already in progress
  public boolean plusClientIsConnecting;
  // A flag to stop multiple dialogues appearing for the user
  private boolean autoResolveOnFail;
  // This is the helper object that connects to Google Play Services.
  private GoogleApiClient googleApiClient;

  // The saved result from {@link #onConnectionFailed(ConnectionResult)}.  If a connection
  // attempt has been made, this is non-null.
  // If this IS null, then the connect method is still running.
  private ConnectionResult connectionResult;

  /**
   * Called when the {@link GoogleApiClient} revokes access to this app.
   */
  protected abstract void onPlusClientRevokeAccess();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initialize the GoogleApiClient connection.
    // Scopes indicate the information about the user your application will be able to access.
    googleApiClient = new Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
        .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE).build();
  }

  @Override
  protected void onStop() {
    super.onStop();
    initiatePlusClientDisconnect();
  }

  /**
   * Disconnect the {@link GoogleApiClient} only if it is connected (otherwise, it can throw an error.)
   * This will call back to {@link #onDisconnected()}.
   */
  private void initiatePlusClientDisconnect() {
    if (googleApiClient.isConnected()) {
      googleApiClient.disconnect();
    }
  }

  /**
   * Try to sign in the user.
   */
  public void signIn() {
    if (!googleApiClient.isConnected()) {
      // Show the dialog as we are now signing in.
      setProgressBarVisible(true);
      // Make sure that we will start the resolution (e.g. fire the intent and pop up a
      // dialog for the user) for any errors that come in.
      autoResolveOnFail = true;
      // We should always have a connection result ready to resolve,
      // so we can start that process.
      if (connectionResult != null) {
        startResolution();
      } else {
        // If we don't have one though, we can start connect in
        // order to retrieve one.
        initiatePlusClientConnect();
      }
    }

    updateConnectButtonState();
  }

  private void setProgressBarVisible(boolean flag) {
    plusClientIsConnecting = flag;
    onPlusClientBlockingUI(flag);
  }

  /**
   * A helper method to flip the mResolveOnFail flag and start the resolution
   * of the ConnectionResult from the failed connect() call.
   */
  private void startResolution() {
    try {
      // Don't start another resolution now until we have a result from the activity we're
      // about to start.
      autoResolveOnFail = false;
      // If we can resolve the error, then call start resolution and pass it an integer tag
      // we can use to track.
      // This means that when we get the onActivityResult callback we'll know it's from
      // being started here.
      connectionResult.startResolutionForResult(this, PlusBaseActivity.OUR_REQUEST_CODE);
    } catch (SendIntentException e) {
      // Any problems, just try to connect() again so we get a new ConnectionResult.
      connectionResult = null;
      initiatePlusClientConnect();
    }
  }

  /**
   * Connect the {@link GoogleApiClient} only if a connection isn't already in progress.  This will
   * call back to {@link #onConnected(Bundle)} or
   * {@link #onConnectionFailed(ConnectionResult)}.
   */
  private void initiatePlusClientConnect() {
    if (!googleApiClient.isConnected() && !googleApiClient.isConnecting()) {
      googleApiClient.connect();
    }
  }

  /**
   * Called when there is a change in connection state.  If you have "Sign in"/ "Connect",
   * "Sign out"/ "Disconnect", or "Revoke access" buttons, this lets you know when their states
   * need to be updated.
   */
  protected abstract void updateConnectButtonState();

  /**
   * Called when the {@link GoogleApiClient} is blocking the UI.  If you have a progress bar widget,
   * this tells you when to show or hide it.
   */
  protected abstract void onPlusClientBlockingUI(boolean show);

  /**
   * Sign out the user (so they can switch to another account).
   */
  public void signOut() {

    // We only want to sign out if we're connected.
    if (googleApiClient.isConnected()) {
      // Clear the default account in order to allow the user to potentially choose a
      // different account from the account chooser.
//TODO      googleApiClient.clearDefaultAccount();

      // Disconnect from Google Play Services, then reconnect in order to restart the
      // process from scratch.
      initiatePlusClientDisconnect();
    }

    updateConnectButtonState();
  }

  /**
   * Revoke Google+ authorization completely.
   */
  public void revokeAccess() {

    if (googleApiClient.isConnected()) {
      // Clear the default account as in the Sign Out.
//TODO      googleApiClient.clearDefaultAccount();

      // Revoke access to this entire application. This will call back to
      // onAccessRevoked when it is complete, as it needs to reach the Google
      // authentication servers to revoke all tokens.
//TODO      googleApiClient.revokeAccessAndDisconnect(new GoogleApiClient.OnAccessRevokedListener() {
//        public void onAccessRevoked(ConnectionResult result) {
//          updateConnectButtonState();
//          onPlusClientRevokeAccess();
//        }
//      });
    }

  }

  public boolean isPlusClientConnecting() {
    return plusClientIsConnecting;
  }

  /**
   * An earlier connection failed, and we're now receiving the result of the resolution attempt
   * by GoogleApiClient.
   *
   * @see #onConnectionFailed(ConnectionResult)
   */
  @Override
  protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
    updateConnectButtonState();
    if (requestCode == PlusBaseActivity.OUR_REQUEST_CODE && responseCode == Activity.RESULT_OK) {
      // If we have a successful result, we will want to be able to resolve any further
      // errors, so turn on resolution with our flag.
      autoResolveOnFail = true;
      // If we have a successful result, let's call connect() again. If there are any more
      // errors to resolve we'll get our onConnectionFailed, but if not,
      // we'll get onConnected.
      initiatePlusClientConnect();
    } else if (requestCode == PlusBaseActivity.OUR_REQUEST_CODE && responseCode != Activity.RESULT_OK) {
      // If we've got an error we can't resolve, we're no longer in the midst of signing
      // in, so we can stop the progress spinner.
      setProgressBarVisible(false);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    initiatePlusClientConnect();
  }

  /**
   * Successfully connected (called by GoogleApiClient)
   */
  @Override
  public void onConnected(Bundle connectionHint) {
    updateConnectButtonState();
    setProgressBarVisible(false);
    onPlusClientSignIn();
  }

  /**
   * Called when the GoogleApiClient is successfully connected.
   */
  protected abstract void onPlusClientSignIn();

  /**
   * Successfully disconnected (called by GoogleApiClient)
   */
//TODO  @Override
  public void onDisconnected() {
    updateConnectButtonState();
    onPlusClientSignOut();
  }

  /**
   * Called when the {@link GoogleApiClient} is disconnected.
   */
  protected abstract void onPlusClientSignOut();

  /**
   * Connection failed for some reason (called by GoogleApiClient)
   * Try and resolve the result.  Failure here is usually not an indication of a serious error,
   * just that the user's input is needed.
   *
   * @see #onActivityResult(int, int, Intent)
   */
  @Override
  public void onConnectionFailed(ConnectionResult result) {
    updateConnectButtonState();

    // Most of the time, the connection will fail with a user resolvable result. We can store
    // that in our connectionResult property ready to be used when the user clicks the
    // sign-in button.
    if (result.hasResolution()) {
      connectionResult = result;
      if (autoResolveOnFail) {
        // This is a local helper function that starts the resolution of the problem,
        // which may be showing the user an account chooser or similar.
        startResolution();
      }
    }
  }

  public GoogleApiClient getPlusClient() {
    return googleApiClient;
  }

}
