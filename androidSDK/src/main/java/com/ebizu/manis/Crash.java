package com.ebizu.manis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author kazao
 */
public abstract class Crash {

    private Context context;
    private String token;

    public Crash() {
        final Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @SuppressLint("NewApi")
			public void uncaughtException(final Thread thread, final Throwable throwable) {
                try {
                    Request request = new Request();
                    request.setPath("/crash/report");
                    String trace = "";
                    StringBuilder stack = new StringBuilder();
                    String code = "";
                    String name = "";
                    String manufacture = "";
                    String model = "";
                    String version = "";
                    String app = context.getPackageName();
                    if (throwable != null) {
                        if (throwable.getStackTrace() != null) {
                            for (StackTraceElement se : throwable.getStackTrace()) {
                                String s = se.toString();
                                stack.append(s);
                                stack.append("\n");
                                if ("".equals(trace) && s.contains(app)) {
                                    trace = s;
                                }
                            }
                        }
                        if (throwable.getCause() != null) {
                            stack.append(throwable.getCause().toString());
                            stack.append("\n");
                            if (throwable.getCause().getStackTrace() != null) {
                                for (StackTraceElement se : throwable.getCause().getStackTrace()) {
                                    String s = se.toString();
                                    stack.append(s);
                                    stack.append("\n");
                                    if ("".equals(trace) && s.contains(app)) {
                                        trace = s;
                                    }
                                }
                            }
                            trace = throwable.getCause().toString() + " - " + trace;
                        }
                    }
                    try {
                        PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                        code = "" + info.versionCode;
                        name = info.versionName;
                    } catch (Exception e) {
                    }
                    try {
                        manufacture = Build.MANUFACTURER;
                        model = Build.MODEL;
                        version = Build.VERSION.RELEASE;
                    } catch (Exception e) {
                    }
                    JSONObject json = new JSONObject();
                    try {
                        json.put("token", token);
                        json.put("trace", trace);
                        json.put("stack", stack.toString());
                        json.put("code", code);
                        json.put("name", name);
                        json.put("manufacture", manufacture);
                        json.put("model", model);
                        json.put("version", version);
                    } catch (Exception e) {

                    }
                    request.setData(json);
                    Worker worker = Worker.create(Session.getInstance(), request, new Callback() {

                        @Override
                        public void onFinish(JSONObject object, JSONArray array, JSONObject error) {
                            handler.uncaughtException(thread, throwable);
                        }
                    });
                    worker.start();
                } catch (Exception e) {
                    handler.uncaughtException(thread, throwable);
                }
            }
        });
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static void fly(Context context, String token) {
        class CrashImpl extends Crash {

        }
        Crash crash = new CrashImpl();
        crash.setContext(context);
        crash.setToken(token);
    }
}
