package tiwence.fr.playersounds;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tiwence.fr.playersounds.listener.OnUpdateSongInformations;
import tiwence.fr.playersounds.model.Song;
import tiwence.fr.playersounds.service.MusicService;

/**
 * Created by Tiwence on 17/12/2017.
 */

public class MediaPlayerActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, OnUpdateSongInformations {

    private ImageView mArtworkImageView;
    private TextView mArtistNameTextView;
    private TextView mSongNameTextView;
    private TextView mCurrentTimeTextView;
    private TextView mTotalTimeTextView;
    private SeekBar mSongSeekBar;
    private Button mPlayPauseButton;
    private Button mNextButton;
    private Button mPrevButton;

    //private MediaPlayer mMediaplayer;

    private ArrayList<Song> mSongList;
    private int mPosition;
    private Song mCurrentSong;

    private MusicService mMusicService;
    private Intent mPlayIntent;
    private boolean mMusicBound = false;

    private SimpleDateFormat df = new SimpleDateFormat("m:ss");

    /**
     * Handler used to update seekbar according to MediaPlayer progression
     */
    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            String currentTime = df.format(new Date(mMusicService.getmMediaPlayer().getCurrentPosition() > 30000 ? 0 : mMusicService.getmMediaPlayer().getCurrentPosition()));
            mSongSeekBar.setProgress(mMusicService.getmMediaPlayer().getCurrentPosition() > 30000 ? 0 : mMusicService.getmMediaPlayer().getCurrentPosition());
            mCurrentTimeTextView.setText(currentTime);
            mSeekbarUpdateHandler.postDelayed(this, 1000);
        }
    };

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mMusicService = binder.getService();
            mMusicBound = true;

            mMusicService.setmContext(MediaPlayerActivity.this);
            mMusicService.setOnUpdateSongInformationsListener(MediaPlayerActivity.this);
            mMusicService.setSongs(mSongList);
            mMusicService.setSongIndex(mPosition);

            mMusicService.playSong();

            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMusicBound = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.media_player_layout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mSongNameTextView = this.findViewById(R.id.mediaPlayerSongName);
        mArtistNameTextView = this.findViewById(R.id.mediaPlayerArtistName);
        mCurrentTimeTextView = this.findViewById(R.id.mediaPlayerCurrentTime);
        mTotalTimeTextView = this.findViewById(R.id.mediaPlayerTotalTime);
        mArtworkImageView = this.findViewById(R.id.mediaPlayerArtwork);
        mSongSeekBar = this.findViewById(R.id.mediaPlayerSeekBar);
        mPlayPauseButton = this.findViewById(R.id.playPauseButton);
        mNextButton = this.findViewById(R.id.mediaPlayerNextButton);
        mPrevButton = this.findViewById(R.id.mediaPlayerPrevButton);

        mSongSeekBar.setOnSeekBarChangeListener(this);
        mPlayPauseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPrevButton.setOnClickListener(this);

        Intent intent = this.getIntent();
        mSongList = (ArrayList<Song>) intent.getSerializableExtra("songs");
        mPosition = intent.getIntExtra("position", 0);

        displaySongInformations();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(this, MusicService.class);
            getApplicationContext().bindService(mPlayIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(mPlayIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
        getApplicationContext().unbindService(musicConnection);
        mMusicService = null;
    }

    @Override
    protected void onDestroy() {
        getApplicationContext().stopService(mPlayIntent);
        super.onDestroy();
    }

    /**
     * Function used to display current played song to the user
     */
    private void displaySongInformations() {
        Song currentSong = mSongList.get(mPosition);
        mSongNameTextView.setText(currentSong.getmName());
        mArtistNameTextView.setText(currentSong.getmArtistName());
        mCurrentTimeTextView.setText(R.string.placeholder_time);
        mTotalTimeTextView.setText(R.string.placeholder_totaltime);
        Picasso.with(this).load(currentSong.getmArtworkUrl()).placeholder(android.R.drawable.screen_background_dark).into(mArtworkImageView);

        mSongSeekBar.setProgress(0);
        mSongSeekBar.setMax(30000);

        //Long secondDuration = currentSong.getmDuration() / 1000;
        String totalTime = df.format(new Date(mSongSeekBar.getMax()));
        mTotalTimeTextView.setText(totalTime);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            this.mMusicService.getmMediaPlayer().seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playPauseButton:
                if (mMusicService.getmMediaPlayer().isPlaying()) {
                    mMusicService.getmMediaPlayer().pause();
                    mPlayPauseButton.setBackgroundResource(R.mipmap.ic_mediaplayer_play);
                } else {
                    mMusicService.getmMediaPlayer().start();
                    mPlayPauseButton.setBackgroundResource(R.mipmap.ic_mediaplayer_pause);
                }
                break;
            case R.id.mediaPlayerNextButton:
                mPlayPauseButton.setBackgroundResource(R.mipmap.ic_mediaplayer_pause);
                mPlayPauseButton.setEnabled(false);
                mMusicService.playNext();
                break;
            case R.id.mediaPlayerPrevButton:
                mPlayPauseButton.setBackgroundResource(R.mipmap.ic_mediaplayer_pause);
                mPlayPauseButton.setEnabled(false);
                mMusicService.playPrevious();
                break;
            default:
                break;
        }
    }

    @Override
    public void onUpdateSongInformations(int currentSongIndex) {
        mPosition = currentSongIndex;
        displaySongInformations();
    }

    @Override
    public void onMediaPlayerPrepareCompleted() {
        mPlayPauseButton.setEnabled(true);
    }
}
