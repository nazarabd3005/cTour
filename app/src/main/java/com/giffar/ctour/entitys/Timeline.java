package com.giffar.ctour.entitys;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;

public class Timeline extends BaseEntity implements Parcelable {

    private static final long serialVersionUID = 4398644845701759658L;

    public static final String SAVED = "saved";
    public static final String TYPE = "type";
    public static final String PHOTO = "photo";
    public static final String TIMESTAMP = "timestamp";
    public static final String CATEGORY = "category";
    public static final String DESCRIPTION = "description";
    public static final String REDEEMED = "redeemed";
    public static final String BANNER = "banner";
    public static final String NAME = "name";
    public static final String TOS = "tos";
    public static final String EXPIRED = "expired";
    public static final String VALID_START = "valid_start";
    public static final String VALID_END = "valid_end";
    public static final String FROM_PAGE = "from_page";
    public static final String IS_BANNER = "is_banner";
    public static final String PROMO_CODE = "promo_code";
    public static final String CREATED_AT = "created_at";
    public static final String IMAGE = "image";
    public static final String VALID_DAY    = "valid_day";
    public static final String START_AT = "start_at";
    public static final String END_AT = "end_at";
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_NAME = "room_name";
    public static final String TERM = "term";
    public static final String SUPPORTED_BY = "supported_by";
    public static final String ROOM_BOOKABLE = "room_bookable";
    public static final String ROOM_LOGO = "room_logo";



    @DatabaseField(columnName = SAVED)
    private boolean saved;
    @DatabaseField(columnName = TYPE)
    private String type;
    @DatabaseField(columnName = PHOTO)
    private String photo;

    @DatabaseField(columnName = TIMESTAMP)
    private String timestamp;

    @DatabaseField(columnName = CATEGORY)
    private String category;

    @DatabaseField(columnName = DESCRIPTION)
    private String description;
    @DatabaseField(columnName = REDEEMED)
    private boolean redeemed;
    @DatabaseField(columnName = BANNER)
    private String banner;
    @DatabaseField(columnName = NAME)
    private String name;
    @DatabaseField(columnName = TOS)
    private String tos;
    @DatabaseField(columnName = EXPIRED)
    private String expired;
    @DatabaseField(columnName = VALID_START)
    private String validStart;
    @DatabaseField(columnName = VALID_END)
    private String validEnd;
    private String fromPage;
    private String action;
    @DatabaseField(columnName = IS_BANNER)
    private String is_banner;
    @DatabaseField(columnName = IMAGE)
    private String image;
    @DatabaseField(columnName = TERM)
    private String term;
    @DatabaseField(columnName = SUPPORTED_BY)
    private String supported_by;

    @DatabaseField(columnName = PROMO_CODE)
    private String promo_code;
    @DatabaseField(columnName = CREATED_AT)
    private String created_at;
    @DatabaseField(columnName = ROOM_ID)
    private String room_id;
    @DatabaseField(columnName = ROOM_NAME)
    private String room_name;
    @DatabaseField(columnName = VALID_DAY)
    private String valid_day;
    @DatabaseField(columnName = START_AT)
    private String start_at;
    @DatabaseField(columnName = END_AT)
    private String end_at;
    @DatabaseField(columnName = ROOM_LOGO)
    private String roomLogo;
    @DatabaseField(columnName = ROOM_BOOKABLE)
    private String room_bookable;
    private String isMonth;


    public String getRoom_bookable() {
        return room_bookable;
    }

    public void setRoom_bookable(String room_bookable) {
        this.room_bookable = room_bookable;
    }

    public String getIsMonth() {
        return isMonth;
    }

    public void setIsMonth(String isMonth) {
        this.isMonth = isMonth;
    }

    public String getSupported_by() {
        return supported_by;
    }

    public void setSupported_by(String supported_by) {
        this.supported_by = supported_by;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getValid_day() {
        return valid_day;
    }

    public void setValid_day(String valid_day) {
        this.valid_day = valid_day;
    }
//
//    @Override
//    public String getImage() {
//        return image;
//    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getIs_banner() {
        return is_banner;
    }

    public void setIs_banner(String is_banner) {
        this.is_banner = is_banner;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String create_at) {
        this.created_at = create_at;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getValidStart() {
        return validStart;
    }

    public void setValidStart(String validStart) {
        this.validStart = validStart;
    }

    public String getValidEnd() {
        return validEnd;
    }

    public void setValidEnd(String validEnd) {
        this.validEnd = validEnd;
    }


    public void setSaved(boolean saved) {
        this.saved = saved;
    }


    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
    }


    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getTos() {
        return tos;
    }

    public void setTos(String tos) {
        this.tos = tos;
    }


    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }





    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

    public void setRedeemed(String redeemed) {
        this.redeemed = redeemed.equalsIgnoreCase("y") ? true : false;
    }



    public boolean isSaved() {
        return saved;
    }

    public void setSaved(String saved) {
        this.saved = saved.equalsIgnoreCase("y") ? true : false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoomLogo() {
        return roomLogo;
    }

    public void setRoomLogo(String roomLogo) {
        this.roomLogo = roomLogo;
    }

//    @Override
//    public String getTitle() {
//        return getName();
//    }




    public Timeline() {
    }



    public Timeline(Parcel in){
        String[] data = new String[20];

        in.readStringArray(data);
        this.created_at = data[0];
        this.description = data[1];
        this.end_at = data[2];
        this.image = data[3];
        this.is_banner = data[4];
        this.name = data[5];
        this.promo_code = data[6];
        this.room_id = data[7];
        this.room_name = data[8];
        this.start_at = data[9];
        this.supported_by = data[10];
        this.type = data[11];
        this.valid_day =data[12];
        this.redeemed = Boolean.parseBoolean(data[13]);
        this.saved = Boolean.parseBoolean(data[14]);
        this.id = data[15];
        this.updateDate = Long.valueOf(data[16]);
        this.isDeleted = Boolean.parseBoolean(data[17]);
        this.term = data[18];
        this.roomLogo = data[19];
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
            this.created_at,
            this.description,
            this.end_at,
            this.image,
            this.is_banner,
            this.name,
            this.promo_code,
            this.room_id,
            this.room_name,
            this.start_at,
            this.supported_by,
            this.type,
            this.valid_day,
                String.valueOf(this.redeemed),
                String.valueOf(this.saved),
            this.id,
                String.valueOf(this.updateDate),
                String.valueOf(this.isDeleted),
                this.term,
                this.roomLogo
        });
    }
    public static final Creator CREATOR = new Creator() {
        public Timeline createFromParcel(Parcel in) {
            return new Timeline(in);
        }

        public Timeline[] newArray(int size) {
            return new Timeline[size];
        }
    };
}
