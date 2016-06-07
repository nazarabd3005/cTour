package com.giffar.ctour.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giffar.ctour.Preferences;
import com.giffar.ctour.R;
import com.giffar.ctour.customview.CircleImageView;
import com.giffar.ctour.entitys.Member;
import com.giffar.ctour.entitys.OnTour;

/**
 * Created by nazar on 5/2/2016.
 */
public class ListMemberAdapter extends BaseAdapter<Member> {
    public ListMemberAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Member member = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_tour_member, parent, false);
            holder = new ViewHolder();
            holder.tvMemberName = (TextView) convertView.findViewById(R.id.tv_member_name);
            holder.tvDistance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.ivMember = (CircleImageView) convertView.findViewById(R.id.iv_member);
            holder.background = (RelativeLayout) convertView.findViewById(R.id.background);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        holder.tvDistance.setVisibility(View.INVISIBLE);
        if (member!=null){
            if (member.getStatus_join().equals("4")){
                holder.background.setBackgroundResource(android.R.color.holo_red_dark);
            }else{
                holder.background.setBackgroundResource(R.color.white);

            }
            loadImage(Preferences.URL_MEMBER+member.getPhoto(),holder.ivMember,null);
            if (member.getStatus().equals("1")){
                holder.tvDistance.setText("Leader");
            }else{
                holder.tvDistance.setText("member");
            }
            holder.tvMemberName.setText(member.getNama());
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
        RelativeLayout background;
    }
}
