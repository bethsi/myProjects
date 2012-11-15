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

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		@SuppressWarnings("rawtypes")
		Enumeration paramNames = request.getParameterNames();

		boolean emptyEnum = false;
		if (!paramNames.hasMoreElements())
			emptyEnum = true;

		PrintWriter out = response.getWriter();
		if (emptyEnum) {
			out.println("<h2>No parameters</h2>");
		}

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (username == null || username.equals("") || password == null || password.equals("")) {
			RequestDispatcher rd = this.getServletConfig()
					.getServletContext()
					.getRequestDispatcher("/welcome");
			rd.include(request, response);
			out.println("<html><body><center><font color=\"red\">*Please fill in the required fields!</font></center></body></html>");			
		} else {

			User user = new User();
			user.setUsername(username);
			user.setPassword(password);

			//create servlet context object
			ServletContext sc = getServletConfig().getServletContext();
			RequestDispatcher rd = null;
			
			//check administrator credentials
			if (username.equalsIgnoreCase("admin") && password.equals("admin")) {
				 rd = this.getServletConfig()
						.getServletContext()
						.getRequestDispatcher("/admin");
				//redirect to admin page
				rd.forward(request, response);				
			} else {
				if (validCredentials(username, password, sc)){
					sc.setAttribute(username, user);
					try {					
						//redirect to user page
						rd = this.getServletConfig()
								.getServletContext()
								.getRequestDispatcher("/counter");
						request.setAttribute("username", username);
						request.setAttribute("password", password);
						rd.forward(request, response);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else{
					//redirect to index page
					rd = this.getServletConfig()
							.getServletContext()
							.getRequestDispatcher("/welcome");
					rd.include(request, response);
					out.println("<html><body><center><font color=\"red\">*Please input valid username and password!</font></center></body></html>");
					
				}
		
				
			}
		}
		out.close();
	}
	
	private boolean validCredentials(String username, String password, ServletContext sc){
		String possiblePassword = (String) sc.getAttribute("username:"+username);
		if (possiblePassword!= null ){
			String enc = encode(password).split("\r")[0]; 
			if (enc.equals(possiblePassword))			
			return true;
		}
		return false;
	}
	
	private String encode(String password){
	    BASE64Encoder encoder = new BASE64Encoder();
	    String encodedBytes = encoder.encodeBuffer(password.getBytes());
		return encodedBytes;
	}
	
	protected byte[] decode(String password) throws IOException{
		 BASE64Decoder decoder = new BASE64Decoder();
		 byte[] decodedBytes = decoder.decodeBuffer(password);
		 return decodedBytes;
	}

}
