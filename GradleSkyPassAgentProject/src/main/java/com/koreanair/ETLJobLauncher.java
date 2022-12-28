package com.koreanair;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.HashSet;
import java.util.Set;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreanair.common.quartz.MyJobListener;
import com.koreanair.common.quartz.MyTriggerListener;

public class ETLJobLauncher {
 
	private final static Logger log = LoggerFactory.getLogger(ETLJobLauncher.class);
	
    // Scheduler 객체 생성
    private static SchedulerFactory factory = null;
    private static Scheduler scheduler = null;
    
    // Scheduler 실행 함수
    public static void start() throws SchedulerException {
        // Scheduler 객체 정의
        factory = new StdSchedulerFactory();
        scheduler = factory.getScheduler();
        
        // Listener 설정
        scheduler.getListenerManager().addJobListener(new MyJobListener());
        scheduler.getListenerManager().addTriggerListener(new MyTriggerListener());
        
        // Scheduler 실행
        scheduler.start();
    }
    
    // Scheduler 종료 함수
    public static void stop() throws SchedulerException {
        try {
        	log.debug("스케줄러가 종료됩니다...");
            
            // Job Key 목록
            Set<JobKey> allJobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
            
            // Job 강제 중단
            allJobKeys.forEach((jobKey)->{
                try {
                    scheduler.interrupt(jobKey);
                } catch (UnableToInterruptJobException e) {
                    e.printStackTrace();
                }
            });
            
            // Scheduler 중단
            //   - true : 모든 Job이  완료될 때까지 대기 후 종료
            //   - false: 즉시 종료
            scheduler.shutdown(true);
 
            log.debug("스케줄러가 종료되었습니다.");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    
	public static void main(String[] args) throws Exception  {
		
		int mb = 1024 * 1024;
		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
		long xmx = memoryBean.getHeapMemoryUsage().getMax() / mb;
		long xms = memoryBean.getHeapMemoryUsage().getInit() / mb;
		log.info(String.format("Initial Memory (xms) : {%s}mb", xms));
		log.info(String.format("Max Memory (xmx) : {%s}mb", xmx) );
		
		//trace>debug>info>warn>error
        // Scheduler 실행
        start();	        

        // JOB Data 객체 생성
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("message", "SkyPass ETL Agent Start!!!");
        
        // JobDetail 설정
        JobDetail jobDetail = JobBuilder.newJob(ETLMainJob.class)
                                .withIdentity("job_ETLMainJob")
                                .setJobData(jobDataMap)
                                .build();
        
        // SimpleTrigger 생성(특정시간 및 횟수에 사용)
        SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                                        .withIdentity("simple_trigger", "simple_trigger_group")
                                        .startNow()
                                        //.withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(5, 4))// 4초마다 반복하며, 최대 5회 실행
                                        .forJob(jobDetail)
                                        .build();	        
        
        // CronTrigger 생성(주지적으로 반복에 사용)	  
        //CronScheduleBuilder cronSch = CronScheduleBuilder.cronSchedule(new CronExpression("0/10 * * * * ?"));
        CronScheduleBuilder cronSch = CronScheduleBuilder.cronSchedule(new CronExpression("0 0 11,12,13,14,15 * * ?"));
        CronTrigger cronTrigger = (CronTrigger) TriggerBuilder.newTrigger()
                                    .withIdentity("cron_trigger", "cron_trigger_group")
                                    .withSchedule(cronSch)
                                    .forJob(jobDetail)
                                    .build();
        
        // JobDtail : Trigger = 1 : N 설정
        Set<Trigger> triggerSet = new HashSet<Trigger>();
        triggerSet.add(simpleTrigger);
        triggerSet.add(cronTrigger);
        
        // Schedule 등록
        scheduler.scheduleJob(jobDetail, triggerSet, false);
        
        
        /*
        // CronTrigger 생성(주지적으로 반복에 사용)
        //CronScheduleBuilder cronSch = CronScheduleBuilder.cronSchedule(new CronExpression("0/10 * * * * ?"));
        CronScheduleBuilder cronSch = CronScheduleBuilder.cronSchedule(new CronExpression("0 20 10,13,16 * * ?"));
        CronTrigger cronTrigger = (CronTrigger) TriggerBuilder.newTrigger()
                                    .withIdentity("cron_trigger", "cron_trigger_group")
                                    .withSchedule(cronSch)
                                    .forJob(jobDetail)
                                    .build();
        
        // JobDtail : Trigger = 1 : N 설정
        Set<Trigger> triggerSet = new HashSet<Trigger>();
        triggerSet.add(simpleTrigger);
        triggerSet.add(cronTrigger);
        
        scheduler.scheduleJob(jobDetail, cronTrigger);
        */
        
   
        
        /*
        try {
            System.out.println("아무키나 입력하면 종료됩니다...");
            System.in.read();
 
            // Scheduler 롱료
            stop();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
	}

}
