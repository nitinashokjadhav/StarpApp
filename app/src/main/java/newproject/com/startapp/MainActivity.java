package newproject.com.startapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {
    //private DatabaseReference mDatabase;
    private EditText UserEmail, UserName, UserPassword;
    private Button regButton;
    private TextView userLogin;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        //mDatabase = FirebaseDatabase.getInstance().getReference();
      //  mDatabase.keepSynced(true);
        setupUIViews();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

    public void setupUIViews() {
        UserName = (EditText) findViewById(R.id.etUserName);
        UserEmail = (EditText) findViewById(R.id.etEmail);
        UserPassword = (EditText) findViewById(R.id.etPassword);
       // Password2 = (EditText) findViewById(R.id.etPassword2);
        regButton = (Button) findViewById(R.id.btnRegister);
        userLogin = (TextView) findViewById(R.id.tvLogin);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
    }

    public void registerUser() {
        //Boolean test=true;
        String Name = UserName.getText().toString();
        String Password = UserPassword.getText().toString();
        String Email = UserEmail.getText().toString();
        if (Name.isEmpty()) {
            UserName.setError("Name is required");
          //  test=false;
        }
        else if (Email.isEmpty()) {
            UserEmail.setError("Email is required");
            UserEmail.requestFocus();
            //test=false;
            //Toast.makeText(this, "IT is working", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            UserEmail.setError("Enter valid email");
            //test=false;
        }
        else if (Password.isEmpty()) {
            UserPassword.setError("Password is required");
            UserPassword.requestFocus();
        }
        else if (Password.length() < 6) {
            UserPassword.setError("Password should contain 6 characters");
            UserPassword.requestFocus();
        }
        else {

            if (sendEmailVerificstion()) {
                mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Registration success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user.isEmailVerified()) {
                                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                                MainActivity.this.startActivity(intent);
                            }
                            //finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "Some error occured", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }
    }

    public Boolean sendEmailVerificstion() {
        Boolean valid = false;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressBar.setVisibility(View.VISIBLE);
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "check email for verification ", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        //finish();
                    }

                }
            });
            if (user.isEmailVerified())
            {
                valid=true;
            }

        }
        return valid;
    }
}