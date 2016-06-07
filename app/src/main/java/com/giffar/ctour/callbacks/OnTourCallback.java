package com.giffar.ctour.callbacks;

import com.giffar.ctour.entitys.Timeline;
import com.giffar.ctour.entitys.Touring;

import java.util.List;

/**
 * Created by nazar on 5/22/2016.
 */
public interface OnTourCallback {
    void OnSuccess(List<Touring> clubs, String message);
    void OnSuccess(Touring touring,String message);
    void OnFailed(String message);
    void OnFinish();
}
