package com.example.quanlysinhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quanglong.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.quanlysinhvien.databinding.ActivityTemplateAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class template_admin_activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTemplateAdminBinding binding;
    private ImageView imageAva;
    private TextView nameAd;
    private TextView mailAd;
    private NavigationView mNavigationView;
    private Uri imageUri;
    private MenuItem item;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("DBUser");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar2);

        binding = ActivityTemplateAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DrawerLayout drawer = binding.drawerLayout;
        setSupportActionBar(binding.appBarTemplateAdmin.toolbar2);
        initUI();

        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_template_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        showUserInfo();


        imageAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              selectImage();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.template_admin_activity, menu);
        return true;
    }
    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(template_admin_activity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_template_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.sign_out){
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initUI(){
        mNavigationView = binding.navView;
        imageAva = mNavigationView.getHeaderView(0).findViewById(R.id.imageAva);
        nameAd = mNavigationView.getHeaderView(0).findViewById(R.id.nameAdmin);
        mailAd = mNavigationView.getHeaderView(0).findViewById(R.id.textViewMail);
    }
    private void showUserInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            return;
        }
        String uid = user.getUid();

        //String name = user.getDisplayName();


        /*if(name == null){
            nameAd.setVisibility(View.GONE);
        }else{
            nameAd.setVisibility(View.VISIBLE);
            nameAd.setText(name);
        }*/
        // Thực hiện truy vấn để đọc thông tin người dùng từ Firebase Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("DBUser").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Nếu thông tin người dùng tồn tại, lấy tên từ cơ sở dữ liệu
                    String nameFromDatabase = dataSnapshot.child("name").getValue(String.class);

                    if (nameFromDatabase != null) {
                        // Hiển thị tên từ cơ sở dữ liệu
                        nameAd.setVisibility(View.VISIBLE);
                        nameAd.setText(nameFromDatabase);
                    } else {
                        // Nếu không có tên từ cơ sở dữ liệu, hiển thị tên từ FirebaseUser
                        String nameFromUser = user.getDisplayName();
                        if (nameFromUser != null) {
                            nameAd.setVisibility(View.VISIBLE);
                            nameAd.setText(nameFromUser);
                        } else {
                            // Nếu cả hai đều không có, ẩn TextView tên
                            nameAd.setVisibility(View.GONE);
                        }
                    }
                } else {
                    // Nếu không có dữ liệu người dùng trong cơ sở dữ liệu, hiển thị tên từ FirebaseUser
                    String nameFromUser = user.getDisplayName();
                    if (nameFromUser != null) {
                        nameAd.setVisibility(View.VISIBLE);
                        nameAd.setText(nameFromUser);
                    } else {
                        // Nếu cả hai đều không có, ẩn TextView tên
                        nameAd.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi đọc từ cơ sở dữ liệu
            }
        });

        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        mailAd.setText(email);
//        Glide.with(this).load(photoUrl).error(R.drawable.ic_avatar_default).into(imageAva);
        ImageHandler imageHandler = new ImageHandler();
        myRef.child(user.getUid()).child("image").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        try {
                            DataSnapshot dataSnapshot = task.getResult();
                            String image = dataSnapshot.getValue().toString();
                            imageHandler.getImage(image,imageAva);
                            Toast.makeText(getApplicationContext(),image +"",Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"Sai roi",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


    }
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageAva.setImageURI(imageUri);
            ImageHandler imageHandler = new ImageHandler();
            String fileName = imageHandler.uploadImage(this, imageUri);

            myRef.child(user.getUid()).child("image").setValue(fileName).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void avoid) {
                    Toast.makeText(getApplicationContext(),"Them thanh cong !",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Them that bai! "+ e.toString(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}