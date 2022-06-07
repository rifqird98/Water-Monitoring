package skripsi.com;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;

import skripsi.com.Fragmant_Keruh.BulananFragment;
import skripsi.com.Fragmant_Keruh.HarianFragment;
import skripsi.com.Fragmant_Keruh.MingguanFragment;
import skripsi.com.Fragment_level.DataKeruh_;
import skripsi.com.Fragment_level.HarianLevel;

public class LayoutLevel extends Fragment {
    TabLayout tabLayout;
    FrameLayout fragment;
    private FragmentActivity myContext;
    public LayoutLevel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_layout_level, container, false);

    }



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragment=view.findViewById(R.id.fragmant_level);
        tabLayout=view.findViewById(R.id.tabs_level);

        tabLayout.addTab(tabLayout.newTab().setText("HOURLY"));
        tabLayout.addTab(tabLayout.newTab().setText("DAILY"));

        if(savedInstanceState == null){
            getChildFragmentManager().beginTransaction().replace(R.id.fragmant_level, new DataKeruh_()).commit();

        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment =null;
                switch (tab.getPosition()){
                    case 0:
                        fragment = new DataKeruh_();
                        break;
                    case 1:
                        fragment = new HarianLevel();

                }

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmant_level,fragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

                getChildFragmentManager().beginTransaction().replace(R.id.fragmant_level, fragment).commit();
                return;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//

    }
}