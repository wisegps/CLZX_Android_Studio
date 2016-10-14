package com.wisegps.clzx.activity.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wisegps.clzx.R;
import com.wisegps.clzx.activity.car.model.DepartmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class DepartmentAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<DepartmentInfo.DataBean> departmentInfoList = new ArrayList<>();

    public DepartmentAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return departmentInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return departmentInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converview, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(converview == null){
            inflater = LayoutInflater.from(mContext);
            converview = inflater.inflate(R.layout.item_department_list,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvDepartmentName = (TextView)converview.findViewById(R.id.tv_department_name);
            converview.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)converview.getTag();
        }
        viewHolder.tvDepartmentName.setText(departmentInfoList.get(position).getName());
        return converview;
    }

    class ViewHolder{
        TextView tvDepartmentName;
    }

    /**
     * @param infoList
     */
    public void setData(List<DepartmentInfo.DataBean>infoList){
        this.departmentInfoList = infoList;
    }
}
