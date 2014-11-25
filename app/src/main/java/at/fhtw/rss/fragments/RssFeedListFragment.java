package at.fhtw.rss.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import java.lang.ref.WeakReference;

import at.fhtw.rss.R;
import at.fhtw.rss.adapter.RssFeedCursorAdapter;
import at.fhtw.rss.dao.RssFeedContentProvider;
import at.fhtw.rss.dao.RssFeedDao;
import at.fhtw.rss.listener.FeedListChoiceModeListener;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RssFeedListFragment extends ListFragment {

    private CursorAdapter adapter;

    public RssFeedListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cursor cursor = getActivity().getContentResolver().query(RssFeedContentProvider.CONTENT_URI,
                new String[]{RssFeedDao.Properties.Id.columnName, RssFeedDao.Properties.Title.columnName, RssFeedDao.Properties.Link.columnName},null,null,null);
        adapter = new RssFeedCursorAdapter(getActivity(), cursor, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_feed_list, container, false);

        ListView feedList = (ListView)view.findViewById(android.R.id.list);
        feedList.setAdapter(adapter);
        feedList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        feedList.setMultiChoiceModeListener(new FeedListChoiceModeListener(new WeakReference<Activity>(getActivity()), feedList));

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.d("Rss Reader", "Add New Feed ITEM Click Event captured");

        Intent intent = new Intent("displayItems");
        intent.putExtra("feedid", id);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);



        }
}
