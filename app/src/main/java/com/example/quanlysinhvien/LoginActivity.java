package com.example.quanlysinhvien;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;

    private FirebaseAuth mAuth;

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
                    } else {
                        // Đăng nhập thất bại
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole(FirebaseUser user) {
        if (user != null) {
            // Lấy vai trò của người dùng từ cơ sở dữ liệu hoặc từ thông tin người dùng
            String userRole = "manager";  // Ví dụ: bạn cần lấy thông tin này từ cơ sở dữ liệu thực

            // Chuyển hướng dựa trên vai trò
            switch (userRole) {
                case "admin":
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
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
    }
}
