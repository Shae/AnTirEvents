package klusman.scaantirevents.mobile.Dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;
import klusman.scaantirevents.mobile.Objects.Event;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig eventDaoConfig;
    private final EventDao eventDao;


    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        eventDaoConfig = daoConfigMap.get(EventDao.class).clone();
        eventDaoConfig.initIdentityScope(type);
        eventDao = new EventDao(eventDaoConfig, this);
        registerDao(Event.class, eventDao);

    }
    
    public void clear() {
        eventDaoConfig.getIdentityScope().clear();

    }

    public EventDao getEventDao() {
        return eventDao;
    }


}
