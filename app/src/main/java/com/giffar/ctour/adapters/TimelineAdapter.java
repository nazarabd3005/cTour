package com.giffar.ctour.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.customview.CircleImageView;
import com.giffar.ctour.entitys.Timeline;
import com.giffar.ctour.helpers.DateHelper;
import com.giffar.ctour.helpers.PictureHelper;

/**
 * Created by nazar on 4/27/2016.
 */
public class TimelineAdapter extends BaseAdapter<Timeline> {
    DateHelper dateHelper;
    PictureHelper pictureHelper;
    public TimelineAdapter(Context context) {
        super(context);
        dateHelper = new DateHelper();
        pictureHelper = PictureHelper.getInstance(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Timeline timeline = getItem(position);
        ViewHolder viewHolder = null;
        int layout = R.layout.list_view_timeline;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(layout, parent, false);
            viewHolder.ivMember = (CircleImageView) convertView.findViewById(R.id.iv_member);
            viewHolder.tvNameMember = (TextView) convertView.findViewById(R.id.tv_name_member);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tv_description);
            viewHolder.ivPost = (ImageView) convertView.findViewById(R.id.iv_image_post);
            viewHolder.tvSumCom = (TextView) convertView.findViewById(R.id.tv_sum_comment);
            viewHolder.tvSumLike = (TextView)convertView.findViewById(R.id.tv_sum_like);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (timeline != null){
            if (!timeline.getImage().equals("")){
                viewHolder.ivMember.setVisibility(View.VISIBLE);
                if (viewHolder.ivPost.getTag() == null ||
                        !viewHolder.ivPost.getTag().equals(timeline.getImage())) {
                    imageLoader(Preferences.URL_STATUS+timeline.getImage(), viewHolder.ivPost,null);
                    viewHolder.ivPost.setTag(timeline.getImage());
                }
                if (viewHolder.ivMember.getTag() == null ||
                        !viewHolder.ivMember.getTag().equals(timeline.getPhoto())) {
                    imageLoader(Preferences.URL_MEMBER+timeline.getPhoto(), viewHolder.ivMember,null);
                    viewHolder.ivMember.setTag(timeline.getPhoto());
                }
            }else{
                viewHolder.ivMember.setVisibility(View.GONE);
            }
            viewHolder.tvNameMember.setText(timeline.getName());
            viewHolder.tvSumCom.setText(timeline.getComment());
            viewHolder.tvSumLike.setText(timeline.getLike());
            viewHolder.tvDescription.setText(timeline.getDescription());
            viewHolder.tvDate.setText(dateHelper.formatlongtoDate(Long.valueOf(timeline.getCreate_at()),"dd MMM yyyy"));
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public static class ViewHolder{
        TextView tvNameMember,tvDate,tvDescription,tvSumLike,tvSumCom;
        CircleImageView ivMember;
        ImageView ivPost;
    }
}
