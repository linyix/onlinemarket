package mytmall.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.*;
import mytmall.util.*;
import mytmall.DAO.*;
/**
 * Servlet implementation class BaseForeServlet
 */
@WebServlet("/BaseForeServlet")
public class BaseForeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();
    
	@Override
    public void service(HttpServletRequest req,HttpServletResponse res)
    {
		try
		{
			int start = 0;
			int count = 5;
			try
			{
				start=Integer.parseInt(req.getParameter("start"));
			}
			catch(Exception ex)
			{
				
			}
			try
			{
				count=Integer.parseInt(req.getParameter("count"));
			}
			catch(Exception ex)
			{
				
			}
			Page page = new Page(start,count);
			String method = req.getAttribute("method").toString();
			Method m = this.getClass().getMethod(method, HttpServletRequest.class,HttpServletResponse.class,Page.class);
			String redirect = m.invoke(this,req,res,page).toString();
			if(redirect.startsWith("@"))
				res.sendRedirect(redirect.substring(1));
			else if(redirect.startsWith("%"))
				res.getWriter().print(redirect.substring(1));
			else
				req.getRequestDispatcher(redirect).forward(req, res);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
    }

}
