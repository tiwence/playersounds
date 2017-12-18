package tiwence.fr.playersounds.listener;

import tiwence.fr.playersounds.model.Song;

/**
 * Created by Tiwence on 14/12/2017.
 */

public interface OnRetrieveItunesSongPreviewCompleted {
    
    public void onRetrieveItunesSongPreviewCompleted(Song song);
    public void onRetrieveItunesSongPreviewError(String message);

}
