package skripsi.com;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import skripsi.com.Fragment_level.DataKeruh_;

public class HomeFragment extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private MenuItem mLogoutMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_fragment);

        bottomNavigationView=findViewById(R.id.bottomView);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmant_container, new Homee()).commit();

        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment=null;

                switch (menuItem.getItemId()){
                    case R.id.Home_:
                        fragment = new Homee();
                        break;
                    case R.id.level_air:
                        fragment = new DataLevel_();
                        break;
                    case R.id.keruh_air:
                        fragment = new LayoutLevel();
                        break;
                    case R.id.akun:
                        fragment = new Profile();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmant_container, fragment).commit();
                return true;

            }
        });
    }


}
