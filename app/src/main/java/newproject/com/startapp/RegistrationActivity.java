package newproject.com.startapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegistrationActivity extends AppCompatActivity {
    private EditText name,password;
    private Button login,nav;
    private TextView register;
    public ProgressBar progressBar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth=FirebaseAuth.getInstance();
        setvaluesUI();
        nav=(Button)findViewById(R.id.btn);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent my=new Intent(getApplicationContext(),SpeechActivity.class);
                RegistrationActivity.this.startActivity(my);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                RegistrationActivity.this.startActivity(myIntent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }
    public void setvaluesUI()
    {
        name=(EditText)findViewById(R.id.etName);
        password=(EditText)findViewById(R.id.Password);
        login=(Button)findViewById(R.id.btnLogin);
        register=(TextView)findViewById(R.id.tvRegister);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
    }

    public Boolean validate()
        {
            Boolean result=true;
            String Name,pass;
            Name=name.getText().toString();
            pass=password.getText().toString();
            if(Name.isEmpty())
            {
                result=false;
                name.setError("enter registered email");
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(Name).matches())
            {
                result=false;
                name.setError("ivalid email pattern");
            }
            if(pass.isEmpty())
            {
                result=false;
                password.setError("Passsword neeeded");
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(Name,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Loggged in successfully",Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(RegistrationActivity.this,ProfileActivity.class);
                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"uytfghhyutyrfg",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            return result;
        }
}
