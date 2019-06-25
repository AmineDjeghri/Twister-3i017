package servlets.user;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.UserS;



public class CreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public CreateUser() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//user map ??
		
		String login=request.getParameter("login");
		String password=request.getParameter("password");
		String prenom=request.getParameter("firstName");
		String nom=request.getParameter("lastName");
		String email=request.getParameter("email");
		
		response.setContentType("text/Json");
		JSONObject res = UserS.createUser(prenom, nom,login, password, email);
		PrintWriter out = response.getWriter();
		
		out.println(res);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
