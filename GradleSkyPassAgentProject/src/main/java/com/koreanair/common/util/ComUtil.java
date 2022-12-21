package com.koreanair.common.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ComUtil {

    public static String NVL(String str){
        if(str == null || str.trim().equals("") || str.trim().equals("null"))
            return "";
        else
            return str;
    }
    
    
	public static String PrintStackTraceToString(Exception e){
		ByteArrayOutputStream ex_out = new ByteArrayOutputStream();
		PrintStream ex_pinrtStream = new PrintStream(ex_out);
		e.printStackTrace(ex_pinrtStream);
		String stackTraceString = ex_out.toString(); // 찍은 값을 가져오고.
		
		try{ex_out.close();}catch(Exception ex){}
		ex_pinrtStream.close();	
		
		return stackTraceString;
	}
}
