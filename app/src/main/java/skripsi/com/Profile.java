package skripsi.com;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class Profile extends Fragment {

    Button logout;
    DatabaseReference myRef;
    FirebaseUser currentUser;
    TextView username;
    CircleImageView photo;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String name_key = "";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        username = rootView.findViewById(R.id.name);
        photo = rootView.findViewById(R.id.photoo);
        getUsernameLocal();

        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(name_key);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username.setText(dataSnapshot.child("name").getValue().toString());
                Picasso.with(getActivity()).load(dataSnapshot.child("url_photo_profile").getValue().toString()).centerCrop().fit().into(photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Intent logoutt = new Intent(getActivity(), Login.class);
        logout = (Button) rootView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(logoutt);
                getActivity().finish();
            }
        });
        return rootView;
    }

    private void getUsernameLocal() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences (USERNAME_KEY, MODE_PRIVATE);
        name_key = sharedPreferences.getString(username_key, "");

    }
}