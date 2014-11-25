package at.fhtw.rss.fragments;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import at.fhtw.rss.R;
import at.fhtw.rss.adapter.RssFeedCursorAdapter;
import at.fhtw.rss.dao.RssFeedContentProvider;
import at.fhtw.rss.dao.RssFeedDao;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RssFeedListFragment extends Fragment {

    private CursorAdapter adapter;

    public RssFeedListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cursor cursor = getActivity().getContentResolver().query(RssFeedContentProvider.CONTENT_URI,
                new String[]{RssFeedDao.Properties.Id.columnName},null,null,null);
        adapter = new RssFeedCursorAdapter(getActivity(), cursor, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_feed_list, container, false);

        ListView feedList = (ListView)view.findViewById(R.id.rssFeedList);
        feedList.setAdapter(adapter);

        return view;
    }


}
