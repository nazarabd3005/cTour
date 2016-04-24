package com.giffar.ctour.helpers;

/**
 * Created by nazar on 27/10/15.
 */
public class ArrayHelper {
    String[] data;
    public String[] makeArray(String string){
        this.data = string.split(",");
        return this.data;
    }
}
