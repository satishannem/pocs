package com.sample.common;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBConnection {

	private static DBConnection instance = null;
	private static Configuration configuration = null;
	private static SessionFactory sessionFactory = null;
	private static Session session = null;
	private DBConnection() {
		
	}
	static{
		configuration.configure();
		sessionFactory = configuration.buildSessionFactory();
		session = sessionFactory.openSession();
	}
	public static DBConnection getInstance(){
		if(instance == null){
			instance = new DBConnection();
			}
		return instance;
	}
	
	public Session getSession(){
		return session;
		
	}
	
	public void closeSession(Session session){
		session.close();
		sessionFactory.close();
		
	}
}
