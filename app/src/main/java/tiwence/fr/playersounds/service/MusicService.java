package tiwence.fr.playersounds.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import tiwence.fr.playersounds.MediaPlayerActivity;
import tiwence.fr.playersounds.R;
import tiwence.fr.playersounds.listener.OnRetrieveItunesSongPreviewCompleted;
import tiwence.fr.playersounds.listener.OnUpdateSongInformations;
import tiwence.fr.playersounds.model.Song;
import tiwence.fr.playersounds.manager.ApiManager;

/**
 * Created by Tiwence on 17/12/2017.
 */

/**
 * Service used to handle all MediaPlayer behaviours asynchronously
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private ArrayList<Song> mSongs;
    private int mSongPos;
    private OnUpdateSongInformations mOnUpdateSongInformationsListener;

    private Context mContext;

    private final IBinder musicBind = new MusicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSongPos = 0;
        mMediaPlayer = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        mMediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    /**
     * Method used to play selected song according to his streaming URL or get his streaming URL if it's not already set
     */
    public void playSong() {
        final Song playedSong = this.mSongs.get(mSongPos);
        if (playedSong.getmStreamingUrl() == null || "".equals(playedSong.getmStreamingUrl().trim())) {
            ApiManager.instance().retrieveItunesSongPreviewUrl(mContext, playedSong, new OnRetrieveItunesSongPreviewCompleted() {
                @Override
                public void onRetrieveItunesSongPreviewCompleted(final Song song) {
                    playSongFinally(song);
                }
                @Override
                public void onRetrieveItunesSongPreviewError(String message) {
                    Toast.makeText(mContext, mContext.getText(R.string.error_msg), Toast.LENGTH_SHORT);
                }
            });
        } else {
            playSongFinally(playedSong);
        }
    }

    /**
     * Method used to play selected song according to his streaming URL
     */
    private void playSongFinally(Song songToPlay) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(songToPlay.getmStreamingUrl());
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSongs(ArrayList<Song> songs) {
        this.mSongs = songs;
    }

    public void setSongIndex(int index) {
        this.mSongPos = index;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playNext();
    }

    public void playNext() {
        this.mSongPos++;
        if(mSongPos == mSongs.size())
            mSongPos = 0;
        playSong();
        mOnUpdateSongInformationsListener.onUpdateSongInformations(mSongPos);
    }

    public void playPrevious() {
        this.mSongPos--;
        if (mSongPos < 0)
            mSongPos = mSongs.size() - 1;
        playSong();
        mOnUpdateSongInformationsListener.onUpdateSongInformations(mSongPos);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mOnUpdateSongInformationsListener.onMediaPlayerPrepareCompleted();
    }

    public MediaPlayer getmMediaPlayer() {
        return this.mMediaPlayer;
    }

    public void setOnUpdateSongInformationsListener(OnUpdateSongInformations listener) {
        this.mOnUpdateSongInformationsListener = listener;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
