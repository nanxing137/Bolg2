package net.bittreasury.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import net.bittreasury.service.AreaStatistics;
import net.bittreasury.service.AsyncOp;
import net.bittreasury.utils.IPUtil;

@Service
public class AsyncOpImpl implements AsyncOp {
	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private AreaStatistics areaStatistics;

	@Override
	@Async
	public void redisOption(HttpServletRequest httpServletRequest) {
		String ipAddr = IPUtil.getIpAddr((HttpServletRequest) httpServletRequest);
		stringRedisTemplate.boundHashOps("IPStatistics").increment(ipAddr, 1);
		String doStatistics = areaStatistics.doStatistics(ipAddr);
		stringRedisTemplate.boundHashOps("AreaStatistics").increment(doStatistics, 1);

	}

}
