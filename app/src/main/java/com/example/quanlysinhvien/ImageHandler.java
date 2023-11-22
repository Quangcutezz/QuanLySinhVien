package com.example.quanlysinhvien;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.google.firebase.storage.FileDownloadTask;
import androidx.annotation.NonNull;
import com.google.firebase.storage.UploadTask;


public class ImageHandler {
    private ProgressDialog progressDialog;
    private StorageReference storageReference;

    public String uploadImage(Context context, Uri imageUri) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);

        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(context, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(context, "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                });

        return fileName;
    }

    public void getImage(String filename, final ImageView imageView) {
        final Bitmap[] bitmap = new Bitmap[1];
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images/" + filename);

        try {
            final File file = File.createTempFile("tempFile", "jpg");
            storageReference.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap[0] = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imageView.setImageBitmap(bitmap[0]);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
