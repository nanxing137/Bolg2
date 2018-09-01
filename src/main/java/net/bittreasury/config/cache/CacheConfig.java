package net.bittreasury.config.cache;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableCaching
@Configuration
public class CacheConfig {

}
