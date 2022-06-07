package skripsi.com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    View splash;
    Animation anim;
    ImageButton Start;

    private FirebaseAuth mAuth;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String name_key = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Start = findViewById(R.id.start);
        splash =(View)findViewById(R.id.splash); // Declare an imageView to show the animation.
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in); // Create the animation.

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                startActivity(new Intent( MainActivity.this, Login.class));
//                finish();
                // HomeActivity.class is the activity to go after showing the splash screen.
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        splash.startAnimation(anim);


        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                name_key = sharedPreferences.getString(username_key, "");

                if (name_key.isEmpty()){
                    startActivity(new Intent( MainActivity.this, Login.class));
                    finish();
                }else {
                    startActivity(new Intent( MainActivity.this, HomeFragment.class));
                    finish();
                }


            }
        });
    }


    }


