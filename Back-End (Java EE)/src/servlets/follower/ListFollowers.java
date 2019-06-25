package servlets.follower;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.FollowerS;

public class ListFollowers  extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login=request.getParameter("login");
		String key_session = request.getParameter("key_session");
		
		JSONObject json= FollowerS.listFollowers(login, key_session);
		PrintWriter resp= response.getWriter();

		response.setContentType("text/json");
		resp.println(json);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
