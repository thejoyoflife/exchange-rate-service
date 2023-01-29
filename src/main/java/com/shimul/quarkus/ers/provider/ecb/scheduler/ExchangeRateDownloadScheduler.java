package com.shimul.quarkus.ers.provider.ecb.scheduler;

import static com.shimul.quarkus.ers.provider.ecb.ProviderUtils.QUARTZ_CRON_EXP_DOWNLOAD_SCHEDULE;
import static com.shimul.quarkus.ers.provider.ecb.ProviderUtils.ZONE_ID;

import java.util.TimeZone;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;

import com.shimul.quarkus.ers.provider.ecb.cache.ExchangeRateDataCache;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class ExchangeRateDownloadScheduler {
	private static final Logger LOGGER = Logger.getLogger(ExchangeRateDownloadScheduler.class);
	
	@ConfigProperty(name = "ecb-exchg.rate.download.cron.exp", defaultValue = QUARTZ_CRON_EXP_DOWNLOAD_SCHEDULE)
	String cronExpression;
	
	@Inject
	Scheduler quartz;
	
	@Inject
	ExchangeRateDataCache cache;
	
	// schedule a download a few minutes later than the rates are published in the website
	void schedulePeriodicDownload(@Observes StartupEvent event) throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(ExchangeRateDownloadQuartzJob.class)
										.withIdentity("ECBExchangeRateDownloadQuartzJob")
										.build();
		CronTrigger trigger = TriggerBuilder.newTrigger()
									.withIdentity("ECBExchangeRateDownloadQuartzCronTrigger")
									.startNow()
									.withSchedule(
											CronScheduleBuilder.cronSchedule(cronExpression)
												.inTimeZone(TimeZone.getTimeZone(ZONE_ID))
											)
									.build();
		quartz.scheduleJob(jobDetail, trigger);
	}
	
	// fill up the cache when the server starts
	void fillupCacheOnServerStart(@Observes StartupEvent event) throws SchedulerException {
		try {
			LOGGER.info("Refreshing the cache during server startup event");
			cache.forceRefresh();
		} catch (Exception e) {
			LOGGER.error("Error in populating cache on server startup event", e);
		}
	}
}
