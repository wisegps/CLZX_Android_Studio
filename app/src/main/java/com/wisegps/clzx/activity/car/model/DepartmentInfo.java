package com.wisegps.clzx.activity.car.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class DepartmentInfo {


    /**
     * status_code : 0
     * total : 3
     * data : [{"name":"部门A","objectId":775903943044763600},{"name":"部门1","objectId":775905632149704700},{"name":"部门1","objectId":775965972279464000}]
     */

    private int status_code;
    private int total;
    /**
     * name : 部门A
     * objectId : 775903943044763600
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

    public static class DataBean {
        private String name;
        private long objectId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getObjectId() {
            return objectId;
        }

        public void setObjectId(long objectId) {
            this.objectId = objectId;
        }
    }
}
