package com.example.quanlysinhvien.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanglong.adapter.userAdapter;
import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.databinding.FragmentManagerHomeBinding;

import java.util.ArrayList;

public class manager_home extends Fragment {
    private FragmentManagerHomeBinding binding;


    public static manager_home newInstance() {
        return new manager_home();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentManagerHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}