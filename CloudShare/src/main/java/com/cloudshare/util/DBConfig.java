package com.cloudshare.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConfig {

	static Logger logger = LoggerFactory.getLogger(DBConfig.class);

	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() throws Exception {
		if (sessionFactory == null) {
			try {
				sessionFactory = new Configuration().configure()
						.buildSessionFactory();
			} catch (Throwable ex) {
				logger.info("Exception in DatabaseConfig");
				logger.error(null, ex);
				throw new ExceptionInInitializerError(ex);
			}
		}

		return sessionFactory;
	}

	private DBConfig() throws Exception {
		if (sessionFactory != null) {
			throw new Exception(DBConfig.class.getName());
		}
	}

}
