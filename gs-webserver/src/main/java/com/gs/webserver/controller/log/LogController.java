package com.gs.webserver.controller.log;

import com.gs.common.define.Constants;
import com.gs.common.util.FileUtil;
import com.gs.common.util.StringUtil;
import com.gs.webserver.entity.to.response.ResponseTo;
import org.springframework.web.bind.annotation.*;

/**
 * 系统日志
 * @author Administator
 */
@RestController
@RequestMapping("/log")
public class LogController {

    /**
     * 获取
     * @param level 日志等级，info、error、debug |error
     * @return
     * @throws Exception
     */
    @PostMapping("/get")
    public ResponseTo<String> get(@RequestParam String level) throws Exception {
        String filePath = Constants.LOG_PATH + "web/" + level + ".log";
        String content = StringUtil.getString(FileUtil.getFile(filePath));
        return ResponseTo.success(content);
    }

}
