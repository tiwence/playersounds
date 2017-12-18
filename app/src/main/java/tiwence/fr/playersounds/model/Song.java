package tiwence.fr.playersounds.model;

import java.io.Serializable;

/**
 * Created by Tiwence on 14/12/2017.
 */

public class Song implements Serializable {
    
    private String mId;
    private String mArtistName;
    private String mName;
    private String mArtistId;
    private String mArtistUrl;
    private String mArtworkUrl;
    private String mStreamingUrl;
    private String mCollectionName;
    private String mUrl;
    private Long mDuration;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public void setmArtistName(String mArtistName) {
        this.mArtistName = mArtistName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmArtistId() {
        return mArtistId;
    }

    public void setmArtistId(String mArtistId) {
        this.mArtistId = mArtistId;
    }

    public String getmArtistUrl() {
        return mArtistUrl;
    }

    public void setmArtistUrl(String mArtistUrl) {
        this.mArtistUrl = mArtistUrl;
    }

    public String getmArtworkUrl() {
        return mArtworkUrl;
    }

    public void setmArtworkUrl(String mArtworkUrl) {
        this.mArtworkUrl = mArtworkUrl;
    }

    public String getmStreamingUrl() {
        return mStreamingUrl;
    }

    public void setmStreamingUrl(String mStreamingUrl) {
        this.mStreamingUrl = mStreamingUrl;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmCollectionName() {
        return mCollectionName;
    }

    public void setmCollectionName(String mCollectionName) {
        this.mCollectionName = mCollectionName;
    }

    public Long getmDuration() {
        return mDuration;
    }

    public void setmDuration(Long mDuration) {
        this.mDuration = mDuration;
    }
}
