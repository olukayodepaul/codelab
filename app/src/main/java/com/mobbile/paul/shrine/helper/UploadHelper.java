package com.mobbile.paul.shrine.helper;

import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class UploadHelper {


    private static volatile UploadHelper uploadHelper;
    private static String TAG = UploadHelper.class.getSimpleName();

    //private constructor.
    private UploadHelper() {

        //Prevent form the reflection api.
        if (uploadHelper != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static UploadHelper getInstance() {
        if (uploadHelper == null) { //if there is no instance available... create new one
            synchronized (UploadHelper.class) {
                if (uploadHelper == null) uploadHelper = new UploadHelper();
            }
        }

        return uploadHelper;
    }

    //Make singleton from serialize and deserialize operation.
    protected UploadHelper readResolve() {
        return getInstance();
    }

    public void uploadImageToStorage(@NonNull Bitmap bitmap, String path, @NonNull final OnUploadListener uploadListener) {

        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference storageReference = storage.getReference().child(path);


        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                uploadListener.onFailure(exception.getLocalizedMessage());

                Log.e(TAG, "onFailure: ", exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...

                Task<Uri> uri = storageReference.getDownloadUrl();

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uploadListener.onSuccess(uri.toString());
                    }
                });


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                uploadListener.onProgress((int) progress);

            }
        });

    }


    public interface OnUploadListener {

        void onFailure(String message);

        void onSuccess(String fileUrl);

        void onProgress(int progress);
    }
}
