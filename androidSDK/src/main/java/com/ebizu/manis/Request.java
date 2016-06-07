package com.ebizu.manis;

import org.json.JSONObject;

/**
 * @author kazao
 */
public class Request {

    private String path;
    private JSONObject request = new JSONObject();
    private JSONObject data;

    public Request() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JSONObject getRequest() {
        return request;
    }

    public void setRequest(JSONObject request) {
        this.request = request;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public static Request appLogin() {
        Request request = new Request();
        request.setPath("/app/login");
        return request;
    }

    public static Request appsVideos() {
        Request request = new Request();
        request.setPath("/apps/videos");
        return request;
    }

    public static Request appsCatalogue() {
        Request request = new Request();
        request.setPath("/apps/catalogue");
        return request;
    }

    public static Request appsItems() {
        Request request = new Request();
        request.setPath("/apps/items");
        return request;
    }

    public static Request appsPhotos() {
        Request request = new Request();
        request.setPath("/apps/photos");
        return request;
    }

    public static Request appsReviews() {
        Request request = new Request();
        request.setPath("/apps/reviews");
        return request;
    }

    public static Request appsReservations() {
        Request request = new Request();
        request.setPath("/apps/reservation");
        return request;
    }

    public static Request businessBusiness() {
        Request request = new Request();
        request.setPath("/business/business");
        return request;
    }

    public static Request businessBusinesses() {
        Request request = new Request();
        request.setPath("/business/businesses");
        return request;
    }

    public static Request businessBusinessesByInterest() {
        Request request = new Request();
        request.setPath("/business/businessesbyinterest");
        return request;
    }

    public static Request businessCategory() {
        Request request = new Request();
        request.setPath("/business/category");
        return request;
    }

    public static Request businessDelete() {
        Request request = new Request();
        request.setPath("/business/delete");
        return request;
    }

    public static Request businessEvent() {
        Request request = new Request();
        request.setPath("/business/event");
        return request;
    }

    public static Request businessEventRsvp() {
        Request request = new Request();
        request.setPath("/business/eventrsvp");
        return request;
    }

    public static Request businessEvents() {
        Request request = new Request();
        request.setPath("/business/events");
        return request;
    }

    public static Request businessFollow() {
        Request request = new Request();
        request.setPath("/business/follow");
        return request;
    }

    public static Request businessFollower() {
        Request request = new Request();
        request.setPath("/business/follower");
        return request;
    }

    public static Request businessForYou() {
        Request request = new Request();
        request.setPath("/business/foryou");
        return request;
    }

    public static Request businessInterest() {
        Request request = new Request();
        request.setPath("/business/interest");
        return request;
    }

    public static Request businessLogin() {
        Request request = new Request();
        request.setPath("/business/login");
        return request;
    }

    public static Request businessLogout() {
        Request request = new Request();
        request.setPath("/business/logout");
        return request;
    }

    public static Request businessNearby() {
        Request request = new Request();
        request.setPath("/business/nearby");
        return request;
    }

    public static Request businessOffer() {
        Request request = new Request();
        request.setPath("/business/offer");
        return request;
    }

    public static Request businessOffers() {
        Request request = new Request();
        request.setPath("/business/offers");
        return request;
    }

    public static Request businessRedeem() {
        Request request = new Request();
        request.setPath("/business/redeem");
        return request;
    }

    public static Request businessRedeemed() {
        Request request = new Request();
        request.setPath("/business/redeemed");
        return request;
    }

    public static Request businessReview() {
        Request request = new Request();
        request.setPath("/business/review");
        return request;
    }

    public static Request businessReward() {
        Request request = new Request();
        request.setPath("/business/reward");
        return request;
    }

    public static Request businessRewards() {
        Request request = new Request();
        request.setPath("/business/rewards");
        return request;
    }

    public static Request businessSave() {
        Request request = new Request();
        request.setPath("/business/save");
        return request;
    }

    public static Request businessSaved() {
        Request request = new Request();
        request.setPath("/business/saved");
        return request;
    }

    public static Request businessTimeline() {
        Request request = new Request();
        request.setPath("/business/timeline");
        return request;
    }

    public static Request businessTimelineCheck() {
        Request request = new Request();
        request.setPath("/business/timelinecheck");
        return request;
    }

    public static Request businessToken() {
        Request request = new Request();
        request.setPath("/business/token");
        return request;
    }

    public static Request businessVoucher() {
        Request request = new Request();
        request.setPath("/business/voucher");
        return request;
    }

    public static Request businessVouchers() {
        Request request = new Request();
        request.setPath("/business/vouchers");
        return request;
    }

    public static Request meAccount() {
        Request request = new Request();
        request.setPath("/me/account");
        return request;
    }

    public static Request meActivity() {
        Request request = new Request();
        request.setPath("/me/activity");
        return request;
    }

    public static Request meActivities() {
        Request request = new Request();
        request.setPath("/me/activities");
        return request;
    }

    public static Request meCheckin() {
        Request request = new Request();
        request.setPath("/me/checkin");
        return request;
    }

    public static Request meDevice() {
        Request request = new Request();
        request.setPath("/me/device");
        return request;
    }

    public static Request meEngagement() {
        Request request = new Request();
        request.setPath("/me/engagement");
        return request;
    }

    public static Request meFacebook() {
        Request request = new Request();
        request.setPath("/me/facebook");
        return request;
    }

    public static Request meFollower() {
        Request request = new Request();
        request.setPath("/me/follower");
        return request;
    }

    public static Request meFollowingBusiness() {
        Request request = new Request();
        request.setPath("/me/followingbusiness");
        return request;
    }

    public static Request meFollowingPeople() {
        Request request = new Request();
        request.setPath("/me/followingpeople");
        return request;
    }

    public static Request meInterest() {
        Request request = new Request();
        request.setPath("/me/interest");
        return request;
    }

    public static Request meInvite() {
        Request request = new Request();
        request.setPath("/me/invite");
        return request;
    }

    public static Request meInvited() {
        Request request = new Request();
        request.setPath("/me/invited");
        return request;
    }

    public static Request meLogin() {
        Request request = new Request();
        request.setPath("/me/login");
        return request;
    }

    public static Request meLogout() {
        Request request = new Request();
        request.setPath("/me/logout");
        return request;
    }

    public static Request mePicture() {
        Request request = new Request();
        request.setPath("/me/picture");
        return request;
    }

    public static Request meChangePassword() {
        Request request = new Request();
        request.setPath("/me/password");
        return request;
    }

    public static Request mePoint() {
        Request request = new Request();
        request.setPath("/me/point");
        return request;
    }

    public static Request mePush() {
        Request request = new Request();
        request.setPath("/me/push");
        return request;
    }

    public static Request meQrCode() {
        Request request = new Request();
        request.setPath("/me/qrcode");
        return request;
    }

    public static Request meSettings() {
        Request request = new Request();
        request.setPath("/me/settings");
        return request;
    }

    public static Request meSignUp() {
        Request request = new Request();
        request.setPath("/me/signup");
        return request;
    }

    public static Request meToken() {
        Request request = new Request();
        request.setPath("/me/token");
        return request;
    }

    public static Request peopleFollow() {
        Request request = new Request();
        request.setPath("/people/follow");
        return request;
    }

    public static Request peopleFollower() {
        Request request = new Request();
        request.setPath("/people/follower");
        return request;
    }

    public static Request peopleFollowingBusiness() {
        Request request = new Request();
        request.setPath("/people/followingbusiness");
        return request;
    }

    public static Request peopleFollowingPeople() {
        Request request = new Request();
        request.setPath("/people/followingpeople");
        return request;
    }

    public static Request peoplePeople() {
        Request request = new Request();
        request.setPath("/people/people");
        return request;
    }

    public static Request peoplePeoples() {
        Request request = new Request();
        request.setPath("/people/peoples");
        return request;
    }

    public static Request settingsHelp() {
        Request request = new Request();
        request.setPath("/help/content");
        return request;
    }

    public static Request aboutLegal() {
        Request request = new Request();
        request.setPath("/help/legal");
        return request;
    }

    public static Request businessRecentBusiness() {
        Request request = new Request();
        request.setPath("/business/recentbusiness");
        return request;
    }

    public static Request businessStatistic() {
        Request request = new Request();
        request.setPath("/business/statistic");
        return request;
    }

    public static Request businessSnapearnCheckin() {
        Request request = new Request();
        request.setPath("/business/snapearncheckin");
        return request;
    }

    public static Request businessReceipt() {
        Request request = new Request();
        request.setPath("/business/receipt");
        return request;
    }

    public static Request meForgotPassword() {
        Request request = new Request();
        request.setPath("/me/forgotpassword");
        return request;
    }

    public static Request meVerifyCode() {
        Request request = new Request();
        request.setPath("/me/verifycode");
        return request;
    }

    public static Request meResetPassword() {
        Request request = new Request();
        request.setPath("/me/resetpassword");
        return request;
    }

    public static Request businessSnapEarnHistory() {
        Request request = new Request();
        request.setPath("/business/snapearnhistory");
        return request;
    }

    public static Request appsMenu() {
        Request request = new Request();
        request.setPath("/apps/menu");
        return request;
    }

    public static Request appsSetting() {
        Request request = new Request();
        request.setPath("/apps/setting");
        return request;
    }

    public static Request appsTransaction() {
        Request request = new Request();
        request.setPath("/apps/transaction");
        return request;
    }

    public static Request meOrder() {
        Request request = new Request();
        request.setPath("/me/order");
        return request;
    }

    public static Request meSnapearnHistory() {
        Request request = new Request();
        request.setPath("/me/snapearnlist");
        return request;
    }

    public static Request loyaltyCheckIn() {
        Request request = new Request();
        request.setPath("/loyalty/checkin");
        return request;
    }

    public static Request loyaltyOffers() {
        Request request = new Request();
        request.setPath("/loyalty/offers");
        return request;
    }

    public static Request businessAccount() {
        Request request = new Request();
        request.setPath("/business/account");
        return request;
    }

    public static Request loyaltySaved() {
        Request request = new Request();
        request.setPath("/loyalty/saved");
        return request;
    }

    public static Request loyaltyRedeem() {
        Request request = new Request();
        request.setPath("/loyalty/redeem");
        return request;
    }

    public static Request mePing() {
        Request request = new Request();
        request.setPath("/me/ping");
        return request;
    }

    public static Request appsAppointments() {
        Request request = new Request();
        request.setPath("/apps/appointment");
        return request;
    }

    //MENU 23 Juli 2014
    public static Request menuCategories() {
        Request request = new Request();
        request.setPath("/menu/categories");
        return request;
    }

    public static Request menuItems() {
        Request request = new Request();
        request.setPath("/menu/items");
        return request;
    }

    public static Request menuItemsModifier() {
        Request request = new Request();
        request.setPath("/menu/modifiers");
        return request;
    }

    public static Request menuSetting() {
        Request request = new Request();
        request.setPath("/menu/setting");
        return request;
    }

    public static Request menuTransaction() {
        Request request = new Request();
        request.setPath("/menu/transaction");
        return request;
    }

    public static Request menuCheckIn() {
        Request request = new Request();
        request.setPath("/menu/checkin");
        return request;
    }

    //POINT OF SALE 23 Juli 2014
    public static Request pointofsaleCategories() {
        Request request = new Request();
        request.setPath("/pointofsale/categories");
        return request;
    }

    public static Request pointofsaleItems() {
        Request request = new Request();
        request.setPath("/pointofsale/items");
        return request;
    }

    public static Request pointofsaleItemsModifier() {
        Request request = new Request();
        request.setPath("/pointofsale/modifiers");
        return request;
    }

    public static Request pointofsaleSetting() {
        Request request = new Request();
        request.setPath("/pointofsale/setting");
        return request;
    }

    public static Request pointofsaleTransaction() {
        Request request = new Request();
        request.setPath("/pointofsale/transaction");
        return request;
    }

    public static Request pointofsaleCustomer() {
        Request request = new Request();
        request.setPath("/pointofsale/customer");
        return request;
    }

    public static Request pointofsaleMenu() {
        Request request = new Request();
        request.setPath("/pointofsale/menu");
        return request;
    }

    public static Request pointofsaleUserRights() {
        Request request = new Request();
        request.setPath("/pointofsale/userrights");
        return request;
    }

    public static Request getBeacon() {
        Request request = new Request();
        request.setPath("/beacon/uuid");
        return request;
    }

    public static Request getBeaconPromo() {
        Request request = new Request();
        request.setPath("/beacon/promotion");
        return request;
    }

    public static Request beaconUuid() {
        Request request = new Request();
        request.setPath("/beacon/uuid");
        return request;
    }

    /*public static Request beaconPromotion() 
     {
     Request request = new Request();
     request.setPath("/beacon/promotion");
     return request;
     }*/
    public static Request beaconPromotion() {
        Request request = new Request();
        request.setPath("/beacon/promotion");
        return request;
    }

    public static Request beaconIndoors() {
        Request request = new Request();
        request.setPath("/beacon/indoors");
        return request;
    }

    public static Request invitationCode() {
        Request request = new Request();
        request.setPath("/invitation/code");
        return request;
    }

    public static Request invitationCheck() {
        Request request = new Request();
        request.setPath("/invitation/check");
        return request;
    }

    public static Request offlineGet() {
        Request request = new Request();
        request.setPath("/offline/get");
        return request;
    }

    public static Request offlineTimeline() {
        Request request = new Request();
        request.setPath("/offline/timeline");
        return request;
    }

    public static Request offlineTimelineCheck() {
        Request request = new Request();
        request.setPath("/offline/timelinecheck");
        return request;
    }

    public static Request offlineBusinesses() {
        Request request = new Request();
        request.setPath("/offline/businesses");
        return request;
    }

    public static Request offlineBusinessesCheck() {
        Request request = new Request();
        request.setPath("/offline/businessescheck");
        return request;
    }

    public static Request offlineCategory() {
        Request request = new Request();
        request.setPath("/offline/category");
        return request;
    }

    public static Request offlineCategoryCheck() {
        Request request = new Request();
        request.setPath("/offline/categorycheck");
        return request;
    }

    public static Request businessAds() {
        Request request = new Request();
        request.setPath("/business/ads");
        return request;
    }

    public static Request offlineActivity() {
        Request request = new Request();
        request.setPath("/offline/activity");
        return request;
    }

    ///9-2-2015 
    public static Request beaconofflineListAds() {
        Request request = new Request();
        request.setPath("/Beaconoffline/Listads");
        return request;
    }

    public static Request offlinePromotions() {
        Request request = new Request();
        request.setPath("/offline/promotions");
        return request;
    }

    public static Request settingsHelpBsc() {
        Request request = new Request();
        request.setPath("/help/content");
        return request;
    }

    public static Request aboutLegalBsc() {
        Request request = new Request();
        request.setPath("/help/legal");
        return request;
    }

    public static Request offlineTimelineCheckSum() {
        Request request = new Request();
        request.setPath("/offline/timelinecheck");
        return request;
    }

    public static Request offlinePromotionCheckSum() {
        Request request = new Request();
        request.setPath("/offline/promotionscheck");
        return request;
    }

    public static Request offlineBusinessesCheckSum() {
        Request request = new Request();
        request.setPath("/offline/businessescheck");
        return request;
    }
}
