package at.fhtw.rss.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import at.fhtw.rss.R;
import at.fhtw.rss.dao.RssItemDao;

/**
 * Created by Marco on 25.11.2014.
 */
public class RssItemCursorAdapter extends CursorAdapter{
    private Context context;
    private Cursor cursor;

    public RssItemCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.context = context;
        this.cursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // There is no view at this position, we create a new one.
        // In this case by inflating an xml layout.
        if (convertView == null) {
            // Inflate a layout
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.rssitem, parent, false);
            holder = new ViewHolder();
            holder.itemTitle = (TextView) convertView.findViewById(R.id.item_title);
            holder.itemUnread = (ImageView) convertView.findViewById(R.id.item_unread);
            holder.itemStarred = (ImageView) convertView.findViewById(R.id.item_starred);
            convertView.setTag(holder);
        } else {
            // We recycle a View that already exists.
            holder = (ViewHolder) convertView.getTag();
        }
        cursor.moveToPosition(position);
        if (cursor.getCount() > 0) {
            holder.itemTitle.setText(
                    cursor.getString(
                            cursor.getColumnIndex(
                                    RssItemDao.Properties.Title.columnName)));
            int unreadvis = cursor.getInt(
                    cursor.getColumnIndex(
                            RssItemDao.Properties.Read.columnName));

            holder.itemUnread.setVisibility(unreadvis==0 ? View.VISIBLE : View.INVISIBLE);

            int starredvis = cursor.getInt(
                    cursor.getColumnIndex(
                            RssItemDao.Properties.Starred.columnName));

            holder.itemStarred.setVisibility(starredvis==1 ? View.VISIBLE : View.INVISIBLE);


        }
        return convertView;
    }

    static class ViewHolder {
        TextView itemTitle;
        ImageView itemUnread, itemStarred;
    }
}
