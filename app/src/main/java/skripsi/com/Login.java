package skripsi.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
TextView newakun;
EditText xusername, xpasssword;
Button signin;
ProgressBar progressBar;
    DatabaseReference reference, reference_username;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        newakun = findViewById(R.id.newakun);
        xusername= findViewById(R.id.user);
        xpasssword=findViewById(R.id.pass);
        signin = findViewById(R.id.signin);
        progressBar= findViewById(R.id.progresbar);
        mAuth = FirebaseAuth.getInstance();

        progressBar.setVisibility(View.INVISIBLE);

        //findViewById(R.id.signin).setOnClickListener(this);

        newakun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = xusername.getText().toString();
                final String password = xpasssword.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (username.isEmpty()){
                    Toast.makeText(getApplication(),"Username kosong", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }else{
                    if (password.isEmpty()){
                        Toast.makeText(getApplication(),"Password kosong", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else{
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    //cek data password
                                    String datapassword = dataSnapshot.child("password").getValue().toString();

                                    if (password.equals(datapassword)) {

                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, xusername.getText().toString());
                                        editor.apply();

                                        updateUI();
                                        //pindah activity
//                                        Intent gotohome = new Intent(getApplicationContext(), HomeFragment.class);
//                                        startActivity(gotohome);
//                                        finish();



                                    } else {
                                        Toast.makeText(getApplicationContext(), "Password salah", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }


                                }else {
                                    Toast.makeText(getApplicationContext(),"users have not yet registerd",Toast.LENGTH_SHORT).show();

                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Database error!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }

                }

            }
        });
    }

    private void updateUI() {

        Intent homeActivity = new Intent(getApplicationContext(),HomeFragment.class);
        startActivity(homeActivity);
        finish();


    }

}
