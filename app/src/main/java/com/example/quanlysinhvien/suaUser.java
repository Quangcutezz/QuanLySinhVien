package com.example.quanlysinhvien;

import androidx.appcompat.app.AppCompatActivity;
import com.example.quanglong.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class suaUser extends AppCompatActivity {
    private EditText nhapten,nhaptuoi,nhapdt,nhaptt;
    private Button back_button,cancel_button,edit_button;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_user);
        addControls();
        addEvents();
    }
    private void addControls(){
        nhapten = findViewById(R.id.nhapten);
        nhaptuoi = findViewById(R.id.nhaptuoi);
        nhapdt = findViewById(R.id.nhapdt);
        nhaptt = findViewById(R.id.nhaptt);

        back_button = findViewById(R.id.back_button);
        cancel_button = findViewById(R.id.cancel_button);
        edit_button = findViewById(R.id.edit_button);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("USER");
        if(user != null){
            nhapten.setText(user.getName());
            nhaptuoi.setText(user.getAge()+"");
            nhapdt.setText(user.getPhone()+"");
            nhaptt.setText(user.getStatus());
        }
        else {
            Toast.makeText(this,"Loi khi load du lieu",Toast.LENGTH_LONG).show();
        }
    }
    private void addEvents(){
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null){
                    nhapten.setText(user.getName());
                    nhaptuoi.setText(user.getAge()+"");
                    nhapdt.setText(user.getPhone()+"");
                    nhaptt.setText(user.getStatus());
                }
                else {
                    Toast.makeText(getApplicationContext(),"Loi khi load du lieu",Toast.LENGTH_LONG).show();
                }
            }
        });
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = nhapten.getText().toString();
                int tuoi = Integer.parseInt(nhaptuoi.getText().toString());
                int sodt = Integer.parseInt(nhapdt.getText().toString());
                String tt = nhaptt.getText().toString();
                String id = user.getId();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("DBUser");
                myRef.child(id).child("name").setValue(ten);
                myRef.child(id).child("age").setValue(tuoi);
                myRef.child(id).child("phone").setValue(sodt);
                myRef.child(id).child("status").setValue(tt);
                finish();
                Toast.makeText(getApplicationContext(),"Sua thanh cong",Toast.LENGTH_LONG).show();
            }
        });
    }
}