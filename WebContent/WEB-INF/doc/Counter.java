package com.first;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Counter
 */
public class Counter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int lYear =  Calendar.getInstance().get(Calendar.YEAR);
	private int lMonth = Calendar.getInstance().get(Calendar.MONTH);
	private int lDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	private int lHour = 18;
	private int lMinute = 0;
	private int lSecond = 0;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Counter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setIntHeader("Refresh", 1);
		PrintWriter out = response.getWriter();
	 	out.print(computeTimeLeft());
	 	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private String computeTimeLeft(){
		Calendar cal1 = Calendar.getInstance();
    	cal1.getTime();
    	Calendar cal2 = Calendar.getInstance();
    	cal2.set(lYear, lMonth, lDay, lHour, lMinute, lSecond);
    	
    	long diff = cal2.getTimeInMillis()-cal1.getTimeInMillis();
    	
    	long diffHours = diff/(60*60*1000);
    	long diffMinutes = (diff%(60*60*1000))/(60*1000);
    	long diffSeconds = ((diff%(60*60*1000))%(60*1000))/1000;
    	
    	if(diffSeconds>=0)
    	return String.valueOf("You have "+diffHours+":"+diffMinutes+":"+diffSeconds+" left to go home!");
    	else return "You're time is off";
	}

}
