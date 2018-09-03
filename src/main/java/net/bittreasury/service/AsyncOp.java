package net.bittreasury.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.annotation.Async;

@Async
public interface AsyncOp {
	void redisOption(HttpServletRequest httpServletRequest);
}
