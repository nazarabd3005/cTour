package com.giffar.ctour.customview;

/**
 * Created by wafdamufti on 10/15/15.
 */

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.giffar.ctour.R;


@SuppressWarnings("unused")
public class QuickAction extends PopupWindows implements OnDismissListener{
    private LayoutInflater inflater;
    private ViewGroup mTrack;
    private OnActionItemClickListener mItemClickListener;
    private OnDismissListener mDismissListener;

    private boolean mDidAction;
    private boolean mAnimateTrack;

    private int mChildPos;
    private int mAnimStyle;

    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_AUTO = 4;
    public String text;

    /**
     * Constructor.
     *
     * @param context Context
     */
    public QuickAction(Context context, String value) {
        super(context);
        inflater 	= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setRootViewId(R.layout.popup_custom_layout, value);
        this.text = value;
    }


    /**
     * Set root view.
     *
     * @param id Layout resource id
     */
    public void setRootViewId(int id, String value) {
        mRootView	= (ViewGroup) inflater.inflate(id, null);
        TextView tv = (TextView) mRootView.findViewById(R.id.popup_signup);
        Log.d("ini text", value);
        tv.setText(value);

        //This was previously defined on show() method, moved here to prevent force close that occured
        //when tapping fastly on a view to show quickaction dialog.
        //Thanx to zammbi (github.com/zammbi)
        mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        setContentView(mRootView);
    }

    /**
     * Animate track.
     *
     * @param mAnimateTrack flag to animate track
     */
    public void mAnimateTrack(boolean mAnimateTrack) {
        this.mAnimateTrack = mAnimateTrack;
    }

    /**
     * Set animation style.
     *
     * @param mAnimStyle animation style, default is set to ANIM_AUTO
     */
    public void setAnimStyle(int mAnimStyle) {
        this.mAnimStyle = mAnimStyle;
    }

    public void setOnActionItemClickListener(OnActionItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * Show popup mWindow
     */
    public void show (View anchor) {
        preShow();

        int[] location 		= new int[2];

        mDidAction 			= false;

        anchor.getLocationOnScreen(location);

        Rect anchorRect 	= new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1]
                + anchor.getHeight());

        //mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        int rootWidth 		= mRootView.getMeasuredWidth();
        int rootHeight 		= mRootView.getMeasuredHeight();

        int screenWidth 	= mWindowManager.getDefaultDisplay().getWidth();
        //int screenHeight 	= mWindowManager.getDefaultDisplay().getHeight();

        int xPos 			= (screenWidth - rootWidth) / 2;
        int yPos	 		= anchorRect.top - rootHeight;

        boolean onTop		= true;

        // display on bottom
        if (rootHeight > anchor.getTop()) {
            yPos 	= anchorRect.bottom;
            onTop	= false;
        }

        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);

    }

    /**
     * Set listener for window dismissed. This listener will only be fired if the quicakction dialog is dismissed
     * by clicking outside the dialog or clicking on sticky item.
     */
    public void setOnDismissListener(QuickAction.OnDismissListener listener) {
        setOnDismissListener(this);

        mDismissListener = listener;
    }

    @Override
    public void onDismiss() {
        if (!mDidAction && mDismissListener != null) {
            mDismissListener.onDismiss();
        }
    }

    /**
     * Listener for item click
     *
     */
    public interface OnActionItemClickListener {
        public abstract void onItemClick(QuickAction source, int pos, int actionId);
    }

    public void setMaxHeightResource(int heightResource) {
        int maxHeight = mContext.getResources().getDimensionPixelSize(heightResource);
        mWindow.setHeight(maxHeight);
    }

    /**
     * Listener for window dismiss
     *
     */
    public interface OnDismissListener {
        public abstract void onDismiss();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
