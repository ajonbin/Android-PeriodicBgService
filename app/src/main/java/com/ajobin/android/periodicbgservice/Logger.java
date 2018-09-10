package com.ajobin.android.periodicbgservice;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private FileOutputStream fos;
	private Context ctx;

	public void init(Context ctx, String logFileName){
		this.ctx = ctx;
		File logFile = CreateLogFile(logFileName);
		if(logFile != null){
			try {
				fos = new FileOutputStream(logFile,true);
				fos.write("-------------\n".getBytes());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public File CreateLogFile(String fileName){
		boolean isSDCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if(isSDCardExist){
			String rootPath = "/" + Helper.getAppName(ctx);
			rootPath = Environment.getExternalStorageDirectory().toString() + rootPath;
			File fileDir = new File(rootPath);
			if(!fileDir.exists()){
				fileDir.mkdirs();
			}

			return new File(rootPath + File.separator + fileName);
		}else{
			return null;
		}

	}
	public void log(String msg){
		if(fos != null){
			Date curDate = new Date(System.currentTimeMillis());
			String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(curDate);
			StringBuilder stringBuilder = new StringBuilder()
				.append(timeStr)
				.append(":")
				.append(msg)
				.append("\n");
			try {
				fos.write(stringBuilder.toString().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void log(String... msgs){
		StringBuilder stringBuilder = new StringBuilder();
		for(String msg :msgs){
			stringBuilder.append(msg);
			stringBuilder.append(' ');
		}
		log(stringBuilder.toString());
	}
}

