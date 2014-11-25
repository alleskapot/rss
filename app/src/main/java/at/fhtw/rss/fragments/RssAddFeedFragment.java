package at.fhtw.rss.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import at.fhtw.rss.R;
import at.fhtw.rss.services.RssFeedService;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class RssAddFeedFragment extends Fragment implements View.OnClickListener {

    EditText rssUrl;

    public RssAddFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rss_add_feed, container, false);
        Button btn_AddNewFeed = (Button)view.findViewById(R.id.button_add_rss_feed);
        rssUrl = (EditText)view.findViewById(R.id.editText_add_rss_feed_url);

        btn_AddNewFeed.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    public void onClick(View view){
        Log.d("Rss Reader","Add New Feed Click Event captured");

        Intent intent = new Intent(getActivity(), RssFeedService.class);
        intent.putExtra("feedUrl",rssUrl.getText().toString());

        getActivity().startService(intent);
        Log.d("Rss Reader","RssFeedService started");

        getFragmentManager().beginTransaction().replace(R.id.container,new RssFeedListFragment()).addToBackStack(null).commit();
    }


}
