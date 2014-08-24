package klusman.scaantirevents.mobile.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.greenrobot.dao.DaoException;
import klusman.scaantirevents.mobile.Dao.DaoSession;
import klusman.scaantirevents.mobile.Dao.EventDao;


public class Event implements Parcelable {

    @SerializedName("EVENT_ID")
    private String eventId;

    @SerializedName("EVENT_NAME")
    private String eventName;

    @SerializedName("EVENT_LOGO")
    private String eventLogo;

    @SerializedName("CONTACT_NAME")
    private String contactName;

    @SerializedName("CONTACT_PHONE")
    private String contactPhone;

    @SerializedName("CONTACT_EMAIL")
    private String contactEmail;

    @SerializedName("EVENT_WEBSITE")
    private String eventWebsite;

    @SerializedName("EVENT_DESCRIPTION")
    private String eventDescription;



    @SerializedName("EVENT_START")
    private String eventStart;

    @SerializedName("EVENT_END")
    private String eventEnd;

    @SerializedName("EVENT_LOCATION")
    private String eventLocation;


    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient EventDao myDao;

    private List<Event> events;

    public Event() {
    }

    public Event(String eventId) {
        this.eventId = eventId;
    }

    public Event(String eventId,
                 String eventLogo,
                 String eventName,
                 String contactName,
                 String contactPhone,
                 String contactEmail,
                 String eventWebsite,
                 String eventDesc,
                 String eventStart,
                 String eventEnd,
                 String eventLocation) {
        this.eventId = eventId;
        this.eventLogo = eventLogo;
        this.eventName = eventName;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.eventWebsite = eventWebsite;
        this.eventDescription = eventDesc;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventLocation = eventLocation;

    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEventDao() : null;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String companyId) {
        this.eventId = companyId;
    }

    public String getEventLogo() {
        return eventLogo;
    }

    public void setEventLogo(String companyLogo) {
        this.eventLogo = companyLogo;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String companyName) {
        this.eventName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getEventWebsite() {
        return eventWebsite;
    }

    public void setEventWebsite(String companyWebsite) {
        this.eventWebsite = companyWebsite;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDesc) {
        this.eventDescription = eventDesc;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public String getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(String eventEnd) {
        this.eventEnd = eventEnd;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }





    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }





    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int arg1) {
        parcel.writeString(eventId);
        parcel.writeString(eventName);
        parcel.writeString(eventLogo);
        parcel.writeString(contactName);
        parcel.writeString(contactPhone);
        parcel.writeString(contactEmail);
        parcel.writeString(eventWebsite);
        parcel.writeString(eventDescription);
        parcel.writeString(eventStart);
        parcel.writeString(eventEnd);
        parcel.writeString(eventLocation);

    }
}
