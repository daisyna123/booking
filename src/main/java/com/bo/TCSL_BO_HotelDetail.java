package com.bo;

import com.dao.mysql.TCSL_DAO_HotelDetail_mysql;
import com.dao.mysql.TCSL_DAO_Hotel_mysql;
import com.dao.oracle.TCSL_DAO_HotelDetail;
import com.dao.oracle.TCSL_DAO_MC_orl;
import com.dao.oracle.TCSL_DAO_ServerFacility;
import com.po.oracle.PHO_HT_HOTELITEM;
import com.po.oracle.PHO_MC_O2O;
import com.util.TCSL_UTIL_Common;
import com.vo.TCSL_VO_Hotel;
import com.vo.TCSL_VO_HotelDetail;
import com.vo.TCSL_VO_Result;
import com.vo.TCSL_VO_RoomInfo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtuoyu on 2016-09-22.
 */
@Repository
public class TCSL_BO_HotelDetail {
    @Resource
    TCSL_DAO_HotelDetail daoHotelDetail;
    @Resource
    TCSL_DAO_HotelDetail_mysql daoHotelDetailMysql;
    @Resource
    TCSL_UTIL_Common utilCommon;
    @Resource
    TCSL_DAO_MC_orl daoMc_orl; //查询商户信息
    @Resource
    TCSL_DAO_ServerFacility daoServerFacility;
    @Resource
    TCSL_DAO_Hotel_mysql daoHotelMysql;
    /**
     * 查询酒店列表
     * @param gcId
     * @return
     * @throws Exception
     */
    public TCSL_VO_Result queryHotelList(String gcId,String name ) throws Exception {
        System.out.println("TCSL_BO_HotelDetail-----查询条件gcId--line48-"+gcId);
        System.out.println("TCSL_BO_HotelDetail-----查询条件name--line49-"+name);
        TCSL_VO_Result result = new TCSL_VO_Result();
        List<TCSL_VO_HotelDetail> hotelList= daoHotelDetail.queryHotelList(gcId); //根据集群号gcId查询酒店列表
        System.out.println("TCSL_BO_HotelDetail-----集群下酒店列表--line52-"+hotelList);
        List<TCSL_VO_Hotel> hotelInfoList = daoHotelMysql.queryAll(); //查询酒店位置信息
        System.out.println("TCSL_BO_HotelDetail-----酒店位置信息列表--line54-"+hotelInfoList);
        List<TCSL_VO_HotelDetail> resultList = new ArrayList<TCSL_VO_HotelDetail>();
        Map<Integer,TCSL_VO_Hotel> hotelInfoMap = new HashMap<Integer,TCSL_VO_Hotel>();
        for (TCSL_VO_Hotel hotel:hotelInfoList) {
            Integer hotel_mcId = hotel.getiMCID();
            hotelInfoMap.put(hotel_mcId,hotel);
        }
        //查询外景图片名称
        String savePath = utilCommon.getPropertyParam("upload-path.properties","upload.path");
        System.out.println("TCSL_BO_HotelDetail--酒店外景图片保存路径--line63--"+savePath);
        for (TCSL_VO_HotelDetail voHotelDetail: hotelList) {
            String shopName = voHotelDetail.getNAME(); //商户名称
            String folderPath = savePath+"/"+shopName+"/"+"outdoor_scene";
            System.out.println("TCSL_BO_HotelDetail--酒店名称--line67--"+shopName);
            //判断文件夹是否存在
            File df = new File(folderPath);
            if(!df.exists()){
                continue;
            }
            System.out.println("TCSL_BO_HotelDetail--酒店名称--line73--该酒店外景图片文件夹存在");
            File[] files = df.listFiles();
            String fileName = "";
            for (int i = 0; i < files.length; i++){
                if(i == 0){
                    File f = files[i];
                    fileName = f.getName();
                    break;
                }
            }
            System.out.println("TCSL_BO_HotelDetail--酒店外景图片名称--line83--"+fileName);
            voHotelDetail.setHoteImg(fileName);
            //添加酒店定位相关信息
            TCSL_VO_Hotel hotel = hotelInfoMap.get(Integer.parseInt(voHotelDetail.getMCID()));
            voHotelDetail.setLongtitude(hotel.getdLONGTITUDE());
            voHotelDetail.setLatitude(hotel.getdLATITUDE());
            String cityName = hotel.getcCITY();
            System.out.println("TCSL_BO_HotelDetail----酒店名称，经纬度---line90-"+
                    cityName+"---"+hotel.getdLONGTITUDE()+"---"+hotel.getdLATITUDE());
            if(name != null){ //城市过滤条件不为空
                System.out.println("TCSL_BO_HotelDetail----查询城市名称---line93-"+name);
                if(cityName != null){ //酒店所在城市不为空
                    System.out.println("TCSL_BO_HotelDetail----酒店所在地名称---line95-"+cityName);
                    //存在name为天津市，cityName为天津的情况
                    //判断城市名称(条件)是否包含城市名称(酒店所在城市)或是否相同
                    if(name.indexOf(cityName) != -1 || name.equals(cityName)
                            || cityName.indexOf(name) != -1){
                        //符合城市名称，添加到resultList中
                        voHotelDetail.setCity(cityName);
                        resultList.add(voHotelDetail);
                        System.out.println("TCSL_BO_HotelDetail---line103-查询城市名称匹配成功，查询成功");
                    }
                }
            }else{ //城市过滤条件为空，不进行过滤
                voHotelDetail.setCity(cityName);
                resultList.add(voHotelDetail);
            }
        }
        result.setContent(resultList);
        result.setRet(0);
        return result;
    }

