/**
 * Base Model
 * @author egiadtya
 * 27 October 2014
 */
package com.giffar.ctour.models;

import android.content.Context;

import com.giffar.ctour.DbHelper;
import com.giffar.ctour.entitys.BaseEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BaseModel<T> {
    private static DbHelper dbHelper;
    protected Dao<T, ?> dao;
    protected Context context;

    public BaseModel(Context context, Class<?> cls) {
        initDB(context);
        getDao(cls);
        this.context = context;
    }

    private void initDB(Context context) {
        if (dbHelper == null) {
            dbHelper = new DbHelper(context);
        }
    }

    @SuppressWarnings({"unchecked", "hiding"})
    public List<T> all() {
        try {
            return (List<T>) dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }

    public BaseEntity find(String ID) {
        try {
            return (BaseEntity) dao.queryBuilder().where()
                    .eq(BaseEntity.ID, ID).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings({"unchecked", "hiding"})
    public List<T> findMany(String ID) {
        try {
            return (List<T>) dao.queryBuilder().where().eq(BaseEntity.ID, ID)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BaseEntity findBy(String condition, Object value) {
        try {
            return (BaseEntity) dao.queryBuilder().where().eq(condition, value)
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings({"unchecked", "hiding"})
    public List<T> findManyBy(String condition, Object value) {
        try {
            QueryBuilder<T, ?> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(condition, value);
            return (List<T>) queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }

    public List<T> findManyBy(String condition, Object value, String orderBy, boolean asc) {
        try {
            QueryBuilder<T, ?> queryBuilder = dao.queryBuilder();
            queryBuilder.orderBy(orderBy, asc);
            queryBuilder.where().eq(condition, value);
            return (List<T>) queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }
    
    @SuppressWarnings("unchecked")
    public void save(BaseEntity entity) {
        try {
            dao.createIfNotExists((T) entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void update(BaseEntity entity) {
        try {
            dao.createOrUpdate((T) entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void delete(BaseEntity entity) {
        try {
            dao.delete((T) entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void softDelete(BaseEntity entity) {
        try {
            entity.setDeleted(true);
            dao.update((T) entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll(List<T> entities) {
        try {
            dao.delete(entities);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void getDao(Class<?> cls) {
        try {
            if (dao == null) {
                dao = (Dao<T, ?>) dbHelper.getDao(cls);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
