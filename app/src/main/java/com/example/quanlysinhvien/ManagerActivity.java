package com.example.quanlysinhvien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quanglong.adapter.userAdapter;
import com.example.quanglong.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerActivity extends AppCompatActivity {

    private ListView listUser;
    private ArrayList<User> userArrayList;
    private userAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        listUser = findViewById(R.id.listUser);
        userArrayList = new ArrayList<>();
        GetData();

        adapter = new userAdapter(this,R.layout.custom_listview_item,userArrayList);
        listUser.setAdapter(adapter);
    }
    //Lay danh sach sinh vien
    private void GetData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DBUser");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Xoa du lieu tren list view va cap nhap lai
                adapter.clear();

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    //Convert data qua user
                    User user = data.getValue(User.class);
                    if(user!= null){
                        //them user vao listview
                        user.setId(data.getKey());
                        adapter.add(user);
                        Log.d("MYTAG","onDataChange: " + user.getName() );
                    }
                }
                Toast.makeText(getApplicationContext(),"Load Data Success",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Load Data Failed",Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCancelled: "+ databaseError.toString());
            }
        });
    }
}