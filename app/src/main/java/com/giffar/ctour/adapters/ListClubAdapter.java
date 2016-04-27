package com.giffar.ctour.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.giffar.ctour.R;
import com.giffar.ctour.entitys.Club;

import java.util.List;

/**
 * Created by nazar on 4/25/2016.
 */
public class ListClubAdapter extends BaseAdapter<Club> {
    private List<Club> clubList;
    Context context;


    public ListClubAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Club club = getItem(position);
        ViewHolder viewHolder;
        int layout = R.layout.list_view_club;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(layout, parent, false);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.year);
            viewHolder.tvMerk = (TextView) convertView.findViewById(R.id.merk);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (club != null){
            viewHolder.tvDate.setText(club.getCreated_date());
            viewHolder.tvName.setText(club.getName_club());
            viewHolder.tvMerk.setText(club.getMerk());
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public static class ViewHolder{
        TextView tvName,tvMerk,tvDate;
    }

}
