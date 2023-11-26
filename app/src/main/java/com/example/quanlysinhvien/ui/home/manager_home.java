package com.example.quanlysinhvien.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quanglong.adapter.ManagerAdapter;
import com.example.quanglong.adapter.userAdapter;
import com.example.quanglong.model.User;
import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.databinding.FragmentManagerHomeBinding;
import com.google.android.material.search.SearchBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import android.widget.SearchView;
import java.util.List;

public class manager_home extends Fragment {
    private ListView listUserManager;
    private ArrayList<User> userArrayList,temptArrayList,originalUserList;
    private ManagerAdapter adapter2;
    private ManagerAdapter adapter3;
    private FragmentManagerHomeBinding binding;
    private SearchView searchView;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentManagerHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button btnSortByName = binding.button;
        Button btnSortByStatus = binding.button2;
        SearchView searchView = binding.searchView;
        listUserManager = binding.listUser2;
        userArrayList = new ArrayList<>();
        temptArrayList = new ArrayList<>();
        GetData();
        adapter2 = new ManagerAdapter(requireActivity(), R.layout.custom_listview_item_manager,userArrayList);
        adapter3 = new ManagerAdapter(requireActivity(), R.layout.custom_listview_item_manager,userArrayList);
        userArrayList = temptArrayList;
        listUserManager.setAdapter(adapter2);
        btnSortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter2.sortByName();
                adapter2.notifyDataSetChanged();
            }
        });
        btnSortByStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter2.sortByStatus();
                adapter2.notifyDataSetChanged();
            }
        });
        // Lưu trữ danh sách ban đầu
        originalUserList = new ArrayList<>(temptArrayList);

        // Khởi tạo searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Tìm kiếm theo tên
                if (newText.isEmpty() && !searchView.isIconified()) {
                    searchView.setIconified(true);
                    // Cập nhật lại danh sách ListView ban đầu
                    GetData();
                }else {
                    // Tìm kiếm và cập nhật danh sách ListView dựa trên newText
                    adapter2.filterData(newText);
                }
                return true;
            }
        });






        return root;
    }

    public void GetData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DBUser");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Xoa du lieu tren list view va cap nhap lai
                adapter2.clear();

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    //Convert data qua user
                    User user = data.getValue(User.class);
                    if(user!= null && "employee".equals(data.child("role").getValue(String.class))){
                        //them user vao listview
                        user.setId(data.getKey());
                        adapter2.add(user);
                        Log.d("MYTAG","onDataChange: " + user.getName() );
                    }
                }
                Toast.makeText(requireContext(),"Load Data Success",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(),"Load Data Failed",Toast.LENGTH_LONG).show();
                Log.d("MYTAG","onCancelled: "+ databaseError.toString());
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}