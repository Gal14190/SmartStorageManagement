package edu.umich.eecs.april.apriltag;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageAsync extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;

    public DownloadImageAsync(ImageView imageView) {
        this.imageView = imageView;
//        Glide.with(imageView.getContext())
//                .load(R.raw.loading)
//                .into(imageView);
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
