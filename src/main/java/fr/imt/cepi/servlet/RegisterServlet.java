package fr.imt.cepi.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet(name = "Register", urlPatterns = { "/Register" })
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(RegisterServlet.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String nom = request.getParameter("nom");
		String errorMsg = null;
		if (login == null || login.equals("")) {
			errorMsg = "Le login est obligatoire.";
		}
		if (password == null || password.equals("")) {
			errorMsg = "Le mot de passe est obligatoire";
		}
		if (nom == null || nom.equals("")) {
			errorMsg = "Le nom est obligatoire";
		}

		if (errorMsg != null) {
			RequestDispatcher rd = request.getRequestDispatcher("/register.jsp");
			request.setAttribute("message", "<font color=red>" + errorMsg + "</font>");
			rd.include(request, response);
		} else {

			Connection con = (Connection) getServletContext().getAttribute("DBConnection");
			PreparedStatement ps = null;
			try {
				ps = con.prepareStatement("insert into utilisateurs(nom, login, password) values (?,?,?)");
				ps.setString(1, nom);
				ps.setString(2, login);
				ps.setString(3, password);

				ps.execute();

				logger.info("Utilisateur enregistré avec le login " + login);

				// forward to login page to login
				RequestDispatcher rd = request.getRequestDispatcher("/login.html");
				request.setAttribute("message",
						"<font color=green>Enregistrement effectué avec succès, veuillez vous identifier.</font>");
				rd.include(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Problème avec la base de données");
				throw new ServletException("Problème d'accès à la base de données.");
			} finally {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("Erreur lors de la fermeture du statement");
				}
			}
		}

	}

}
