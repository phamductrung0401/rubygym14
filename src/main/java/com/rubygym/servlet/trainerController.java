package com.rubygym.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.rubygym.model.*;
import com.rubygym.utils.*;



@WebServlet("/trainer")
public class trainerController extends HttpServlet  {
	private static final long serialVersionUID = 1L;
	static SessionFactory factory = HibernateUtil.getSessionFactory();
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			Session session = factory.openSession();
			Transaction tx = session.beginTransaction();
			
			//đọc body của http request
			JSONObject t =  (JSONObject) HttpRequestUtil.getBody(req);
			Trainer newTrainer = new Trainer();
			if (t.get("avatar") != null) newTrainer.setAvatar((String) t.get("avatar"));
			if (t.get("name") != null)newTrainer.setName((String) t.get("name"));
			if (t.get("sex") != null) newTrainer.setSex((int) t.get("sex"));
			if (t.get("date_of_birth") != null)newTrainer.setDate_of_birth(null);
			if (t.get("phone_number") != null)newTrainer.setPhone_number((String) t.get("phone_number"));
			if (t.get("email") != null)newTrainer.setEmail((String) t.get("email"));
			if (t.get("description") != null)newTrainer.setDescription((String) t.get("description"));
			if (t.get("account_trainer_id") != null) newTrainer.setAccount_trainer_id( ((Long) t.get("account_trainer_id")).intValue());
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
		}
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			Session session = factory.openSession();
			Transaction tx = session.beginTransaction();
			String[] criteria_array = HttpRequestUtil.getQuery(req);
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Trainer> cr = cb.createQuery(Trainer.class);
			Root<Trainer> root  = cr.from(Trainer.class);
			if(criteria_array != null) {
				for(int i=0;i<criteria_array.length;i++) {
					System.out.print(criteria_array[i].split("=")[0]);
					System.out.print(criteria_array[i].split("=")[1]);
					cr.where(root.get(criteria_array[i].split("=")[0]).in(criteria_array[i].split("=")[1]));
				}
			}
			List<Trainer> result = session.createQuery(cr).getResultList();
			
			for(Trainer temp:result) {
				System.out.print(temp.getName());
			}
			JSONObject bodyJsonResponse = new JSONObject();
			JSONArray data = new JSONArray();
			for(Trainer temp:result) {
				JSONObject jo = new JSONObject();
				jo.put("id", temp.getId());
				jo.put("avatar", temp.getAvatar());
				jo.put("name", temp.getName());
				jo.put("sex", temp.getSex());
				jo.put("date_of_birth", temp.getDate_of_birth());
				jo.put("phone_number", temp.getPhone_number());
				jo.put("description", temp.getDescription());
				jo.put("account_trainer_id", temp.getAccount_trainer_id());
				((ArrayList) data).add(jo);
			}
			bodyJsonResponse.put("data", data);
			bodyJsonResponse.put("error", "null");
			String bodyStringResponse = bodyJsonResponse.toJSONString();
			PrintWriter out = res.getWriter();
		    res.setContentType("application/json");
		    res.setCharacterEncoding("UTF-8");
		    out.print(bodyStringResponse);
		    out.flush(); 
		}
		catch(Exception e) {
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
		}
	}
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			Session session = factory.openSession();
			Transaction tx = session.beginTransaction();
			
			//đọc body của http request
			JSONObject t =  (JSONObject) HttpRequestUtil.getBody(req);
			Trainer newTrainer = new Trainer();
		
			Long temp = (Long) t.get("id");
			
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Trainer> cr = cb.createQuery(Trainer.class);
			Root<Trainer> root  = cr.from(Trainer.class);			
			cr.where(root.get("id").in(temp.intValue()));
			List<Trainer> result = session.createQuery(cr).getResultList();
			newTrainer = result.get(0);		

			if (t.get("avatar") != null) newTrainer.setAvatar((String) t.get("avatar"));
			if (t.get("name") != null)newTrainer.setName((String) t.get("name"));
			if (t.get("sex") != null) newTrainer.setSex((int) t.get("sex"));
			if (t.get("date_of_birth") != null)newTrainer.setDate_of_birth(Date.valueOf((String) t.get("date_of_birth")));
			if (t.get("phone_number") != null)newTrainer.setPhone_number((String) t.get("phone_number"));
			if (t.get("email") != null)newTrainer.setEmail((String) t.get("email"));
			if (t.get("description") != null)newTrainer.setDescription((String) t.get("description"));
			if (t.get("account_trainer_id") != null)newTrainer.setAccount_trainer_id( ((Long) t.get("account_trainer_id")).intValue());
			session.update(newTrainer);
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
		}
	}
	
}
