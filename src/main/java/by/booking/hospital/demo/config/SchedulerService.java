package by.booking.hospital.demo.config;

import by.booking.hospital.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/*
 * Schedule configurationClass for schedule database for check if date of room is expirated
 */
@Component
public class SchedulerService implements SchedulingConfigurer {
    private BookingService bookingService;

    @Autowired
    public SchedulerService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    //thread schedule pool
    public TaskScheduler poolScheduler1() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        scheduler.setPoolSize(3);
        scheduler.initialize();
        return scheduler;
    }

    // schedulerThread
    Thread t1 = new Thread(new Runnable() {
        @Override
        public void run() {
            schedule();
        }
    });

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(poolScheduler1());
        //scheduler
        scheduledTaskRegistrar.addTriggerTask(t1, t -> {
            Calendar nextExecutionTime = new GregorianCalendar();
            Date lastActualExecutionTime = t.lastActualExecutionTime();
            nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
            nextExecutionTime.add(Calendar.SECOND,3);
            return nextExecutionTime.getTime();
        });

    }

    private void schedule() {
        bookingService.schedule();
    }

}

