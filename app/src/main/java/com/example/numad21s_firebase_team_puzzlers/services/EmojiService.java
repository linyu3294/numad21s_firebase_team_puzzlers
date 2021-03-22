package com.example.numad21s_firebase_team_puzzlers.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EmojiService {
    // Max image size
    static long ONE_MEGABYTE = 1024 * 1024;

    // TODO: hard-coded URL
    public static List<Bitmap> getAllEmoji() {
        List<Bitmap> outputBitmaps = new ArrayList<>();

        FirebaseStorage
                .getInstance("gs://numad21s.appspot.com")
                .getReference()
                .listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            item.getBytes(ONE_MEGABYTE)
                                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            outputBitmaps.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            System.out.println("[EmojiService] Could not retrieve emoji!");
                                            exception.printStackTrace();
                                        }
                                    });
                        }
                    }
                });

        return outputBitmaps;
    }
}
