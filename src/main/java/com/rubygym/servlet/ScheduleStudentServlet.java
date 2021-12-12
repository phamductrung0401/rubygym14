package com.rubygym.servlet;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.rubygym.model.Schedule;
import com.rubygym.utils.HibernateUtil;
import com.rubygym.utils.HttpRequestUtil;
import com.rubygym.utils.HttpResponseUtil;
import com.rubygym.utils.ScheduleUtil;
import com.rubygym.utils.TimeUtil;

@WebServlet(urlPatterns = "/schedule-student/*")
public class ScheduleStudentServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Object[]> list = null;
		JSONArray data = new JSONArray();
		JSONArray error = new JSONArray();
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
//		if (HttpRequestUtil.checkAuthentication(req)) {
			
			try {
				String idString = HttpRequestUtil.parseURL(req, "schedule-student");
				if (ScheduleUtil.checkStudentId(Integer.parseInt(idString))) {
					list = ScheduleUtil.getStudentSchedule(Integer.parseInt(idString));
					for (Object[] s : list) {
						JSONObject tmp = new JSONObject();
						tmp.put("scheduleId", s[0]);
						tmp.put("dayOfWeek", s[1]);
						tmp.put("start", s[2]);
						tmp.put("finish", s[3]);
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
			
			
//		}
//		
//		
//		else {
//			data.add(null);
//			error.add("Yêu cầu đăng nhập");
//			HttpResponseUtil.setResponse(resp, data, error);	
//		}
	}
}
