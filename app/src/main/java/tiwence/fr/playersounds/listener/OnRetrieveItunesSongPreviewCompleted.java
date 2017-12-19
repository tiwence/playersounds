package tiwence.fr.playersounds.listener;

import tiwence.fr.playersounds.model.Song;

/**
 * Created by Tiwence on 14/12/2017.
 */

/**
 * Listener used to notify when a Itunes preview URL search request is done
 */
public interface OnRetrieveItunesSongPreviewCompleted {
    
    public void onRetrieveItunesSongPreviewCompleted(Song song);
    public void onRetrieveItunesSongPreviewError(String message);

}
