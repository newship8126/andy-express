package com.manage.express.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manage.express.model.Addr;
import com.manage.express.model.ImportData;
import com.manage.express.model.OrderExpressInfo;
import com.manage.express.model.RequestData;
import com.manage.express.utils.ExcelImportUtils;
import com.manage.express.utils.KdniaoSubscribeAPI;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class FileUploadService {

  @Autowired
  private SqlSessionTemplate template;

  private Addr getSender(String senderCode) {
    Map<String, Object> params = new HashMap<>();
    params.put("senderCode", senderCode);
    return template.selectOne("Sender.querySenders", params);
  }

  private Addr getReceive(String orderNo) {
    Map<String, Object> params = new HashMap<>();
    params.put("orderNo", orderNo);
    return template.selectOne("Sender.queryReceive", params);
  }

  /**
   * 上传excel文件到临时目录后并开始解析
   * @param fileName
   * @param mfile
   * @return
   */
  @Transactional(rollbackFor = Exception.class)
  public String batchImport(String fileName, MultipartFile mfile){

    File uploadDir = new  File("/tmp");
    //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
    if (!uploadDir.exists()) uploadDir.mkdirs();
    //新建一个文件
    File tempFile = new File("/tmp/temp.xlsx");
    //初始化输入流
    InputStream is = null;
    try{
      //将上传的文件写入新建的文件中
      mfile.transferTo(tempFile);

      //根据新建的文件实例化输入流
      is = new FileInputStream(tempFile);

      //根据版本选择创建Workbook的方式
      Workbook wb = null;
      //根据文件名判断文件是2003版本还是2007版本
      if(ExcelImportUtils.isExcel2007(fileName)){
        wb = new XSSFWorkbook(is);
      }else{
        wb = new HSSFWorkbook(is);
      }
      //根据excel里面的内容读取知识库信息
      return readExcelValue(wb,tempFile);
    }catch(Exception e){
      e.printStackTrace();
      throw new RuntimeException("导入出错！请检查数据格式！" + e);
    } finally{
      if(is !=null)
      {
        try{
          is.close();
        }catch(IOException e){
          is = null;
          e.printStackTrace();
        }
      }
    }
  }


  /**
   * 解析Excel里面的数据
   * @param wb
   * @return
   */
  private String readExcelValue(Workbook wb, File tempFile) throws Exception {

    //错误信息接收器
    String errorMsg = "";
    //得到第一个shell
    Sheet sheet=wb.getSheetAt(0);
    //得到Excel的行数
    int totalRows=sheet.getPhysicalNumberOfRows();
    //总列数
    int totalCells = 0;
    //得到Excel的列数(前提是有行数)，从第二行算起
    if(totalRows>=2 && sheet.getRow(1) != null){
      totalCells=sheet.getRow(1).getPhysicalNumberOfCells();
    }
    List<ImportData> importDataList =new ArrayList<ImportData>();
    ImportData importData;

    String br = "\r\n";

    //循环Excel行数,从第二行开始。标题不入库
    for(int r=1;r<totalRows;r++){
      String rowMessage = "";
      Row row = sheet.getRow(r);
      if (row == null){
        errorMsg += br+"第"+(r+1)+"行数据有问题，请仔细检查！";
        continue;
      }
      importData = new ImportData();

      String orderNo = "";
      String shipperCode = "";
      String logisticCode = "";
      String senderCode = "";

      //循环Excel的列
      for(int c = 0; c <totalCells; c++){
        Cell cell = row.getCell(c);
        if (null != cell){
          if(c==0){
            orderNo = cell.getStringCellValue();
            if(StringUtils.isEmpty(orderNo)){
              rowMessage += "订单号不能为空；";
            }
            importData.setOrderNo(orderNo);
          }else if(c==1){
            shipperCode = cell.getStringCellValue();
            if(StringUtils.isEmpty(shipperCode)){
              rowMessage += "快递公司编码不能为空；";
            }else if(shipperCode.length()>10){
              rowMessage += "快递公司编码的字数不能超过10位；";
            }
            importData.setShipperCode(shipperCode);
          }else if(c==2){
            logisticCode = cell.getStringCellValue();
            if(StringUtils.isEmpty(logisticCode)){
              rowMessage += "快递单号不能为空；";
            }
            importData.setLogisticCode(logisticCode);
          }else if (c==3) {
            senderCode = cell.getStringCellValue();
            if(StringUtils.isEmpty(senderCode)){
              rowMessage += "发货地编码不能为空；";
            }
            importData.setSenderCode(senderCode);
          }
        }else{
          rowMessage += "第"+(c+1)+"列数据有问题，请仔细检查；";
        }
      }
      //拼接每行的错误提示
      if(!StringUtils.isEmpty(rowMessage)){
        errorMsg += br+"第"+(r+1)+"行，"+rowMessage;
      }else{
        importDataList.add(importData);
      }
    }

    //删除上传的临时文件
    if(tempFile.exists()){
      tempFile.delete();
    }

    //全部验证通过
    if(StringUtils.isEmpty(errorMsg)){
      StringBuffer errorData = new StringBuffer();
      int n = 0;
      for(ImportData e : importDataList){
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", e.getOrderNo());
        params.put("logisticCode", e.getLogisticCode());
        OrderExpressInfo oei = template.selectOne("ExpressInfo.queryOrderExpressInfo", params);
        if (oei == null) {
          n++;
          oei = new OrderExpressInfo();
          oei.setOrderNo(e.getOrderNo());
          oei.setLogisticCode(e.getLogisticCode());
          oei.setShipperCode(e.getShipperCode());
          oei.setSenderCode(e.getSenderCode());
          oei.setStatus(0);
          oei.setUpdateTime(DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss"));
          template.insert("ExpressInfo.insertOrderExpressInfo", oei);
        } else {
          errorData.append("\r\n订单号:" + e.getOrderNo() + ", 快递单号:" + e.getLogisticCode() + ",");
        }
      }
      errorMsg = "共" + importDataList.size()+"条数据, 导入" + n + "条记录. \r\n";
      if (errorData.length() > 0) {
        errorMsg += "以下订单已被订阅:" + errorData.substring(0, errorData.length() - 1).toString();
      }
      subscribeExpressInfo();
    }
    return errorMsg;
  }

  public void subscribeExpressInfo() throws Exception {
    List<Map<String, String>> kdlParams = template.selectList("ExpressInfo.queryKdlParams");
    String eBusinessId = "", appKey = "", reqURL = "";
    for (Map<String, String> kdlparam : kdlParams) {
      if (StringUtils.equals("EBusinessId", kdlparam.get("flag"))) {
        eBusinessId = kdlparam.get("value");
      }
      if (StringUtils.equals("AppKey", kdlparam.get("flag"))) {
        appKey = kdlparam.get("value");
      }
      if (StringUtils.equals("ReqURL", kdlparam.get("flag"))) {
        reqURL = kdlparam.get("value");
      }
    }
    KdniaoSubscribeAPI api = new KdniaoSubscribeAPI(eBusinessId, appKey, reqURL);
    List<OrderExpressInfo> list = template.selectList("ExpressInfo.queryUnSubscribe");
    for (OrderExpressInfo oei : list) {
      RequestData rd = new RequestData();
      rd.setOrderCode(oei.getOrderNo());
      rd.setShipperCode(oei.getShipperCode());
      rd.setLogisticCode(oei.getLogisticCode());
      rd.setSender(getSender(oei.getSenderCode()));
      rd.setReceiver(getReceive(oei.getOrderNo()));
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(rd);
      System.out.println("快递鸟订阅参数" + json);
      String result = api.orderTracesSubByJson(json);
      System.out.println("快递鸟订阅结果:" + result);
      Map<String, Object> params = new HashMap<>();
      params.put("orderCode", oei.getOrderNo());
      params.put("logisticCode", oei.getLogisticCode());
      params.put("status", 1);
      template.update("ExpressInfo.updateSubscribeStatus", params);
      Map<String, Object> orderParams = new HashMap<>();
      params.put("orderNo", oei.getOrderNo());
      params.put("logistic", oei.getLogisticCode());
      params.put("expressCode", oei.getShipperCode());
      params.put("expressName", oei.getExpName());
      params.put("deliveryId", oei.getSenderCode());
      template.update("ExpressInfo.updateOrderStatus", orderParams);
    }

  }
}
