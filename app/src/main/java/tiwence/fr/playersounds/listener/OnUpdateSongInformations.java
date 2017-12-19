package tiwence.fr.playersounds.listener;

/**
 * Created by Tiwence on 18/12/2017.
 */

/**
 * Interface use to notify Media Player about new songs informations to display to the user
 */
public interface OnUpdateSongInformations {
    public void onUpdateSongInformations(int currentSongIndex);
    public void onMediaPlayerPrepareCompleted();
}
