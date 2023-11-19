package com.example.quanlysinhvien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanglong.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class ThemUser extends AppCompatActivity {
    private EditText nhapten,nhaptuoi,nhapdt,nhaptt;
    private Button back_button,cancel_button,add_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_user);
        addControls();
        addEvents();
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
                nhapten.setText("");
                nhaptuoi.setText("");
                nhapdt.setText("");
                nhaptt.setText("");
            }
        });
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = nhapten.getText().toString();
                int tuoi = Integer.parseInt(nhaptuoi.getText().toString());
                int sodt = Integer.parseInt(nhapdt.getText().toString());
                String tt = nhaptt.getText().toString();
                User user = new User(ten,tuoi,sodt,tt);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("DBUser");

                String id = myRef.push().getKey();
                myRef.child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Toast.makeText(getApplicationContext(),"Them thanh cong !",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Them that bai! "+ e.toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    private void addControls(){
        nhapten = findViewById(R.id.nhapten);
        nhaptuoi = findViewById(R.id.nhaptuoi);
        nhapdt = findViewById(R.id.nhapdt);
        nhaptt = findViewById(R.id.nhaptt);

        back_button = findViewById(R.id.back_button);
        cancel_button = findViewById(R.id.cancel_button);
        add_button = findViewById(R.id.add_button);
    }
}