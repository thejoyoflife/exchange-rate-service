package com.shimul.quarkus.ers.provider.ecb.scheduler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.shimul.quarkus.ers.provider.ecb.cache.ExchangeRateDataCache;

@ApplicationScoped
public class ExchangeRateDownloadQuartzJob implements Job {
	
	private static final Logger LOGGER = Logger.getLogger(ExchangeRateDownloadQuartzJob.class);
	
	@Inject
	ExchangeRateDataCache cache;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.debug("Scheduled Job is executing to refresh the cache");
		cache.forceRefresh();
		LOGGER.debug("Scheduled Job has been executed successfully");
	}
}