    /**
     * 查询酒店详情
     * @param mcId
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public TCSL_VO_Result queryHotelDetail(String mcId,String startDate,String endDate) throws Exception {
        TCSL_VO_Result result = new TCSL_VO_Result();
        TCSL_VO_HotelDetail voHotelDetail = new TCSL_VO_HotelDetail(); //酒店信息
        PHO_MC_O2O mc = daoMc_orl.queryByMcId(mcId);
        String shopName = mc.getNAME();
        voHotelDetail.setNAME(shopName); //商户名称
        voHotelDetail.setADDRESS(mc.getADDRESS()); //商户地址
        voHotelDetail.setMCID(mc.getMCID()); //商户mcId
        voHotelDetail.setPhone(mc.getORDERTEL()); //联系电话
        List<TCSL_VO_RoomInfo> roomList = daoHotelDetailMysql.queryRoomListByTime(mcId,startDate,endDate);
        String savePath = utilCommon.getPropertyParam("upload-path.properties","upload.path"); //图片保存路径
        String outPath = savePath+"/"+shopName+"/"+"outdoor_scene"; //酒店外景图片保存路径
        File outFile = new File(outPath); //外景图片文件夹对象
        String outImg = "";
        if(outFile.exists()){ //判断外景图片所在文件夹是否存在
            File[] files = outFile.listFiles(); //获取文件夹中所有文件对象
            for (int i=0; i<files.length; i++) { //获取一张图片名称不为空的图片名称
                outImg = files[i].getName();
                if(outImg != null && !outImg.equals("")){
                    break;
                }
            }
        }
        voHotelDetail.setHoteImg(outImg);
        //获取房间图片名称
        for (TCSL_VO_RoomInfo room:roomList) {
            String roomName = room.getCNAME();
            String folderPath = savePath+"/"+shopName+"/"+roomName;
            String imgName = "";
            File file = new File(folderPath);
            if(file.exists()){
                File[] files = file.listFiles();
                for (int i=0; i<files.length; i++) {
                    imgName = (files[i].getName());
                }
            }
            room.setImgName(imgName);
        }
        voHotelDetail.setRoomInfoList(roomList);
        result.setRet(0);
        result.setContent(voHotelDetail);
        return result;
    }

    /**
     * 查询商户设施
     * @param mcId
     * @return
     */
    public TCSL_VO_Result queryFacility(String mcId) {
        TCSL_VO_Result result = new TCSL_VO_Result();
        TCSL_VO_HotelDetail voHotelDetail = new TCSL_VO_HotelDetail(); //酒店信息
        PHO_MC_O2O mc = daoMc_orl.queryByMcId(mcId);
        voHotelDetail.setNAME(mc.getNAME()); //商户名称
        voHotelDetail.setDESP(mc.getDESP()); //商户描述
        List<PHO_HT_HOTELITEM> roomList =
                daoServerFacility.queryRoomItems(mcId,"客房设施");
        List<PHO_HT_HOTELITEM> multipleList =
                daoServerFacility.queryRoomItems(mcId,"综合设施");
        List<PHO_HT_HOTELITEM> serverList =
                daoServerFacility.queryRoomItems(mcId,"服务项目");
        List<PHO_HT_HOTELITEM> toyList =
                daoServerFacility.queryRoomItems(mcId,"娱乐设施");
        voHotelDetail.setRoomList(roomList);
        voHotelDetail.setMultipleList(multipleList);
        voHotelDetail.setServerList(serverList);
        voHotelDetail.setToyList(toyList);
        result.setContent(voHotelDetail);
        result.setRet(0);
        return result;
    }

    public TCSL_VO_Result queryRoomList(String mcId) {
        TCSL_VO_Result result = new TCSL_VO_Result();
        List<TCSL_VO_RoomInfo> roomList = daoHotelDetailMysql.queryRoomListByMcId(mcId);
        result.setContent(roomList);
        result.setRet(0);
        return result;
    }
}
