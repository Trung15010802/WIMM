package com.example.wimm.fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.wimm.R;

import java.util.List;

public class ListHistoryAdapter extends BaseAdapter {
    private List<ItemHistorySpending> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListHistoryAdapter(Context aContext, List<ItemHistorySpending> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.history_item, null);
            holder = new ViewHolder();
            holder.imgTypeView = convertView.findViewById(R.id.imageView_type);
            holder.txtTypeView = convertView.findViewById(R.id.textView_type);
            holder.moneyView = convertView.findViewById(R.id.textView_money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemHistorySpending itemHistorySpending = this.listData.get(position);
        holder.txtTypeView.setText(itemHistorySpending.getTxtType());
        holder.moneyView.setText("Đã chi: " + itemHistorySpending.getMoney() + " $");

        int imgID = this.getMipmapResIdByName(itemHistorySpending.getImgType());
        holder.imgTypeView.setImageResource(imgID);
        return convertView;
    }


    static class ViewHolder {
        ImageView imgTypeView;
        TextView txtTypeView;
        TextView moneyView;
    }

    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName) {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName, "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }

}


