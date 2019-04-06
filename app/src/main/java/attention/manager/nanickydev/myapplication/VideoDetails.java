package attention.manager.nanickydev.myapplication;

import android.graphics.Bitmap;

public class VideoDetails {

    public VideoDetails(Bitmap videoThumbNail, String videoPath) {
        this.videoThumbNail = videoThumbNail;
        this.videoPath = videoPath;
    }

    public Bitmap getVideoThumbNail() {
        return videoThumbNail;
    }
    public void setVideoThumbNail(Bitmap imageN) {
        this.videoThumbNail = imageN;
    }


    public String getVideoPath() {
        return videoPath;
    }
    public void setVideoPath(String text) {
        this.videoPath = text;
    }



    private Bitmap videoThumbNail;
    private String videoPath;

}
