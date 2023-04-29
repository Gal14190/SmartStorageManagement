package edu.umich.eecs.april.apriltag.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DownloadImageAsync extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    Context context;

    public DownloadImageAsync(ImageView imageView, Context context) {
        this.imageView = imageView;
        this.context = context;
//        Glide.with(imageView.getContext())
//                .load(R.raw.loading)
//                .into(imageView);
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;
        try {
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://assm-14a68.appspot.com");

            storageRef.child("images/" + url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try {
                        Picasso.get().load(uri).into(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    // Handle any errors
                }
            });
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
      //  imageView.setImageBitmap(result);
    }
}
