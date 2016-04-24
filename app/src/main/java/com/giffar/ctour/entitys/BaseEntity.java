/**
 * Base Entity
 * @author egiadtya
 * 27 October 2014
 */
package com.giffar.ctour.entitys;

import android.os.Parcel;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable {

	public static final String ID = "id";
	public static final String UPDATED_DATE = "updated_date";
	public static final String CREATED_DATE = "created_date";
	public static final String IS_DELETED = "is_deleted";
	public static final String OBJECT = "object";
	@DatabaseField(columnName = ID, id = true)
	protected String id;
	@DatabaseField(columnName = UPDATED_DATE)
	protected Long updateDate;
	@DatabaseField(columnName = CREATED_DATE)
	protected Long createdDate;
	@DatabaseField(columnName = IS_DELETED)
	protected boolean isDeleted;

	public BaseEntity(Parcel parcel) {
	}

	public BaseEntity() {
        updateDate = System.currentTimeMillis();
	}

	public BaseEntity(long updateDate) {
		setUpdateDate(updateDate);
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Long updateDate) {
		this.updateDate = updateDate;
	}

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }


}
