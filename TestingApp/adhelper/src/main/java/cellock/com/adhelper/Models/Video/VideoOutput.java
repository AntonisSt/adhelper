package cellock.com.adhelper.Models.Video;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import cellock.com.adhelper.Models.SuperClasses.AdOutput;

/**
 * Created by AntonisS on 11/2/2017.
 */

public class VideoOutput extends AdOutput {

    private VideoView videoView;

    public VideoOutput(VideoView videoView) {
        this.videoView = videoView;
    }

    @Override
    public void setResult() {
        try {
            MediaController mediaController = new MediaController(videoView.getContext());
            mediaController.setAnchorView(videoView);
            Uri video = Uri.parse(contentUrl);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.start();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            Log.d("AdSdk", "Error encountered while fetching video chunks. Stack trace " + e.getStackTrace());
        }
    }
}
