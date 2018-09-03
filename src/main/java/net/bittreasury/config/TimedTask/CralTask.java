package net.bittreasury.config.TimedTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

//@Configuration
//@EnableScheduling
public class CralTask {

	@Scheduled(fixedDelay = 21600)
	public void timerToNow() {
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(50);

		newFixedThreadPool.shutdown();
	}
}
