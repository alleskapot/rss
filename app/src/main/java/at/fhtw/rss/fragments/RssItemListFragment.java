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
import android.widget.ListView;

import java.lang.ref.WeakReference;

import at.fhtw.rss.R;
import at.fhtw.rss.adapter.RssItemCursorAdapter;
import at.fhtw.rss.dao.DaoMaster;
import at.fhtw.rss.dao.RssItemContentProvider;
import at.fhtw.rss.dao.RssItemDao;
import at.fhtw.rss.listener.ItemListChoiceModeListener;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RssItemListFragment extends ListFragment {

    RssItemCursorAdapter adapter;
    private DaoMaster dao;

    public RssItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = getArguments().getLong("feedid");
        Cursor cursor = getActivity().getContentResolver().query(RssItemContentProvider.CONTENT_URI,
                new String[]{RssItemDao.Properties.Id.columnName, RssItemDao.Properties.Title.columnName, RssItemDao.Properties.Read.columnName, RssItemDao.Properties.Starred.columnName},RssItemDao.Properties.FeedId.columnName + "=?",new String[]{String.valueOf(id)},null);
        adapter = new RssItemCursorAdapter(getActivity(), cursor, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_feed_list, container, false);

        ListView itemList = (ListView)view.findViewById(android.R.id.list);
        itemList.setAdapter(adapter);
        itemList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        itemList.setMultiChoiceModeListener(new ItemListChoiceModeListener(new WeakReference<Activity>(getActivity()), itemList));



        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.d("Rss Reader", "Open Item in Browser");

        Intent intent = new Intent("itemBrowser");
        intent.putExtra("itemid", id);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);





    }


    public static RssItemListFragment newItemList(long id) {
            RssItemListFragment fragment = new RssItemListFragment();
            Bundle args = new Bundle();
            args.putLong("feedid", id);
            fragment.setArguments(args);

            return fragment;
        }

    }

