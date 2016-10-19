package com.wisegps.clzx.activity.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wisegps.clzx.R;
import com.wisegps.clzx.activity.car.model.CarInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class CarListAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater inflater;
    private List<CarInfo.DataBean> carInfoList = new ArrayList<>();
    private OnCarEditClickListener mOnCarEditClickListener = null;
    private OnBindDeviceClickListener mOnBindDeviceClickListener = null;
    private OnCarDetailClickListener mOnCarDetailClickListener = null;
    private OnDeviceClickListener mOnDeviceClickListener = null;

    public CarListAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return carInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return carInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View converview, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(converview == null){
            inflater = LayoutInflater.from(mContext);
            converview = inflater.inflate(R.layout.item_cars_manager_list,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvCarName = (TextView)converview.findViewById(R.id.tv_car_name);
            viewHolder.tvDesc = (TextView)converview.findViewById(R.id.tv_car_desc);
            viewHolder.tvDeapatmentId = (TextView)converview.findViewById(R.id.tv_car_depatment);
            viewHolder.llBindDevice = (LinearLayout)converview.findViewById(R.id.ll_bind_device);
            viewHolder.ivDevice = (ImageView)converview.findViewById(R.id.iv_device);
            viewHolder.ivEditCar = (ImageView)converview.findViewById(R.id.iv_edit_car);
            viewHolder.ivCarDetail = (ImageView)converview.findViewById(R.id.iv_car_detail);
            viewHolder.vLine = (View)converview.findViewById(R.id.v_vertal_line);
            converview.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)converview.getTag();
        }
        viewHolder.tvCarName.setText(carInfoList.get(position).getName());
        viewHolder.tvDesc.setText(carInfoList.get(position).getDesc());
        viewHolder.tvDeapatmentId.setText(carInfoList.get(position).getDepartId());
        if("0".equals(carInfoList.get(position).getDid())){
            viewHolder.llBindDevice.setVisibility(View.VISIBLE);
            viewHolder.ivDevice.setVisibility(View.GONE);
            viewHolder.vLine.setVisibility(View.VISIBLE);
        }else{
            viewHolder.llBindDevice.setVisibility(View.GONE);
            viewHolder.ivDevice.setVisibility(View.VISIBLE);
            viewHolder.vLine.setVisibility(View.GONE);
        }
        viewHolder.ivDevice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mOnDeviceClickListener!=null){
                    mOnDeviceClickListener.onClick(view,position);
                }
            }
        });

        viewHolder.ivEditCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnBindDeviceClickListener!=null){
                    mOnCarEditClickListener.onClick(view,position);
                }
            }
        });
        viewHolder.llBindDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnBindDeviceClickListener!=null){
                    mOnBindDeviceClickListener.onClick(view,position);
                }
            }
        });
        viewHolder.ivCarDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnCarDetailClickListener != null){
                    mOnCarDetailClickListener.onClick(view,position);
                }
            }
        });
        return converview;
    }

    class ViewHolder{
        TextView tvCarName;
        TextView tvDesc;
        TextView tvDeapatmentId;
        LinearLayout llBindDevice;
        View vLine;
        ImageView ivDevice;
        ImageView ivEditCar;
        ImageView ivCarDetail;
    }

    /**
     * @param infoList
     */
    public void setData(List<CarInfo.DataBean>infoList){
        this.carInfoList = infoList;
    }


    public interface OnCarEditClickListener {
        void onClick(View v,int position);
    }

    public void setOnCarEditClickListener(OnCarEditClickListener onClickListener){
        this.mOnCarEditClickListener = onClickListener;
    }

    public interface OnBindDeviceClickListener{
        void onClick(View v,int position);
    }

    public void setOnBindDeviceClickListener(OnBindDeviceClickListener onClickListener){
        this.mOnBindDeviceClickListener = onClickListener;
    }

    public  interface OnCarDetailClickListener{
        void onClick(View v,int position);
    }

    public void setOnCarDetailClickListener (OnCarDetailClickListener onClickListener){
        this.mOnCarDetailClickListener = onClickListener;
    }

    public interface OnDeviceClickListener{
        void onClick(View v,int position);
    }

    public void setOnDeviceClickListener (OnDeviceClickListener onClickListener){
        this.mOnDeviceClickListener = onClickListener;
    }

}
