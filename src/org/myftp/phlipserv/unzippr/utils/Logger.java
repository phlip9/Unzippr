package org.myftp.phlipserv.unzippr.utils;

public class Logger {
	private static Logger instance;
	
	private LogInterface logger = null;
	
	private Logger() {}
	
	public void init(LogInterface logger) {
		this.logger = logger;
	}
	
	public void log(String toLog) {
		if(logger != null) {
			logger.log(toLog);
		}
	}
	
	public static synchronized Logger getInstance(){
		if(instance == null){
			instance = new Logger();
		}
		return instance;
	}
	
	public interface LogInterface {
		public void log(String toLog);
	}
}
