package com.rubygym.servlet;

import java.io.IOException;
import java.net.Inet4Address;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.rubygym.model.Requirement;
import com.rubygym.model.Time;
import com.rubygym.model.TrainerStudent;
import com.rubygym.utils.HibernateUtil;
import com.rubygym.utils.HttpRequestUtil;
import com.rubygym.utils.HttpResponseUtil;
import com.rubygym.utils.ScheduleUtil;
import com.rubygym.utils.TimeUtil;
import com.rubygym.utils.TrainerStudentUtil;

@WebServlet(urlPatterns = "/requirement-student/*")
public class RequirementStudent extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<Object[]> list = null;
		JSONArray data = new JSONArray();
		JSONArray error = new JSONArray();
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
//		if (HttpRequestUtil.checkAuthentication(req)) {
			
			try {
//				resp.addHeader("Access-Control-Allow-Origin", "*");
				String idString = HttpRequestUtil.parseURL(req, "requirement-student");
				if (ScheduleUtil.checkStudentId(Integer.parseInt(idString))) {
					Session session = HibernateUtil.getSessionFactory().openSession();
					session.beginTransaction();
					
					list = session.createQuery("select r.timeId, r.scheduleId, r.category, r.id"
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
						// toString Json
						tmp.put("oldTime", r[1]);
						tmp.put("newTime", r[0]);
						tmp.put("category", r[2]);
						tmp.put("requireId", r[3]);
						data.add(tmp);
						
					}
					error.add(null);
					HttpResponseUtil.setResponse(resp, data, error);
					
					session.close();
				}
				
				else {
					data.add(null);
					error.add("ID n??y kh??ng ????ng k?? g??i t???p luy???n v???i PT ho???c ch??a t???n t???i trong h??? th???ng. \nKh??ng c?? ch???c n??ng n??y");
					HttpResponseUtil.setResponse(resp, data, error);
				}
				
			} catch (Exception e) {
//				resp.addHeader("Access-Control-Allow-Origin", "*");
				// TODO: handle exception
				e.printStackTrace();
				
				data.add(null);
				error.add(e.getMessage());
				HttpResponseUtil.setResponse(resp, data, error);
			}
			
