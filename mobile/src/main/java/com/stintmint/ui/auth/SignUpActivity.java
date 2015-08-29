package com.stintmint.ui.auth;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.stintmint.R;
import com.stintmint.ui.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

  @Bind(R.id.username) EditText username;
  @Bind(R.id.password) EditText password;
  @Bind(R.id.email) EditText email;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_sign_up);
    ButterKnife.bind(this);
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_sign_up, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    final int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @OnClick(R.id.button_login_sign_up)
  public void signUpClicked(final View view) {
    final String username = this.username.getText().toString().trim();
    final String password = this.password.getText().toString().trim();
    final String email = this.email.getText().toString().trim();

    if (username.isEmpty()) {
      new Builder(this).setTitle(R.string.error_title).setMessage(R.string.username_cannot_be_empty)
          .setPositiveButton(android.R.string.ok, null).create().show();
    } else if (password.isEmpty()) {
      new Builder(this).setTitle(R.string.error_title).setMessage(R.string.password_cannot_be_empty)
          .setPositiveButton(android.R.string.ok, null).create().show();
    } else if (email.isEmpty()) {
      new Builder(this).setTitle(R.string.error_title).setMessage(R.string.email_cannot_be_empty)
          .setPositiveButton(android.R.string.ok, null).create().show();
    } else {
      setProgressBarIndeterminate(true);

      final ParseUser newUser = new ParseUser();
      newUser.setUsername(username);
      newUser.setPassword(password);
      newUser.setEmail(email);
      newUser.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(final ParseException exception) {
          setProgressBarIndeterminate(false);
          if (exception == null) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
          } else {
            new Builder(SignUpActivity.this).setTitle(R.string.error_title).setMessage(exception.getMessage())
                .setPositiveButton(android.R.string.ok, null).create().show();
          }
        }
      });
    }
  }
}
