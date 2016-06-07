package com.giffar.ctour.callbacks;

import com.giffar.ctour.entitys.Club;

import java.util.List;

/**
 * Created by nazar on 5/21/2016.
 */
public interface OnClubCallBack {
    void OnSuccess(List<Club> clubs,String message);
    void OnSuccess(Club club,String message);
    void OnFailed(String message);
    void OnFinish();
}
