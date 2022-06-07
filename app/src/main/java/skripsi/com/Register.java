package skripsi.com;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Register extends AppCompatActivity  {

    Button btn_continue;
    Button btn_add_photo;
    ImageView user_pic;
    EditText xname, xemail,xpassword;
    ProgressBar regBar;

    Uri photo_location;
    Integer photo_max = 1;
    //static int PReqCode = 1 ;

    DatabaseReference reference, reference_username;
    StorageReference storage;

    private FirebaseAuth mAuth;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String name_key = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getUsernameLocal();

        btn_continue = findViewById(R.id.btn_continue);
        xname= findViewById(R.id.xname);
        xemail=findViewById(R.id.xemail);
        xpassword=findViewById(R.id.xpassword);
        user_pic=findViewById(R.id.user_pic);
        btn_add_photo=findViewById(R.id.addfoto);
        regBar=findViewById(R.id.progresbar);
        mAuth = FirebaseAuth.getInstance();
        regBar.setVisibility(View.INVISIBLE);

btn_add_photo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

            findPhoto();
    }
});

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regBar.setVisibility(View.VISIBLE);
                final String email = xemail.getText().toString();
                final String password = xpassword.getText().toString();
                //final String password2 = userPAssword2.getText().toString();
                final String name = xname.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, xname.getText().toString());
                editor.apply();


                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(xname.getText().toString());
                storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(xname.getText().toString());


                if( email.isEmpty() || name.isEmpty() || password.isEmpty()) {
                    // something goes wrong : all fields must be filled
                    // we need to display an error message
                    showMessage("Please Verify all fields") ;
                    regBar.setVisibility(View.INVISIBLE);
                }
                else if (photo_location==null){
                    Toast.makeText(getApplicationContext(),"foto kosong",Toast.LENGTH_SHORT).show();
                    regBar.setVisibility(View.INVISIBLE);
                }else if(photo_location != null){
                    final StorageReference storageReference = storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                    storageReference.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uri_photo = uri.toString();
                                            reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                            reference.getRef().child("name").setValue(xname.getText().toString());
                                            reference.getRef().child("password").setValue(xpassword.getText().toString());
                                            reference.getRef().child("email").setValue(xemail.getText().toString());

                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {

                                            Intent intent = new Intent(Register.this, HomeFragment.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    });
                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                            Intent gotosuccessregister = new Intent(RegisterTwo.this,SuccessRegister.class);
//                            startActivity(gotosuccessregister);
                        }
                    });


                }

            }
        });


    }


//    private void CreateUserAccount(String email, final String name, String password) {
//
//
//        // this method create user account with specific email and password
//
//        mAuth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString(username_key, xname.getText().toString());
//                            editor.apply();
//
//                            reference_username =  FirebaseDatabase.getInstance().getReference()
//                                    .child("Users").child(xname.getText().toString());
//                            reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.exists()){
//                                        Toast.makeText(getApplicationContext(),"username sudah terdaftar",Toast.LENGTH_SHORT).show();
//                                        xname.requestFocus();
//                                        regBar.setVisibility(View.INVISIBLE);
//
//                                    }else if (photo_location==null){
//
//                                        showMessage("foto harap di isi");
//                                        regBar.setVisibility(View.INVISIBLE);
//
//
//                                    }else {
//
//                                        // simpan kepada database
//                                        reference = FirebaseDatabase.getInstance().getReference()
//                                                .child("Users").child(xname.getText().toString());
//                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                dataSnapshot.getRef().child("username").setValue(xname.getText().toString());
//                                                dataSnapshot.getRef().child("password").setValue(xpassword.getText().toString());
//                                                dataSnapshot.getRef().child("email_address").setValue(xemail.getText().toString());
//
////                                    dataSnapshot.getRef().child("user_balance").setValue(800);
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
//
//                                        // user account created successfully
//                                        showMessage("Account created");
//                                        // after we created user account we need to update his profile picture and name
//                                        updateUserInfo( name ,photo_location,mAuth.getCurrentUser());
//
//                                    }
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//
//
//                        }
//                        else
//                        {
//
//                            // account creation failed
//                            showMessage("account creation failed" + task.getException().getMessage());
//                            //regBtn.setVisibility(View.VISIBLE);
//                            regBar.setVisibility(View.INVISIBLE);
//
//                        }
//                    }
//                });
//
//    }

//    // update user photo and name
//    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
//
//        // first we need to upload user photo to firebase storage and get url
//
//        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
//
//        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
//        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                // image uploaded succesfully
//                // now we can get our image url
//
//                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//
//
//                        // uri contain user image url
//
//                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
//                                .setDisplayName(name)
//                                .setPhotoUri(uri)
//                                .build();
//                            //reference.getRef().child("url_photo").setValue(uri_foto);
//                        currentUser.updateProfile(profleUpdate)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//
//                                        if (task.isSuccessful()) {
//                                            // user info updated successfully
//                                            showMessage("Register Complete");
//                                            updateUI();
//                                        }
//
//                                    }
//                                });
//
//                    }
//                });
//
//
//            }
//        });
//
//
//    }




//    String getFileExtension(Uri uri){
//        ContentResolver contentResolver = getContentResolver();
//        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//    }
public void findPhoto(){
    Intent pic = new Intent();
    pic.setType("image/*");
    pic.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(pic, photo_max);
}

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null){
//            photo_location = data.getData();
//            Picasso.with(this).load(photo_location).centerCrop().fit().into(user_pic);
//        }
//    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        name_key = sharedPreferences.getString(username_key, "");

    }
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null){
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(user_pic);
        }
    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}
