package pl.amilosh.managementservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@EnableScheduling
@Configuration
public class ScheduledConfiguration implements SmartInitializingSingleton {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Scheduled(fixedDelay = 1000) // between previous end and next start
    public void scheduleFixedDelayTask() throws InterruptedException {
        log.info("scheduleFixedDelayTask " + Thread.currentThread().getName());
    }

    @Scheduled(fixedRate = 1000) // between previous start and next start
    public void scheduleFixedRateTask() throws InterruptedException {
        log.info("scheduleFixedRateTask " + Thread.currentThread().getName());
    }

    @Scheduled(initialDelay = 10000) // delay after starting og application
    public void scheduleInitialDelayTask() throws InterruptedException {
        log.info("scheduleInitialDelayTask");
    }

    @Scheduled(cron = "0 15 10 15 * ?", zone = "Europe/Paris")
    public void scheduleTaskUsingCronExpression() {
        log.info("scheduleTaskUsingCronExpression");
    }

    @Scheduled(cron = "${cron.expression}")
    public void scheduleTaskUsingCronExpressionProperty() {
        log.info("scheduleTaskUsingCronExpression");
    }

    @Override
    public void afterSingletonsInstantiated() {
        var now = Instant.now();
        var threadPoolTaskScheduler = threadPoolTaskScheduler();
        threadPoolTaskScheduler.scheduleWithFixedDelay(this::runScheduleWithFixedDelay, now, Duration.ofSeconds(3)); // every 3 seconds

        var cronTrigger = new CronTrigger("10 * * * * ?"); // at the 10th second of every minute
        threadPoolTaskScheduler.schedule(this::runScheduleWithTrigger, cronTrigger);
    }

    public void runScheduleWithFixedDelay() {
        log.info("runScheduleWithFixedDelay " + Thread.currentThread().getName());
    }

    public void runScheduleWithTrigger() {
        log.info("runScheduleWithTrigger " + Thread.currentThread().getName());
    }
}
