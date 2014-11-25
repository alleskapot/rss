package at.fhtw.rss.services;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.fhtw.rss.dao.RssFeedContentProvider;
import at.fhtw.rss.dao.RssFeedDao;
import at.fhtw.rss.dao.RssItemContentProvider;
import at.fhtw.rss.dao.RssItemDao;
import at.fhtw.rss.saxrssreader.RssFeedModel;
import at.fhtw.rss.saxrssreader.RssItemModel;
import at.fhtw.rss.saxrssreader.RssReader;

/**
 * Created by Daniel on 17.11.2014.
 */
public class RssFeedService extends IntentService{


    public RssFeedService() {
        super("RssFeedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String feedUrl = intent.getStringExtra("feedUrl");

        RssFeedModel feedModel = new RssFeedModel();
        try {
            URL url = new URL(feedUrl);
            feedModel = RssReader.read(url);

            long feedId = createRssFeedEntry(feedModel, intent.getStringExtra(feedModel.getTitle()));
            Log.d("RssReader","New Record id: "+feedId);
            createRssItemEntries(feedId, feedModel.getRssItems());
        } catch (MalformedURLException e) {
            Log.e("Rss Reader", e.toString() );
            e.printStackTrace();
        } catch (SAXException e) {
            Log.e("Rss Reader", e.toString() );
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Rss Reader", e.toString() );
            e.printStackTrace();
        }
    }

    private long createRssItemEntries(long feedId, List<RssItemModel> rssItemModels) {
        List<ContentValues> list = convertRssItems(feedId, rssItemModels);
        // Convert list to array of ContentValues.
        ContentValues[] values = new ContentValues[list.size()];
        values = list.toArray(values);
        // Bulk insert all feed items.
        return getContentResolver().bulkInsert(RssItemContentProvider.CONTENT_URI, values);
    }

    private long createRssFeedEntry(RssFeedModel rssFeedModel, String title) {
        title = TextUtils.isEmpty(title) ? rssFeedModel.getTitle() : title;
        ContentValues values = new ContentValues();
        values.put(RssFeedDao.Properties.Title.columnName, title);
        values.put(RssFeedDao.Properties.Link.columnName, rssFeedModel.getLink());
        //Log.v("RssReader", "URI: "+RssFeedContentProvider.CONTENT_URI);
        // Insert rss feed.
        Uri uri = getContentResolver().insert(RssFeedContentProvider.CONTENT_URI, values);
        Log.d("RssReader","Feed inserted");
        // Return new record id.
        return ContentUris.parseId(uri);
    }

    private long createRssItemEntry(List<RssItemModel> feedModel, long feedId){

        List<ContentValues> list = convertRssItems(feedId, feedModel);
        // Convert list to array of ContentValues.
        ContentValues[] values = new ContentValues[list.size()];
        values = list.toArray(values);
        // Bulk insert all feed items.
        return getContentResolver().bulkInsert(RssItemContentProvider.CONTENT_URI, values);
    }

    private List<ContentValues> convertRssItems(long feedId, List<RssItemModel> rssItemModels) {
        List<ContentValues> list = new ArrayList<ContentValues>(rssItemModels.size());
        // Map RssItemModels to RssItems
        for (RssItemModel rssItemModel : rssItemModels) {
            ContentValues values = new ContentValues();
            values.put(RssItemDao.Properties.Title.columnName, rssItemModel.getTitle());
            values.put(RssItemDao.Properties.Link.columnName, rssItemModel.getLink());
            values.put(RssItemDao.Properties.Description.columnName, rssItemModel.getDescription());
            values.put(RssItemDao.Properties.PubDate.columnName, parseDate(rssItemModel.getPubDate()));
            values.put(RssItemDao.Properties.FeedId.columnName, feedId);
            list.add(values);
            Log.d("RssReader","Item "+rssItemModel.getTitle()+" inserted");
        }
        return list;
    }

    // Convert Date to String
    private String parseDate(Date date) {
        if (date == null) {
            date = new Date();
        }
        return new SimpleDateFormat().format(date);
    }
}
