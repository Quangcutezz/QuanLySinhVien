package com.example.quanlysinhvien;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUtils {

    // Lớp FirebaseUtils
    public static class UserUtils {

        // Hàm để lấy và gán giá trị từ trường "role" vào biến userRole
        public void getUserRole(String userId, final OnGetRoleListener listener) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("DBUser").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Lấy giá trị của trường "role" từ cơ sở dữ liệu
                        String userRole = dataSnapshot.child("role").getValue(String.class);

                        // Gán giá trị vào biến userRole
                        if (listener != null) {
                            listener.onGetRole(userRole);
                        }
                    } else {
                        // Không tìm thấy dữ liệu cho người dùng
                        if (listener != null) {
                            listener.onFailed("Không tìm thấy dữ liệu cho người dùng");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi khi đọc từ cơ sở dữ liệu
                    if (listener != null) {
                        listener.onFailed(databaseError.getMessage());
                    }
                }
            });
        }
    }

    // Interface để lắng nghe sự kiện lấy vai trò
    public interface OnGetRoleListener {
        void onGetRole(String role);

        void onFailed(String message);
    }
}



