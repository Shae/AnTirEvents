package klusman.scaantirevents.mobile.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import klusman.scaantirevents.mobile.Objects.Event;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table COMPANY.
*/
public class EventDao extends AbstractDao<Event, String> {

    public static final String TABLENAME = "EVENTS";

    /**
     * Properties of entity Company.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property EventId = new Property(0, String.class, "eventId", true, "EVENT_ID");
        public final static Property EventLogo = new Property(1, String.class, "eventLogo", false, "EVENT_LOGO");
        public final static Property EventName = new Property(2, String.class, "eventName", false, "EVENT_NAME");
        public final static Property ContactName = new Property(3, String.class, "contactName", false, "CONTACT_NAME");
        public final static Property ContactPhone = new Property(4, String.class, "contactPhone", false, "CONTACT_PHONE");
        public final static Property ContactEmail = new Property(5, String.class, "contactEmail", false, "CONTACT_EMAIL");
        public final static Property EventWebsite = new Property(6, String.class, "eventWebsite", false, "EVENT_WEBSITE");
        public final static Property EventDescription = new Property(7, String.class, "eventDescription", false, "EVENT_DESCRIPTION");
        public final static Property EventStart = new Property(8, String.class, "eventStart", false, "EVENT_START");
        public final static Property EventEnd = new Property(9, String.class, "eventEnd", false, "EVENT_END");
        public final static Property EventLocation = new Property(10, String.class, "eventLocation", false, "EVENT_LOCATION");
        public final static Property EventFavorite = new Property(11, String.class, "eventFavorite", false, "EVENT_FAVORITE");

    }

    private DaoSession daoSession;


    public EventDao(DaoConfig config) {
        super(config);
    }

    public EventDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'EVENTS' (" + //
                "'EVENT_ID' TEXT PRIMARY KEY NOT NULL ," + // 0: eventId
                "'EVENT_LOGO' TEXT," + // 1: eventLogo
                "'EVENT_NAME' TEXT," + // 2: eventName
                "'CONTACT_NAME' TEXT," + // 3: contactName
                "'CONTACT_PHONE' TEXT," + // 4: contactPhone
                "'CONTACT_EMAIL' TEXT," + // 5: contactEmail
                "'EVENT_WEBSITE' TEXT," + // 6: eventWebsite
                "'EVENT_DESCRIPTION' TEXT," + // 7: eventDescription
                "'EVENT_START' TEXT," + // 8: eventStart
                "'EVENT_END' TEXT," + // 9: eventEnd
                "'EVENT_LOCATION' TEXT," + // 10: eventLocation
                "'EVENT_FAVORITE' TEXT);"); //11: eventFavorite

    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'EVENT'";
        db.execSQL(sql);
    }



    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Event entity) {
        stmt.clearBindings();
 
        String eventId = entity.getEventId();
        if (eventId != null) {
            stmt.bindString(1, eventId);
        }
 
        String eventLogo = entity.getEventLogo();
        if (eventLogo != null) {
            stmt.bindString(2, eventLogo);
        }
 
        String eventName = entity.getEventName();
        if (eventName != null) {
            stmt.bindString(3, eventName);
        }
 
        String contactName = entity.getContactName();
        if (contactName != null) {
            stmt.bindString(4, contactName);
        }
 
        String contactPhone = entity.getContactPhone();
        if (contactPhone != null) {
            stmt.bindString(5, contactPhone);
        }
 
        String contactEmail = entity.getContactEmail();
        if (contactEmail != null) {
            stmt.bindString(6, contactEmail);
        }
 
        String eventWebsite = entity.getEventWebsite();
        if (eventWebsite != null) {
            stmt.bindString(7, eventWebsite);
        }
 
        String eventDescription = entity.getEventDescription();
        if (eventDescription != null) {
            stmt.bindString(8, eventDescription);
        }

        String eventStart = entity.getEventStart();
        if (eventStart != null) {
            stmt.bindString(9, eventStart);
        }

        String eventEnd = entity.getEventEnd();
        if (eventEnd != null) {
            stmt.bindString(10, eventEnd);
        }

        String eventLocation = entity.getEventLocation();
        if (eventLocation != null) {
            stmt.bindString(11, eventLocation);
        }

        String eventFavorite = entity.getEventFavorite();
        if (eventFavorite != null) {
            stmt.bindString(12, eventFavorite);
        }

    }

    @Override
    protected void attachEntity(Event event) {
        super.attachEntity(event);
        event.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Event readEntity(Cursor cursor, int offset) {
        Event entity = new Event( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // eventId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // eventLogo
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // eventName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // contactName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // contactPhone
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // contactEmail
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // eventWebsite
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // eventDescription
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // eventStart
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // eventEnd
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // eventLocation
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // eventFavorite
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Event entity, int offset) {
        entity.setEventId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setEventLogo(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setEventName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setContactName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setContactPhone(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setContactEmail(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setEventWebsite(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setEventDescription(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setEventStart(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setEventEnd(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setEventLocation(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setEventFavorite(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Event entity, long rowId) {
        return entity.getEventId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Event entity) {
        if(entity != null) {
            return entity.getEventId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
