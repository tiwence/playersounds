package tiwence.fr.playersounds.listener;

import java.util.ArrayList;

import tiwence.fr.playersounds.model.Song;

/**
 * Created by Tiwence on 14/12/2017.
 */
/**
 * Listener used to notify when the Itunes charts request is done
 */
public interface OnRetrieveItunesTopChartsCompleted {

    public void onRetrieveItunesTopChartsCompleted(ArrayList<Song> songList);
    public void onRetrieveItunesTopChartsError(String message);

}
