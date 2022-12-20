package com.koreanair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.koreanair.biz.CreateJsonParsingDataService;
import com.koreanair.common.util.DateUtil;
import com.koreanair.dao.SpParsingMasterDAO;
import com.koreanair.dto.ServiceMDEEntriesGroupKey;

public class ETLMainJob implements InterruptableJob {
	private final static Logger log = LoggerFactory.getLogger(ETLMainJob.class);
	
	private static final SimpleDateFormat TIMESTAMP_FMT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSSS");
	private ArrayList<Future<String>> callableFutureList = new ArrayList<Future<String>>();
    private Thread jboCurrentThread = null;
    
    @Override
    public void execute(JobExecutionContext context) {
    	
    	try {
	        // 현재 Thread 저장
	        this.jboCurrentThread = Thread.currentThread();
	               
	        JobDataMap map = context.getJobDetail().getJobDataMap();
	        Calendar startDate = Calendar.getInstance();
	        String startCurrentDate = TIMESTAMP_FMT.format(startDate.getTime());
	        String message = map.getString("message");
	        String jobName = context.getJobDetail().getKey().getName();
	        
	        System.out.println(String.format("[%-18s][%s][%s][%s] ETLMainJob Running...", this.getClass().getName(), jobName, message,startCurrentDate));
	        
	        ///////////////////////////////////////////////////////////////////////////
	        CreateJsonParsingDataService service = new CreateJsonParsingDataService();
	        //1. 파싱데이터 생성
	        try {
	        	service.createMoveParsingData();
	        	//service.createParsingDataSmaple();
	        }catch(Exception ex) {
	    		log.error("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆CreateJSONParsing ERROR☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆", ex);
	    		service.tableTruncate(false);
	    		throw ex;
	        }
	        
	        //2. Thread
	        //bizThreadCall();
	        
	        //3. truncate
	        service.tableTruncate(true);
	        ///////////////////////////////////////////////////////////////////////////	        
	        
	        Calendar endDate = Calendar.getInstance();
	        String endCurrentDate = TIMESTAMP_FMT.format(endDate.getTime());
	        System.out.println(String.format("[%-18s][%s][%s][%s] ETLMainJob Finish!!!", this.getClass().getName(), jobName, message,endCurrentDate));	        
	        System.out.println(DateUtil.dateDiff(startDate, endDate));
	        
    	}catch(Exception ex) {
    		log.error("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆ETLMainJob ERROR☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆", ex);
    	}
    }
 
    @Override
    public void interrupt() throws UnableToInterruptJobException {
        // interrupt 설정
        //   - 강제종료
        if( this.jboCurrentThread != null ) {
            this.jboCurrentThread.interrupt();
        }
    }
    
    
    
	public void bizThreadCall() throws Exception{
		int threadCount = 1000;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		try{
			SpParsingMasterDAO spParsingMasterDAO = new SpParsingMasterDAO();
			
			int totalInsertCnt = 0;
			while(true) {
		        List<HashMap<String, Object>> alist= spParsingMasterDAO.jsonContentList(new HashMap<String, Object>());
		        if(alist!=null && alist.size()>0) {
			        //그룹핑
			        Map<ServiceMDEEntriesGroupKey, List<HashMap<String, Object>>> collect = alist.stream()
			                								  .collect(Collectors.groupingBy(ServiceMDEEntriesGroupKey::new));
			        int i=0;
			        for (Entry<ServiceMDEEntriesGroupKey, List<HashMap<String, Object>>> entrySet : collect.entrySet()) {	
			        	List<HashMap<String, Object>> list = entrySet.getValue();
			        	
						String threadName = i + "번째 스레드";				
						Callable<String> thread = new ETLProcessThread(this, threadName, list) ;				
						callableFutureList.add(executorService.submit(thread));
						i++;
					}
					
					while(callableFutureList.size() != 0){
						for(int z = 0 ; z < callableFutureList.size() ; z++){
							Future<String> callable = callableFutureList.get(z);
							if(callable.isDone()){
								callableFutureList.remove(z);
							}
						}
					}
					totalInsertCnt = totalInsertCnt + alist.size();
					log.debug(String.format("[%-18s][☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆ %d]", "Thread End", totalInsertCnt));
		        }else {
		        	break;
		        }
			}
		}catch(Exception ex){
			log.error("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆ETLMainJob Thread Call ERROR☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆", ex);
			executorService.shutdownNow();
			throw ex;
		}finally{
			executorService.shutdown(); 
		}
	}	
	
}
