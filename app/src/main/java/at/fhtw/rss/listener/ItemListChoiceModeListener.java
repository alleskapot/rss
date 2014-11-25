package at.fhtw.rss.listener;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Color;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.lang.ref.WeakReference;

import at.fhtw.rss.R;
import at.fhtw.rss.dao.RssItemContentProvider;
import at.fhtw.rss.dao.RssItemDao;

/**
 * Created by Marco on 25.11.2014.
 */
public class ItemListChoiceModeListener implements ListView.MultiChoiceModeListener {
        private WeakReference<Activity> activity;
        private ListView listView;

        public ItemListChoiceModeListener(WeakReference<Activity> activity, ListView listView) {
            this.activity = activity;
            this.listView = listView;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
            // Set the action mode title.
            actionMode.setTitle("Items");

            // Set background color on selected item.
            if (listView.isItemChecked(position)) {
                listView.getChildAt(position).setBackgroundColor(Color.LTGRAY);
            } else {
                listView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.item_actionmode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            long[] selectedItemids;
            ContentResolver cr;
            switch (menuItem.getItemId()) {
                case R.id.action_starred:
                    // Get a list of selected item ids.
                    selectedItemids = listView.getCheckedItemIds();
                    cr = activity.get().getContentResolver();

                    // Delete all selected items.
                    for (long selectedItemid : selectedItemids) {
                        // Execute delete statement.

                        ContentValues cv = new ContentValues();
                        cv.put(RssItemDao.Properties.Starred.columnName, true);


                        cr.update(RssItemContentProvider.CONTENT_URI, cv, RssItemDao.Properties.Id.columnName + "= ?", new String[]{String.valueOf(selectedItemid)});
                    }
                    actionMode.finish();
                    break;
                case R.id.action_unstarred:
                    // Get a list of selected item ids.
                    selectedItemids = listView.getCheckedItemIds();
                    cr = activity.get().getContentResolver();

                    // Delete all selected items.
                    for (long selectedItemid : selectedItemids) {
                        // Execute delete statement.

                        ContentValues cv = new ContentValues();
                        cv.put(RssItemDao.Properties.Starred.columnName, false);


                        cr.update(RssItemContentProvider.CONTENT_URI, cv, RssItemDao.Properties.Id.columnName + "= ?", new String[]{String.valueOf(selectedItemid)});
                    }
                    actionMode.finish();
                    break;

                case R.id.action_unread:
                    // Get a list of selected item ids.
                    selectedItemids = listView.getCheckedItemIds();
                    cr = activity.get().getContentResolver();

                    // Delete all selected items.
                    for (long selectedItemid : selectedItemids) {
                        // Execute delete statement.

                        ContentValues cv = new ContentValues();
                        cv.put(RssItemDao.Properties.Read.columnName, false);


                        cr.update(RssItemContentProvider.CONTENT_URI, cv, RssItemDao.Properties.Id.columnName + "= ?", new String[]{String.valueOf(selectedItemid)});
                    }
                    actionMode.finish();
                    break;

                case R.id.action_read:
                    // Get a list of selected item ids.
                    selectedItemids = listView.getCheckedItemIds();
                    cr = activity.get().getContentResolver();

                    // Delete all selected items.
                    for (long selectedItemid : selectedItemids) {
                        // Execute delete statement.

                        ContentValues cv = new ContentValues();
                        cv.put(RssItemDao.Properties.Read.columnName, true);


                        cr.update(RssItemContentProvider.CONTENT_URI, cv, RssItemDao.Properties.Id.columnName + "= ?", new String[]{String.valueOf(selectedItemid)});
                    }
                    actionMode.finish();
                    break;

                default:
                    return false;
            }
        return true;
        }


        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            listView.clearChoices();
            listView.requestLayout();
        }

    }

