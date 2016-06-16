package model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

/**
 * Given a image URL, fetch bitmap.
 */
public class GetImageOnline extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String[] params) {
        try {
            URL url = new URL(params[0]);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            return bmp;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;

    }

}