package com.giffar.ctour.helpers;

import android.content.Context;
import android.util.SparseArray;

import com.giffar.ctour.entitys.Country;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by nazar on 24/03/16.
 */
public class PhoneHelper {
    protected static final TreeSet<String> CANADA_CODES = new TreeSet<String>();
    static {
        CANADA_CODES.add("204");
        CANADA_CODES.add("236");
        CANADA_CODES.add("249");
        CANADA_CODES.add("250");
        CANADA_CODES.add("289");
        CANADA_CODES.add("306");
        CANADA_CODES.add("343");
        CANADA_CODES.add("365");
        CANADA_CODES.add("387");
        CANADA_CODES.add("403");
        CANADA_CODES.add("416");
        CANADA_CODES.add("418");
        CANADA_CODES.add("431");
        CANADA_CODES.add("437");
        CANADA_CODES.add("438");
        CANADA_CODES.add("450");
        CANADA_CODES.add("506");
        CANADA_CODES.add("514");
        CANADA_CODES.add("519");
        CANADA_CODES.add("548");
        CANADA_CODES.add("579");
        CANADA_CODES.add("581");
        CANADA_CODES.add("587");
        CANADA_CODES.add("604");
        CANADA_CODES.add("613");
        CANADA_CODES.add("639");
        CANADA_CODES.add("647");
        CANADA_CODES.add("672");
        CANADA_CODES.add("705");
        CANADA_CODES.add("709");
        CANADA_CODES.add("742");
        CANADA_CODES.add("778");
        CANADA_CODES.add("780");
        CANADA_CODES.add("782");
        CANADA_CODES.add("807");
        CANADA_CODES.add("819");
        CANADA_CODES.add("825");
        CANADA_CODES.add("867");
        CANADA_CODES.add("873");
        CANADA_CODES.add("902");
        CANADA_CODES.add("905");
    }
    protected SparseArray<ArrayList<Country>> mCountriesMap = new SparseArray<ArrayList<Country>>();
    protected PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();

    Context mContext;
    public PhoneHelper(Context mContext){
        this.mContext = mContext;
    }
    public Country findCountry(Phonenumber.PhoneNumber p){
        ArrayList<Country> list = mCountriesMap.get(p.getCountryCode());
        Country country = null;
        if (list != null) {
            if (p.getCountryCode() == 1) {
                String num = String.valueOf(p.getNationalNumber());
                if (num.length() >= 3) {
                    String code = num.substring(0, 3);
                    if (CANADA_CODES.contains(code)) {
                        for (Country c : list) {
                            // Canada has priority 1, US has priority 0
                            if (c.getPriority() == 1) {
                                country = c;
                                break;
                            }
                        }
                    }
                }
            }
            if (country == null) {
                for (Country c : list) {
                    if (c.getPriority() == 0) {
                        country = c;
                        break;
                    }
                }
            }
        }
        return country;
    }

    public ArrayList<Country> getReader(ArrayList<Country> data){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mContext.getApplicationContext().getAssets().open("countries.dat"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                //process line
                Country c = new Country(mContext, line, i);
                data.add(c);
                ArrayList<Country> list = mCountriesMap.get(c.getCountryCode());
                if (list == null) {
                    list = new ArrayList<Country>();
                    mCountriesMap.put(c.getCountryCode(), list);
                }
                list.add(c);
                i++;
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return data;
    }

    public PhoneNumberUtil getmPhoneNumberUtil() {
        return mPhoneNumberUtil;
    }

    public void setmPhoneNumberUtil(PhoneNumberUtil mPhoneNumberUtil) {
        this.mPhoneNumberUtil = mPhoneNumberUtil;
    }

    public SparseArray<ArrayList<Country>> getmCountriesMap() {
        return mCountriesMap;
    }

    public void setmCountriesMap(SparseArray<ArrayList<Country>> mCountriesMap) {
        this.mCountriesMap = mCountriesMap;
    }

    public static TreeSet<String> getCanadaCodes() {
        return CANADA_CODES;
    }
}
