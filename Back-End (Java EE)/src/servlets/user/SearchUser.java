package servlets.user;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.UserS;



public class SearchUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public SearchUser() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String recherche=request.getParameter("recherche");	
		String key_session = request.getParameter("key_session");
		
		response.setContentType("text/Json");
		JSONObject res = UserS.SearchUser(recherche, key_session);
		PrintWriter out = response.getWriter();
		
		out.println(res);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}