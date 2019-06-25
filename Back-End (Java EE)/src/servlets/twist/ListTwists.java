package servlets.twist;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.TwistS;



public class ListTwists extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ListTwists() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ça liste les messages, n'importe quel utilisateur (connecté ou non)
		
		String key_session=request.getParameter("key_session");
		String id_user = request.getParameter("id_user");
		
		JSONObject json= TwistS.listTwists(key_session, id_user);
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
