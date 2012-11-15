package com.interaction;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LogoutServlet
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = (String) request.getParameter("username");
		
		ServletContext sc = getServletConfig().getServletContext();
		if (username!= null)
			sc.removeAttribute(username);
		
		RequestDispatcher rd = this.getServletConfig().getServletContext()
				.getRequestDispatcher("/menu.html");
		rd.include(request, response);
		RequestDispatcher rd1 = this.getServletConfig().getServletContext()
		.getRequestDispatcher("/index.html");
		rd1.include(request, response);
	}

}
