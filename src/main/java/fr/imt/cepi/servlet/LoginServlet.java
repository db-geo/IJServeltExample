package fr.imt.cepi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import fr.imt.cepi.util.Utilisateur;

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static Logger logger = Logger.getLogger(LoginServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String errorMsg = null;
        if (login == null || login.equals("")) {
            errorMsg = "Le login est obligatoire";
        }
        if (password == null || password.equals("")) {
            errorMsg = "Le mot de passe est obligatoire";
        }

        if (errorMsg != null) {
            RequestDispatcher rd = request.getRequestDispatcher("/login.html");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + errorMsg + "</font>");
            rd.include(request, response);
        } else {

            Connection con = (Connection) request.getServletContext().getAttribute("DBConnection");
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = con.prepareStatement(
                        "select id, nom, login from utilisateurs where login=? and password=? limit 1");
                ps.setString(1, login);
                ps.setString(2, password);
                rs = ps.executeQuery();

                if (rs != null && rs.next()) {
                    Utilisateur utilisateur = new Utilisateur(rs.getString("nom"), rs.getString("login"),
                            rs.getInt("id"));
                    logger.info("Utilisateur trouvé :" + utilisateur);
                    HttpSession session = request.getSession();
                    session.setAttribute("utilisateur", utilisateur);
                    response.sendRedirect("home.jsp");
                } else {
                    RequestDispatcher rd = request.getRequestDispatcher("/login.html");
                    PrintWriter out = response.getWriter();
                    logger.error("Utilisateur introuvable =" + login);
                    out.println(
                            "<font color=red>Aucun utilisateur connu avec ce login, veuillez vous enregistrer d'abord.</font>");
                    rd.include(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Problème d'accès à la base de données");
                throw new ServletException("Problème d'accès à la base de données");
            } finally {
                try {
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                    logger.error("Exception lors de la fermeture du Statement ou du ResultSet");
                }

            }
        }
    }

}
