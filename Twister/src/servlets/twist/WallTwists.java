package servlets.twist;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.TwistS;



public class WallTwists extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public WallTwists() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ça liste les messages, n'importe quel utilisateur (connecté ou non)
		
		String key_session=request.getParameter("key_session");
		
		
		JSONObject json= TwistS.wallTwists(key_session);
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
