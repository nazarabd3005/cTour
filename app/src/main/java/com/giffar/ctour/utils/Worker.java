package com.giffar.ctour.utils;

import android.util.Log;

import com.ebizu.manis.Logger;
import com.giffar.ctour.Session;
import com.giffar.ctour.callbacks.Callback;

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
 * Created by wafdamufti on 11/17/15.
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

    // zouk app

    public static void Login(Session session, String username, String password, Callback callback) {
        Request request = Request.meLogin();
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

    public static void meAccount(Session session, JSONObject member, Callback callback) {
        Request request = Request.meAccount();
        request.setData(member);
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

    public static void offlineTimeline(Session session, JSONObject info, String k, Callback callback) {
        Request request = Request.timeline();
        try {
            request.getRequest().put("k", k);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.setData(info);
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

    public static void meForgotPassword(Session session, JSONObject info, Callback callback) {
        Request request = Request.meForgotPassword();
        request.setData(info);
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



}
