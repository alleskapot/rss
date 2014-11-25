package at.fhtw.rss.listener;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.lang.ref.WeakReference;

import at.fhtw.rss.R;
import at.fhtw.rss.dao.RssFeedContentProvider;
import at.fhtw.rss.dao.RssFeedDao;
import at.fhtw.rss.dao.RssItemContentProvider;
import at.fhtw.rss.dao.RssItemDao;

/**
 * Created by Marco on 25.11.2014.
 */

    public class FeedListChoiceModeListener implements ListView.MultiChoiceModeListener {
        private WeakReference<Activity> activity;
        private ListView listView;

        public FeedListChoiceModeListener(WeakReference<Activity> activity, ListView listView) {
            this.activity = activity;
            this.listView = listView;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
            // Set the action mode title.
            actionMode.setTitle("Feeds");

            // Set background color on selected item.
            if (listView.isItemChecked(position)) {
                listView.getChildAt(position).setBackgroundColor(Color.LTGRAY);
            } else {
                listView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.feed_actionmode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    // Get a list of selected item ids.
                    long[] selectedFeedIds = listView.getCheckedItemIds();
                    ContentResolver cr = activity.get().getContentResolver();

                    // Delete all selected items.
                    for (long selectedFeedId : selectedFeedIds) {
                        // Execute delete statement.


                        cr.delete(RssFeedContentProvider.CONTENT_URI,
                                RssFeedDao.Properties.Id.columnName + " = ?",
                                new String[]{String.valueOf(selectedFeedId)});
                        cr.delete(RssItemContentProvider.CONTENT_URI, RssItemDao.Properties.FeedId.columnName + " = ?",
                                new String[]{String.valueOf(selectedFeedId)});
                    }

                    actionMode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            listView.clearChoices();
            listView.requestLayout();
        }

    }
