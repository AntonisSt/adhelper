package cellock.com.adhelper.Models.Video;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.concurrent.TimeUnit;

import cellock.com.adhelper.Models.SuperClasses.AdOutput;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AntonisS on 11/2/2017.
 */

public class VideoOutput extends AdOutput {

    private SimpleExoPlayerView videoView;
    private ProgressBar progressBar;
    //private Button closeBtn;

    public VideoOutput(final RelativeLayout videoParent) {
        Looper.prepare();
        videoView = new SimpleExoPlayerView(videoParent.getContext());

        videoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        videoView.setMinimumHeight(320);
        videoView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);


        progressBar = new ProgressBar(videoParent.getContext());
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        progressBar.setIndeterminate(true);

        /*closeBtn = new Button(videoParent.getContext());
        closeBtn.setLayoutParams(new ViewGroup.LayoutParams(96, 96));
        closeBtn.setText("X");
        closeBtn.setGravity(Gravity.CENTER);
        closeBtn.setTextColor(Color.BLACK);
        closeBtn.setBackgroundColor(Color.LTGRAY);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoParent.setVisibility(View.GONE);
            }
        });

        closeBtn.setEnabled(false);
        closeBtn.setVisibility(View.INVISIBLE);*/

        ((Activity)(videoParent.getContext())).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                videoParent.addView(videoView);
                videoParent.addView(progressBar);
                //videoParent.addView(closeBtn);



                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams)progressBar.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                progressBar.setLayoutParams(layoutParams);

                /*RelativeLayout.LayoutParams layoutParamsBtn =
                        (RelativeLayout.LayoutParams)closeBtn.getLayoutParams();
                layoutParamsBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                layoutParamsBtn.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                layoutParamsBtn.setMargins(16, 16, 16, 16);
                closeBtn.setLayoutParams(layoutParamsBtn);*/
            }
        });


    }

    @Override
    public void setResult() {
        try {
            SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(videoView.getContext(), new DefaultTrackSelector());

            videoView.setPlayer(player);

            videoView.setControllerAutoShow(false);
            videoView.setUseController(false);

            Uri uri = Uri.parse(contentUrl);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);

            player.setPlayWhenReady(true);

            player.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch(playbackState) {
                        case ExoPlayer.STATE_READY:
                            progressBar.setVisibility(View.GONE);
                            /*Observable.interval(2, TimeUnit.SECONDS)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<Long>() {
                                        @Override
                                        public void accept(@NonNull Long aLong) throws Exception {
                                            closeBtn.setVisibility(View.VISIBLE);
                                            closeBtn.setEnabled(true);
                                        }
                                    });*/
                            break;
                    }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    Log.d("AdSdk", "Error encountered while fetching video chunks. Stack trace " + error.getStackTrace());
                }

                @Override
                public void onPositionDiscontinuity() {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }
            });

        } catch (Exception e) {
            // TODO: handle exception
            Log.d("AdSdk", "Error encountered while fetching video chunks. Stack trace " + e.getStackTrace());
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }
}
