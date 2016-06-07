package com.giffar.ctour.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.giffar.ctour.R;
import com.giffar.ctour.entitys.LeftMenu;

public class LeftMenuAdapter extends BaseAdapter<LeftMenu> {

    public LeftMenuAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LeftMenu menu = getItem(position);
//            if (position == getCount() - 1) {
//                convertView = inflater.inflate(R.layout.grid_item_left_menu_uber, parent, false);
//                holder.ivIcon = (ImageView) convertView.findViewById(R.id.icon);
//                holder.ivIcon.setImageResource(menu.getIcon());
//            }else {
                convertView = inflater.inflate(R.layout.grid_item_left_menu, parent, false);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.icon);
                holder.tvTitle.setText(menu.getTitle());
                holder.ivIcon.setImageResource(menu.getIcon());
//            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder {
        TextView tvTitle;
        ImageView ivIcon;
    }

}
