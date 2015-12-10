package filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class AuthFilter
 */
@WebFilter("/AuthFilter")
public class AuthFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public AuthFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub

		System.out.println("doFilter function");

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String uri = req.getRequestURI();

		HttpSession session = req.getSession(false);

		if ((session == null || session.getAttribute("username") == null) && !(uri.endsWith("SDProjectGit/")
				|| uri.endsWith("index.jsp") || uri.endsWith("error.jsp") || uri.endsWith("availableProjects.jsp")
				|| uri.endsWith("olderProjects.jsp") || uri.endsWith("login.jsp") || uri.endsWith("register.jsp")
				|| uri.endsWith("projDetails.jsp") || uri.endsWith("/ws") || uri.endsWith("/style.css"))) {

			if (session == null) {
				session = req.getSession();
			}
			session.setAttribute("authErrorNC", "Please Log in");
			res.sendRedirect("index.jsp");
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
