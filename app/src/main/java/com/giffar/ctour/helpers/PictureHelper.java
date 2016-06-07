/**
 * @author egiadtya
 */

package com.giffar.ctour.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.giffar.ctour.Config;
import com.giffar.ctour.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.IOException;

public class PictureHelper {
    public static final int TAKE_PICTURE = 13579;
    public static int PICK_FROM_GALLERY = 2468;
    private DateHelper dateHelper;
    private Uri selectedImage;
    private AlertDialog pickerDialog;
    private Context ctx;
    private Fragment fragment;
    private ImageLoader imageLoader;
    private static PictureHelper pictureHelper;
    private Activity activity;

    public static PictureHelper getInstance(Context context) {
        if (pictureHelper == null) {
            pictureHelper = new PictureHelper(context);
        }
        pictureHelper.ctx = context;
        return pictureHelper;
    }

    public static PictureHelper getInstance(Context context, Fragment fragment) {
        pictureHelper = getInstance(context);
        pictureHelper.fragment = fragment;
        return pictureHelper;
    }
    public static PictureHelper getInstance(Context context,Activity activity){
        pictureHelper = getInstance(context);
        pictureHelper.activity = activity;
        return pictureHelper;
    }

    public PictureHelper(Context context) {
        dateHelper = new DateHelper();
        this.ctx = context;
        imageLoader = ImageLoader.getInstance();
        if (fragment==null){
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    ctx)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .discCacheFileNameGenerator(new Md5FileNameGenerator())
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .writeDebugLogs() // Remove for release app
                    .build();
            // Initialize ImageLoader with configuration.

            imageLoader.init(config);
        }
    }

    public PictureHelper(Context context, Fragment fragment) {
        this(context);
        this.fragment = fragment;
    }

    public void showPickPhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        String title = "Add Photo";
        builder.setTitle(title);
        View view = LayoutInflater.from(ctx).inflate(R.layout.view_dialog_pick_image, null);
        Button pickFromCamera = (Button) view.findViewById(R.id.from_camera);
        Button pickFromGalery = (Button) view.findViewById(R.id.from_galery);

        pickFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFormCamera();
            }
        });

        pickFromGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGalery();
            }
        });

        builder.setView(view);
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        pickerDialog = builder.create();
        pickerDialog.show();
    }

    public void pickFormCamera() {
        int requestCode = TAKE_PICTURE;
        String fileName = "IMG-" + dateHelper.getDateTime() + "-" + Config.APP_NAME;
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        values.put(Media.TITLE, fileName);
        selectedImage = ctx.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
        fragment.startActivityForResult(takePicture, requestCode);
        if (pickerDialog != null) pickerDialog.dismiss();
    }

    public void pickFromGalleryFragment(){
        int requestCode = PICK_FROM_GALLERY;
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
        fragment.startActivityForResult(pickPhoto, requestCode);
        if (pickerDialog != null) pickerDialog.dismiss();
    }
    public void pickFromGalery() {

        int requestCode = PICK_FROM_GALLERY;
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(pickPhoto, requestCode);
        if (pickerDialog != null) pickerDialog.dismiss();
    }

    public String getPath(Context ctx, Uri uri) {
        String[] projection = {MediaColumns.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = ((Activity) ctx).managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public Bitmap rotate(Bitmap sourceBitmap, int degrees) {
        if (degrees != 0 && sourceBitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees, (float) sourceBitmap.getWidth() / 2,
                    (float) sourceBitmap.getHeight() / 2);
            try {
                Bitmap rotateBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                        sourceBitmap.getWidth(), sourceBitmap.getHeight(),
                        matrix, true);
                sourceBitmap = rotateBitmap;
            } catch (OutOfMemoryError ex) {
                throw ex;
            }
        }
        return sourceBitmap;
    }

    public int getImageRotation(String imagePath) throws IOException {
        ExifInterface exif = new ExifInterface(imagePath);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        int rotationAngle = 0;

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotationAngle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotationAngle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotationAngle = 270;
                break;
            default:
                break;
        }
        return rotationAngle;
    }

    public Uri getSelectedImage() {
        return selectedImage;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void loadImage(String url, final ImageView imageView, final int stubImage) {
        loadImage(url, imageView, stubImage, null);
    }

    public void loadImage(String url, final ImageView imageView, final int stubImage, ImageSize imageSize) {
        if (url != null && !url.equalsIgnoreCase("")) {
            ImageLoadingListener loadingListener = new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    if (stubImage != 0)
                        imageView.setImageResource(stubImage);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if (stubImage != 0)
                        imageView.setImageResource(stubImage);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            };
            if (imageSize != null) {
                imageLoader.loadImage(url, imageSize, null, loadingListener);
            } else {
                imageLoader.loadImage(url, new ImageSize(imageView.getWidth(), imageView.getHeight()), null, loadingListener);
            }
        }
    }

    public Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 80, 80);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(50, 50, 50, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException | OutOfMemoryError ignored) {
        }
        return result;
    }
}
