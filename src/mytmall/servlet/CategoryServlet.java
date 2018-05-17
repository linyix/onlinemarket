package mytmall.servlet;
import mytmall.util.*;
import mytmall.bean.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.*;
/**
 * Servlet implementation class CategoryServlet
 */
@WebServlet("/categoryServlet")
public class CategoryServlet extends BaseBackServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoryServlet() {
        super();
  
    }
	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page)
    {
    	Map<String,String> params = new HashMap<>();
    	InputStream is = super.parseUpload(request, params);
    	String name = params.get("name");
    	Category c = new Category();
    	c.setName(name);
    	categoryDAO.add(c);
    	File file = new File(request.getServletContext().getRealPath("img/category/")+c.getId()+".jpg");
    	
    	try
    	{
    		if(null != is  &&  0!=is.available())
    		{
    			try(FileOutputStream fop = new FileOutputStream(file))
    			{
    				byte b[] = new byte[1024*1024];
    				int length = 0;
    				while(-1 != (length = is.read(b)))
    					fop.write(b, 0, length);
    				fop.flush(); 
    				BufferedImage img = ImageUtil.change2jpg(file);
    				ImageIO.write(img, "jpg",file );
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return "@admin_category_list";
    	
    }
	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page)
    {
    	int cid =Integer.parseInt(request.getParameter("id"));
    	categoryDAO.delete(cid);
    	return "@admin_category_list";
    }
	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page)
    {
    	int cid =Integer.parseInt(request.getParameter("id"));
    	Category c = categoryDAO.get(cid);
    	request.setAttribute("c", c);
    	return "admin/editCategory.jsp";
    }
    
	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page)
    {
    	Map<String,String> params = new HashMap<>();
    	InputStream is = super.parseUpload(request, params);
    	String name = params.get("name");
    	int cid = Integer.parseInt(params.get("id"));
    	Category c = new Category();
    	c.setId(cid);
    	c.setName(name);
    	categoryDAO.update(c);
    	File file = new File(request.getSession().getServletContext().getRealPath("img/category/")+c.getId()+".jpg");
		System.out.println(request.getSession().getServletContext().getRealPath("img/category"));
    	//file.getParentFile().mkdirs();
    	try
    	{
    		if(null != is  &&  0!=is.available())
    		{
    			try(FileOutputStream fop = new FileOutputStream(file))
    			{
    				byte b[] = new byte[1024*1024];
    				int length = 0;
    				while(-1 != (length = is.read(b)))
    					fop.write(b, 0, length);
    				fop.flush(); 
    				BufferedImage img = ImageUtil.change2jpg(file);
    				ImageIO.write(img, "jpg",file );
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    			}
    		}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return "@admin_category_list";
    }
	@Override
    public String list(HttpServletRequest request , HttpServletResponse response ,Page page)
    {
    	List<Category> cs = categoryDAO.list(page.getStart(),page.getCount());
    	int total = categoryDAO.getTotal();
    	page.setTotal(total);
    	request.setAttribute("thecs", cs);
    	request.setAttribute("page", page);
    	return "admin/listCategory.jsp";
    }

}
