package com.interaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CounterServlet
 */
public class CounterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int lYear = Calendar.getInstance().get(Calendar.YEAR);
	private int lMonth = Calendar.getInstance().get(Calendar.MONTH);
	private int lDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CounterServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		RequestDispatcher dispatch1 = request
				.getRequestDispatcher("/components/menu.html");
		dispatch1.include(request, response);

		// create session variable for username and password attributes
		HttpSession session = request.getSession();
		@SuppressWarnings("unused")
		String hourString = "", minuteString = "", secondString = "";
		Integer sessionHour = null, sessionMinute = null, sessionSecond = null;
		try {
			sessionHour = Integer.parseInt((String) session
					.getAttribute("hour"));
			sessionMinute = Integer.parseInt((String) session
					.getAttribute("minute"));
			sessionSecond = Integer.parseInt((String) session
					.getAttribute("second"));
		} catch (Exception e) {
		}

		String username = (String) request.getAttribute("username");
		String password = (String) request.getAttribute("password");

		if (sessionHour == null || sessionMinute == null
				|| sessionSecond == null) {
			session.setAttribute("hour", 0);
			session.setAttribute("minute", 0);
			session.setAttribute("second", 0);

		} else {
			hourString = sessionHour.toString();
			minuteString = sessionMinute.toString();
			secondString = sessionSecond.toString();
		}
		printHTMLContent(out, request, response, username, password, false,
				null);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// display the parameter names and values	

		PrintWriter out = response.getWriter();		

		// get session variable
		@SuppressWarnings("unused")
		HttpSession session = request.getSession();

		// read input variables
		boolean ok = true;
		String errorMessage = "";
		int sHour = 9, sMinute = 0, sSecond = 0;
		String username = (String) request.getParameter("username");
		String password = (String) request.getParameter("password");
		try {
			sHour = Integer.parseInt(request.getParameter("hour"));
			sMinute = Integer.parseInt(request.getParameter("minute"));
			sSecond = Integer.parseInt(request.getParameter("second"));
		} catch (Exception e) {
			ok = false;
		}

		// if input contains invalid data, which is not a number
		if (!ok) {
			errorMessage = "*Please input valid data!";
			printHTMLContent(out, request, response, username, password, true,
					errorMessage);
		} else {
			errorMessage = isHourValid(sHour);
			if (!errorMessage.equals("ok")) {
				printHTMLContent(out, request, response, username, password,
						true, errorMessage);
			} else {
				// session.setAttribute("hour", sHour);
				errorMessage = isMinuteValid(sMinute);
				if (!errorMessage.equals("ok")) {
					printHTMLContent(out, request, response, username,
							password, true, errorMessage);
				} else {
					// session.setAttribute("minute", sMinute);
					errorMessage = isSecondValid(sSecond);
					if (!errorMessage.equals("ok")) {
						printHTMLContent(out, request, response, username,
								password, true, errorMessage);
					} else {
						// session.setAttribute("second", sSecond);

						// display the time left
						RequestDispatcher rd = request.getRequestDispatcher("/components/menu.html");
						rd.include(request, response);
						
						rd = request.getRequestDispatcher("/components/header.html");
						rd.include(request, response);
						out.println("<div style=\"width: 400px; margin-right: auto; margin-left: 38%;\">");
						out.println("<h3>"+computeTimeLeft(sHour, sMinute, sSecond)+"</h3></div>");

						// show logout button
						out.println("<form method=\"post\" action =\""
								+ request.getContextPath() + "/logout\" >");
						out.println("<input type=\"hidden\" name=\"username\" value="
								+ username + ">");
						 rd = request
								.getRequestDispatcher("/components/logout.html");
						rd.include(request, response);
						
						rd = request.getRequestDispatcher("/components/footer.html");
						rd.include(request, response);

						// save user configuration into global servlet context
						saveConfig(username, password, sHour, sMinute, sSecond);
						out.close();
					}
				}
			}
		}
	}

	private void printHTMLContent(PrintWriter out, HttpServletRequest request,
			HttpServletResponse response, String username, String password,
			boolean showError, String errorMessage) throws ServletException,
			IOException {

		// output the HTML content
		RequestDispatcher rd = request.getRequestDispatcher("/components/menu.html");
		rd.include(request, response);
		
		rd = request.getRequestDispatcher("/components/header.html");
		rd.include(request, response);
		
		out.println("<div style=\"width: 400px; margin-right: auto; margin-left: auto; border: 1px solid #000;\">");
		out.println("<form class=\"form-horizontal\" action=\"counter\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"username\" value="
				+ username + ">");
		out.println("<input type=\"hidden\" name=\"password\" value="
				+ password + ">");
		rd = request.getRequestDispatcher("/components/timeForm.html");
		rd.include(request, response);

		// show logout button
		out.println("<form method=\"post\" action =\""
				+ request.getContextPath() + "/logout\" >");
		out.println("<input type=\"hidden\" name=\"username\" value="
				+ username + ">");

		rd = request.getRequestDispatcher("/components/logout.html");
		rd.include(request, response);

		if (showError) {			
			out.println("<center><font color=\"red\">" + errorMessage + "</font></center>");
		}
		rd = request.getRequestDispatcher("/components/footer.html");
		rd.include(request, response);

		out.close();

	}

	private String computeTimeLeft(int hour, int minute, int second) {
		Calendar cal1 = Calendar.getInstance();
		// current time
		cal1.getTime();
		Calendar cal2 = Calendar.getInstance();
		int lHour = hour + 9;
		// time to leave
		cal2.set(lYear, lMonth, lDay, lHour, minute, second);

		long diff = cal2.getTimeInMillis() - cal1.getTimeInMillis();

		long diffHours = diff / (60 * 60 * 1000);
		long diffMinutes = (diff % (60 * 60 * 1000)) / (60 * 1000);
		long diffSeconds = ((diff % (60 * 60 * 1000)) % (60 * 1000)) / 1000;

		if (diffSeconds >= 0)
			return String.valueOf("You have " + diffHours + ":" + diffMinutes
					+ ":" + diffSeconds + " left to go home!");
		else
			return "You're time is off";
	}

	private String isHourValid(Integer sHour) {
		String message = "ok";
		if (sHour < 0 || sHour > 23) {
			message = "*Please input a valid hour!";
		}
		return message;
	}

	private String isMinuteValid(Integer sMinute) {
		String message = "ok";
		if (sMinute < 0 || sMinute > 59) {
			message = "*Please input a valid minute!";

		}
		return message;
	}

	private String isSecondValid(Integer sSecond) {
		String message = "ok";
		if (sSecond < 0 || sSecond > 59) {
			message = "*Please input a valid second!";

		}
		return message;
	}

	private void saveConfig(String username, String password, Integer sHour,
			Integer sMinute, Integer sSecond) {
		ServletContext sc = getServletConfig().getServletContext();
		@SuppressWarnings("rawtypes")
		Enumeration users = sc.getAttributeNames();
		while (users.hasMoreElements()) {
			String nextName = (String) users.nextElement();
			Object u = (Object) sc.getAttribute(nextName);
			if (u instanceof User) {
				if (((User) u).getUsername().equals(username)
						&& ((User) u).getPassword().equals(password)) {
					((User) u).setHour(sHour);
					((User) u).setMinute(sMinute);
					((User) u).setSecond(sSecond);
					sc.setAttribute(((User) u).getUsername(), u);
				}
			}
		}
	}
}
