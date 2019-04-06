package attention.manager.nanickydev.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceUtil {
    private static String KEY = "videos_key";

    public static String VIDEOS_KEY = "video_paths";
    public static String VIDEOS_THUMBS_KEY = "video_thumbs";
    public static String VIDEOS_SIZE_KEY = "video_size";

    public static List<String> getVideos(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, MODE_PRIVATE);

        int size = getVideosSize(context);
        List<String> result = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            String pathVid = sharedPreferences.getString(String.valueOf(VIDEOS_KEY + i), "default");
            result.add(pathVid);
        }

        return result;
    }

    public static void setVideos(Context context, List<String> data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < data.size(); i++) {
            editor.putString(String.valueOf(VIDEOS_KEY + i), data.get(i));
            editor.commit();
        }
    }

    public static int getVideosSize(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, MODE_PRIVATE);
        return sharedPreferences.getInt(VIDEOS_SIZE_KEY, 0);
    }

    public static void setVideosSize(Context context, int size) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VIDEOS_SIZE_KEY, size);
        editor.commit();
    }

    public static List<Bitmap> getVideosThumbs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, MODE_PRIVATE);

        int bitmapsSize = getVideosSize(context);

        List<Bitmap> result = new ArrayList<Bitmap>(bitmapsSize);
        for (int i = 0; i < bitmapsSize; i++) {

            String temp = sharedPreferences.getString(String.valueOf(VIDEOS_THUMBS_KEY + i), "defaultString");
            try {
                byte[] encodeByte = Base64.decode(temp, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                result.add(bitmap);
            } catch (Exception e) {
                e.getMessage();
            }
        }

        return result;
    }

    public static void setVideosThumbs(Context context, List<Bitmap> data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        ArrayList<Bitmap> bitmaps = new ArrayList<>(data);
        int size = bitmaps.size();
        for (int i = 0; i < size; i++) {
            Bitmap bitmap = bitmaps.get(i);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            editor.putString(String.valueOf(VIDEOS_THUMBS_KEY + i), temp);
            editor.commit();
        }
    }
}
