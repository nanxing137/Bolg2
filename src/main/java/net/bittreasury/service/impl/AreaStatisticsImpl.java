package net.bittreasury.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import net.bittreasury.service.AreaStatistics;
import net.bittreasury.utils.IOUtils;

@Service
public class AreaStatisticsImpl implements AreaStatistics {

	private final String URI = "http://ip.taobao.com/service/getIpInfo.php?ip=";

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	@Cacheable("doStatistics")
	public String doStatistics(String ipAddress) {
		String url = URI + ipAddress;
		URL resource = null;
		try {
			resource = new URL(url);
			InputStream content = resource.openConnection().getInputStream();
			String string = IOUtils.toString(content);
			return string;
		} catch (IOException e) {
			e.printStackTrace();
			return doStatistics(ipAddress);
		}

	}

}
