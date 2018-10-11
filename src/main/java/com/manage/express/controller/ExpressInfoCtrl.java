package com.manage.express.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manage.express.model.ExpressInfo;
import com.manage.express.service.ExpressInfoService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/express")
public class ExpressInfoCtrl {
  private static final String TRACE_CODE = "102";

  private static final String MONEY_CODE = "107";

  @Autowired
  private ExpressInfoService expressInfoService;

  private ExecutorService executorService = Executors.newSingleThreadExecutor();


  @ResponseBody
  @PostMapping("/receive-express-info")
  public Map<String, Object> receiveExpressInfo(HttpServletResponse response,
                                                HttpServletRequest request) {
    response.setCharacterEncoding("utf-8");
    response.setContentType("text/html");

    String inputLine;
    String str = "";
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      while ((inputLine = br.readLine()) != null) {
        str += inputLine;
      }
      br.close();
    } catch (IOException e) {
      System.out.println("IOException: " + e);
    }
    System.out.println("快递鸟推送服务BODY:" + str);

    String requestType = request.getParameter("RequestType");
    String requestData = request.getParameter("RequestData");

    Map<String, Object> result = new HashMap<>();
    result.put("UpdateTime", DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss"));
    if (StringUtils.equals(requestType, TRACE_CODE)) {
      try {
        String requestDataJson = URLDecoder.decode(requestData, "UTF-8");
        expressInfoService.saveLogs(requestDataJson);
        ObjectMapper objectMapper = new ObjectMapper();
        ExpressInfo ei = objectMapper.readValue(requestDataJson, ExpressInfo.class);
        result.put("EBusinessID", ei.geteBusinessId());
          executorService.execute(new Runnable() {
            @Override
            public void run() {
              try {
                expressInfoService.saveExpressInfo(ei);
              } catch (Exception ex) {
                System.out.println("快递鸟推送信息处理异常" + ex);
              }
            }
          });
        result.put("Success", true);
        result.put("Reason", "");
      } catch (Exception e) {
        System.out.println("快递鸟推送信息获取异常" + e);
        result.put("Success", false);
        result.put("Reason", "");
      }
    } else {
      System.out.println("跳过非物流信息推送");
      result.put("Success", true);
      result.put("Reason", "");
    }
    System.out.println(result);
    return result;
  }

  public static void main(String[] args) {
    String jsonData = "{\"PushTime\":\"2018-09-30 10:42:37\",\"EBusinessID\":\"test1340460\",\"Data\":[{\"LogisticCode\":\"1234561\",\"ShipperCode\":\"SF\",\"Traces\":[{\"AcceptStation\":\"顺丰速运已收取快件\",\"AcceptTime\":\"2018-09-30 10:42:37\",\"Remark\":\"\"},{\"AcceptStation\":\"货物已经到达深圳\",\"AcceptTime\":\"2018-09-30 10:42:372\",\"Remark\":\"\"},{\"AcceptStation\":\"货物到达福田保税区网点\",\"AcceptTime\":\"2018-09-30 10:42:373\",\"Remark\":\"\"},{\"AcceptStation\":\"货物已经被张三签收了\",\"AcceptTime\":\"2018-09-30 10:42:374\",\"Remark\":\"\"}],\"State\":\"3\",\"EBusinessID\":\"test1340460\",\"Success\":true,\"Reason\":\"\",\"CallBack\":\"\",\"EstimatedDeliveryTime\":\"2018-09-30 10:42:37\"}],\"Count\":\"1\"}";
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ExpressInfo ei = objectMapper.readValue(jsonData, ExpressInfo.class);
      System.out.println(ei);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
