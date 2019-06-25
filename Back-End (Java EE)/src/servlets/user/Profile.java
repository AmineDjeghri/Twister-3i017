package servlets.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.UserS;


public class Profile extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String login=request.getParameter("login");
		String key_session=request.getParameter("key_session");
		
		response.setContentType("text/Json");
		JSONObject res = UserS.profileUser(login, key_session);
		PrintWriter out = response.getWriter();
		
		out.println(res);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
