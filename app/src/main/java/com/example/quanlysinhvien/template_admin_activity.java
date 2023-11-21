package com.example.quanlysinhvien;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlysinhvien.databinding.ActivityTemplateAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.net.Uri;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseError;

public class template_admin_activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTemplateAdminBinding binding;
    private ImageView imageAva;
    private TextView nameAd;
    private TextView mailAd;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTemplateAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarTemplateAdmin.toolbar);
        initUI();
        DrawerLayout drawer = binding.drawerLayout;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.template_admin_activity, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_template_admin);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
        Glide.with(this).load(photoUrl).error(R.drawable.ic_avatar_default).into(imageAva);

    }
}