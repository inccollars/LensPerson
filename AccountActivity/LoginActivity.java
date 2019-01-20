package inc.collars.lensperson.AccountActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import inc.collars.lensperson.CreatePortfolio;
import inc.collars.lensperson.GuestActivity;
import inc.collars.lensperson.MainActivity;
import inc.collars.lensperson.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //defining objects
    private EditText email, password;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button signup, login, reset,guest;
    LoginButton login_button;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //get firebase authentication instance
        auth = FirebaseAuth.getInstance();

        //check if the user is already logged in
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);

        //referencing elements
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        reset = findViewById(R.id.reset);
        guest= findViewById(R.id.guest);
        login_button=findViewById(R.id.login_button);

        //for facebook's login button
        callbackManager = CallbackManager.Factory.create();
        login_button.setReadPermissions(Arrays.asList("email"));
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //setting on click listeners
        login.setOnClickListener(this);
        reset.setOnClickListener(this);
        signup.setOnClickListener(this);
        guest.setOnClickListener(this);
    }

    //setting a function for facebook login
    public void buttonloginFB(View v){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFB(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Facebook Login Cancelled!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
//setting a function to handle the accesstoken for facebook
    private void handleFB(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Login Unsuccessful!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(LoginActivity.this, CreatePortfolio.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        //opeing other activities
        if(view.getId()==R.id.reset){
            Intent intent=new Intent(LoginActivity.this, PasswordReset.class);
            startActivity(intent);
        }
        else if(view.getId()==R.id.signup){
            Intent intent=new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        }

        //checking fields and loging in
        else if(view.getId()==R.id.login){
            String inputemail = email.getText().toString();
            final String inputpassword = password.getText().toString();

            if (TextUtils.isEmpty(inputemail)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(inputpassword)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }
            //making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            //authenticate user
            auth.signInWithEmailAndPassword(inputemail, inputpassword)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 8) {
                                    Toast.makeText(LoginActivity.this,"Password too short",Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
            }
            //login as guest
            else if(view.getId()==R.id.guest){
            Intent intent = new Intent(LoginActivity.this, GuestActivity.class);
            startActivity(intent);
            finish();
        }
        }
//setting callback manager for facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    }
