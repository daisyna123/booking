package com.vo;

import java.util.List;

/**
 * Created by zhangtuoyu on 2016-09-22.
 */
public class TCSL_VO_HotelDetail {
    private String MCID;
    private String NAME;
    private String ADDRESS;
    private String hoteImg;
    private String phone;
    private List<TCSL_VO_RoomInfo> roomInfoList;
    public String getMCID() {
        return MCID;
    }

    public void setMCID(String MCID) {
        this.MCID = MCID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getHoteImg() {
        return hoteImg;
    }

    public void setHoteImg(String hoteImg) {
        this.hoteImg = hoteImg;
    }

    public List<TCSL_VO_RoomInfo> getRoomInfoList() {
        return roomInfoList;
    }

    public void setRoomInfoList(List<TCSL_VO_RoomInfo> roomInfoList) {
        this.roomInfoList = roomInfoList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}