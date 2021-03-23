package com.example.numad21s_firebase_team_puzzlers.services;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

public class EmojiService {
    // TODO: remove hardcoded string names for emoji values
    static String[] emojiNames = new String[]{
            "1-2-wink-emoji-png.png",
            "3-2-love-hearts-eyes-emoji-png.png",
            "36857-1-sad-emoji.png",
            "36860-2-sad-emoji-transparent-image.png",
            "36885-5-sad-emoji-photos.png",
            "36965-5-sad-emoji-file.png",
            "4-2-smiling-face-with-sunglasses-cool-emoji-png.png"
    };

    // Max image size to download
    static long ONE_MEGABYTE = 1024 * 1024;

    public static void getEmojiByID(FirebaseStorage fs, int emojiID, OnSuccessListener<byte[]> onSuccess, OnFailureListener onFail) {
        if (emojiID > 6) {
            onFail.onFailure(new Exception("Invalid ID!"));
            return;
        }

        fs.getReference().child(emojiNames[emojiID]).getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFail);
    }
}

// example code on how to call getEmojiByID

//        EmojiService.getEmojiByID(FirebaseStorage.getInstance(), 0, new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//                ImageView img = findViewById(R.id.testImg);
//                img.setMinimumHeight(bm.getHeight());
//                img.setMinimumWidth(bm.getWidth());
//                img.setImageBitmap(bm);
//
//                System.out.println("[Emoji Output] " + bm.getHeight());
//            }
//        }, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println("[EmojiService] Could not retrieve emoji!");
//                e.printStackTrace();
//            }
//        });
