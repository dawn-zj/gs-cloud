package com.gs.webserver.controller.log;

import com.alibaba.fastjson.JSON;
import com.gs.common.util.date.DateUtil;
import com.gs.webserver.entity.to.request.JsonTo;
import com.gs.webserver.entity.to.request.LoginLogTo;
import com.gs.webserver.entity.to.response.ResponseTo;
import com.gs.webserver.service.IJsonService;
import com.gs.webserver.util.IpUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录日志
 * @author Administator
 */
@RestController
@RequestMapping("/loginLog")
public class LoginLogController {
    @Autowired
    private IJsonService jsonService;
    /**
     * 获取
     * @return
     */
    @PostMapping("/add")
    public ResponseTo<String> add(@RequestBody JsonTo jsonTo, HttpServletRequest request) throws Exception {
        LoginLogTo loginLogTo = JSON.parseObject(JSON.toJSONString(jsonTo.getData()), LoginLogTo.class);
        loginLogTo.setLoginTime(DateUtil.getCurrentTime());

        final String ip = IpUtil.getIpAddr(request);
        String uaStr = request.getHeader("User-Agent");
        UserAgent ua = UserAgent.parseUserAgentString(uaStr);

        loginLogTo.setIpaddr(ip);
        loginLogTo.setBrowser(ua.getBrowser().toString().toLowerCase());
        loginLogTo.setOs(ua.getOperatingSystem().toString().toLowerCase());

        jsonTo.setData(loginLogTo);

        jsonService.add(jsonTo);
        return ResponseTo.success();
    }

}
