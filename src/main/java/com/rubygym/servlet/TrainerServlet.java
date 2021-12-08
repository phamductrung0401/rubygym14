package com.rubygym.servlet;
import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.rubygym.model.Trainer;
import com.rubygym.utils.HttpRequestUtil;
import com.rubygym.utils.HibernateUtil;


@WebServlet("/trainer")
public class TrainerServlet extends HttpServlet  {
	
	private static final long serialVersionUID = 1L;
	private SessionFactory factory =  HibernateUtil.getSessionFactory();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			Session session = factory.openSession();
			Transaction tx = session.beginTransaction();
			
			//đọc body của http request
			JSONObject t =  (JSONObject) HttpRequestUtil.getBody(req);
			Trainer newTrainer = new Trainer();
			if (t.get("avatar") != null) newTrainer.setAvatar((String) t.get("avatar"));
			if (t.get("name") != null)newTrainer.setName((String) t.get("name"));
			if (t.get("sex") != null) newTrainer.setSex((int) t.get("sex"));
			if (t.get("date_of_birth") != null)newTrainer.setDateOfBirth(null);
			if (t.get("phone_number") != null)newTrainer.setPhoneNumber((String) t.get("phone_number"));
			if (t.get("email") != null)newTrainer.setEmail((String) t.get("email"));
			if (t.get("description") != null)newTrainer.setDescription((String) t.get("description"));
			
			session.save(newTrainer);
			
			tx.commit();
			
			
			//gửi http response về cho client
			JSONObject bodyJsonResponse = new JSONObject();
			bodyJsonResponse.put("error", "null");
			bodyJsonResponse.put("data", "null");
			String bodyStringResponse = bodyJsonResponse.toJSONString();
			
			PrintWriter out = res.getWriter();
		    res.setContentType("application/json");
		    res.setCharacterEncoding("UTF-8");
		    out.print(bodyStringResponse);
		    out.flush();  					
			
		} catch (Exception e) {
			JSONObject bodyJsonResponse = new JSONObject();
			bodyJsonResponse.put("error", e.getMessage());
			JSONArray errors = new JSONArray();
			((ArrayList) errors).add(e.getMessage());
			bodyJsonResponse.put("error", errors);
			bodyJsonResponse.put("data", "null");
			String bodyStringResponse = bodyJsonResponse.toJSONString();
			PrintWriter out = res.getWriter();
		    res.setContentType("application/json");
		    res.setCharacterEncoding("UTF-8");
		    out.print(bodyStringResponse);
		    out.flush();
 				
		} finally {
			factory.close();
		}
	}
	
	
}
