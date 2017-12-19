package tiwence.fr.playersounds;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tiwence.fr.playersounds.adapter.SongAdapter;
import tiwence.fr.playersounds.listener.OnRetrieveItunesSearchCompleted;
import tiwence.fr.playersounds.model.Song;
import tiwence.fr.playersounds.manager.ApiManager;

/**
 * Created by Tiwence on 15/12/2017.
 */
public class SearchableActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView mSongsListView;
    private ArrayList<Song> mSongList;
    private TextView mHeaderTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSongsListView = (ListView) this.findViewById(R.id.songsListView);
        mSongsListView.setOnItemClickListener(this);

        ConstraintLayout headerLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.list_song_header, null);
        mHeaderTextView = headerLayout.findViewById(R.id.headerTextView);
        mSongsListView.addHeaderView(headerLayout);

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            this.onSearchRequested();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("QUERY", query);
            if (!"".equals(query.trim())) {
                CharSequence searchQuery = Html.fromHtml(getResources().getString(R.string.search_results, query));
                mHeaderTextView.setText(searchQuery);
                ApiManager.instance().retrieveItunesSearchMusic(this, query.trim(), new OnRetrieveItunesSearchCompleted() {
                    @Override
                    public void onRetrieveItunesSearchCompleted(ArrayList<Song> songList) {
                        mSongList = songList;
                        mSongsListView.setAdapter(new SongAdapter(SearchableActivity.this, mSongList));
                    }

                    @Override
                    public void onRetrieveItunesSearchError(String message) {
                        Toast.makeText(SearchableActivity.this
                                , message, Toast.LENGTH_SHORT);
                    }
                });
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(SearchableActivity.this, MediaPlayerActivity.class);
        intent.putExtra("songs", mSongList);
        intent.putExtra("position", position - 1);
        startActivity(intent);
    }
}
