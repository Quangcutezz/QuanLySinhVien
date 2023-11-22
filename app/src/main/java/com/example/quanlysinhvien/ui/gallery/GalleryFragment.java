package com.example.quanlysinhvien.ui.gallery;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.quanlysinhvien.ImageHandler;

import com.example.quanlysinhvien.R;
import com.example.quanlysinhvien.databinding.ActivityTemplateAdminBinding;
import com.example.quanlysinhvien.databinding.FragmentGalleryBinding;
import com.google.android.material.navigation.NavigationView;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private Uri imageUri;

    private ImageView imageAva;
    private TextView nameAd;
    private TextView mailAd;
    private NavigationView mNavigationView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}