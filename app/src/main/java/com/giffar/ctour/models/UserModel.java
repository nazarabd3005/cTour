package com.giffar.ctour.models;

import android.content.Context;

import com.giffar.ctour.APP;
import com.giffar.ctour.Preferences;
import com.giffar.ctour.entitys.User;


/**
 * Created by egiadtya on 2/25/15.
 */
public class UserModel extends BaseModel<User> {

    public UserModel(Context context) {
        super(context, User.class);
    }

    public static User getMe(Context context) {
        UserModel userModel = new UserModel(context);
        String loggedUserId = APP.getConfig(context, Preferences.LOGGED_USER_ID);
        return (User) userModel.findBy(User.ID, loggedUserId);
    }

//    public static void setSavedOfferCount(Context context,boolean isSave){
//        UserModel userModel = new UserModel(context);
//        User user = getMe(context);
//        int savedOffer = user.getSavedOffer();
//        if (isSave)
//            savedOffer = savedOffer +1;
//        else
//            savedOffer = savedOffer -1;
//
//        if (savedOffer < 0)
//            savedOffer = 0;
//        APP.log("Saved Offer : " + savedOffer);
//        user.setSavedOffer(savedOffer);
//        userModel.update(user);
//    }
}
