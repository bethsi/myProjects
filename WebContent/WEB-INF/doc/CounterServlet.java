package com.interaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Enumeration;

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
	public CounterServlet(String username, String password) {
		super();

	}

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

		// create session variable for username and password attributes
		HttpSession session = request.getSession();
		String hourString = "", minuteString = "", secondString = "";
		Integer sessionHour = (Integer) session.getAttribute("hour");
		Integer sessionMinute = (Integer) session.getAttribute("minute");
		Integer sessionSecond = (Integer) session.getAttribute("second");

		String username = (String) request.getAttribute("username");
		String password = (String) request.getAttribute("password");

		if (sessionHour == null || sessionMinute == null
				|| sessionSecond == null) {

		} else {
			hourString = sessionHour.toString();
			minuteString = sessionMinute.toString();
			secondString = sessionSecond.toString();
		}
		printHTMLContent(out, request, hourString, minuteString, secondString,
				username, password, false, null);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// display the parameter names and values
		@SuppressWarnings("rawtypes")
		Enumeration paramNames = request.getParameterNames();

		boolean emptyEnum = false;
		if (!paramNames.hasMoreElements())
			emptyEnum = true;

		PrintWriter out = response.getWriter();
		if (emptyEnum) {
			out.println("<h2>No parameters</h2>");
		}

		//get session variable
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
			errorMessage = "<h3>*Please input valid data!</h3>";
			printHTMLContent(out, request, session.getAttribute("hour")
					.toString(), session.getAttribute("minute").toString(),
					session.getAttribute("second").toString(), username,
					password, true, errorMessage);
		} else {
			errorMessage = isHourValid(sHour);
			if (!errorMessage.equals("ok")) {
				printHTMLContent(out, request, session.getAttribute("hour")
						.toString(), session.getAttribute("minute").toString(),
						session.getAttribute("second").toString(), username,
						password, true, errorMessage);
			} else {
				session.setAttribute("hour", sHour);
				errorMessage = isMinuteValid(sMinute);
				if (!errorMessage.equals("ok")) {
					printHTMLContent(out, request, session.getAttribute("hour")
							.toString(), session.getAttribute("minute")
							.toString(), session.getAttribute("second")
							.toString(), username, password, true, errorMessage);
				} else {
					session.setAttribute("minute", sMinute);
					errorMessage = isSecondValid(sSecond);
					if (!errorMessage.equals("ok")) {
						printHTMLContent(out, request,
								session.getAttribute("hour").toString(),
								session.getAttribute("minute").toString(),
								session.getAttribute("second").toString(),
								username, password, true, errorMessage);
					} else {
						session.setAttribute("second", sSecond);
						// display the time left
						out.println("<h2>Hello," + username + "</h2>");
						out.print(computeTimeLeft(sHour, sMinute, sSecond));

						// show logout button
						out.println("<html><body><form method=\"post\" action =\""
								+ request.getContextPath()
								+ "/LogoutServlet\" >");
						out.println("<input type=\"hidden\" name=\"username\" value="
								+ username + ">");
						out.println("<input type=\"submit\" value=\"Logout\"></form></body></html>");

						// save user configuration into global servlet context
						ServletContext sc = getServletConfig()
								.getServletContext();
						@SuppressWarnings("rawtypes")
						Enumeration users = sc.getAttributeNames();
						while (users.hasMoreElements()) {
							String nextName = (String) users.nextElement();
							Object u = (Object) sc.getAttribute(nextName);
							if (u instanceof User) {
								if (((User) u).getUsername().equals(username)
										&& ((User) u).getPassword().equals(
												password)) {
									((User) u).setHour(sHour);
									((User) u).setMinute(sMinute);
									((User) u).setSecond(sSecond);
									sc.setAttribute(((User) u).getUsername(), u);
								}
							}
						}
					}
				}
			}

		}

	}

	private void printHTMLContent(PrintWriter out, HttpServletRequest request,
			String hourString, String minuteString, String secondString,
			String username, String password, boolean showError,
			String errorMessage) {
		// output the HTML content
		out.println("<html><head>");

		out.println("<title>Count down</title></head><body>");
		out.println("<h2>Hello," + username + "</h2>");
		out.println("<h2>Please submit your arrival time</h2>");

		out.println("<form method=\"post\" action =\""
				+ request.getContextPath() + "/counter\" >");
		// show error message
		if (showError) {
			out.println("<font color=\"red\">"+errorMessage+"</font>");
		}
		out.println("Hour:");
		out.println("<input type=\"text\" name=\"hour\" value=" + hourString
				+ "><br>");
		out.println("Minutes:");
		out.println("<input type=\"text\" name=\"minute\" value="
				+ minuteString + "><br>");
		out.println("Second:");
		out.println("<input type=\"text\" name=\"second\" value="
				+ secondString + "><br>");

		out.println("<input type=\"hidden\" name=\"username\" value="
				+ username + ">");
		out.println("<input type=\"hidden\" name=\"password\" value="
				+ password + ">");
		
		out.println("<input type=\"submit\" value=\"Submit Time\"></form>");

		// show logout button
		out.println("<form method=\"post\" action =\""
				+ request.getContextPath() + "/logout\" >");
		out.println("<input type=\"hidden\" name=\"username\" value="
				+ username + ">");
		out.println("<input type=\"submit\" value=\"Logout\"></form>");

		out.println("</body></html>");

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
			message = "<h3>*Please input a valid hour!</h3>";
		}
		return message;
	}

	private String isMinuteValid(Integer sMinute) {
		String message = "ok";
		if (sMinute < 0 || sMinute > 59) {
			message = "<h3>*Please input a valid minute!</h3>";

		}
		return message;
	}

	private String isSecondValid(Integer sSecond) {
		String message = "ok";
		if (sSecond < 0 || sSecond > 59) {
			message = "<h3>*Please input a valid second!</h3>";

		}
		return message;
	}
}
