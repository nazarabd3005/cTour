package com.giffar.ctour.callbacks;

import com.giffar.ctour.entitys.Club;
import com.giffar.ctour.entitys.Timeline;

import java.util.List;

/**
 * Created by nazar on 5/22/2016.
 */
public interface OnStatusCallback {
    void OnSuccess(List<Timeline> clubs, String message);
    void OnSuccess(Timeline timeline,String message);
    void OnFailed(String message);
    void OnFinish();
}
