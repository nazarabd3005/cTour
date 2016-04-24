package com.giffar.ctour.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.giffar.ctour.R;
import com.giffar.ctour.entitys.Notification;
import com.giffar.ctour.helpers.DateHelper;

import mehdi.sakout.fancybuttons.FancyButton;

public class NotificationAdapter extends BaseAdapter<Notification> {
    public NotificationAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notification notification = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_notif_custody, parent, false);
            holder = new ViewHolder();
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_notif_date);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_bottle_name);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_notif_title);
            holder.btnStoreName = (FancyButton) convertView.findViewById(R.id.btn_details);
            holder.ivLogo = (ImageView) convertView.findViewById(R.id.iv_notif_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(notification.getTitle());
        holder.tvName.setText(notification.getName());
        holder.btnStoreName.setText(notification.getCompany());
        holder.tvDate.setText(DateHelper.getInstance().getTimeAgo(notification.getTimestamp()));
//        imageLoader(notification.getPicture(),holder.ivLogo,R.dimen.circle_image_border);
        loadImage(notification.getPicture(), holder.ivLogo, null);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        TextView tvDate, tvTitle,tvName;
        FancyButton btnStoreName;
        ImageView ivLogo;
    }

}
