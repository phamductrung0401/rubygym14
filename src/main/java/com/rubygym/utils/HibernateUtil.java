package com.rubygym.utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.rubygym.model.Schedule;

public class HibernateUtil {
	
	public static SessionFactory factory = getSessionFactory();
	
	public static SessionFactory getSessionFactory() {

		Configuration configuration = new Configuration();
		configuration.configure("hibernate.cfg.xml");
		return configuration.buildSessionFactory();
	
	}
	
	
	public static boolean checkTrainerId(int trainerId) throws Exception {
		Session session = factory.openSession();
		session.beginTransaction();
		
		List list = session.createQuery("from TrainerStudent ts where"
				+ " ts.trainerId = " + trainerId).getResultList();
		
		session.getTransaction().commit();
		if (list.size() > 0)
			return true;
		return false;
	}
	
	public static boolean checkStudentId(int studentId) throws Exception {
		Session session = factory.openSession();
		session.beginTransaction();
		
		List list = session.createQuery("from TrainerStudent ts where"
				+ " ts.studentId = " + studentId).getResultList();
		
		session.getTransaction().commit();
		if (list.size() > 0)
			return true;
		return false;
	}
	
	public static List<Object[]> getTrainerSchedule(int trainerId) throws Exception {

		Session session = factory.openSession();
		session.beginTransaction();
		
		List<Object[]> list = session.createQuery("select t.id, t.dateOfWeek, t.start, t.finish, t.id" 
				+ " from Schedule s, TrainerStudent ts, Time t where"
				+ " s.trainerStudentId = ts.id and s.timeId = t.id and "
				+ " ts.trainerId = " + trainerId
				+ " group by t.id, t.dateOfWeek, t.start, t.finish, t.id").getResultList();
		
		// lấy ds tên trong 1 buổi tập của 1 hlv (theo timeId)
//		Map<Integer, List<String>> studentNames = new HashMap<Integer, List<String>>();
		for (int i = 0; i < list.size(); i++) {
			int timeId = (int) list.get(i)[0];
			List<String> names = session.createQuery("select student.name"
					+ " from Schedule s, TrainerStudent ts, Student student"
					+ " where s.trainerStudentId = ts.id and student.id = ts.studentId"
					+ " and s.timeId = " + timeId).getResultList();
//			studentNames.put(timeId, names);
			list.get(i)[4] = names;
		}
		
		
		session.getTransaction().commit();
		
		return list;
	}
	
	public static List<Object[]> getStudentSchedule(int studentId) throws Exception {
		Session session = factory.openSession();
		session.beginTransaction();
		
		List<Object[]> list = session.createQuery("select tr.name, t.dateOfWeek, t.start, t.finish" 
				+ " from Schedule s, TrainerStudent ts, Time t, Trainer tr where"
				+ " s.trainerStudentId = ts.id and s.timeId = t.id and ts.trainerId = tr.id and "
				+ " ts.studentId = " + studentId).getResultList();
		
		session.getTransaction().commit();
		
		return list;
	}
	
}