//		}
//		else {
//			data.add(null);
//			error.add("Y??u c???u ????ng nh???p");
//			HttpResponseUtil.setResponse(resp, data, error);
//		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		JSONArray data = new JSONArray();
		JSONArray error = new JSONArray();
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		String idString = HttpRequestUtil.parseURL(req, "requirement-student");
		
		try {
//			resp.addHeader("Access-Control-Allow-Origin", "*");
			JSONObject dataClient = (JSONObject) HttpRequestUtil.getBody(req);
			int category = Integer.parseInt(dataClient.get("category").toString()); // ch??a check null category ?
			
			// deleted requirement
			if (category == -1) {
				int oldScheduleId = Integer.parseInt(dataClient.get("scheduleId").toString()); // ch??a check oldSchedule n???m trong schedule table
				int trainerStudentId = TrainerStudentUtil.getTrainerStudentId(Integer.parseInt(idString));
				
				Session session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();
				if (ScheduleUtil.checkDuplicatedRequirement(category, oldScheduleId, null, Integer.parseInt(idString))) {
					Requirement requirement = new Requirement(trainerStudentId, oldScheduleId, null, category);
					session.save(requirement);
					
					data.add("Th??m y??u c???u xo?? th??nh c??ng");
					error.add(null);
					HttpResponseUtil.setResponse(resp, data, error);
				} else {
					error.add("Y??u c???u thay ?????i/xo?? l???ch t???p n??y ???? ???????c th???c hi???n");
					data.add(null);
					HttpResponseUtil.setResponse(resp, data, error);
				}
				
				session.getTransaction().commit();
				session.close();
			}
			// updated requirement
			else if (category == 0) {
				int oldScheduleId = Integer.parseInt(dataClient.get("scheduleId").toString()); // ch??a check oldSchedule n???m trong schedule table
				int trainerStudentId = TrainerStudentUtil.getTrainerStudentId(Integer.parseInt(idString));
				// l???y newTime t??? request
				JSONObject newTime = (JSONObject)dataClient.get("newTime");
				Time studentReqTime = TimeUtil.getTime(Integer.parseInt(newTime.get("dayOfWeek").toString()),
						LocalTime.parse(newTime.get("start").toString()),
						LocalTime.parse(newTime.get("finish").toString()));
				
				if (studentReqTime != null) {
				
					int timeId = studentReqTime.getId();
					
					Session session = HibernateUtil.getSessionFactory().openSession();
					session.beginTransaction();
					if (ScheduleUtil.checkDuplicatedRequirement(category, oldScheduleId, timeId, Integer.parseInt(idString))) {
						
						Requirement requirement = new Requirement(trainerStudentId, oldScheduleId, timeId, category);
						session.save(requirement);
						
						data.add("Th??m y??u c???u c???p nh???t th??nh c??ng");
						error.add(null);
						HttpResponseUtil.setResponse(resp, data, error);
						
					}
					
					else {
						error.add("Y??u c???u thay ?????i/xo?? l???ch t???p n??y ???? ???????c th???c hi???n ho???c l???ch t???p m???i ch??a ???????c thay ?????i."
								+ " Xo?? c??c y??u c???u thay ?????i ho???c t???o m???i l???ch t???p v?? li??n h??? v???i hu???n luy???n vi??n c???a b???n.");
						data.add(null);
						HttpResponseUtil.setResponse(resp, data, error);
					}
					
					session.getTransaction().commit();
					session.close();
				}
				
				else {
					data.add(null);
					error.add("Kh??ng th??? ????ng k?? t???p v??o gi??? ngh??? tr??a c???a trung t??m (t??? 11:00 ?????n 13:30)!");
					HttpResponseUtil.setResponse(resp, data, error);
				}
			}
			
			// created requirement
			else if (category == 1) {
				
				int trainerStudentId = TrainerStudentUtil.getTrainerStudentId(Integer.parseInt(idString));
				// l???y newTime t??? request
				JSONObject newTime = (JSONObject)dataClient.get("newTime");
				Time studentReqTime = TimeUtil.getTime(Integer.parseInt(newTime.get("dayOfWeek").toString()),
						LocalTime.parse(newTime.get("start").toString()),
						LocalTime.parse(newTime.get("finish").toString()));
				if (studentReqTime != null) {
					Integer timeId = studentReqTime.getId();
				
					Session session = HibernateUtil.getSessionFactory().openSession();
					session.beginTransaction();
					if (ScheduleUtil.checkDuplicatedRequirement(category, null, timeId, Integer.parseInt(idString))) {
						if (!ScheduleUtil.isExceedPeriod(Integer.parseInt(idString))) {
							
							
							Requirement requirement = new Requirement(trainerStudentId, null, timeId, category);
							session.save(requirement);
							
							data.add("Th??m y??u c???u t???o bu???i t???p m???i th??nh c??ng");
							error.add(null);
							HttpResponseUtil.setResponse(resp, data, error);
						}
						
						else {
							error.add("Kh??ng th??? th??m y??u c???u t???o m???i l???ch t???p do gi???i h???n c???a g??i t???p b???n ????ng k??."
									+ "Th??m y??u c???u xo?? l???ch t???p v?? li??n h??? hu???n luy???n vi??n c???a b???n ho???c Xo?? b???t y??u c???u t???o m???i l???ch t???p");
							data.add(null);
							HttpResponseUtil.setResponse(resp, data, error);
						}
					}
						
					else {
						error.add("L???ch t???p m???i n??y xung ?????t trong b???ng y??u c???u c???a b???n");
						data.add(null);
						HttpResponseUtil.setResponse(resp, data, error);
					}
			
				
					session.getTransaction().commit();
					session.close();
				}
				
				else {
					data.add(null);
					error.add("Kh??ng th??? ????ng k?? t???p v??o gi??? ngh??? tr??a c???a trung t??m (t??? 11:00 ?????n 13:30)!");
					HttpResponseUtil.setResponse(resp, data, error);
				}
			}
			// ??t x???y ra
			else {
				data.add(null);
				error.add("H??? th???ng kh??ng c?? thao t??c n??y!");
				HttpResponseUtil.setResponse(resp, data, error);
			}
			
		} catch (Exception e) {
//			resp.addHeader("Access-Control-Allow-Origin", "*");
			// TODO: handle exception
			e.printStackTrace();
			
			data.add(null);
			error.add(e.getMessage());
			HttpResponseUtil.setResponse(resp, data, error);
		}
		
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		JSONArray data = new JSONArray();
		JSONArray error = new JSONArray();
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		String idString = HttpRequestUtil.parseURL(req, "requirement-student");
		
		try {
			JSONObject dataClient = (JSONObject) HttpRequestUtil.getBody(req);
			int requireId = Integer.parseInt(dataClient.get("requireId").toString());
			
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			session.createQuery("delete from Requirement r where r.id = " + requireId).executeUpdate();
			
			session.getTransaction().commit();
			
			data.add("Xo?? y??u c???u th??nh c??ng");
			error.add(null);
			HttpResponseUtil.setResponse(resp, data, error);
			session.close();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
			data.add(null);
			error.add(e.getMessage());
			HttpResponseUtil.setResponse(resp, data, error);
		}
	}
}
