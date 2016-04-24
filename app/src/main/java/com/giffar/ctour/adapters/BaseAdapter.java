/**
 * Base Adapter
 * @author egiadtya
 * 27 October 2014
 */
package com.giffar.ctour.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.giffar.ctour.helpers.ScreenHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    protected List<T> Data;
    protected Context context = null;
    protected LayoutInflater inflater = null;
    protected List<T> originalList;
    private ImageLoader imageLoader;

    public BaseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();

    }

    @Override
    public int getCount() {
        return this.Data.size();
    }

    @Override
    public T getItem(int postion) {
        return Data.get(postion);
    }

    public List<T> getData() {
        return Data;
    }

    public void setData(List<T> data) {
        Data = data;
        originalList = data;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    @Override
    public abstract long getItemId(int position);

    public void loadImage(String uri, final ImageView imageView, ImageSize imageSize) {
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        imageLoader.loadImage(uri, imageSize, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (imageView != null) {
                    imageView.setImageBitmap(loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void imageLoader(String uri,final  ImageView imageView, final int imageDefault, final GifImageView spinner){
        ImageLoader.getInstance().displayImage(uri, imageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                imageView.setImageResource(imageDefault);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                spinner.setVisibility(View.GONE);
            }
        });
    }

    public void imageLoader(String uri,final  ImageView imageView, final int imageDefault, final ProgressBar spinner){
        ImageLoader.getInstance().displayImage(uri, imageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                imageView.setImageResource(imageDefault);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                spinner.setVisibility(View.GONE);
            }
        });
    }

    public void imageLoader(String uri,final  ImageView imageView, final ProgressBar spinner){
        ImageLoader.getInstance().displayImage(uri,imageView,new SimpleImageLoadingListener(){
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                spinner.setVisibility(View.GONE);
            }
        });
    }

    public String addNewLine(String value) {
        String[] temp = value.split(" ");
        String newString = "";
        if (temp.length > 1) {
            temp[0] = temp[0] + "\n";
            for (int i = 0; i < temp.length; i++) {
                if (i > 0)
                    newString = newString + temp[i] + " ";
                else
                    newString = newString + temp[i];
            }
        } else {
            newString = value;
        }
        return newString;
    }

    public void calculateHeight(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int width = ScreenHelper.getInstance().getScreenWidth(context);
        layoutParams.height = Math.round(width - (width / 2));
        layoutParams.width = width / 2;
        view.setLayoutParams(layoutParams);
    }
}
