package com.cloudshare.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConfig {

	static Logger logger = LoggerFactory.getLogger(DBConfig.class);

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	public static SessionFactory getSessionFactory() throws Exception {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();
				configuration.configure();
				serviceRegistry = new ServiceRegistryBuilder().applySettings(
						configuration.getProperties()).buildServiceRegistry();
				sessionFactory = configuration
						.buildSessionFactory(serviceRegistry);
				sessionFactory = configuration
						.buildSessionFactory(serviceRegistry);
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
