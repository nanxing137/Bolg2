package net.bittreasury.config.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.bittreasury.service.AsyncOp;

@Aspect
@Component
public class PlatformAspect {

	@Autowired
	private AsyncOp asyncOp;
	
	

	@Pointcut("execution(* net.bittreasury.controller.RESTful.*.*(..))")
	public void ipStatistics() {
	}

	// @Before("ipStatistics()")
	@After("ipStatistics()")
	public void addIPStatistics() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		asyncOp.redisOption(request);
	}

}
