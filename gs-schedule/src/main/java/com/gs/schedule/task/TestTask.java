package com.gs.schedule.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Administator
 */
@Component
public class TestTask {
    private static final Logger logger = LoggerFactory.getLogger(TestTask.class);

    public void test1() {
        logger.info("---测试任务1");
    }

    public void test2(String str1) {
        logger.info("---测试任务2: " + str1);
    }

    public void test3(String str1, String str2) {
        logger.info("---测试任务3: " + str1 + "---" + str2);
    }
}
