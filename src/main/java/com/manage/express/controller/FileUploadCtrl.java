package com.manage.express.controller;

import com.manage.express.service.FileUploadService;
import com.manage.express.utils.ExcelImportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/fileUpload")
public class FileUploadCtrl {
  @Autowired
  private FileUploadService fileUploadService;

  @RequestMapping("/query")
  public String query(HttpSession session) {
    session.removeValue("msg");
    return "fileupload/upload";
  }

  //导入
  @PostMapping(value = "batchImport")
  public String batchImportUserKnowledge(@RequestParam(value="filename") MultipartFile file,
                                         HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {

    //判断文件是否为空
    if(file==null){
      session.setAttribute("msg","文件不能为空！");
      return "fileupload/upload";
    }

    //获取文件名
    String fileName=file.getOriginalFilename();

    //验证文件名是否合格
    if(!ExcelImportUtils.validateExcel(fileName)){
      session.setAttribute("msg","文件必须是excel格式！");
      return "fileupload/upload";
    }

    //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
    long size=file.getSize();
    if(StringUtils.isEmpty(fileName) || size==0){
      session.setAttribute("msg","文件不能为空！");
      return "fileupload/upload";
    }

    String message = "";
    //批量导入
    try {
      message = fileUploadService.batchImport(fileName, file);
    } catch (Exception e) {
      System.out.println("导入失败" + e);
    }
    session.setAttribute("msg",message);
    return "fileupload/upload";
  }

  @PostMapping("/subscribe-express")
  public String subscribe(HttpSession session) {
    try {
      fileUploadService.subscribeExpressInfo();
      session.setAttribute("msg","物流跟踪信息订阅成功");
    } catch (Exception e) {
      System.out.println("物流跟踪信息订阅失败" + e);
      session.setAttribute("msg","物流跟踪信息订阅失败");
    }
    return "fileupload/upload";
  }

  @ResponseBody
  @PostMapping("/subscribe-order-express")
  public Map<String, Object> subscribeOrderExpress(@RequestBody Map<String, Object> params) {
    Map<String, Object> resultMap = new HashMap<>();
    String orderNo = String.valueOf(params.get("orderNo"));
    try {
      fileUploadService.subscribeOrderExpress(params);
      System.out.println("订单号:" + String.valueOf(params.get("orderNo")) + "订阅物流信息成功");
      resultMap.put("status", 0);
      resultMap.put("msg", "订单号:" + String.valueOf(params.get("orderNo")) + "订阅物流信息成功");
    } catch (Exception e) {
      System.out.println("订单号:" + String.valueOf(params.get("orderNo")) + "订阅物流信息失败");
      e.printStackTrace();
      resultMap.put("status", 1);
      resultMap.put("msg", "订单号:" + String.valueOf(params.get("orderNo")) + "订阅物流信息失败");
    }
    return resultMap;
  }

}
