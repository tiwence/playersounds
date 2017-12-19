package tiwence.fr.playersounds.manager;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tiwence.fr.playersounds.R;
import tiwence.fr.playersounds.listener.OnRetrieveItunesSearchCompleted;
import tiwence.fr.playersounds.listener.OnRetrieveItunesSongPreviewCompleted;
import tiwence.fr.playersounds.listener.OnRetrieveItunesTopChartsCompleted;
import tiwence.fr.playersounds.model.Song;

/**
 * Created by Tiwence on 14/12/2017.
 */

public class ApiManager {

    public static ApiManager instance;
    public static final String COUNTRY = Locale.getDefault().getCountry();

    private static String mItunesChartBaseURL = "https://rss.itunes.apple.com/api/v1/" + COUNTRY + "/itunes-music/top-songs/all/100/explicit.json";
    private static String mItunesSearchBaseURL = "https://itunes.apple.com/search?country=" + COUNTRY + "&media=music";
    private static String mItunesLookUpBaseURL = "http://itunes.apple.com/" + COUNTRY  + "/lookup"; //?id=1293873984

    private OkHttpClient mHttpClient = new OkHttpClient();

    public static final MediaType JSON;

    static {
        JSON = MediaType.parse("application/json; charset=utf-8");
    }

    public static ApiManager instance() {
        if (instance == null)
            instance = new ApiManager();
        return instance;
    }


    /**
     * Function used to retrieve top 100 songs from Itunes Music Store
     * @param listener callback
     */
    public void retrieveItunesTopMusicCharts(final Context context,final OnRetrieveItunesTopChartsCompleted listener) {

        new AsyncTask<Void, Void, ArrayList<Song>>() {
            @Override
            protected ArrayList<Song> doInBackground(Void... voids) {
                try {
                    //HttpURLConnection connection =
                    Request request = new Request.Builder().url(mItunesChartBaseURL).build();
                    Response response = null;
                    response = mHttpClient.newCall(request).execute();
                    String jsonString = response.body().string();
                    return DataManager.instance().parseItunesJSON(jsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Song> songs) {
                super.onPostExecute(songs);
                if (songs != null) {
                    listener.onRetrieveItunesTopChartsCompleted(songs);
                } else {
                    listener.onRetrieveItunesTopChartsError(context.getResources().getString(R.string.error_msg));
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * Function used to search user query on Itunes music API
     * @param query query ask by the user
     * @param listener callback
     */
    public void retrieveItunesSearchMusic(final Context context, final String query, final OnRetrieveItunesSearchCompleted listener) {

        new AsyncTask<Void, Void, ArrayList<Song>>() {
            @Override
            protected ArrayList<Song> doInBackground(Void... voids) {
                try {
                    //HttpURLConnection connection =
                    Request request = new Request.Builder().url(mItunesSearchBaseURL + "&term=" + URLEncoder.encode(query, "utf-8")).build();
                    Response response = null;
                    response = mHttpClient.newCall(request).execute();
                    String jsonString = response.body().string().replaceAll("\n", "");
                    return DataManager.instance().parseItunesJSON(jsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Song> songs) {
                super.onPostExecute(songs);
                if (songs != null) {
                    listener.onRetrieveItunesSearchCompleted(songs);
                } else {
                    listener.onRetrieveItunesSearchError(context.getResources().getString(R.string.error_msg));
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    /**
     * Function used to retrieve preview streaming URL from a specific song
     * @param song song to fill with more informations
     * @param listener callback
     */
    public void retrieveItunesSongPreviewUrl(final Context context, final Song song, final OnRetrieveItunesSongPreviewCompleted listener) {

        new AsyncTask<Void, Void, Song>() {
            @Override
            protected Song doInBackground(Void... voids) {
                try {
                    //HttpURLConnection connection =
                    Request request = new Request.Builder().url(mItunesLookUpBaseURL + "?id=" + song.getmId()).build();
                    Response response = null;
                    response = mHttpClient.newCall(request).execute();
                    String jsonString = response.body().string().replaceAll("\n", "");
                    JSONObject json = new JSONObject(jsonString);
                    if (json.optInt("resultCount") > 0) {
                        JSONArray results = json.optJSONArray("results");
                        JSONObject jsonSong = results.getJSONObject(0);
                        song.setmStreamingUrl(jsonSong.optString("previewUrl"));
                    }
                    return song;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Song song) {
                super.onPostExecute(song);
                if (song != null) {
                    listener.onRetrieveItunesSongPreviewCompleted(song);
                } else {
                    listener.onRetrieveItunesSongPreviewError(context.getResources().getString(R.string.error_msg));
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
