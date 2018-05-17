package mytmall.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;
import org.apache.commons.lang.StringUtils;

public class BackServletFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String contextpath = request.getContextPath();
		String uri = request.getRequestURI();
		uri = StringUtils.remove(uri, contextpath);
		if(uri.startsWith("/admin_"))
		{
			String servletPath = StringUtils.substringBetween(uri, "_","_")+"Servlet";
			String method = StringUtils.substringAfterLast(uri, "_");
			request.setAttribute("method",method);
			request.getRequestDispatcher("/"+servletPath).forward(request, response);
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
