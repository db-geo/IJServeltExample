package fr.imt.cepi.servlet.listeners;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import fr.imt.cepi.util.DBConnectionManager;

@WebListener
public class AppContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext ctx = servletContextEvent.getServletContext();

		// initialize DB Connection
		String dbURL = ctx.getInitParameter("dbURL");
		String user = ctx.getInitParameter("dbUser");
		String pwd = ctx.getInitParameter("dbPassword");

		try {
			DBConnectionManager connectionManager = new DBConnectionManager(dbURL, user, pwd);
			ctx.setAttribute("DBConnection", connectionManager.getConnection());
			System.out.println("Connection à la base de données effectuée avec succès");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// initialize log4j
		String log4jConfig = ctx.getInitParameter("log4j-config");
		if (log4jConfig == null) {
			System.err.println(
					"Pas de paramètre log4j-config trouvé dans le fichier web.xml : initialisation de log4j avec la configuration de base");
			BasicConfigurator.configure();
		} else {
			String webAppPath = ctx.getRealPath("/");
			String log4jProp = webAppPath + log4jConfig;
			File log4jConfigFile = new File(log4jProp);
			if (log4jConfigFile.exists()) {
				System.out.println("Initialisation de log4j avec le fichier  " + log4jProp);
				DOMConfigurator.configure(log4jProp);
			} else {
				System.err.println(
						log4jProp + " fichier non trouvé : initialisation de log4j avec la configuration de base");
				BasicConfigurator.configure();
			}
		}
		System.out.println("log4j configuré correctement");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		Connection con = (Connection) servletContextEvent.getServletContext().getAttribute("DBConnection");
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
