package com.example.quanglong.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.MenuInflater;
import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import com.example.quanglong.model.User;
import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.ThemUser;
import com.example.quanlysinhvien.suaUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManagerAdapter extends ArrayAdapter<User> {
    @NonNull
    private Activity activity;
    private int resource;
    @NonNull
    private List<User> object;
    public ManagerAdapter(@NonNull Activity activity,int resource,@NonNull List<User> object){
        super(activity,resource,object);
        this.activity = activity;
        this.resource = resource;
        this.object = object;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //moi sv tra ve mot view va luu lai position
        LayoutInflater inflater = this.activity.getLayoutInflater();
        View view = inflater.inflate(this.resource,null);
        //Khai bao cac textView
        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtAge = view.findViewById(R.id.txtAge);
        TextView txtPhone = view.findViewById(R.id.txtPhone);
        TextView txtStatus = view.findViewById(R.id.txtStatus);
        TextView txtRole = view.findViewById(R.id.txtRole);
        //lay user dua len textView
        User user = this.object.get(position);
        txtName.setText(user.getName());
        txtPhone.setText(String.valueOf(user.getPhone()));
        txtAge.setText(String.valueOf(user.getAge()));
        txtStatus.setText(user.getStatus());
        txtRole.setText(user.getRole());

        ImageView iconMenu = view.findViewById(R.id.ic_menu);
        iconMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(activity,view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getItemId() == R.id.item_themUser){
                            // Toast.makeText(activity,"Bạn đã nhấn nút Thêm",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(activity, ThemUser.class);
                            activity.startActivity(intent);
                        }
                        else if(menuItem.getItemId() == R.id.item_sua_User){
                            //Toast.makeText(activity,"Bạn đã nhấn nút Sửa: "+user.getName(),Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(activity, suaUser.class);
                            intent.putExtra("USER",user);
                            activity.startActivity(intent);
                        }
                        else if(menuItem.getItemId() == R.id.item_xoa_User){
                            //Toast.makeText(activity,"Bạn đã nhấn nút Xoá: "+user.getName(),Toast.LENGTH_LONG).show();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("DBUser");
                            myRef.child(user.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(activity,"Xoa thanh cong",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        return false;
                    }
                });
                //truyen popup menu de show len
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                //show icon
                try {
                    Field field = popupMenu.getClass().getDeclaredField("mPopUp");
                    field.setAccessible(true);
                    Object popUpMenuHelper = field.get(popupMenu);
                    Class<?> cls = Class.forName("com.android.internal.view.menu.MenuPopupHelper");
                    Method method = cls.getDeclaredMethod("setForceShowIcon",new Class[] {boolean.class});
                    method.setAccessible(true);
                    method.invoke(popUpMenuHelper,new Object[]{true});
                }catch (Exception e)
                {
                    Log.d("MYTAG","onclick: "+ e.toString());
                }
                popupMenu.show();
            }
        });
        return view;
    }

    // Phương thức tìm kiếm theo tên, status và tuổi
    public void filterData(String query) {
        // Tạo danh sách tạm thời để lưu trữ kết quả lọc
        ArrayList<User> filteredList = new ArrayList<>();

        // Thực hiện lọc dữ liệu dựa trên query và thêm vào filteredList
        for (User user : object) {
            if (user.getName().toLowerCase().contains(query.toLowerCase()) ||
                    String.valueOf(user.getAge()).toLowerCase().contains(query.toLowerCase()) ||
                    user.getStatus().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }

        // Xóa sạch dữ liệu hiện tại và thêm vào dữ liệu đã lọc
        this.clear();
        this.addAll(filteredList);

        // Thông báo cho adapter rằng dữ liệu đã thay đổi
        this.notifyDataSetChanged();
    }

    // Tìm kiếm theo tên
//    public void searchByName(String query) {
//        query = query.toLowerCase();
//        ArrayList<User> filteredList = new ArrayList<>();
//
//        for (User user : object) {
//            if (user.getName().toLowerCase().contains(query)) {
//                filteredList.add(user);
//            }
//        }
//
//        this.clear();
//        this.addAll(filteredList);
//        notifyDataSetChanged();
//    }

//    // Tìm kiếm theo status
//    public void searchByStatus(String query) {
//        query = query.toLowerCase();
//        ArrayList<User> filteredList = new ArrayList<>();
//
//        for (User user : object) {
//            if (user.getStatus().toLowerCase().contains(query)) {
//                filteredList.add(user);
//            }
//        }
//
//        this.clear();
//        this.addAll(filteredList);
//        notifyDataSetChanged();
//    }
//
//    // Tìm kiếm theo tuổi
//    public void searchByAge(int age) {
//        ArrayList<User> filteredList = new ArrayList<>();
//
//        for (User user : object) {
//            if (user.getAge() == age) {
//                filteredList.add(user);
//            }
//        }
//        object.clear();
//        object.addAll(filteredList);
//        notifyDataSetChanged();
//    }

    //Sap xep theo ten (name)
    public void sortByName(){
        Collections.sort(object, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        notifyDataSetChanged();
    }

    //Sap xep theo trang thai (status), Normal len tren, Locked xuong duoi
    public void sortByStatus(){
        Collections.sort(object, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if("Normal".equals(o1.getStatus()) && "Locked".equals(o2.getStatus())) {
                    return -1;
                }else if ("Locked".equals(o1.getStatus()) && "Normal".equals(o2.getStatus())){
                    return 1;
                }else {
                    //Neu ca hai deu la Normal thi sap xep theo ten
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            }
        });
        notifyDataSetChanged();
    }
}
