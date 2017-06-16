package org.erhmutlu.jbeat.service;

import org.erhmutlu.jbeat.api.exceptions.JBeatException;
import org.erhmutlu.jbeat.persistency.models.PeriodicTask;
import org.erhmutlu.jbeat.service.schedule.ScheduledJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by erhmutlu on 16/06/17.
 */

@Service
public class JBeatFacadeImpl implements JBeatFacade {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private PeriodicTaskService periodicTaskService;
    private ScheduledJobService scheduledJobService;

    public JBeatFacadeImpl(PeriodicTaskService periodicTaskService, ScheduledJobService scheduledJobService) {
        this.periodicTaskService = periodicTaskService;
        this.scheduledJobService = scheduledJobService;
    }

    @Override
    @Transactional(rollbackFor = {JBeatException.class})
    public ScheduledJob scheduleNewTask(String taskName, String queue, String crontab, Map params, String description) throws JBeatException {
        logger.info("JBeatFacade createPeriodicTask(taskName: {}, crontab: {})",
                taskName, crontab);

        PeriodicTask periodicTask = periodicTaskService.createPeriodicTask(taskName, queue, crontab, params, description);
        logger.debug("Created PeriodicTask: {}", periodicTask);

        return scheduledJobService.schedule(periodicTask);
    }


}