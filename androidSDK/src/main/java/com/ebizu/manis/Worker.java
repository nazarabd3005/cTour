package com.ebizu.manis;

import android.util.Log;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

/**
 * @author kazao
 */
public class Worker {

    public static int STARTING = 1;
    public static int CONNECTING = 2;
    public static int CONNECTED = 3;
    public static int SENDING_REQUEST = 4;
    public static int REQUEST_SENT = 5;
    public static int RECEIVING_RESPONDS = 6;
    public static int RESPONDS_RECEIVED = 7;
    public static int DISCONNECTING = 8;
    public static int DISCONNECTED = 9;
    public static int FINISH = 10;
    private Request request;
    private Callback callback;
    private final Session session;
    private HttpURLConnection connection;

    //private static String mall_key="";
    public Worker(Session session) {
        this.session = session;
    }

    public void start() {
        start(true);
    }

    public void start(boolean onThread) {
        if (onThread) {
            new Thread() {

                @Override
                public void run() {
                    process();
                }

            }.start();
        } else {
            process();
        }
    }

    private void process() {
        try {
            if (session.getToken() != null) {
                try {
                    request.getRequest().put("t", session.getToken());
                } catch (JSONException e) {
                    Log.i("info", "Setting token error : " + e.getMessage());
                }
            } else {
                Log.i("info", "Token null");
            }
            if (session.getKey() != null) {
                try {
                    request.getRequest().put("k", session.getKey());
                } catch (JSONException e) {
                    Log.i("info", "Setting key error : " + e.getMessage());
                }
            } else {
                Log.i("info", "Key null");
            }
            if (request.getData() != null) {
                try {
                    request.getRequest().put("d", request.getData());
                } catch (JSONException e) {
                }
            }
            Security security = Security.getInstance();
            String sData = security.isReady() ? security.encrypt("" + request.getRequest()) : "" + request.getRequest();
            byte[] data = ("r=" + URLEncoder.encode(sData, "UTF-8")).getBytes("UTF-8");
            // start
            if (callback != null) {
                callback.onStart();
            }
            // create url and connect
            URL url = new URL(session.getUrl() + request.getPath());
            if (session.isHttps()) {
                trustAllHosts();
                connection = (HttpsURLConnection) url.openConnection();
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            // connecting
            if (callback != null) {
                callback.onProgress(CONNECTING, 0);
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.connect();
            // notify on connected
            if (callback != null) {
                callback.onProgress(CONNECTED, 0);
            }
            OutputStream os = connection.getOutputStream();
            // sending request
            if (callback != null) {
                callback.onProgress(SENDING_REQUEST, 0);
            }
            byte[] buffer = new byte[256];
            Logger.l("REQUEST: " + url.toString() + " (" + sData + ")");
            double sent = 0;
            while (sent < data.length) {
                int count = Math.min(buffer.length, data.length - (int) sent);
                os.write(data, (int) sent, count);
                sent += count;
                // sending request
                if (callback != null) {
                    callback.onProgress(SENDING_REQUEST, (int) (sent / data.length * 100));
                }
            }
            os.flush();
            os.close();
            String s = new String(data);
            // request sent
            if (callback != null) {
                callback.onProgress(REQUEST_SENT, 0);
            }
            // receiving response
            if (callback != null) {
                callback.onProgress(RECEIVING_RESPONDS, 0);
            }
            InputStream is;
            int status = connection.getResponseCode();
            if (status >= HttpStatus.SC_BAD_REQUEST) {
                is = connection.getErrorStream();
            } else {
                is = connection.getInputStream();
            }
            int length;
            StringBuilder response = new StringBuilder();
            double total = is.available();
            int received = 0;
            while ((length = is.read(buffer)) > 0) {
                response.append(new String(buffer, 0, length));
                received += length;
                if (total > 0) {
                    // receiving response
                    if (callback != null) {
                        callback.onProgress(RECEIVING_RESPONDS, (int) (received / total * 100));
                    }
                }
            }
            Logger.l("RESPONSE: " + response);
            // response received
            if (callback != null) {
                callback.onProgress(RESPONDS_RECEIVED, 0);
            }
            // disconnecting
            if (callback != null) {
                callback.onProgress(DISCONNECTING, 0);
            }
            is.close();
            connection.disconnect();
            // disconnected
            if (callback != null) {
                callback.onProgress(DISCONNECTED, 0);
            }
            if (callback != null) {
                // finish
                callback.onProgress(FINISH, 100);
                JSONObject result;
                try {
                    String ecrypted = response.toString();
                    result = new JSONObject(!ecrypted.startsWith("{") && security.isReady() ? security.decrypt(ecrypted) : ecrypted);
                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;
                    JSONObject jsonError = null;
                    try {
                        Object json = result.get("d");
                        if (json instanceof JSONObject) {
                            jsonObject = (JSONObject) json;
                        } else if (json instanceof JSONArray) {
                            jsonArray = (JSONArray) json;
                        }
                    } catch (JSONException e) {
                    }
                    try {
                        jsonError = result.getJSONObject("e");
                    } catch (JSONException e) {
                    }
                    callback.onFinish(jsonObject, jsonArray, jsonError);
                } catch (JSONException e) {
                    callback.onFinish(null, null, createError(e.getMessage()));
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFinish(null, null, createError(e.getMessage()));
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            if (callback != null) {
                //callback.onFinish(null, null, createError(e.getMessage()));
                try {
                    callback.onFinish(null, null, new JSONObject().put("m", "Failed to connect"));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFinish(null, null, createError(e.getMessage()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFinish(null, null, createError(e.getMessage()));
            }
        }
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public JSONObject createError(String message) {
        JSONObject json = new JSONObject();
        try {
            json.put("c", -1);
            json.put("m", message);
        } catch (JSONException e) {
        }
        return json;
    }

    public static void appLogin(Session session, String pin, Callback callback) {
        Request request = Request.appLogin();
        try {
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("pin", pin);
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsCatalogue(Session session, JSONObject info, Callback callback) {
        Request request = Request.appsCatalogue();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsItems(Session session, JSONObject info, Callback callback) {
        Request request = Request.appsItems();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsVideos(Session session, JSONObject info, Callback callback) {
        Request request = Request.appsVideos();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsPhotos(Session session, JSONObject info, Callback callback) {
        Request request = Request.appsPhotos();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsReviews(Session session, JSONObject info, Callback callback) {
        Request request = Request.appsReviews();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsReservations(Session session, JSONObject info, Callback callback) {
        Request request = Request.appsReservations();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessBusiness(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessBusiness();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessBusinesses(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessBusinesses();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessBusinessesByInterest(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessBusinessesByInterest();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessCategory(Session session, Callback callback) {
        Request request = Request.businessCategory();
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessDelete(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessDelete();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessEvent(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessEvent();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessEventRsvp(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessEventRsvp();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessEvents(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessEvents();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessFollow(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessFollow();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessFollower(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessFollower();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessForYou(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessForYou();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessInterest(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessInterest();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessLogin(Session session, String username, String password, Callback callback) {
        Request request = Request.businessLogin();
        try {
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("username", username);
            data.put("password", password);

        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessNearby(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessNearby();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessOffer(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessOffer();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessOffers(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessOffers();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessRedeem(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessRedeem();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessRedeemed(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessRedeemed();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessReview(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessReview();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessReward(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessReward();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessRewards(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessRewards();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessSave(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessSave();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessSaved(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessSaved();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessTimeline(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessTimeline();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessVoucher(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessVoucher();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessVouchers(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessVouchers();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meAccount(Session session, JSONObject member, Callback callback) {
        Request request = Request.meAccount();
        request.setData(member);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meActivities(Session session, JSONObject info, Callback callback) {
        Request request = Request.meActivities();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meActivity(Session session, JSONObject info, Callback callback) {
        Request request = Request.meActivity();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meChangePassword(Session session, JSONObject info, Callback callback) {
        Request request = Request.meChangePassword();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meCheckin(Session session, JSONObject info, Callback callback) {
        Request request = Request.meCheckin();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meDevice(Session session, JSONObject info, Callback callback) {
        Request request = Request.meDevice();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meEngagement(Session session, JSONObject info, Callback callback) {
        Request request = Request.meEngagement();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meFacebook(Session session, JSONObject info, Callback callback) {
        Request request = Request.meFacebook();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meFollower(Session session, JSONObject info, Callback callback) {
        Request request = Request.meFollower();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meFollowingBusiness(Session session, JSONObject info, Callback callback) {
        Request request = Request.meFollowingBusiness();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meFollowingPeople(Session session, JSONObject info, Callback callback) {
        Request request = Request.meFollowingPeople();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meInterest(Session session, JSONObject interest, Callback callback) {
        Request request = Request.meInterest();
        request.setData(interest);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meInvite(Session session, JSONObject info, Callback callback) {
        Request request = Request.meInvite();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meInvited(Session session, JSONObject info, Callback callback) {
        Request request = Request.meInvited();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meLogin(Session session, String username, String password, Callback callback) {
        Worker.meLogin(session, username, password, false, callback);
    }

    public static void meLogin(Session session, String username, String password, boolean connectedApp, Callback callback) {
        Request request = Request.meLogin();
        try {
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("username", username);
            data.put("password", password);
            if (connectedApp) {
                data.put("connected_app", "Y");
            }
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meLogout(Session session, Callback callback) {
        Request request = Request.meLogout();
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void mePicture(Session session, JSONObject info, Callback callback) {
        Request request = Request.mePicture();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void mePoint(Session session, Callback callback) {
        Request request = Request.mePoint();
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void mePush(Session session, JSONObject info, Callback callback) {
        Request request = Request.mePush();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meQrCode(Session session, Callback callback) {
        Request request = Request.meQrCode();
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meSettings(Session session, JSONObject info, Callback callback) {
        Request request = Request.meSettings();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meSignUp(Session session, JSONObject member, String password, Callback callback) {
        Request request = Request.meSignUp();
        try {
            request.setData(member);
            request.getData().put("password", password);
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meToken(Session session, String token, Callback callback) {
        Request request = Request.meToken();
        try {
            request.setData(new JSONObject());
            request.getData().put("token", token);
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void peopleFollow(Session session, JSONObject info, Callback callback) {
        Request request = Request.peopleFollow();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void peopleFollower(Session session, JSONObject info, Callback callback) {
        Request request = Request.peopleFollower();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void peopleFollowingBusiness(Session session, JSONObject info, Callback callback) {
        Request request = Request.peopleFollowingBusiness();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void peopleFollowingPeople(Session session, JSONObject info, Callback callback) {
        Request request = Request.peopleFollowingPeople();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void peoplePeople(Session session, JSONObject info, Callback callback) {
        Request request = Request.peoplePeople();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void peoplePeoples(Session session, JSONObject info, Callback callback) {
        Request request = Request.peoplePeoples();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static Worker create(Session session, Request request) {
        return create(session, request, null);
    }

    public static Worker create(Session session, Request request, Callback callback) {
        Worker worker = new Worker(session);
        worker.setRequest(request);
        worker.setCallback(callback);
        return worker;
    }

    /*public static Worker create(String mall_key, Session session, Request request, Callback callback) {
     Worker worker = new Worker(session);
     worker.setRequest(request);
     worker.setCallback(callback);
     return worker;
     }*/
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws java.security.cert.CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
        } catch (KeyManagementException e) {
        }
    }

    public static void settingsHelp(Session session, JSONObject info, Callback callback) {
        Request request = Request.settingsHelp();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void aboutLegal(Session session, JSONObject info, Callback callback) {
        Request request = Request.aboutLegal();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessRecentBusiness(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessRecentBusiness();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessStatistic(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessStatistic();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessSnapearnCheckin(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessSnapearnCheckin();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessReceipt(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessReceipt();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meForgotPassword(Session session, JSONObject info, Callback callback) {
        Request request = Request.meForgotPassword();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meVerifyCode(Session session, JSONObject info, Callback callback) {
        Request request = Request.meVerifyCode();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meResetPassword(Session session, JSONObject info, Callback callback) {
        Request request = Request.meResetPassword();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessSnapEarnHistory(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessSnapEarnHistory();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsMenu(Session session, JSONObject info, Callback callback) {
        Request request = Request.appsMenu();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsSetting(Session session, JSONObject info, Callback callback) {
        Request request = Request.appsSetting();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsTransaction(Session session, JSONObject info, Callback callback) {
        Request request = Request.appsTransaction();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meOrder(Session session, JSONObject info, Callback callback) {
        Request request = Request.meOrder();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meSnapearnHistory(Session session, JSONObject info, Callback callback) {
        Request request = Request.meSnapearnHistory();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void loyaltyCheckIn(Session session, String tag, Callback callback) {
        Request request = Request.loyaltyCheckIn();
        try {
            request.setData(new JSONObject());
            request.getData().put("tag", tag);
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void loyaltyOffers(Session session, String page, Callback callback) {
        Request request = Request.loyaltyOffers();
        try {
            request.setData(new JSONObject());
            request.getData().put("page", page);
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessAccount(Session session, Callback callback) {
        Request request = Request.businessAccount();
        try {
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("t", session);
        } catch (JSONException e) {

        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void loyaltySaved(Session session, String type, String member, String page, Callback callback) {
        Request request = Request.loyaltySaved();
        try {
            request.setData(new JSONObject());
            request.getData().put("type", type);
            request.getData().put("member", member);
            request.getData().put("page", page);
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void loyaltyRedeem(Session session, String type, String memberId, String offerId, Callback callback) {
        Request request = Request.loyaltyRedeem();
        try {
            request.setData(new JSONObject());
            request.getData().put("type", type);
            request.getData().put("member", memberId);
            request.getData().put("id", offerId);
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void mePing(Session session, Callback callback) {
        Request request = Request.mePing();
        try {
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("t", session);
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsAppointments(Session session, JSONObject info, Callback callback) {
        Request request = Request.appsAppointments();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    ////------------------ MENU 23juli2014 ------------------\\
    /*
     public static void menuCategories(Session session, String token,Callback callback) {
     Request request = Request.menuCategories();
     try {
     request.setData(new JSONObject());
     JSONObject data = request.getData();
     data.put("t", token);
     } catch (JSONException e)
     {

     }
     Worker worker = Worker.create(session, request, callback);
     worker.start();
     }
     public static void menuItems(Session session, String token, String id,Callback callback) {
     Request request = Request.menuItems();
     try {
     request.setData(new JSONObject());
     JSONObject data = request.getData();
     data.put("t", token);
     data.put("id", id);
     } catch (JSONException e)
     {

     }
     Worker worker = Worker.create(session, request, callback);
     worker.start();
     }
     public static void menuItemsModifier(Session session, String token, String itemId,Callback callback) {
     Request request = Request.menuItemsModifier();
     try {
     request.setData(new JSONObject());
     JSONObject data = request.getData();
     data.put("t", token);
     data.put("id", itemId);
     } catch (JSONException e)
     {

     }
     Worker worker = Worker.create(session, request, callback);
     worker.start();
     }
     public static void menuSetting(Session session,Callback callback)
     {
     Request request = Request.menuSetting();
     try
     {
     request.setData(new JSONObject());
     JSONObject data = request.getData();
     data.put("t", session);
     } catch (JSONException e)
     {

     }
     Worker worker = Worker.create(session, request, callback);
     worker.start();
     }
     public static void menuTransaction(Session session,JSONObject object,Callback callback)
     {
     Request request = Request.menuTransaction();
     try
     {
     request.setData(object);
     JSONObject data = request.getData();
     data.put("t", session);

     } catch (JSONException e)
     {

     }
     Worker worker = Worker.create(session, request, callback);
     worker.start();
     }
     public static void menuCheckIn(Session session, String token, String qrCode ,Callback callback) {
     Request request = Request.menuCheckIn();
     try {
     request.setData(new JSONObject());
     JSONObject data = request.getData();
     //data.put("t", token);
     data.put("tag", qrCode);
     } catch (JSONException e)
     {

     }
     Worker worker = Worker.create(session, request, callback);
     worker.start();
     }*/
    ////------------------ POINT OF SALE 23juli2014 ------------------\\
    public static void pointofsaleCategories(Session session, String token, Callback callback) {
        Request request = Request.pointofsaleCategories();
        try {
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("t", token);
        } catch (JSONException e) {

        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void pointofsaleItems(Session session, String token, String id, Callback callback) {
        Request request = Request.pointofsaleItems();
        try {
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("t", token);
            data.put("id", id);
        } catch (JSONException e) {

        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void pointofsaleItemsModifier(Session session, String token, String itemId, Callback callback) {
        Request request = Request.pointofsaleItemsModifier();
        try {
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("t", token);
            data.put("id", itemId);
        } catch (JSONException e) {

        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void pointofsaleSetting(Session session, Callback callback) {
        Request request = Request.pointofsaleSetting();
        try {
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("t", session);
        } catch (JSONException e) {

        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void pointofsaleTransaction(Session session, JSONObject object, Callback callback) {
        Request request = Request.pointofsaleTransaction();
        try {
            request.setData(object);
            JSONObject data = request.getData();
            data.put("t", session);

        } catch (JSONException e) {

        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void pointofsaleCustomer(Session session, Callback callback) {
        Request request = Request.pointofsaleCustomer();
        try {
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("t", session);

        } catch (JSONException e) {

        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void pointofsaleMenu(Session session, Callback callback) {
        Request request = Request.pointofsaleMenu();
        request.setData(new JSONObject());
        //JSONObject data = request.getData();
        //data.put("t", session);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void pointofsaleUserRights(Session session, Callback callback) {
        Request request = Request.pointofsaleUserRights();
        try {
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("t", session);

        } catch (JSONException e) {

        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }


    /*
     * BSC
     */
    public static void businessInterestBsc(Session session, JSONObject info, String mall_key, Callback callback) {
        Request request = Request.businessInterest();
        //Log.d("EKSEKUSI","businessInterestBsc");
        try {
            request.getRequest().put("k", mall_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessTimelineBsc(Session session, JSONObject info, String mall_key, Callback callback) {
        Request request = Request.businessTimeline();
        try {
            request.getRequest().put("k", mall_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meLoginBsc(Session session, String username, String password, String mall_key, boolean connectedApp, Callback callback) {
        Request request = Request.meLogin();
        try {
            //request.getRequest().put("k", mall_key);
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("username", username);
            data.put("password", password);
            data.put("mall_key", mall_key);
            if (connectedApp) {
                data.put("connected_app", "Y");
            }
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meSignUpBsc(Session session, JSONObject member, String password, String mall_key, Callback callback) {
        Request request = Request.meSignUp();
        try {
            //request.getRequest().put("k", mall_key);
            request.setData(member);
            request.getData().put("password", password);
            request.getData().put("mall_key", mall_key);
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meInterestBsc(Session session, JSONObject interest, String mall_key, Callback callback) {
        Request request = Request.meInterest();
        try {
            request.getRequest().put("k", mall_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(interest);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessBusinessesByInterestBsc(Session session, JSONObject info, String mall_key, Callback callback) {
        Request request = Request.businessBusinessesByInterest();
        try {
            request.getRequest().put("k", mall_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void aboutLegalBsc(Session session, JSONObject info, String mall_key, Callback callback) {
        Request request = Request.aboutLegalBsc();
        try {
            request.getRequest().put("k", mall_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meActivitiesBsc(Session session, JSONObject info, String mall_key, Callback callback) {
        Request request = Request.meActivities();
        try {
            request.getRequest().put("k", mall_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsAppointmentsBsc(Session session, JSONObject info, String mall_key, Callback callback) {
        Request request = Request.appsAppointments();
        try {
            request.getRequest().put("k", mall_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsItemsBsc(Session session, JSONObject info, String mall_key, Callback callback) {
        Request request = Request.appsItems();
        try {
            request.getRequest().put("k", mall_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsMenuBsc(Session session, JSONObject info, String mall_key, Callback callback) {
        Request request = Request.appsMenu();
        try {
            request.getRequest().put("k", mall_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsSettingBsc(Session session, JSONObject info, String mall_key, Callback callback) {
        Request request = Request.appsSetting();
        try {
            request.getRequest().put("k", mall_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsTransactionBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.appsTransaction();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meOrderBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meOrder();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsPhotosBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.appsPhotos();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsReservationsBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.appsReservations();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsReviewsBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.appsReviews();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessSnapearnCheckinBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessSnapearnCheckin();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void appsVideosBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.appsVideos();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meLogoutBsc(Session session, String k, Callback callback) {
        Request request = Request.meLogout();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void mePictureBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.mePicture();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meEngagementBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meEngagement();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessStatisticBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessStatistic();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessBusinessBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessBusiness();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meChangePasswordBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meChangePassword();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessForYouBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessForYou();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessBusinessesBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessBusinesses();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meAccountBsc(Session session, JSONObject member, String k, Callback callback) {
        Request request = Request.meAccount();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(member);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessEventBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessEvent();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessRedeemBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessRedeem();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meForgotPasswordBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meForgotPassword();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meVerifyCodeBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meVerifyCode();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meResetPasswordBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meResetPassword();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meInviteBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meInvite();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meFacebookBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meFacebook();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meInvitedBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meInvited();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meDeviceBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meDevice();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meTokenBsc(Session session, String token, String k, Callback callback) {
        Request request = Request.meToken();
        try {
            request.getRequest().put("k", k);
            request.setData(new JSONObject());
            request.getData().put("token", token);
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void mePushBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.mePush();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meCheckinBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meCheckin();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void mePingBsc(Session session, String k, Callback callback) {
        Request request = Request.mePing();
        try {
            request.getRequest().put("k", k);
            request.setData(new JSONObject());
            JSONObject data = request.getData();
            data.put("t", session);
        } catch (JSONException e) {
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessDeleteBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessDelete();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessSaveBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessSave();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessSavedBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessSaved();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessOfferBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessOffer();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessOffersBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessOffers();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessRedeemedBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessRedeemed();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessRewardsBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessRewards();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessVoucherBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessVoucher();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessVouchersBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessVouchers();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessRewardBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessReward();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void settingsHelpBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.settingsHelp();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void meSettingsBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.meSettings();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessSnapEarnHistoryBsc(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.businessSnapEarnHistory();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessCategoryBsc(Session session, String k, Callback callback) {
        Request request = Request.businessCategory();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void beaconUuid(Session session, int size, String k, Callback callback) {
        Request request = Request.beaconUuid();
        JSONObject data = new JSONObject();
        try {
            data.put("size", size);
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(data);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }
    /*public static void beaconPromotion(Session session,String uuid,String major,String minor,String k, Callback callback) {
     Request request = Request.beaconPromotion();
     JSONObject data= new JSONObject();
     try {
     data.put("uuid", uuid);
     data.put("major", major);
     data.put("minor", minor); 
     data.put("mall", k); 
     request.getRequest().put("k",k);
     request.getRequest().put("t","");
     } catch (JSONException e) {
     e.printStackTrace();
     }
     request.setData(data);
     Worker worker = Worker.create(session, request, callback);
     worker.start();
     } */

    public static void beaconPromotion(Session session, JSONObject data, String k, Callback callback) {
        Request request = Request.beaconPromotion();
        try {
            request.setData(data);
            request.getRequest().put("k", k);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void beaconnIndoorsBsc(Session session, String k, Callback callback) {
        Request request = Request.beaconIndoors();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void invitationCode(Session session, JSONObject data, String k, Callback callback) {
        Request request = Request.invitationCode();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(data);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void invitationCheck(Session session, JSONObject data, String k, Callback callback) {
        Request request = Request.invitationCheck();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(data);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineGet(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.offlineGet();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineTimeline(Session session, JSONObject info, Callback callback) {
        Request request = Request.offlineTimeline();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineTimelineCheck(Session session, JSONObject info, Callback callback) {
        Request request = Request.offlineTimelineCheck();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineBusinesses(Session session, JSONObject info, Callback callback) {
        Request request = Request.offlineBusinesses();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineBusinessesCheck(Session session, JSONObject info, Callback callback) {
        Request request = Request.offlineBusinessesCheck();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineCategory(Session session, JSONObject info, Callback callback) {
        Request request = Request.offlineCategory();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineCategoryCheck(Session session, JSONObject info, Callback callback) {
        Request request = Request.offlineCategoryCheck();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void businessAds(Session session, JSONObject info, Callback callback) {
        Request request = Request.businessAds();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineActivity(Session session, JSONObject info, Callback callback) {
        Request request = Request.offlineActivity();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    // 9-2-2015
    public static void offlineTimeline(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.offlineTimeline();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void beaconofflineListAds(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.beaconofflineListAds();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlinePromotions(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.offlinePromotions();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineBusinesses(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.offlineBusinesses();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineTimelineCheckSum(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.offlineTimelineCheckSum();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlinePromotionCheckSum(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.offlinePromotionCheckSum();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineBusinessesCheckSum(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.offlineBusinessesCheckSum();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void offlineActivity(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.offlineActivity();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }

    public static void settingsHelpBsc(Session session, JSONObject info, Callback callback) {
        Request request = Request.settingsHelpBsc();
        request.setData(info);
        Worker worker = Worker.create(session, request, callback);
        worker.start();
    }
}
