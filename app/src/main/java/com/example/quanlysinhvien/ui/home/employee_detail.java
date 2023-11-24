package com.example.quanlysinhvien.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.quanglong.model.User;
import com.example.quanlysinhvien.ImageHandler;
import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.databinding.FragmentEmployeeDetailBinding;
import com.example.quanlysinhvien.suaUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class employee_detail extends Fragment {

    private FragmentEmployeeDetailBinding binding;
    private int resource;
    private User user2;
    private Button edit_button;
    private EditText name,age,phone,status;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("DBUser");


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEmployeeDetailBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        editControl();
        showUserInfo();
        //editEvent();
        return root;
    }
    private void editControl(){
        name = binding.ten;
        age = binding.tuoi;
        phone = binding.dt;
        status = binding.tt;
    }
    private void showUserInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            return;
        }
        String uid = user.getUid();
        // Thực hiện truy vấn để đọc thông tin người dùng từ Firebase Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("DBUser").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Nếu thông tin người dùng tồn tại, lấy tên từ cơ sở dữ liệu
                    String nameFromDatabase = dataSnapshot.child("name").getValue(String.class);
                    String ageFromDatabase = String.valueOf(dataSnapshot.child("age").getValue(Integer.class));
                    String phoneFromDatabase = String.valueOf(dataSnapshot.child("phone").getValue(Integer.class));
                    String statusFromDatabase = dataSnapshot.child("status").getValue(String.class);

                    if (nameFromDatabase != null) {
                        // Hiển thị tên từ cơ sở dữ liệu
                        age.setVisibility(View.VISIBLE);
                        age.setText(ageFromDatabase);
                        phone.setVisibility(View.VISIBLE);
                        phone.setText(phoneFromDatabase);
                        status.setVisibility(View.VISIBLE);
                        status.setText(statusFromDatabase);
                        name.setVisibility(View.VISIBLE);
                        name.setText(nameFromDatabase);
                    } else {
                        // Nếu không có tên từ cơ sở dữ liệu, hiển thị tên từ FirebaseUser
                        String nameFromUser = user.getDisplayName();
                        String ageFromUser = String.valueOf(user2.getAge());
                        String phoneFromUser = String.valueOf(user2.getPhone());
                        String statusFromUser = user2.getStatus();
                        if (nameFromUser != null) {
                            name.setVisibility(View.VISIBLE);
                            name.setText(nameFromUser);
                        } else if (ageFromUser != null) {
                            age.setVisibility(View.VISIBLE);
                            age.setText(ageFromUser);
                        }  else if (phoneFromUser != null) {
                            phone.setVisibility(View.VISIBLE);
                            phone.setText(phoneFromUser);
                        } else if (statusFromUser != null) {
                            status.setVisibility(View.VISIBLE);
                            status.setText(statusFromUser);
                        }else {
                            // Nếu cả hai đều không có, ẩn TextView tên
                            name.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi đọc từ cơ sở dữ liệu
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}