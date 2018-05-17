package mytmall.servlet;

import mytmall.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mytmall.bean.*;
import java.util.*;

/**
 * Servlet implementation class PropertyServlet
 */
@WebServlet("/propertyServlet")
public class PropertyServlet extends BaseBackServlet {
	private static final long serialVersionUID = 1L;

	@Override
    public String add(HttpServletRequest request,HttpServletResponse response,Page page)
    {
		int cid = Integer.parseInt(request.getParameter("cid"));
		String name = request.getParameter("name");
		Property p = new Property();
		p.setCid(cid);;
		p.setName(name);
		propertyDAO.add(p);
		return "@admin_property_list?cid="+cid;
    }

	@Override
    public String delete(HttpServletRequest request,HttpServletResponse response,Page page)
    {
		int id = Integer.parseInt(request.getParameter("id"));
		Property p = propertyDAO.get(id);
		propertyDAO.delete(id);
		return "@admin_property_list?cid="+p.getCid();
    }

	@Override
    public String edit(HttpServletRequest request,HttpServletResponse response,Page page)
    {
		int id = Integer.parseInt(request.getParameter("id"));
		Property p = propertyDAO.get(id);
		request.setAttribute("p", p);
		Category c = categoryDAO.get(p.getCid());
		request.setAttribute("c", c);
		return "admin/editProperty.jsp";
		
    }

	@Override
    public String update(HttpServletRequest request,HttpServletResponse response,Page page)
    {
		int cid = Integer.parseInt(request.getParameter("cid"));
		int id = Integer.parseInt(request.getParameter("id"));
		String name= request.getParameter("name");
		Property p = new Property();
		p.setCid(cid);
		p.setId(id);
		p.setName(name);
		propertyDAO.update(p);
		return "@admin_property_list?cid="+cid;
    }

	@Override
    public String list(HttpServletRequest request,HttpServletResponse response,Page page)
    {
		int cid =Integer.parseInt(request.getParameter("cid"));
		Category c = categoryDAO.get(cid);
		int total = propertyDAO.getTotal(cid);
		System.out.println(total);
		page.setTotal(total);
		page.setParams("&cid="+c.getId());
		List<Property> ps = propertyDAO.list(cid, page.getStart(), page.getCount());
		System.out.println(ps.size());
		request.setAttribute("c", c);
		request.setAttribute("page", page);
		request.setAttribute("ps", ps);
		return "admin/listProperty.jsp";
		
		
		
    }
	

}
