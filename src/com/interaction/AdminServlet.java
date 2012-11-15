package com.interaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {		
		PrintWriter out = response.getWriter();
		
		//create context servlet object
		ServletContext sc = getServletConfig().getServletContext();
		
		@SuppressWarnings("rawtypes")
		Enumeration users = sc.getAttributeNames();
		
		RequestDispatcher rd = request.getRequestDispatcher("menu.html");
		rd.include(request, response);
		
		rd = request.getRequestDispatcher("header.html");
		rd.include(request, response);
		
		out.println("<h3>Users online:</h3><br>");
		
		//parse context attributes
		while (users.hasMoreElements()) {
			String name =  (String) users.nextElement();
			// Get the value of the attribute
		    Object u = getServletContext().getAttribute(name);
			if (u instanceof User) {
				out.println("<div style=\"width: 400px; margin-right: auto; margin-left: 5%;\">");
				out.println("<h4>     "+((User) u).getUsername());
				out.println("came in at "+((User) u).getHour()+":"+((User) u).getMinute()+":"+((User) u).getSecond()+"</h4></div><br>");
			}
		}
		
		rd = request.getRequestDispatcher("footer.html");
		rd.include(request, response);
		out.close();
	}

}
