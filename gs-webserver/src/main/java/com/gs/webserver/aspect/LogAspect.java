package com.gs.webserver.aspect;

import com.alibaba.fastjson.JSON;
import com.gs.webserver.util.IpUtil;
import com.gs.webserver.util.ServletUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 操作日志记录处理
 * @author Administator
 */
@Aspect
@Component
@Order(1)
public class LogAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);


	/**
	 * 配置织入点
	 */
	@Pointcut("execution(* com.gs.webserver.controller..*.*(..))")
	public void logPointCut() {}

	/**
	 * 业务处理
	 *
	 * @param proceedingJoinPoint 切点
	 * @return 业务放行
	 */
	@Around(value = "logPointCut()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) {
		Object result = null;

		try {
			String className = proceedingJoinPoint.getTarget().getClass().getName();
			String methodName = proceedingJoinPoint.getSignature().getName();
			String ip = IpUtil.getIpAddr(ServletUtil.getRequest());
			String url = ServletUtil.getRequest().getRequestURI();
			Object[] args = proceedingJoinPoint.getArgs();

			String msg = "操作成功";
			try {
				String jsonString = JSON.toJSONString(args);
				// 结果
				logger.debug("[操作] [{}] [{}] [{}] [{}] [{}]", ip, url, className + "." + methodName + "()", msg, jsonString);
			} catch (Throwable ex) {
				logger.debug("[操作] [{}] [{}] [{}] [{}] [{}]", ip, url, className + "." + methodName + "()", msg, "");
			}

			result = proceedingJoinPoint.proceed();
		} catch (Throwable ex) {
			logger.error(ex.getMessage(), ex);
		}
		return result;

	}

}
