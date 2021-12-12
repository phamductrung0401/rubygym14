package com.rubygym.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.rubygym.utils.HibernateUtil;
import com.rubygym.utils.HttpRequestUtil;
import com.rubygym.utils.HttpResponseUtil;
import com.rubygym.utils.ScheduleUtil;

@WebServlet(urlPatterns = "/period-trainer/*")
public class PeriodTrainerServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<Object[]> list = null;
		JSONArray data = new JSONArray();
		JSONArray error = new JSONArray();
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		try {
			
			String idString = HttpRequestUtil.parseURL(req, "period-trainer");
			if (ScheduleUtil.checkTrainerId(Integer.parseInt(idString))) {
				
				Session session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();
				list = session.createQuery("select p.id, t.dayOfWeek, t.start, t.finish, p.pDate, p.content, p.note, s.name"
						+ " from Time t, TrainerStudent ts, Period p, Student s"
						+ " where t.id = p.timeId and ts.id = p.trainerStudentId"
						+ " and s.id = ts.studentId and ts.trainerId = " + idString)
						.getResultList();
				session.getTransaction().commit();
				
				for (Object[] s : list) {
					JSONObject tmp = new JSONObject();
					tmp.put("periodId", s[0]);
					tmp.put("dayOfWeek", s[1]);
					tmp.put("start", s[2]);
					tmp.put("finish", s[3]);
					tmp.put("pDate", s[4]);
					tmp.put("content", s[5]);
					tmp.put("note", s[6]);
					tmp.put("studentName", s[7]);
					data.add(tmp);
				}
				error.add(null);
				HttpResponseUtil.setResponse(resp, data, error);
				
			}
			
			else {
				data.add(null);
				error.add("ID này không tồn tại hoặc chưa được phân công trong hệ thống.");
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
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		JSONArray data = new JSONArray();
		JSONArray error = new JSONArray();
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		try {
			
			String idString = HttpRequestUtil.parseURL(req, "period-trainer");
			JSONObject jsonObject = (JSONObject) HttpRequestUtil.getBody(req);
			Integer periodId = Integer.parseInt(jsonObject.get("periodId").toString());
			String content = jsonObject.get("content").toString();
			String note = jsonObject.get("note").toString();
			
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.createQuery("update Period p set p.content = ?0, p.note = ?1 where p.id = ?2")
								.setParameter(0, content)
								.setParameter(1, note)
								.setParameter(2, periodId)
								.executeUpdate();
			session.getTransaction().commit();
			
			data.add("Sửa nội dung và ghi chú cho buổi tập thành công");
			error.add(null);
			HttpResponseUtil.setResponse(resp, data, error);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
			data.add(null);
			error.add(e.getMessage());
			HttpResponseUtil.setResponse(resp, data, error);
		}
	}
}
