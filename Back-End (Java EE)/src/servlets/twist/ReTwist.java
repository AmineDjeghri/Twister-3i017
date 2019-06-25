package servlets.twist;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.TwistS;



public class ReTwist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ReTwist() {
        super();
        // TODO Auto-generated constructor stub
    }

    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key_session=request.getParameter("key_session");
		String id_message=request.getParameter("id_message");
		String message=request.getParameter("message");

		
		JSONObject json= TwistS.ReTwist(key_session, id_message, message);
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
