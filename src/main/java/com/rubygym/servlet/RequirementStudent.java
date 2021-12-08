package com.rubygym.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.rubygym.model.Time;
import com.rubygym.utils.HibernateUtil;
import com.rubygym.utils.HttpRequestUtil;
import com.rubygym.utils.HttpResponseUtil;

@WebServlet(urlPatterns = "/requirement-student/*")
public class RequirementStudent extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<Object[]> list = null;
		JSONArray data = new JSONArray();
		JSONArray error = new JSONArray();
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		if (HttpRequestUtil.checkAuthentication(req)) {
			
			try {
				String idString = HttpRequestUtil.parseURL(req, "requirement-student");
				if (HibernateUtil.checkStudentId(Integer.parseInt(idString))) {
					Session session = HibernateUtil.factory.openSession();
					session.beginTransaction();
					
					list = session.createQuery("select r.timeId, r.scheduleId, r.category"
							+ " from Requirement r, TrainerStudent ts"
							+ " where r.trainerStudentId = ts.id"
							+ " and ts.studentId = " + Integer.parseInt(idString)).getResultList();
					
					for (Object[] r : list) {
						// update requirement
						if (r[0] != null && r[1] != null) {
							// new time
							r[0] = session.createQuery("select t from Time t where t.id = " + (int) r[0]).uniqueResult();
							// old time
							r[1] = session.createQuery("select t"
									+ " from Time t, Schedule s"
									+ " where t.id = s.timeId and"
									+ " s.id = " + (int) r[1]).uniqueResult();
							
						}
						// delete requirement
						else if ((int) r[2] == -1) {
							r[0] = null;
							r[1] = session.createQuery("select t"
									+ " from Time t, Schedule s"
									+ " where t.id = s.timeId and"
									+ " s.id = " + (int) r[1]).uniqueResult();
						}
						// create requirement
						else if ((int) r[2] == 1) {
							r[0] = session.createQuery("select t from Time t where t.id = " + (int) r[0]).uniqueResult();
							r[1] = null;
						}
					}
					
					session.getTransaction().commit();
					
					for (Object[] r : list) {
						JSONObject tmp = new JSONObject();
						
						tmp.put("oldTime", r[1]);
						tmp.put("newTime", r[0]);
						tmp.put("category", r[2]);
						data.add(tmp);
						
					}
					error.add(null);
					HttpResponseUtil.setResponse(resp, data, error);
				}
				
				else {
					data.add(null);
					error.add("ID này không đăng ký gói tập luyện với PT hoặc chưa tồn tại trong hệ thống. \nKhông có chức năng này");
					HttpResponseUtil.setResponse(resp, data, error);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				
				data.add(null);
				error.add(e.getMessage());
				HttpResponseUtil.setResponse(resp, data, error);
			}
		
		}
		else {
			data.add(null);
			error.add("Yêu cầu đăng nhập");
			HttpResponseUtil.setResponse(resp, data, error);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doDelete(req, resp);
	}
}
