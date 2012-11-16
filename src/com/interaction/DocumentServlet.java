package com.interaction;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DocumentServlet
 */
public class DocumentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String PATH = "/WEB-INF/doc";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DocumentServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html; charset=utf-8");
		ServletContext context = getServletConfig().getServletContext();

		File vDir = new File(context.getRealPath(PATH));
		File[] fileNames = vDir.listFiles();

		// output the HTML content
		//include menu
		RequestDispatcher dispatch1 = request.getRequestDispatcher("/components/menu.html");  
		dispatch1.include(request, response);

		//include header
		dispatch1 = request.getRequestDispatcher("/components/header.html");
		dispatch1.include(request, response);
		
		
		out.println("<h3>The files in " + PATH + " are:</h3>");
		for (int i = 0; i < fileNames.length; i++) {
	
			out.println("<a href=\"document?doc=" + fileNames[i].toString().split("doc\\\\")[1] + "\">"
					+ fileNames[i].toString().split("doc\\\\")[1] + "</a><br>");
		}
	
		String doc = request.getParameter("doc");
		if (doc != null) {

			RequestDispatcher dispatch = request.getRequestDispatcher(PATH+"/"+doc);  
			out.println("<h3>"+doc+" starts here</h3><br>");	
			dispatch.include(request, response);
			

			out.println("<br>");
			out.println("----------------------------------------------------------------------------<br>");
		}
		dispatch1 = request.getRequestDispatcher("/components/footer.html");
		dispatch1.include(request, response);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

}
