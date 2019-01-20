package inc.collars.lensperson.AccountActivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import inc.collars.lensperson.CreatePortfolio;
import inc.collars.lensperson.MainActivity;
import inc.collars.lensperson.R;

public class SignupActivity extends AppCompatActivity  implements View.OnClickListener {

    //defining objects
    private EditText email, password;
    private Button register, reset, login;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //getting firebase reference
        auth = FirebaseAuth.getInstance();

        //referencing elements
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        reset = findViewById(R.id.reset);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);
        register.setOnClickListener(this);
        reset.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //opening new activities
        if (view.getId() == R.id.reset) {
            Intent intent = new Intent(this, PasswordReset.class);
            startActivity(intent);
        } else if (view.getId() == R.id.login) {
            finish();
        } else if (view.getId() == R.id.register) {
            //registering new user
            String inputemail = email.getText().toString().trim();
            String inputpassword = password.getText().toString().trim();
//checks for empty fields
            if (TextUtils.isEmpty(inputemail)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(inputpassword)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (inputpassword.length() < 8) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 8 characters!", Toast.LENGTH_SHORT).show();
                return;
            }
//making progressbar visible
            progressBar.setVisibility(View.VISIBLE);
            //check fields and create user
            auth.createUserWithEmailAndPassword(inputemail, inputpassword)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(SignupActivity.this, "Signed Up Successfully!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Authentication failed",Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(SignupActivity.this, CreatePortfolio.class));
                                finish();
                            }
                        }
                    });

        }

    }
    //making progress bar visibility gone
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}


