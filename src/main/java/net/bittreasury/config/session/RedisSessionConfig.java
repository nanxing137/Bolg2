package net.bittreasury.config.session;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 使用redis储存session
 * 
 * @author Thornhill
 *
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class RedisSessionConfig {

}
