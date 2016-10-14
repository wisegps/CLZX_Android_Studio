package com.wisegps.clzx.activity.car.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class CarInfo {

    /**
     * status_code : 0
     * total : 4
     * data : [{"buyDate":"2016-09-21T00:00:00.000Z","frameNo":"abcde","desc":"奥迪A8","name":"粤：999999","engineNo":"zz","mileage":10,"departId":"775903943044763600","maintainMileage":1000},{"buyDate":"2016-09-22T00:00:00.000Z","frameNo":"op","desc":"aodi q7","name":"888888","engineNo":"ae86","mileage":3,"departId":"778521551048937500","maintainMileage":100},{"buyDate":"2016-09-22T00:00:00.000Z","frameNo":"ii","desc":"ai","name":"888888","engineNo":"oo","mileage":0,"departId":"778529581912166400","maintainMileage":0},{"buyDate":"1970-01-01T00:00:00.000Z","frameNo":"","desc":"","name":"","engineNo":"","mileage":10000,"departId":"778529581912166400","maintainMileage":123}]
     */

    private int status_code;
    private int total;
    /**
     * buyDate : 2016-09-21T00:00:00.000Z
     * frameNo : abcde
     * desc : 奥迪A8
     * name : 粤：999999
     * engineNo : zz
     * mileage : 10
     * departId : 775903943044763600
     * maintainMileage : 1000
     */

    private List<DataBean> data;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private String buyDate;
        private String frameNo;
        private String desc;
        private String name;
        private String engineNo;
        private int mileage;
        private String departId;
        private long uid;
        private int maintainMileage;
        private long objectId;
        private int serviceType;
        private int feeType;
        private String serviceRegDate;
        private String serviceExpireln;
        private String did = "0";//0 未绑定
        private String createdAt;
        private String updatedAt;


        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public long getObjectId() {
            return objectId;
        }

        public void setObjectId(long objectId) {
            this.objectId = objectId;
        }

        public int getCreator() {
            return creator;
        }

        public void setCreator(int creator) {
            this.creator = creator;
        }

        public int getServiceType() {
            return serviceType;
        }

        public void setServiceType(int serviceType) {
            this.serviceType = serviceType;
        }

        public int getFeeType() {
            return feeType;
        }

        public void setFeeType(int feeType) {
            this.feeType = feeType;
        }

        public String getServiceRegDate() {
            return serviceRegDate;
        }

        public void setServiceRegDate(String serviceRegDate) {
            this.serviceRegDate = serviceRegDate;
        }

        public String getServiceExpireln() {
            return serviceExpireln;
        }

        public void setServiceExpireln(String serviceExpireln) {
            this.serviceExpireln = serviceExpireln;
        }

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        private int creator;

        public String getBuyDate() {
            return buyDate;
        }

        public void setBuyDate(String buyDate) {
            this.buyDate = buyDate;
        }

        public String getFrameNo() {
            return frameNo;
        }

        public void setFrameNo(String frameNo) {
            this.frameNo = frameNo;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEngineNo() {
            return engineNo;
        }

        public void setEngineNo(String engineNo) {
            this.engineNo = engineNo;
        }

        public int getMileage() {
            return mileage;
        }

        public void setMileage(int mileage) {
            this.mileage = mileage;
        }

        public String getDepartId() {
            return departId;
        }

        public void setDepartId(String departId) {
            this.departId = departId;
        }

        public int getMaintainMileage() {
            return maintainMileage;
        }

        public void setMaintainMileage(int maintainMileage) {
            this.maintainMileage = maintainMileage;
        }
    }
}
