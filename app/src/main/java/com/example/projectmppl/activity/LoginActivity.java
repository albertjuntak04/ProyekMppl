package com.example.projectmppl.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projectmppl.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.input_email)
    EditText editTextEmail;
    @BindView(R.id.input_password)
    EditText editTextPassword;

    private FirebaseAuth firebaseAuth;
    private boolean loggedIn;
    private DatabaseReference getReference;

    EditText _passwordText;
    EditText _emailText;
    Button _loginButton;
    Button _registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _passwordText = findViewById(R.id.input_password);
        _emailText = findViewById(R.id.input_email);
        _loginButton = findViewById(R.id.btn_login);
        _registerButton = findViewById(R.id.btn_register);

        initFirebase();

        ButterKnife.bind(this);
//        loggedIn = isLoggedIn();
//        if (loggedIn) {
//            //  go to dashboard
//            goToDashboard();
//        }
        hideProgress();

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                login(username, password);

            }
        });


    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void goToDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void login(final String username, final String password) {
        if (TextUtils.isEmpty(username)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.error_message_username_empty, Snackbar.LENGTH_LONG)
                    .show();
        } else if (TextUtils.isEmpty(password)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.error_message_password_empty, Snackbar.LENGTH_LONG)
                    .show();
        } else {
            //  do login
            showProgress();
            firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideProgress();
                            if (task.isSuccessful()) {
                                //  login sucess
                                //  go to dashboard

                                goToDashboard();
                            } else {
                                //  login failed
                                showMessageBox("Login failed. Your username and password is not matched");
                            }
                        }
                    });

        }
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        editTextEmail.setEnabled(true);
        editTextPassword.setEnabled(true);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        editTextEmail.setEnabled(false);
        editTextPassword.setEnabled(false);
    }

    private void showMessageBox(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Login");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    public boolean isLoggedIn() {
        if (firebaseAuth.getCurrentUser() != null) {
            //  user logged in
            return true;
        } else {
            return false;
        }
    }

}
