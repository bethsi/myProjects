package com.interaction;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletContextExample implements ServletContextListener {
	private ServletContext context;
	final static Charset ENCODING = StandardCharsets.UTF_8;

	public void contextInitialized(ServletContextEvent contextEvent) {
		context = (ServletContext) contextEvent.getServletContext();

		// set variable to servlet context
		List<String> fileLines = null;
		try {
			fileLines = readTextFile(context.getRealPath("/WEB-INF/user.txt"));					
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//save file content to application context
		saveToServletContext(fileLines);		
	}

	public void contextDestroyed(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();
	}

	private List<String> readTextFile(String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
		return Files.readAllLines(path, ENCODING);
	}
	
	private void saveToServletContext(List<String> lines){
		String[] values;
		for (String s: lines){
			values = s.split(",");	
			context.setAttribute("username:"+values[0], values[1]); 
		}
	}
	
	
}
