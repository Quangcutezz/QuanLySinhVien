package com.example.quanlysinhvien.ui.slideshow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.quanglong.adapter.LoginHistoryAdapter;
import com.example.quanglong.adapter.userAdapter;
import com.example.quanglong.model.User;
import com.example.quanlysinhvien.LoginActivity;
import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.databinding.FragmentSlideshowBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SlideshowFragment extends Fragment {

    private ListView listlogin;
    private ArrayList<User> userArrayList;
    private LoginHistoryAdapter adapter;
    private FragmentSlideshowBinding binding;
    private FirebaseDatabase database;
    private LoginHistoryAdapter loginHistoryAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listlogin = binding.listlogin;
        userArrayList = new ArrayList<>();
        GetData();

        adapter = new LoginHistoryAdapter(requireActivity(), R.layout.list_item_history,userArrayList);
        listlogin.setAdapter(adapter);

        return root;
    }

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