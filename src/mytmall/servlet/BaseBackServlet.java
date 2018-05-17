package mytmall.servlet;
import mytmall.DAO.*;
import mytmall.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.lang.reflect.*;
import java.util.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/**
 * Servlet implementation class BaseBackServlet
 */
@WebServlet("/BaseBackServlet")
public abstract class BaseBackServlet extends HttpServlet {
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
    public abstract String add(HttpServletRequest request, HttpServletResponse response, Page page) ;
    public abstract String delete(HttpServletRequest request, HttpServletResponse response, Page page) ;
    public abstract String edit(HttpServletRequest request, HttpServletResponse response, Page page) ;
    public abstract String update(HttpServletRequest request, HttpServletResponse response, Page page) ;
    public abstract String list(HttpServletRequest request, HttpServletResponse response, Page page) ;
    @Override
    public void service (HttpServletRequest request,HttpServletResponse response) 
    {
    	try
    	{
    		
    		int start = 0;
    		int count = 5;
    		try
    		{
    			start = Integer.parseInt(request.getParameter("page.start")) ;
    		}
    		catch(Exception e)
    		{
    			//e.printStackTrace();
    		}
    		try
    		{
    			count = Integer.parseInt(request.getParameter("page.count")) ;
    		}
    		catch(Exception e)
    		{
    			//e.printStackTrace();
    		}
    		Page page = new Page(start,count);
    		String method = (String) request.getAttribute("method");
    		Method m = this.getClass().getMethod(method,HttpServletRequest.class, HttpServletResponse.class, Page.class);
    		String redirect =m.invoke(this,request,response,page).toString();
    		if(redirect.startsWith("@"))
    			response.sendRedirect(redirect.substring(1));
    		else if(redirect.startsWith("%"))
    			response.getWriter().write(redirect.substring(1));
    		else
    			request.getRequestDispatcher(redirect).forward(request, response);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		throw new RuntimeException(e);
    	}
    		
    }
    @SuppressWarnings("unchecked")
    public InputStream parseUpload(HttpServletRequest request,Map<String,String> params)
    {
    	InputStream is = null;
    	try
    	{
    		DiskFileItemFactory factory = new DiskFileItemFactory();
    		ServletFileUpload upload = new ServletFileUpload(factory);
    		upload.setHeaderEncoding("UTF-8");    //上传文件名中文问题
    		factory.setSizeThreshold(1024*10240);  //10M
    		List<FileItem>items = upload.parseRequest(request);
    		Iterator<FileItem>iter = items.iterator();
    		while(iter.hasNext())
    		{
    			FileItem item = iter.next();
    			if(!item.isFormField())
    			{
    				is = item.getInputStream();
    			}
    			else
    			{
    				String paramName = item.getFieldName();
    				String ParamValue = item.getString("UTF-8");
    				/*
    				 	paramValue = new String(paramValue.getBytes("ISO-8859-1"), "UTF-8");
                        params.put(paramName, paramValue);
    				*/
    				params.put(paramName, ParamValue);
    				
    			}
    		}		
    	}
    	catch(Exception e)
		{
			e.printStackTrace();
		}
    	return is;
    }
}
