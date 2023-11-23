package com.example.quanglong.adapter;
import com.example.quanglong.model.User;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlysinhvien.R;

import java.util.List;

public class LoginHistoryAdapter extends ArrayAdapter<User> {

    @NonNull
    private Activity activity;
    private int resource;
    @NonNull
    private List<User> object;

    public LoginHistoryAdapter(@NonNull Activity activity,int resource,@NonNull List<User> object) {
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
        View view = inflater.inflate(this.resource, null);
        //Khai bao cac textView
        TextView txtUsername = view.findViewById(R.id.txtUsername);
        TextView txtLoginTime = view.findViewById(R.id.txtLoginTime);
        //lay user dua len textView
        User user = this.object.get(position);
        txtUsername.setText(user.getName());
        txtLoginTime.setText(user.getLoginHistory());

        return view;
    }
}