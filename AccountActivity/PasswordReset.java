package inc.collars.lensperson.AccountActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import inc.collars.lensperson.R;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity implements View.OnClickListener {

    //defining objects
    private EditText email;
    private Button reset, back;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        //referencing objects
        email = findViewById(R.id.email);
        reset = (Button) findViewById(R.id.reset);
        back = (Button) findViewById(R.id.back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        reset.setOnClickListener(this);
        back.setOnClickListener(this);

        //getting firebase authentication instance
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.back){
            finish();
        }
        //checking the field and sending email
        else if(view.getId()==R.id.reset){
            String inputemail = email.getText().toString().trim();

            if (TextUtils.isEmpty(inputemail)) {
                Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                return;
            }
//making progressbar visible and making firebase authentication instance to send password reset email
            progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(inputemail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PasswordReset.this, "Email has been sent!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PasswordReset.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }

                            progressBar.setVisibility(View.GONE);
                        }
                    });
            }
        }
    }