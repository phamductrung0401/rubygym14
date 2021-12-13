package com.rubygym.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
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


@WebServlet("/account-student")
public class AccountStudentController extends HttpServlet  {
	private static final long serialVersionUID = 1L;
	static SessionFactory factory = HibernateUtil.getSessionFactory();
	// admin đăng ký tài khoản của student
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			res.addHeader("Access-Control-Allow-Origin", "*");
			Session session = factory.openSession();
			Transaction tx = session.beginTransaction();
			
			//đọc body của http request
			JSONObject t =  (JSONObject) HttpRequestUtil.getBody(req);
			System.out.print(t.toJSONString());
			AccountStudent newAccountStudent = new AccountStudent();
			if (t.get("username") != null) newAccountStudent.setUsername((String) t.get("username"));
			else {
				throw new Exception("Không được để trống tên tài khoản");
			}
			if (t.get("password") != null) { newAccountStudent.setPassword((String) t.get("password"));
			newAccountStudent.setAccumulation(0);
			newAccountStudent.setExpireDate(LocalDate.now());
			}
			
			else {
				throw new Exception("Không được để trống mật khẩu");
			}
			//Kiểm tra xem đã tồn tại tại khoản trên chưa
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<AccountStudent> cr = cb.createQuery(AccountStudent.class);
			Root<AccountStudent> root  = cr.from(AccountStudent.class);
			cr.where(root.get("username").in(t.get("username")));		
			List<AccountStudent> result = session.createQuery(cr).getResultList();
			System.out.print(result.toString());
			if(result.size() > 0) {
				throw new Exception("Tài khoản này đã tồn tại");
			}
			
			session.save(newAccountStudent);
			tx.commit();
			
			//Lấy thông tin tài khoản vừa tạo
			cb = session.getCriteriaBuilder();
			CriteriaQuery<AccountStudent> cr1 = cb.createQuery(AccountStudent.class);
			Root<AccountStudent> root1  = cr1.from(AccountStudent.class);
			cr1.where(root1.get("username").in(t.get("username")));		
			List<AccountStudent> result1 = session.createQuery(cr1).getResultList();
			
			//Gửi thông tin tài khoản vừa tạo về cho client
			JSONObject bodyJsonResponse = new JSONObject();
			JSONArray data = new JSONArray();
			for(AccountStudent temp:result1) {
				JSONObject jo = new JSONObject();
				jo.put("id", temp.getId());
				jo.put("username", temp.getUsername());
				jo.put("password", temp.getPassword());
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
							
			
		} catch (Exception e) {
			res.addHeader("Access-Control-Allow-Origin", "*");
			System.out.print(e.getMessage());
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
	
	// admin lấy tất cả thông tin về tài khoản của student
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			res.addHeader("Access-Control-Allow-Origin", "*");
			Session session = factory.openSession();
			Transaction tx = session.beginTransaction();
			String[] criteria_array = HttpRequestUtil.getQuery(req);
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<AccountStudent> cr = cb.createQuery(AccountStudent.class);
			Root<AccountStudent> root  = cr.from(AccountStudent.class);
			if(criteria_array != null) {
				for(int i=0;i<criteria_array.length;i++) {
					System.out.print(criteria_array[i].split("=")[0]);
					System.out.print(criteria_array[i].split("=")[1]);
					cr.where(root.get(criteria_array[i].split("=")[0]).in(criteria_array[i].split("=")[1]));
				}
			}
			List<AccountStudent> result = session.createQuery(cr).getResultList();
			System.out.print(result.get(0).getUsername().toString());
			JSONObject bodyJsonResponse = new JSONObject();
			JSONArray data = new JSONArray();
			for(AccountStudent temp:result) {
				JSONObject jo = new JSONObject();
				jo.put("id", temp.getId());
				jo.put("username", temp.getUsername());
				jo.put("password", temp.getPassword());
				jo.put("accumulation", temp.getAccumulation());
				jo.put("expire", temp.getExpireDate());
				jo.put("service_id", temp.getServiceId());
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
			res.addHeader("Access-Control-Allow-Origin", "*");
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
	
	
	
	// student sửa mật khẩu
	// thêm chức năng gia hạn gói tập tại đây 
	// phân biệt bằng body request ......
	
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			res.addHeader("Access-Control-Allow-Origin", "*");
			Session session = factory.openSession();
			Transaction tx = session.beginTransaction();
			
			//đọc body của http request
			JSONObject t =  (JSONObject) HttpRequestUtil.getBody(req);
			AccountStudent newAccountStudent = new AccountStudent();
		
			Long temp = (Long) t.get("id");
			
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<AccountStudent> cr = cb.createQuery(AccountStudent.class);
			Root<AccountStudent> root  = cr.from(AccountStudent.class);			
			cr.where(root.get("id").in(temp.intValue()));
			List<AccountStudent> result = session.createQuery(cr).getResultList();
			newAccountStudent = result.get(0);		

			if (t.get("password") != null) newAccountStudent.setPassword((String) t.get("password"));
			//if (t.get("expire") != null) newAccountStudent.setExpireDate((LocalDate) LocalDate.parse((CharSequence) t.get("expire")));
			//if (t.get("accumulation") != null) newAccountStudent.setAccumulation((Integer) t.get("accumulation"));
			if (t.get("service_id") != null) {
			newAccountStudent.setServiceId(((Long) t.get("service_id")).intValue());
			
			//tim kiem trong bang service record tuong ung voi service_id
			CriteriaQuery<Service> cr1 = cb.createQuery(Service.class);
			Root<Service> root1  = cr1.from(Service.class);			
			cr.where(root.get("id").in(((Long) t.get("service_id")).intValue()));
			List<Service> result1 = session.createQuery(cr1).getResultList();
			if(result1.size()==0) {
				throw new Exception("Không tồn tại gói tập");
			}
			else {	
				newAccountStudent.setExpireDate( newAccountStudent.getExpireDate().plusMonths(result1.get(0).getnMonths()));
			
				newAccountStudent.setAccumulation( newAccountStudent.getAccumulation() + result1.get(0).getnMonths());
			
			}
			}
			//tim kiem trong bang service record tuong ung voi service_id end
			session.update(newAccountStudent);
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
			res.addHeader("Access-Control-Allow-Origin", "*");
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
