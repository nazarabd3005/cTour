package com.giffar.ctour.callbacks;

import com.giffar.ctour.entitys.Member;

import java.util.List;

/**
 * Created by nazar on 5/20/2016.
 */
public interface OnMemberCallback {
    void onSuccess(Member member, String message);
    void onSuccess(List<Member> members, String message);
    void onFailed(String message);
    void OnFinish();


}
