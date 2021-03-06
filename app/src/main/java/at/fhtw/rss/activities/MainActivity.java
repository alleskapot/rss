package at.fhtw.rss.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import at.fhtw.rss.R;
import at.fhtw.rss.dao.DaoMaster;
import at.fhtw.rss.dao.RssFeedContentProvider;
import at.fhtw.rss.dao.RssItemContentProvider;
import at.fhtw.rss.dao.RssItemDao;
import at.fhtw.rss.fragments.RssAddFeedFragment;
import at.fhtw.rss.fragments.RssFeedListFragment;
import at.fhtw.rss.fragments.RssItemListFragment;

import static at.fhtw.rss.dao.DaoMaster.DevOpenHelper;


public class MainActivity extends Activity {

    private RssFeedBroadcastReceiver feedreceiver = new RssFeedBroadcastReceiver();
    DaoMaster daoMaster;
    SQLiteDatabase db;

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentfiler = new IntentFilter();
        intentfiler.addAction("displayFeedList");
        intentfiler.addAction("displayItems");
        intentfiler.addAction("itemBrowser");

        LocalBroadcastManager.getInstance(this).registerReceiver(feedreceiver, intentfiler);

    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(feedreceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RssAddFeedFragment addFeedFragment = new RssAddFeedFragment();
        getFragmentManager().beginTransaction().add(R.id.container, addFeedFragment).commit();

        //Toast.makeText(this,"Add Fragment loaded",Toast.LENGTH_SHORT);
        Log.d("RssReader", "Add Fragment loaded");

        DevOpenHelper helper = new DevOpenHelper(this, "rssdb", null);
        db = helper.getWritableDatabase();


        daoMaster = new DaoMaster(db);

        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, true);

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

            case R.id.action_feedlist:
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new RssFeedListFragment())
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

    private class RssFeedBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // ACTION equals OPEN_FEED, load fragment containing feed items.
            if (action.equals("displayFeedList")) {

                getFragmentManager().beginTransaction().replace(R.id.container,new RssFeedListFragment()).addToBackStack(null).commit();

            } else if (action.equals("displayItems")) {

                long id =  intent.getLongExtra("feedid", 1L);

                getFragmentManager().beginTransaction().replace(R.id.container,RssItemListFragment.newItemList(id)).addToBackStack(null).commit();

            } else if (action.equals("itemBrowser")) {

                long id =  intent.getLongExtra("itemid", 1L);

                String url = null;
                    try
                    {
                        Cursor c = null;

                        c = getContentResolver().query(RssItemContentProvider.CONTENT_URI,
                                new String[]{RssItemDao.Properties.Id.columnName,  RssItemDao.Properties.Link.columnName}, null, null, null);

                        c.moveToFirst();
                        url = c.getString(c.getColumnIndex(RssItemDao.Properties.Link.columnName));
                        c.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                ContentValues cv = new ContentValues();
                cv.put(RssItemDao.Properties.Read.columnName, true);


                getContentResolver().update(RssItemContentProvider.CONTENT_URI, cv, RssItemDao.Properties.Id.columnName + "= ?", new String[]{String.valueOf(id)});



            }
        }
    }
}
