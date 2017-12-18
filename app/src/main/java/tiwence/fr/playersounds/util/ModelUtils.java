package tiwence.fr.playersounds.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tiwence.fr.playersounds.model.Song;

/**
 * Created by Tiwence on 15/12/2017.
 */

public class ModelUtils {

    private static ModelUtils instance;

    public static ModelUtils instance() {
        if (instance == null)
            instance = new ModelUtils();
        return instance;
    }

    protected ArrayList<Song> parseItunesChartsSongs(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        JSONObject feed = json.optJSONObject("feed");
        JSONArray results = feed.optJSONArray("results");
        ArrayList<Song> songList = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.optJSONObject(i);
            Song song = new Song();
            song.setmId(result.optString("id"));
            song.setmArtistId(result.optString("artistId"));
            song.setmArtistName(result.optString("artistName"));
            song.setmArtistUrl(result.optString("artistUrl"));
            song.setmName(result.optString("name"));
            song.setmUrl(result.optString("url"));
            song.setmArtworkUrl(result.optString("artworkUrl100"));
            songList.add(song);
        }
        return songList;

    }

    protected ArrayList<Song> parseItunesSearchSongs(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        JSONArray results = json.optJSONArray("results");
        ArrayList<Song> songList = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.optJSONObject(i);
            Song song = new Song();
            song.setmId(result.optString("id"));
            song.setmArtistId(result.optString("artistId"));
            song.setmArtistName(result.optString("artistName"));
            song.setmArtistUrl(result.optString("artistUrl"));
            song.setmName(result.optString("trackName"));
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
