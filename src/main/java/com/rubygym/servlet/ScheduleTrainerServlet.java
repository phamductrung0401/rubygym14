package com.rubygym.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.rubygym.model.Schedule;
import com.rubygym.utils.HibernateUtil;
import com.rubygym.utils.HttpRequestUtil;
import com.rubygym.utils.HttpResponseUtil;

@WebServlet(urlPatterns = "/schedule-trainer/*")
public class ScheduleTrainerServlet extends HttpServlet {
	
//	private SessionFactory factory = HibernateUtil.getSessionFactory();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Object[]> list = null;
		JSONArray data = new JSONArray();
		JSONArray error = new JSONArray();
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		if (HttpRequestUtil.checkAuthentication(req)) {
			
			try {
				String idString = HttpRequestUtil.parseURL(req, "schedule-trainer"); // c1
				
				// c2: truy van tren query string:
//				Map<String, String> paramsMap = HttpRequestUtil.parseQuery(req);
//				
//				// kiểm tra mã trainer
//				String idString = paramsMap.get("trainerId");
//				if (idString != null) {
				
				if (HibernateUtil.checkTrainerId(Integer.parseInt(idString))) {
					list = HibernateUtil.getTrainerSchedule(Integer.parseInt(idString));
					for (Object[] s : list) {
						JSONObject tmp = new JSONObject();
						tmp.put("timeId", s[0]);
						tmp.put("dayOfWeek", s[1]);
						tmp.put("start", s[2]);
						tmp.put("finish", s[3]);
						tmp.put("studentNames", s[4]);
						
						data.add(tmp);
					}
					error.add(null);
					HttpResponseUtil.setResponse(resp, data, error);
				}
				
				else {
					data.add(null);
					error.add("ID này không tồn tại trong hệ thống.");
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
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPut(req, resp);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doDelete(req, resp);
	}
}
