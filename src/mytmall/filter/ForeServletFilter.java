package mytmall.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import mytmall.bean.*;
import mytmall.DAO.*;
import java.util.*;
/**
 * Servlet Filter implementation class ForeServletFilter
 */
public class ForeServletFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String contextpath = request.getContextPath();
		String uri = request.getRequestURI();
		uri = StringUtils.remove(uri, contextpath);
		if(uri.startsWith("/fore")&&!uri.startsWith("/foreServlet"))
		{
			User user =(User) request.getSession().getAttribute("user");
	        int cartTotalItemNumber= 0;
	        if(null!=user){
	            List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
	            for (OrderItem oi : ois) {
	                cartTotalItemNumber+=oi.getNumber();
	            }
	        }
	        List<Category> cs=(List<Category>) request.getAttribute("cs");
	        if(null==cs){
	            cs=new CategoryDAO().list();
	            request.setAttribute("cs", cs);        
	        }
	        request.getServletContext().setAttribute("contextPath", contextpath);
	        request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
			String method = StringUtils.substringAfterLast(uri, "/fore");
			request.setAttribute("method",method);
			request.getRequestDispatcher("/foreServlet").forward(request, response);
			return;
		}
		chain.doFilter(request, response);
	}



}
