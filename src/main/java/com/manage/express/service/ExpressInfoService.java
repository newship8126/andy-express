package com.manage.express.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manage.express.model.ExpressInfo;
import com.manage.express.model.Logistic;
import com.manage.express.model.Traces;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpressInfoService {
  @Autowired
  private SqlSessionTemplate template;

  public Integer saveLogs(String receiveInfo) {
    Map<String, String> params = new HashMap<>();
    params.put("receiveInfo", receiveInfo);
    int n = template.insert("ExpressInfo.saveLogs", params);
    return n;
  }

  public Map<String, String> queryExpConfig() {
    List<Map<String, String>> list = template.selectList("ExpressInfo.queryConfig");
    Map<String, String> result = new HashMap<>();
    for (Map<String, String> map : list) {
      result.put(map.get("code"), map.get("name"));
    }
    return result;
  }

  public void saveExpressInfo(ExpressInfo ei) throws IOException {
    Map<String, String> expConfigMap = queryExpConfig();
    List<Logistic> logistics = ei.getData();
    for (Logistic logistic : logistics) {
      List<Traces> tracesList = logistic.getTraces();
      ObjectMapper mapper = new ObjectMapper();
      String tracesJson = mapper.writeValueAsString(tracesList);
      Map<String, Object> params = new HashMap<>();
      params.put("logistic", logistic.getLogisticCode());
      Traces traces = template.selectOne("ExpressInfo.queryExpInfo", params);
      if (traces == null) {
        traces = new Traces();
        traces.setExpCode(logistic.getShipperCode());
        traces.setExpName(expConfigMap.get(logistic.getShipperCode()));
        traces.setLogistic(logistic.getLogisticCode());
        traces.setPushTime(ei.getPushTime());
        traces.setTrace(tracesJson);
        template.insert("ExpressInfo.saveExpInfo", traces);
      } else {
        traces.setTrace(tracesJson);
        template.update("ExpressInfo.updateExpInfo", traces);
      }

      System.out.println("快递单号:" + logistic.getLogisticCode() + "数据处理完成");
    }
    System.out.println("快递鸟推送数据处理完成记录数:" + ei.getCount());
  }
}
