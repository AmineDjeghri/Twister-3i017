package servlets.follower;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.FollowerS;



public class Follow extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Follow() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String key_session=request.getParameter("key_session");
		String id_friend=request.getParameter("id_friend");

		
		JSONObject json= FollowerS.follow(key_session, id_friend);
		PrintWriter resp=response.getWriter();

		response.setContentType("text/json");
		resp.println(json);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
