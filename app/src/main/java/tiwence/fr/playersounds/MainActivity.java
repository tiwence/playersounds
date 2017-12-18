package tiwence.fr.playersounds;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
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
import tiwence.fr.playersounds.listener.OnRetrieveItunesTopChartsCompleted;
import tiwence.fr.playersounds.model.Song;
import tiwence.fr.playersounds.util.ApiUtils;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mSongsListView;
    private ArrayList<Song> mSongList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSongsListView = (ListView) this.findViewById(R.id.songsListView);
        mSongsListView.setOnItemClickListener(this);
        ConstraintLayout headerLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.list_song_header, null);
        TextView headerTextView = headerLayout.findViewById(R.id.headerTextView);
        headerTextView.setText(R.string.top);
        mSongsListView.addHeaderView(headerLayout);

        //Get top 100 from iTunes  music store
        ApiUtils.instance().retrieveItunesTopMusicCharts(new OnRetrieveItunesTopChartsCompleted() {
            @Override
            public void onRetrieveItunesTopChartsCompleted(ArrayList<Song> songList) {
                mSongList = songList;
                mSongsListView.setAdapter(new SongAdapter(MainActivity.this, mSongList));
            }

            @Override
            public void onRetrieveItunesTopChartsError(String message) {
                Toast.makeText(MainActivity.this
                        , "ERROR " + message, Toast.LENGTH_SHORT);            }
        });

    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(MainActivity.this, MediaPlayerActivity.class);
        intent.putExtra("songs", mSongList);
        intent.putExtra("position", (position - 1));
        startActivity(intent);
    }
}
