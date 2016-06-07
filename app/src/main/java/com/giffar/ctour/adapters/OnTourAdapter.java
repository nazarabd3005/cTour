package com.giffar.ctour.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.giffar.ctour.R;
import com.giffar.ctour.customview.CircleImageView;
import com.giffar.ctour.entitys.Notification;
import com.giffar.ctour.entitys.OnTour;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by nazar on 5/2/2016.
 */
public class OnTourAdapter extends BaseAdapter<OnTour> {
    public OnTourAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OnTour onTour = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_tour_member, parent, false);
            holder = new ViewHolder();
            holder.tvMemberName = (TextView) convertView.findViewById(R.id.tv_member_name);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.ivMember = (CircleImageView) convertView.findViewById(R.id.iv_member);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (onTour!=null){
            holder.tvMemberName.setText(onTour.getNama());
            holder.tvDistance.setText(onTour.getDistance());
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private static class ViewHolder {
        TextView tvMemberName,tvDistance;
        CircleImageView ivMember;
    }

}
