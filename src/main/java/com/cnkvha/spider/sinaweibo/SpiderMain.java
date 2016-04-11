package com.cnkvha.spider.sinaweibo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

public class SpiderMain {

	@Getter
	private final static SpiderMain instance = new SpiderMain(); 
	
	public static void main(String[] args) {
		instance.run();
	}
	
	@Getter
	private ConsoleManager console;
	
	@Getter
	private boolean running;
	
	@Getter
	private Logger logger = Logger.getLogger("");
	
	private void run() {
		running = true;
		console = new ConsoleManager();
		//console.startFile("log.txt");
		logger.info("SinaWeibo Web Spider");
		logger.info("Developed by KEVIN WANG");
		logger.info("kevin@cnkvha.com");
		
		console.startFile("LOG.txt");
		
		/*
		try {
			logger.info("Would you like to log into file? (y/n)");
			int logFile = console.getReader().readCharacter('Y','y','N','n');
			if(logFile == 'Y' || logFile == 'y'){
				logger.info("Enabled file logging! ");
				console.startFile("log.txt");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		*/

		Spider spider = Spider.create(new SinaWeiboProcessor())
				.addPipeline(new ConsolePipeline())
				.addUrl("http://weibo.com/u")
				.thread(2);
		
		spider.test("http://weibo.com/tofms");
		
		String command = "";
        while (isRunning()) {
            try {
                command = console.getReader().readLine(">", null);

                if (command == null || command.trim().length() == 0) {
                    continue;
                }

                if(command.equalsIgnoreCase("exit")){
                	logger.info("Shutting down... ");
                	spider.stop();
                }
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error while reading commands", ex);
            }
        }
	}
}
