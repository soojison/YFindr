package io.github.soojison.yfindr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    TextInputEditText etEmail;

    @BindView(R.id.etPassword)
    TextInputEditText etPassword;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btnRegister)
    public void registerClick() {
        if(!isFormValid()) {
            return;
        } else {
            showProgressDialog();
            firebaseAuth.createUserWithEmailAndPassword(
                    etEmail.getText().toString(),
                    etPassword.getText().toString()
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    hideProgressDialog();
                    if(task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(
                                getUsernameFromEmail(user.getEmail())).build());
                        // TODO: String extract
                        Toast.makeText(LoginActivity.this, R.string.login_registration_successful,
                                Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            etPassword.setError(e.getLocalizedMessage());
                            etPassword.requestFocus();
                        } catch(FirebaseAuthInvalidCredentialsException |
                                FirebaseAuthUserCollisionException e) {
                            etEmail.setError(e.getLocalizedMessage());
                            etEmail.requestFocus();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // i.e. network is down
                    hideProgressDialog();
                    Toast.makeText(LoginActivity.this,
                            "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.btnLogin)
    public void loginClick() {
        if(!isFormValid()) {
            return;
        }
        showProgressDialog();

        // then you log in
        firebaseAuth.signInWithEmailAndPassword(
                etEmail.getText().toString(),
                etPassword.getText().toString()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            // async shit! wait for DB to respond and exec code when it's done
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
                if(task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Failed: " + task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean isFormValid() {
        if(TextUtils.isEmpty(etEmail.getText().toString())) {
            //TODO: extract string
            etEmail.setError(getString(R.string.login_error_email));
            return false;
        } else if(TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError(getString(R.string.login_error_password));
            return false;
        } else {
            return true;
        }
    }

    // helper method: progress dialog
    private void showProgressDialog() {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.login_error_loading));
        }
        progressDialog.show();
    }

    // code is good if you can read it and understand what it is doing like a sentence
    private void hideProgressDialog() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    private String getUsernameFromEmail(String email) {
        if(email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

}
