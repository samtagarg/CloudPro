package com.cloudshare.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class FamedenContextListener
 *
 */
@WebListener
public class CloudShareListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public CloudShareListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		System.setProperty(
				"privateKey",
				arg0.getServletContext().getRealPath(
						"/WEB-INF/classes/private.key"));
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

}