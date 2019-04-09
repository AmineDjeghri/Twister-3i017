package servlets.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.UserS;


public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key_session = request.getParameter("key_session");
		
		JSONObject json = new JSONObject();
		json = UserS.logout(key_session);
		
		
		PrintWriter resp=response.getWriter();
		resp.print(json);
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
