package at.fhtw.rss.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import at.fhtw.rss.R;
import at.fhtw.rss.dao.DaoMaster;
import at.fhtw.rss.dao.RssFeedContentProvider;
import at.fhtw.rss.dao.RssItemContentProvider;
import at.fhtw.rss.fragments.RssAddFeedFragment;
import at.fhtw.rss.fragments.RssFeedListFragment;

import static at.fhtw.rss.dao.DaoMaster.DevOpenHelper;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RssAddFeedFragment addFeedFragment = new RssAddFeedFragment();
        getFragmentManager().beginTransaction().add(R.id.container, addFeedFragment).commit();

        //Toast.makeText(this,"Add Fragment loaded",Toast.LENGTH_SHORT);
        Log.d("RssReader", "Add Fragment loaded");

        DevOpenHelper helper = new DevOpenHelper(this, "rssdb", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);

        Log.d("RssReader", "Database Setup complete");

        RssFeedContentProvider.daoSession = daoMaster.newSession();
        RssItemContentProvider.daoSession = daoMaster.newSession();

        Log.d("RssReader", "Dao Sessions created");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // app icon in action bar clicked; goto parent activity.
            case android.R.id.home:
                getFragmentManager().beginTransaction().replace(R.id.container, new RssFeedListFragment()).commit();

                ActionBar actionBar = getActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                return true;
            case R.id.action_add:
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new RssAddFeedFragment())
                        .addToBackStack(null)
                        .commit();
                actionBar = getActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
