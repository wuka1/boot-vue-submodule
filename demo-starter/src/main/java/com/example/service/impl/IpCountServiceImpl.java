package com.example.service.impl;

import com.example.properties.IpCounterProperties;
import com.example.service.IpCountService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IpCountServiceImpl implements IpCountService {

    @Resource
    private HttpServletRequest request;

    @Resource
    private IpCounterProperties properties;

    private Map<String, Integer> map = new HashMap<>();

    @Override
    public void record() {
        //ip
        String ip = request.getRemoteAddr();
        //访问次数
        Integer i = map.get(ip);
        i = Objects.isNull(i)?1:++i;
        //更新
        map.put(ip, i);
        //输出
        map.forEach((key, value) ->{
        if (properties.getDisplay().equals(IpCounterProperties.DisplayMode.DETAIL.getValue())) {
            System.out.println("+---------------ip-----------+count--------+");
            System.out.println("ip:" + key + "\tcount: " + value);
            System.out.println("+---------------i-----+--------------+");
        }else {
            System.out.println("ip:" + key + "\tcount: " + value);
        }
        });
    }
}
