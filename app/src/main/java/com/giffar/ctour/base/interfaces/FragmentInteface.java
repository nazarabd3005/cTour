/**
 * @author egiadtya
 * 27 October 2014
 */
package com.giffar.ctour.base.interfaces;

import android.view.View;

public interface FragmentInteface {
	public void initView(View view);
	public void setUICallbacks();
	public void updateUI();
	public String getPageTitle();
	public int getFragmentLayout();
	public String getTag();
	
}
