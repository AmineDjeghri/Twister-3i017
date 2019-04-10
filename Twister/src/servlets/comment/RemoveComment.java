package servlets.comment;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.CommentS;
import services.TwistS;



public class RemoveComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//use map ??
		
		String key_session=request.getParameter("key_session");
		String id_message = request.getParameter("id_message");
		String id_comment=request.getParameter("id_comment");

		
		JSONObject json= CommentS.removeComment(key_session, id_message, id_comment);
		PrintWriter resp=response.getWriter();

		response.setContentType("text/json");
		resp.println(json);
		
	}

@Override
protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// TODO Auto-generated method stub
	doGet(req, resp);
}

}