package com.giffar.ctour.models;

import android.content.Context;
import android.util.Log;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.entitys.Timeline;
import com.giffar.ctour.helpers.DateHelper;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TimelineModel extends BaseModel<Timeline> {
    DateHelper dateHelper;
    public TimelineModel(Context context) {
        super(context, Timeline.class);
        dateHelper = new DateHelper();
    }

    public List<Timeline> getNewestOffer() {
        List<Timeline> timelines = new ArrayList<>();
        try {
            QueryBuilder<Timeline, ?> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(Timeline.IS_DELETED, false).and().eq(Timeline.IS_BANNER,"1");
            queryBuilder.orderBy(Timeline.CREATED_AT, false);
            APP.log(queryBuilder.prepareStatementString());
            timelines = queryBuilder.query();
            //timelines = dao.queryBuilder().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timelines;
    }

    //event



    public List<Timeline> getAllEvents() {
        List<Timeline> events = new ArrayList<>();
        try {
            QueryBuilder<Timeline, ?> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(Timeline.TYPE, "event").and().eq(Timeline.IS_DELETED,false);
            queryBuilder.orderBy(Timeline.CREATED_AT, false);
            APP.log(queryBuilder.prepareStatementString());
            events = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return events;
    }

    //promotion
    public List<Timeline> getAllPromotion(Long set) {
        List<Timeline> events = new ArrayList<>();
        try {
            QueryBuilder<Timeline, ?> queryBuilder = dao.queryBuilder();
            queryBuilder.limit(set);
            queryBuilder.where().eq(Timeline.TYPE, "promotion").and().eq(Timeline.IS_DELETED,false);
            queryBuilder.orderBy(Timeline.CREATED_AT,false);
            APP.log(queryBuilder.prepareStatementString());
            events = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }






    public List<Timeline> getTimelines(Long set) {
        List<Timeline> timelines = new ArrayList<>();
        try {
            QueryBuilder<Timeline, ?> queryBuilder = dao.queryBuilder();
            queryBuilder.limit(set);
            queryBuilder.where().eq(Timeline.IS_DELETED, false);
            queryBuilder.orderBy(Timeline.CREATED_AT, false);
            APP.log(queryBuilder.prepareStatementString());
            timelines = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.i("CHECK_DB",timelines.toString());
        return timelines;
    }

    public List<Timeline> getTimelines(String type) {
        return getTimelines(type, false);
    }

    public List<Timeline> getTimelines(String type, boolean isSavedOffer) {
        List<Timeline> timelines = new ArrayList<Timeline>();
        try {
            QueryBuilder<Timeline, ?> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(Timeline.CREATED_AT, false);
            if (isSavedOffer) {
                queryBuilder.where().eq(Timeline.SAVED, true).and().raw(filterExpireQuery());
            } else {
                if (!type.equalsIgnoreCase("ALL"))
                    queryBuilder.where().eq(Timeline.TYPE, type).and().raw(filterExpireQuery());
                else
                    queryBuilder.where().raw(filterExpireQuery());
            }
            APP.log(queryBuilder.prepareStatementString());
            timelines = queryBuilder.orderBy(Timeline.CREATED_AT, false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timelines;
    }

    //ROOM
    public List<Timeline> getNewsOfferByRoom(String id){
        List<Timeline> timelines = new ArrayList<>();
        try {
            QueryBuilder<Timeline, ?> timelineQb = dao.queryBuilder();
            timelineQb.limit(4L);
            timelineQb.where().eq(Timeline.ROOM_ID, id);
            APP.log(timelineQb.prepareStatementString());
            timelines = timelineQb.orderBy(Timeline.START_AT, false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timelines;
    }


    public List<Timeline> getTimelineByRoom(String id){
        List<Timeline> timelines = new ArrayList<>();
        try {
            QueryBuilder<Timeline, ?> timelineQb = dao.queryBuilder();
            timelineQb.where().eq(Timeline.ROOM_ID, id).and().eq(Timeline.IS_DELETED,false);
            APP.log(timelineQb.prepareStatementString());
            timelines = timelineQb.orderBy(Timeline.CREATED_AT, true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timelines;
    }

    private String filterExpireQuery() {
        return "strftime('%Y-%m-%d'," + Timeline.VALID_END + ") >= '" + DateHelper.getInstance().getDate() + "'";
    }

    /**
     * Method for collect promo are followed stores
     *
     * @param storeIds (should be format like 12,34,45 where 12/34/45 is store id
     * @return
     */
    public List<Timeline> getFollowedPromos(String storeIds) {
        List<Timeline> timelines = new ArrayList<>();
        try {
            QueryBuilder<Timeline, ?> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(Timeline.CREATED_AT, false);
            APP.log("FOLLOWED STORE QUERY : " + queryBuilder.prepareStatementString());
            timelines = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timelines;
    }

    public List<Timeline> getExpiringPromos() {
        List<Timeline> timelines = new ArrayList<>();
        try {
            QueryBuilder<Timeline, ?> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(Timeline.CREATED_AT, false);
            queryBuilder.where().raw("(julianday(" + Timeline.EXPIRED + ")-julianday('now')) <= 3")
                    .and().raw("(julianday(" + Timeline.EXPIRED + ")-julianday('now')) >= 0")
                    .and().eq(Timeline.TYPE, "OFFER");
            APP.log("EXPIRING QUERY : " + queryBuilder.prepareStatementString());
            timelines = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timelines;
    }

    public List<Timeline> searchTimeline(String text, String timelineType) {
        List<Timeline> timelines = new ArrayList<>();
        try {
            QueryBuilder<Timeline, ?> queryBuilder = dao.queryBuilder();
            timelines = queryBuilder.where().eq(Timeline.TYPE, timelineType)
                    .and().like(Timeline.NAME, new SelectArg("%" + text + "%")).or()
                    .like(Timeline.DESCRIPTION, new SelectArg("%" + text + "%")).or()
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timelines;
    }


}
