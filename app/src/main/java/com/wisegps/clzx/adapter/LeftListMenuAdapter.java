package com.wisegps.clzx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wisegps.clzx.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public class LeftListMenuAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;

    private List<String> menuNames = new ArrayList<>();

    public LeftListMenuAdapter (Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return menuNames.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return menuNames.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(converView == null){
            inflater = LayoutInflater.from(mContext);
            converView = inflater.inflate(R.layout.item_menu_left_list,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvMenuName = (TextView)converView.findViewById(R.id.tv_menu_name);
            viewHolder.ivMenuIcon = (ImageView)converView.findViewById(R.id.iv_menu_ico);
            converView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)converView.getTag();
        }
        viewHolder.tvMenuName.setText(menuNames.get(position));
        return converView;
    }

    class ViewHolder{
        ImageView ivMenuIcon;
        TextView tvMenuName;
    }

    /**
     * @param munes
     */
    public void setData(List<String> munes){
        this.menuNames  = munes;
    }
}
