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


/**
 * A simple {@link Fragment} subclass.
 */
public class DataLevel_ extends Fragment {
    TabLayout tabLayout;
    FrameLayout fragment;
    private FragmentActivity myContext;
    public DataLevel_() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_level_, container, false);

    }



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragment=view.findViewById(R.id.fragmant);
        tabLayout=view.findViewById(R.id.tabs);

        tabLayout.addTab(tabLayout.newTab().setText("HOURLY"));
        tabLayout.addTab(tabLayout.newTab().setText("DAILY"));
        tabLayout.addTab(tabLayout.newTab().setText("WEEKLY"));

        if(savedInstanceState == null){
            getChildFragmentManager().beginTransaction().replace(R.id.fragmant, new HarianFragment()).commit();

        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment =null;
                switch (tab.getPosition()){
                    case 0:
                        fragment = new HarianFragment();
                        break;
                    case 1:
                        fragment = new MingguanFragment();
                        break;
                    case 2:
                        fragment = new BulananFragment();
                }

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmant,fragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();

                getChildFragmentManager().beginTransaction().replace(R.id.fragmant, fragment).commit();
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
