package tiwence.fr.playersounds.listener;

import java.util.ArrayList;

import tiwence.fr.playersounds.model.Song;

/**
 * Created by Tiwence on 14/12/2017.
 */

/**
 * Listener used to notify when a Itunes search request is done
 */
public interface OnRetrieveItunesSearchCompleted {
    
    public void onRetrieveItunesSearchCompleted(ArrayList<Song> songList);
    public void onRetrieveItunesSearchError(String message);

}
