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
    private final String firebase_url = "gs://assm-14a68.appspot.com";
    private final String firebase_image_path = "images/";
    ImageView imageView;
    Context context;

    public DownloadImageAsync(ImageView imageView, Context context) {
        this.imageView = imageView;
        this.context = context;
    }

    /**
     * Download image from firebase storage
     * @param urls
     * @return
     */
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;
        try {
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(firebase_url);

            storageRef.child(firebase_image_path + url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
