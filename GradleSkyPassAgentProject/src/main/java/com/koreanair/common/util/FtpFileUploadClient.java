package com.koreanair.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FtpFileUploadClient {
	private final static Logger log = LoggerFactory.getLogger(FtpFileUploadClient.class);
	
	private String serverip = "";
	private String id = "";
	private String pwd = "";
	private int port;
	private String downLocation = "";
	private String workingLocation = "";
	private FTPClient ftpClient;

	public FtpFileUploadClient() throws Exception {
		ftpClient = new FTPClient();
		
		String skypassInstance = System.getProperty("skypass.instance");
		String envResource = "config/environments.properties";            
		Reader envReader = Resources.getResourceAsReader(envResource);
        Properties properties = new Properties();
        properties.load(envReader);
        
		this.serverip = properties.getProperty(skypassInstance+".ftp.ip");
		this.id = properties.getProperty(skypassInstance+".ftp.id");
		this.pwd = properties.getProperty(skypassInstance+".ftp.password");
		this.port = Integer.parseInt(properties.getProperty(skypassInstance+".ftp.port"));
		this.downLocation = properties.getProperty(skypassInstance+".ftp.down.location");
		this.workingLocation = properties.getProperty(skypassInstance+".ftp.working.location"); 
	}

	/**
	 * ftp 서버 접속
	 */
	protected void connect() {
		try{
			ftpClient.connect(serverip, port);
			int reply;
			reply = ftpClient.getReplyCode();

			if(!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				log.debug("FTP 서버 연결 거부");
			}else{
				log.debug("FTP 서버 연결");
			}
		}catch(IOException e){
			if(ftpClient.isConnected()) {
				try {ftpClient.disconnect();} catch(IOException f) {}
			}
		}
	}

	/**
	 * ftp서버 로그인
	 * @return
	 */
	protected boolean login(){
		try{
			this.connect();
			log.debug("FTP 서버 로그인");

			return ftpClient.login(id, pwd);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ftp 서버 로그아웃
	 * @return
	 */
	protected boolean logout(){
		try{
			log.debug("FTP 서버 로그아웃");

			return ftpClient.logout();
		}catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}

	protected boolean disConnect(){
		try{
			log.debug("FTP disConnect");

			ftpClient.disconnect();
			return true;
		}catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 전송 인코딩
	 */
	protected void utf8() {
		ftpClient.setControlEncoding("UTF-8");
	}

	/**
	 * passmod
	 */
	protected void passmod() {
		ftpClient.enterLocalPassiveMode();
	}
	
	/**
	 * 파일 전송 타입
	 * @param filetype
	 */
	protected void setFileType(int filetype){
		try {
			ftpClient.setFileType(filetype);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 파일삭제
	 * @param filename
	 */
	protected void delfile(String filename) {
		try {
			ftpClient.deleteFile(filename);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 폴더 이동
	 * @param path
	 */
	protected void chdir(String path){
		try{
			ftpClient.changeWorkingDirectory(path);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 폴더생성
	 * @param path
	 * @return
	 */
	protected boolean makeDirectory(String path){
		try{
			return ftpClient.makeDirectory(path);
		}catch(IOException e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 파일명 변경
	 * @param fromname
	 * @param toname
	 */
	protected void renamefile(String fromname, String toname) {
		try {
			ftpClient.rename(fromname, toname);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 파일 리스트 정보
	 * @return
	 */
	protected String[] fileList(String path){
		String[] files = null;
		try{
			//files = this.ftpClient.listNames("*.*");

            FTPFileFilter filter = new FTPFileFilter(){
                @Override
                public boolean accept(FTPFile ftpFile){
                	boolean bi = ftpFile.isFile();
                	if(bi){
	                	if(!ftpFile.getName().contains("db")){
	                		bi = true;
	                	}else{
	                		bi = false;
	                	}
                	}
                	return bi;
                }
            };
            
			FTPFile[] ftpFiles = this.ftpClient.listFiles(path, filter);
	        if (ftpFiles != null){
	        	files = new String[ftpFiles.length];
	        	for (int i = 0; i < ftpFiles.length; i++){
	        		files[i] = ftpFiles[i].getName();
	        	}
	        }
			
	        log.debug("File list 가져오기 >>>>> " + path);
			return files;
			
            /*
		    int fileTotalCnt = 0;
		    FTPListParseEngine engine = ftpClient.initiateListParsing(path); // 목록을 나타낼 디렉토리
		    while (engine.hasNext()) {
		        FTPFile[] ftpfiles = engine.getNext(100); // 100개 단위로 끊어서 가져온다

		        if (ftpfiles != null) {
		        	for (int i = 0; i < ftpfiles.length; i++) {
		        		FTPFile file = ftpfiles[i];
		        		files[fileTotalCnt] = file.getName();
		            	fileTotalCnt++;
		        	}
		        }
		    }
		    */
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 폴더 정보를 가져온다.
	 * @param path
	 * @return
	 */
	protected String[] folderList(String path){
		String[] folders = null;
		try{
			FTPFile[] ftpFolder = this.ftpClient.listDirectories(path);  // public 폴더의 모든 파일을 list 합니다
	        if (ftpFolder != null) {
	        	folders = new String[ftpFolder.length];
	        	for (int i = 0; i < ftpFolder.length; i++) {
	        		folders[i] = ftpFolder[i].getName();
	        	}
	        }
			
			return folders;
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ftp서버에서 파일 다운
	 * @param sourceFile
	 * @param targetFolder
	 * @param name
	 * @return
	 */
	protected boolean downloadFile(String sourceFile, String targetFolder){
		boolean result = false;

		OutputStream os = null;
		try {
			File downloadFile = new File(targetFolder, sourceFile);
			os = new FileOutputStream(downloadFile);
		
			if(ftpClient.retrieveFile(sourceFile, os)) {
				result = true;
			}

			log.debug("	sourceFile : "+sourceFile+ " => File Download 완료");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(os !=null){ try {os.close();} catch (IOException e) {} os=null;}
		}
		return result;
	}

	/**
	 * ftp서버에 파일 업로드
	 * @param sourceFile
	 * @param targetFolder
	 * @return
	 */
	protected boolean uploadFile(String sourceFile){
		boolean result = false;

        FileInputStream fis = null;
		try {
			File uploadFile = new File(sourceFile);
            fis = new FileInputStream(uploadFile);

            if(ftpClient.storeFile(uploadFile.getName(), fis)) {
            	result = true;
            }

            log.debug("	sourceFile : "+sourceFile+ " => File Upload 완료");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis !=null){ try {fis.close();} catch (IOException e) {} fis=null;}
		}
		return result;
	}

	/**
	 * ftp서버의 하위폴더 모든 문서 다운로드.
	 * @param sourceFolder
	 * @throws Exception
	 */
	public void subFileFolderListDownload(String sourceFolder) throws Exception{
		File targetFolderRoot = new File(downLocation+File.separator+sourceFolder);
		if(!targetFolderRoot.exists()){
				targetFolderRoot.mkdirs();
		}
		
    	String[] fileList = fileList(sourceFolder);
    	if(fileList != null) {
    		for(int i = 0; i < fileList.length; i++) {
    			String file = fileList[i];
    			chdir(sourceFolder);//ftp서버 폴더 이동
    			boolean bi = downloadFile(file, downLocation+File.separator+sourceFolder);
    			if(bi){
    				renamefile(file, file+".complite");
    				//다운로드 완료 되었으니. Thread객체를 이용하여 파일 변환 또는 파싱 작업
    			}
    		}
    	}
        	
    	String[] folderList = folderList(sourceFolder);
		if(folderList != null){
			for(int i = 0; i < folderList.length; i++) {
				String folder = folderList[i];

				File targetFolders = new File(downLocation+File.separator+sourceFolder+File.separator+folder);
				targetFolders.mkdirs();
				
				subFileFolderListDownload(sourceFolder+File.separator+folder);
			}
		}

	}
	
	/**
	 * 로컬의 하위폴더 모든 문서 업로드.
	 * @param sourceFolder
	 * @throws Exception
	 */
	public void subFileFolderListUpload(String sourceFolder, String ftpRootFolder) throws Exception{
		makeDirectory(ftpRootFolder);
		chdir(ftpRootFolder);//ftp서버 폴더 이동
		
		File rootFoldor = new File(sourceFolder);
        if(rootFoldor.exists() && rootFoldor.isDirectory()){
        	File[] fileList = rootFoldor.listFiles(); 
        	for(int i = 0 ; i < fileList.length ; i++){
        		File file = fileList[i]; 
				if(file.isFile()){
					boolean bi = uploadFile(sourceFolder+File.separator+file.getName());
					if(bi){
						
					}
				}
        		
        	}
        	
        	for(int i = 0 ; i < fileList.length ; i++){
        		File file = fileList[i]; 
				if(file.isDirectory()){
					String folder = file.getName();
					//makeDirectory(folder);
					
					subFileFolderListUpload(sourceFolder+File.separator+folder, folder);
				}
				//break;
			}
        }
	}
	
	/**
	 * 로컬파일을 특정 폴더로 이동
	 * @param sourceFolder
	 * @param targetFolder
	 */
	public void subFileFolderListMove(String sourceFolder, String targetFolder) throws Exception{
		File rootFoldor = new File(sourceFolder);
        if(rootFoldor.exists() && rootFoldor.isDirectory()){
        	File[] fileList = rootFoldor.listFiles(); 
        	for(int i = 0 ; i < fileList.length ; i++){
        		File file = fileList[i]; 
				if(file.isFile()){
					moveFile(sourceFolder+File.separator+file.getName(), targetFolder+File.separator+file.getName());
				}
        		
        	}
        	
        	for(int i = 0 ; i < fileList.length ; i++){
        		File file = fileList[i]; 
				if(file.isDirectory()){
					String folder = file.getName();

					File targetFolders = new File(targetFolder+File.separator+folder);
					targetFolders.mkdirs();
					
					subFileFolderListMove(sourceFolder+File.separator+folder, targetFolder+File.separator+folder);
				}
				//break;
			}
        }
	}
	
	/**
	 * 파일 이동
	 * @param folderName 폴더 생성할 이름
	 * @param fileName 바뀔 이름
	 * @param beforeFilePath 옮길 대상 경로
	 * @param afterFilePath 옮겨질 경로
	 * @return
	 */
    public boolean moveFile(String sourceFilePathName, String targetFilePathName) {

        try{
            File file =new File(sourceFilePathName);
 
            if(file.renameTo(new File(targetFilePathName))){ //파일 이동
                return true; //성공시 성공 파일 경로 return
            }else{
                return false;
            }
 
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public void ftpClientBatchStart(){
		try{
			login();
			utf8();
			passmod();
			setFileType(FTP.BINARY_FILE_TYPE);
			
			//파일다운로드
			//subFileFolderListDownload("");//최상위 부터 모든 폴더및 파일 다운로드
			//subFileFolderListDownload("\\ftpFolder1");//특정폴더 밑에부터 다운로드
			
			//하나파일 업로드
			//uploadFile("D:\\test.db");
			
			//특정폴더 밑에 있는 모든 파일 업로드
			//subFileFolderListUpload("D:\\work\\downSourceFile_tot", "downSourceFile_tot");
			
			//작업폴더로 이동.
			subFileFolderListMove("D:\\work\\downTargetFile", workingLocation);
		}catch(Exception e){
			e.printStackTrace(); 
		}finally{
			logout();
			disConnect();
		}	
    }
    
	public void ftpClientThread(){

		while(true){
			ftpClientBatchStart();
			try {
				Thread.sleep(60*1000);//1분
				//Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		FtpFileUploadClient main = new FtpFileUploadClient();
		main.ftpClientThread();
	}

}