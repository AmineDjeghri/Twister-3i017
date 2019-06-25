package servlets.user;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.UserS;



public class ChangePasswordUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ChangePasswordUser() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//user map ??
		String new_password = request.getParameter("new_password");
		String old_password = request.getParameter("old_password");
		String key_session= request.getParameter("key_session");
		
		response.setContentType("text/Json");
		JSONObject res = UserS.changePassword(old_password,new_password, key_session);
		PrintWriter out = response.getWriter();
		
		out.println(res);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}