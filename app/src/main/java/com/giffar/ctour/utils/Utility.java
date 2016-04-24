package com.giffar.ctour.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wafdamufti on 11/17/15.
 */
public class Utility {

    private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static String createPadding(int level) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < level; i++) {
            buffer.append(" ");
        }
        return buffer.toString();
    }

    public static String subtitute(String text, String... args) {
        StringBuilder builder = new StringBuilder(text);
        int index = 0;
        for (String arg : args) {
            int x = builder.indexOf("?", index);
            if (x != 0) {
                builder.replace(x, x + 1, arg);
                index = x + 1;
            }
        }
        return builder.toString();
    }

    public static void dump(Object object) {
        dump(object, 0);
    }

    public static void dump(Object object, int level) {
        if (object != null) {
            if (object instanceof JSONObject) {
                System.out.println(createPadding(level) + "JSONObject: {");
                JSONObject json = (JSONObject) object;
                JSONArray keys = json.names();
                if (keys != null) {
                    for (int i = 0; i < keys.length(); i++) {
                        try {
                            Object value = json.get(keys.getString(i));
                            if (value instanceof JSONObject || value instanceof JSONArray) {
                                System.out.println(createPadding(level + 1) + keys.getString(i) + ":");
                                dump(value, level + 1);
                            } else {
                                System.out.println(createPadding(level + 1) + keys.getString(i) + ": " + value);
                            }
                        } catch (JSONException e) {

                        }
                    }
                }
                System.out.println(createPadding(level) + "}");
            } else if (object instanceof JSONArray) {
                System.out.println(createPadding(level) + "JSONArray: {");
                JSONArray json = (JSONArray) object;
                for (int i = 0; i < json.length(); i++) {
                    try {
                        Object value = json.get(i);
                        if (value instanceof JSONObject || value instanceof JSONArray) {
                            System.out.println(createPadding(level + 1) + "[" + i + "]:");
                            dump(value, level + 1);
                        } else {
                            System.out.println(createPadding(level + 1) + "[" + i + "]: " + value);
                        }
                    } catch (JSONException e) {

                    }
                }
                System.out.println(createPadding(level) + "}");
            }
        }
    }

    public static String encode(String data) {
        try {
            return URLEncoder.encode(data, "utf8");
        } catch (UnsupportedEncodingException e) {
        }
        return data;
    }

    public static String formatDate(Date date) {
        try {
            return FORMAT_DATE.format(date);
        } catch (Exception e) {
        }
        return null;
    }

    public static String formatTime(Date time) {
        try {
            return FORMAT_TIME.format(time);
        } catch (Exception e) {
        }
        return null;
    }

    public static String formatDateTime(Date datetime) {
        try {
            return FORMAT_DATE_TIME.format(datetime);
        } catch (Exception e) {
        }
        return null;
    }

    public static String load(InputStream is) {
        StringBuilder builder = new StringBuilder();
        InputStreamReader inputreader = new InputStreamReader(is);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        try {
            while ((line = buffreader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } catch (Exception e) {
        }
        return builder.toString();
    }

}
