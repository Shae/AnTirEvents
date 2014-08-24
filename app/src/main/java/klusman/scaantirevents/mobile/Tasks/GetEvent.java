package klusman.scaantirevents.mobile.Tasks;

import android.os.Bundle;

import java.io.IOException;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import klusman.scaantirevents.mobile.Dao.EventDao;
import klusman.scaantirevents.mobile.MyApp;
import klusman.scaantirevents.mobile.Objects.Event;

public class GetEvent extends TaskFragment_new<Event> {

    public static GetEvent newInstance(String EventID) {
        GetEvent event = new GetEvent();
        Bundle args = new Bundle();
        args.putString("EventID", EventID);
        event.setArguments(args);
        return event;
    }

    @Override
    public String getUri() {
        addParameter("EventID", getArguments().getString("EventID"));
        return SERVER_URL + "CompanyDetailsJson";
    }

    @Override
    public Event loadFromLocal() {
        QueryBuilder<Event> qb = MyApp.getInstance().getDaoSession().getEventDao().queryBuilder();
        qb.where(EventDao.Properties.EventId.eq(getArguments().getString("EventID")));
        return qb.unique();
    }


    @Override
    protected Event handleJSON(String response) throws IOException {
        List<Event> res = JsonHelper.parseEventList(response);
        if (res.isEmpty())
            throw new TaskException("An error occurred, please try again later");
        return res.get(0);
    }


}
