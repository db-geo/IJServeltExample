package fr.imt.cepi;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.imt.cepi.servlet.LoginServlet;
import fr.imt.cepi.util.DBConnectionManager;

public class TestMyServlet extends Mockito {

	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	RequestDispatcher dispatcher;
	@Mock
	ServletContext context;

	@Before
	public void setUp() throws Exception, SQLException {
		MockitoAnnotations.initMocks(this);
		DBConnectionManager connectionManager = new DBConnectionManager("jdbc:mysql://localhost:3306/tst", "tst",
				"tst");
		when(context.getAttribute("DBConnection")).thenReturn(connectionManager.getConnection());
	}

	@Test
	public void testLoginServlet() throws Exception {

		when(request.getServletContext()).thenReturn(context);

		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

		when(request.getParameter("login")).thenReturn("me");
		when(request.getParameter("password")).thenReturn("secret");

		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(writer);

		LoginServlet servlet = new LoginServlet();

		servlet.doPost(request, response);

		verify(request, atLeast(1)).getParameter("login");
		verify(request, atLeast(1)).getParameter("password");
		writer.flush();
		assertTrue(stringWriter.toString().contains("Aucun utilisateur connu avec ce login"));
	}

}