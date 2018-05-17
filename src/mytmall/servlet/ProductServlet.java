package mytmall.servlet;

import mytmall.util.*;

import java.io.InputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mytmall.DAO.*;
import mytmall.bean.*;
import java.util.*;

/**
 * Servlet implementation class ProductServlet
 */
@WebServlet("/productServlet")
public class ProductServlet extends BaseBackServlet {
	private static final long serialVersionUID = 1L;

	@Override
    public String add(HttpServletRequest request,HttpServletResponse response,Page page)
    {
		int cid = Integer.parseInt(request.getParameter("cid"));
		String name= request.getParameter("name");
		String subTitle= request.getParameter("subTitle");
		float orignalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
		float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		Product p = new Product();

		p.setCid(cid);
		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOriginalPrice(orignalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);
		p.setCreateDate(new Date());
		

		
		productDAO.add(p);
		return "@admin_product_list?cid="+cid;
    }

	@Override
	public String delete(HttpServletRequest request,HttpServletResponse response,Page page)
    {
    	int id = Integer.parseInt(request.getParameter("id"));
    	Product p = productDAO.get(id);
    	productDAO.delete(id);
    	return "@admin_product_list?cid="+p.getCid();
    	
    }

	@Override
	public String edit(HttpServletRequest request,HttpServletResponse response,Page page)
    {
		int id = Integer.parseInt(request.getParameter("id"));
    	Product p = productDAO.get(id);
    	Category c= categoryDAO.get(p.getCid());
    	request.setAttribute("p", p);
    	request.setAttribute("c", c);
    	return "admin/editProduct.jsp";
    }

	@Override
	public String update(HttpServletRequest request,HttpServletResponse response,Page page)
    {
		int cid = Integer.parseInt(request.getParameter("cid"));
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.get(id);
		int stock = Integer.parseInt(request.getParameter("stock"));
		float originalPrice = Float.parseFloat(request.getParameter("orignalPrice"));
		float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		String subTitle= request.getParameter("subTitle");
		String name= request.getParameter("name");
		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOriginalPrice(originalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);	
		productDAO.update(p);
		return "@admin_product_list?cid="+cid;
    }

	@Override
	public String list(HttpServletRequest request,HttpServletResponse response,Page page)
    {
    	int cid = Integer.parseInt(request.getParameter("cid"));
    	Category c =categoryDAO.get(cid);
    	List<Product> ps = productDAO.list(cid, page.getStart(), page.getCount());
    	for(int i=0;i<ps.size();i++)
    	{
    		//List<ProductImage> ptis =productImageDAO.list(ps.get(i).getId(),ProductImageDAO.type_single);
    		productDAO.setFirstProductImage(ps.get(i));
    		/*if(ptis.size()>0)
    		ps.get(i).setFirstProductImage(productImageDAO.list(ps.get(i).getId(),ProductImageDAO.type_single).get(0));
    		*/
    	}
    	int total = productDAO.getTotal(cid);
    	page.setTotal(total);
    	page.setParams("&cid="+cid);
    	request.setAttribute("ps", ps);
    	request.setAttribute("page", page);
    	request.setAttribute("c", c);
    	return "admin/listProduct.jsp";
    }
	public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Product p = productDAO.get(id);
		request.setAttribute("p", p);
		Category c = categoryDAO.get(p.getCid());
		request.setAttribute("c", c);
		propertyValueDAO.init(p);
		
		List<PropertyValue> pvs = propertyValueDAO.list(p.getId());
		List<Property>ps = propertyDAO.list(c.getId());
		request.setAttribute("ps", ps);
		request.setAttribute("pvs", pvs);
		
		return "admin/editProductValue.jsp";		
	}

	public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page) {
		int pvid = Integer.parseInt(request.getParameter("pvid"));
		String value = request.getParameter("value");
		PropertyValue pv =propertyValueDAO.get(pvid);
		pv.setValue(value);
		propertyValueDAO.update(pv);
		return "%success";
	}
}
