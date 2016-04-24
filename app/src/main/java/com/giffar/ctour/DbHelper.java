/**
 * Database configuration and helper
 * @author egiadtya
 * 27 October 2014
 */
package com.giffar.ctour;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.giffar.ctour.entitys.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DbHelper extends OrmLiteSqliteOpenHelper {

    private static final int DATABASE_VERSION = 22;
    private Context context;

    public DbHelper(Context context) {
        super(context, Config.DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        createTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        resetDatabase();
        createTable();
        APP.removeConfig(context, Preferences.USER_LOGIN);
        APP.removeConfig(context, Preferences.LOGGED_USER_ID);
        APP.removeConfig(context, Preferences.CHECKSUM_VAL_BUSINESSES);
        APP.removeConfig(context, Preferences.CHECKSUM_VAL_TIMELINE);
        APP.removeConfig(context, Preferences.HAS_LOAD_UUID);
    }

    private void createTable() {
        try {
//            TableUtils.createTableIfNotExists(connectionSource, Timeline.class);

            TableUtils.createTableIfNotExists(connectionSource, User.class);
//            TableUtils.createTableIfNotExists(connectionSource, Beacon.class);
//            TableUtils.createTableIfNotExists(connectionSource, Notification.class);
//            TableUtils.createTableIfNotExists(connectionSource, UserSaveData.class);
//            TableUtils.createTableIfNotExists(connectionSource, Promotion.class);
//            TableUtils.createTableIfNotExists(connectionSource, Event.class);
//            TableUtils.createTableIfNotExists(connectionSource, ForgotPassword.class);
//            TableUtils.createTableIfNotExists(connectionSource, Room.class);
//            TableUtils.createTableIfNotExists(connectionSource, RoomMusicGenre.class);
//            TableUtils.createTableIfNotExists(connectionSource, Image.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetDatabase() {
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
//            TableUtils.dropTable(connectionSource, Timeline.class, true);
//            TableUtils.dropTable(connectionSource, Beacon.class, true);
//            TableUtils.dropTable(connectionSource, Notification.class, true);
//            TableUtils.dropTable(connectionSource, UserSaveData.class, true);
//            TableUtils.dropTable(connectionSource, Promotion.class, true);
//            TableUtils.dropTable(connectionSource, Event.class, true);
//            TableUtils.dropTable(connectionSource, ForgotPassword.class, true);
//            TableUtils.dropTable(connectionSource, Room.class, true);
//            TableUtils.dropTable(connectionSource, RoomMusicGenre.class, true);
//            TableUtils.dropTable(connectionSource, Image.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz)
            throws SQLException {
        return super.getDao(clazz);
    }

    @Override
    public void close() {
        super.close();

    }


}
