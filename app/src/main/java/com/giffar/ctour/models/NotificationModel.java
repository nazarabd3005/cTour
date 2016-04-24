package com.giffar.ctour.models;

import android.content.Context;


import com.giffar.ctour.entitys.Notification;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by egiadtya on 3/5/15.
 */
public class NotificationModel extends BaseModel<Notification> {
    public NotificationModel(Context context) {
        super(context, Notification.class);
    }

    @Override
    public List<Notification> all() {
        List<Notification> notifications = new ArrayList<>();
        try {
            notifications = dao.queryBuilder().orderBy(Notification.TIMESTAMP, false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public List<Notification> getUnreadNotif() {
        List<Notification> notifications = new ArrayList<>();
        try {
            notifications = dao.queryBuilder().orderBy(Notification.TIMESTAMP, false)
                    .where().eq(Notification.READ, false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public void updateReadNotif() {
        try {
            dao.updateBuilder().updateColumnValue(Notification.READ, true).update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
