/**
 * @author egiadtya
 */
package com.giffar.ctour.callbacks;

public interface OnCallAPI {
	public void onAPIsuccess();
	public void onAPIFailed(String errorMessage);
	public void executeAPI();
}
