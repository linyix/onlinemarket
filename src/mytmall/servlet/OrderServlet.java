package mytmall.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mytmall.bean.*;
import mytmall.DAO.OrderDAO;
import mytmall.DAO.ProductImageDAO;
import mytmall.util.Page;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/orderServlet")
public class OrderServlet extends BaseBackServlet {
	private static final long serialVersionUID = 1L;
       
	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	
	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}
	public String delivery(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Order o = orderDAO.get(id);
		o.setDeliveryDate(new Date());
		o.setStatus(OrderDAO.waitConfirm);
		orderDAO.update(o);
		return "@admin_order_list";
	}

	
	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;	
	}

	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		return null;
	}

	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		List<Order> os = orderDAO.list(page.getStart(),page.getCount());
		for(int i=0;i<os.size();i++)
		{
			int total =0,totalnum=0;
			List<OrderItem> ois = orderItemDAO.list(os.get(i).getId());
			os.get(i).setOrderItems(ois);
			os.get(i).setUser(userDAO.get(os.get(i).getUid()));
			for(int j=0;j<ois.size();j++)
			{
				totalnum +=ois.get(j).getNumber();
				Product p = productDAO.get(ois.get(j).getPid());
				total += ois.get(j).getNumber()*p.getPromotePrice();
				ois.get(j).setProduct(p);
				List<ProductImage> ptis =productImageDAO.list(p.getId(),ProductImageDAO.type_single);
	    		if(ptis.size()>0)
	    		p.setFirstProductImage(productImageDAO.list(p.getId(),ProductImageDAO.type_single).get(0));
			}
			os.get(i).setTotal(total);
			os.get(i).setTotalNumber(totalnum);
			
		}
		int total = orderDAO.getTotal();
		page.setTotal(total);
		request.setAttribute("os", os);
		request.setAttribute("page", page);
		
		return "admin/listOrder.jsp";
	}

}
