package model;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class model {
	public static SessionFactory getSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			return configuration.buildSessionFactory();
		}
		catch(Exception e) {
			return null;
		}
	
	}
}