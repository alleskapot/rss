package at.fhtw.rss.dao;

import at.fhtw.rss.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table RSS_ITEM.
 */
public class RssItem {

    private Long id;
    /** Not-null value. */
    private String title;
    /** Not-null value. */
    private String link;
    private String description;
    private java.util.Date pubDate;
    private Boolean read;
    private Boolean starred;
    private long feedId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient RssItemDao myDao;

    private RssFeed rssFeed;
    private Long rssFeed__resolvedKey;


    public RssItem() {
    }

    public RssItem(Long id) {
        this.id = id;
    }

    public RssItem(Long id, String title, String link, String description, java.util.Date pubDate, Boolean read, Boolean starred, long feedId) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.read = read;
        this.starred = starred;
        this.feedId = feedId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRssItemDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getTitle() {
        return title;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Not-null value. */
    public String getLink() {
        return link;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.util.Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(java.util.Date pubDate) {
        this.pubDate = pubDate;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    public long getFeedId() {
        return feedId;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    /** To-one relationship, resolved on first access. */
    public RssFeed getRssFeed() {
        long __key = this.feedId;
        if (rssFeed__resolvedKey == null || !rssFeed__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RssFeedDao targetDao = daoSession.getRssFeedDao();
            RssFeed rssFeedNew = targetDao.load(__key);
            synchronized (this) {
                rssFeed = rssFeedNew;
            	rssFeed__resolvedKey = __key;
            }
        }
        return rssFeed;
    }

    public void setRssFeed(RssFeed rssFeed) {
        if (rssFeed == null) {
            throw new DaoException("To-one property 'feedId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.rssFeed = rssFeed;
            feedId = rssFeed.getId();
            rssFeed__resolvedKey = feedId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
