package tiwence.fr.playersounds.manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tiwence.fr.playersounds.model.Song;

/**
 * Created by Tiwence on 15/12/2017.
 */

public class DataManager {

    private static DataManager instance;

    public static DataManager instance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

    /**
     * Function used to parse JSON from the Itunes search API to Song objects
     * @param jsonString returned by the Itunes search API
     * @return list of Song objects according to the JSON structure
     * @throws JSONException
     */
    protected ArrayList<Song> parseItunesJSON(String jsonString) throws JSONException {
        ArrayList<Song> songList = new ArrayList<>();

        JSONObject json = new JSONObject(jsonString);
        JSONArray results = null;

        if (json.has("feed")) { //from Itunes charts API
            JSONObject feed = json.optJSONObject("feed");
            results = feed.optJSONArray("results");
        } else {
            results = json.optJSONArray("results");
        }

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.optJSONObject(i);
            Song song = new Song();
            song.setmId(result.optString("id"));
            song.setmArtistId(result.optString("artistId"));
            song.setmArtistName(result.optString("artistName"));
            song.setmArtistUrl(result.optString("artistUrl"));
            song.setmName( result.has("trackName") ? result.optString("trackName") : result.optString("name"));
            song.setmUrl(result.optString("url"));
            song.setmUrl(result.optString("collectionName"));
            song.setmStreamingUrl(result.optString("previewUrl"));
            song.setmArtworkUrl(result.optString("artworkUrl100"));
            song.setmDuration(result.optLong("trackTimeMillis"));
            songList.add(song);
        }

        return songList;
    }
}
