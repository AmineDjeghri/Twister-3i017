package servlets.user;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.UserS;



public class EditUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public EditUser() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//user map ??
		
		// login pour vérifier que l'utilisateur est présent dans la base
		String login=request.getParameter("login");
		
		// prenom & nom & email 
		String prenom=request.getParameter("prenom");
		String nom=request.getParameter("nom");
		String email=request.getParameter("email");
		
		// key_session pour avoir l'utilisateur
		String key_session= request.getParameter("key_session");
		
		response.setContentType("text/Json");
		JSONObject res = UserS.EditUser(login, prenom, nom, email, key_session);
		PrintWriter out = response.getWriter();
		
		out.println(res);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}