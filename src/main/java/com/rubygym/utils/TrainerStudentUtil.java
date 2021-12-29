package com.rubygym.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class TrainerStudentUtil {
	
	private static SessionFactory factory = HibernateUtil.getSessionFactory();
	
	public static Integer getTrainerId(int studentId) throws Exception {
		Session session = factory.openSession();
		session.beginTransaction();
		
		Integer trainerId = (Integer) session.createQuery("select ts.trainerId"
				+ " from TrainerStudent ts where ts.studentId = " + studentId).uniqueResult();
		
		session.getTransaction().commit();
		return trainerId;
	}
	
	public static Integer getTrainerStudentId(int studentId) throws Exception {
		Session session = factory.openSession();
		session.beginTransaction();
		
		Integer trainerStudentId = (Integer) session.createQuery("select ts.id"
				+ " from TrainerStudent ts where ts.studentId = " + studentId).uniqueResult();
		
		session.getTransaction().commit();
		return trainerStudentId;
	}
	
	public static Integer getStudentId(int trainerStudentId) throws Exception {
		Session session = factory.openSession();
		session.beginTransaction();
		
		Integer studentId = (Integer) session.createQuery("select ts.studentId"
				+ " from TrainerStudent ts where ts.id = " + trainerStudentId).uniqueResult();
		
		session.getTransaction().commit();
		return studentId;
	}
}
