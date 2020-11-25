package com.framelibrary.bean;

import com.framelibrary.util.StringUtils;

/**
 * APP登录获取UserInfo
 *
 * @author:wangwx
 * @Date:2018/4/26 15:35
 */
public class UserInfo extends BaseRespBean {
    /**
     * errcode : 0
     * data : {"userId":"1","token":"2PsGFWjs77y-BNP8vMdrsHbUVZ1cjE6P","head_img":null,"nickName":null,"userPhone":"15330000272","userName":"刘","job":"刘","education":"博士","hospital":"123","gender":"1","birthday":"2018-10-20","address":"重庆 渝北区 ","app_new":"1","score":"0","couponCount":0}
     */
    private DataBean data;

    public UserInfo() {
    }

    public UserInfo(String userid, String token) {
        DataBean data = new DataBean();
        data.userId = userid;
        data.token = token;
        this.data = data;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends BaseBean {
        /**
         * userId : 1
         * token : 2PsGFWjs77y-BNP8vMdrsHbUVZ1cjE6P
         * head_img : null
         * nickName : null
         * userPhone : 15330000272
         * userName : 刘
         * job : 刘
         * education : 博士
         * hospital : 123
         * gender : 1
         * birthday : 2018-10-20
         * address : 重庆 渝北区
         * app_new : 1
         * score : 0
         * couponCount : 0
         */

        private String userId;
        private String token;
        private String head_img;
        private String nickName;
        private String userPhone;
        private String userName;
        private String job;
        private String education;
        private String hospital;
        private String department; //科室
        private String gender; // 1 男  2 女
        private String birthday;
        private String address;
        private String score;
        private int couponCount;
        private String union_id;
        private String app_new; // 是否app新用户 0:否,1:是
        private boolean comment_status; // 是否有最新评论

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getJob() {
            return StringUtils.isBlank(job) ? "请选择" : job;

        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getEducation() {
            return StringUtils.isBlank(education) ? "请输入" : education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getDepartment() {
            return StringUtils.isBlank(department) ? "请选择" : department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getHospital() {
            return StringUtils.isBlank(hospital) ? "" : hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getGender() {
            return StringUtils.isBlank(gender) ? "请选择" : gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getAddress() {
            return StringUtils.isBlank(address) ? "请选择" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getApp_new() {
            return app_new;
        }

        public void setApp_new(String app_new) {
            this.app_new = app_new;
        }

        public boolean isComment_status() {
            return comment_status;
        }

        public void setComment_status(boolean comment_status) {
            this.comment_status = comment_status;
        }

        public String getScore() {
            score = StringUtils.isBlank(score) ? "0" : score.startsWith("-") ? "0" : score;
            return score.endsWith(".00") ? score.substring(0, score.length() - 3) : score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public int getCouponCount() {
            return couponCount;
        }

        public void setCouponCount(int couponCount) {
            this.couponCount = couponCount;
        }

        public String getUnion_id() {
            return union_id;
        }

        public void setUnion_id(String union_id) {
            this.union_id = union_id;
        }

    }
}
