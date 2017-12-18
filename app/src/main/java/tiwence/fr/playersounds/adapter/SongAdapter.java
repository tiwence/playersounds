package tiwence.fr.playersounds.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tiwence.fr.playersounds.R;
import tiwence.fr.playersounds.model.Song;

/**
 * Created by Tiwence on 15/12/2017.
 */

public class SongAdapter extends BaseAdapter {

    private List<Song> mSongs;
    private LayoutInflater mInflater;
    private Context mContext;

    public SongAdapter(Context context, List<Song> songs) {
        this.mContext = context;
        this.mSongs = songs;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        if (mSongs != null)
            return mSongs.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mSongs != null)
            return mSongs.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_song_item, parent, false
            );
            vh = new ViewHolder();
            vh.mSongNameTextView = (TextView) convertView.findViewById(R.id.songNameTextView);
            vh.mArtistNameTextView = (TextView) convertView.findViewById(R.id.artistNameTextView);
            vh.mArtworkImageView = (ImageView) convertView.findViewById(R.id.artworkImageView);
            convertView.setTag(vh);
        }

        vh = (ViewHolder) convertView.getTag();
        Song currentSong = mSongs.get(position);
        vh.mSongNameTextView.setText(currentSong.getmName());
        vh.mArtistNameTextView.setText(currentSong.getmArtistName());
        Picasso.with(mContext).load(currentSong.getmArtworkUrl()).into(vh.mArtworkImageView);

        return convertView;
    }

    public class ViewHolder {
        ImageView mArtworkImageView;
        TextView mArtistNameTextView;
        TextView mSongNameTextView;
    }
}
