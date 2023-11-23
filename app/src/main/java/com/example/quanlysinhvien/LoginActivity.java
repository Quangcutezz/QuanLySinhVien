package com.example.quanlysinhvien;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanglong.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;

    private FirebaseAuth mAuth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.login);

        mAuth =FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }
    private void loginHistory(FirebaseUser user){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference historyRef = database.getReference("DBUser");
        //lay id
        String id = user.getUid();
        //lay time dang nhap hien tai
        String loginTime = getCurrentTimeString();

        //set du lieu loginHistory trong FireBase
        historyRef.child(id).child("loginHistory").setValue(loginTime);
    }
    private String getCurrentTimeString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date(System.currentTimeMillis());
        return dateFormat.format(currentDate);
    }
    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công
                        FirebaseUser user = mAuth.getCurrentUser();
                        checkUserRole(user);
                        loginHistory(user);
                    } else {
                        // Đăng nhập thất bại
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole(FirebaseUser user) {
        if (user != null) {
            // Gọi hàm để lấy và gán giá trị từ trường "role" vào biến userRole
            FirebaseUtils.UserUtils userUtils = new FirebaseUtils.UserUtils();
            userUtils.getUserRole(user.getUid(), new FirebaseUtils.OnGetRoleListener() {
                @Override
                public void onGetRole(String userRole) {
                    // Chuyển hướng dựa trên vai trò
                    switch (userRole) {
                        case "admin":
                            startActivity(new Intent(LoginActivity.this, template_admin_activity.class));
                            break;
                        case "manager":
                            startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
                            break;
                        case "employee":
                            startActivity(new Intent(LoginActivity.this, EmployeeActivity.class));
                            break;
                        default:
                            // Vai trò không xác định hoặc xử lý theo mặc định
                    }
                    finish();
                }

                @Override
                public void onFailed(String message) {
                    // Xử lý lỗi khi đọc từ cơ sở dữ liệu
                    Toast.makeText(getApplicationContext(),"Loi khi doc du lieu" + message,Toast.LENGTH_LONG).show();
                }
            });
        }
    }


}
