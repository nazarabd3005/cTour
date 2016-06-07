/**
 * Base dialog Fragment
 * @author Wafda Mufti
 * 27 October 2014
 */
package com.giffar.ctour.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.giffar.ctour.R;
import com.giffar.ctour.activities.BaseActivity;
import com.giffar.ctour.customview.FragmentInteface;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public abstract class BaseDialogFragment extends DialogFragment implements FragmentInteface {
	private View view;
	protected Activity activity;
	protected ImageLoader imageLoader;
	protected abstract int getMinimumWidth();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageLoader = ImageLoader.getInstance();
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(getFragmentLayout(), container,false);
		initView(view);
		view.setMinimumWidth(getMinimumWidth());
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		updateUI();
		setUICallbacks();
		if (getPageTitle() != null) {
			getBaseActivity().setActionBarTitle(getPageTitle());
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	public BaseActivity getBaseActivity(){
		return ((BaseActivity)activity);
	}

	public void replaceFragment(int container,BaseDialogFragment fragment,boolean addBackToStack){
		getBaseActivity().getFragmentHelper().replaceFragment(container, fragment, addBackToStack);
	}

	public void addFragment(int container,BaseDialogFragment fragment,boolean addBackToStack){
		getBaseActivity().getFragmentHelper().addFragment(container, fragment, addBackToStack);
	}

	public void addChildFragment(int container,BaseDialogFragment fragment,boolean addBackToStack){
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		if (addBackToStack) {
			ft.addToBackStack(fragment.getPageTitle());
		}
		ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
		ft.add(container, fragment);
		ft.commitAllowingStateLoss();
	}

	public void loadImage(String url,final ImageView imageView,final int stubImage){
		loadImage(url, imageView, stubImage, null);
	}

	public void loadImage(String url,final ImageView imageView,final int stubImage,ImageSize imageSize){
		if (url != null && !url.equalsIgnoreCase("")) {
			ImageLoadingListener loadingListener = new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					imageView.setImageResource(stubImage);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
					imageView.setImageResource(stubImage);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					imageView.setImageBitmap(loadedImage);
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {}
			};
			if (imageSize != null) {
				imageLoader.loadImage(url, imageSize, null, loadingListener);
			}else{
				imageLoader.loadImage(url,loadingListener);
			}
		}
	}


	public String checkNullString(String string){
		return (string == null) ? "": string;
	}
}
