package mytmall.servlet;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mytmall.util.*;
import mytmall.DAO.OrderDAO;
import mytmall.DAO.ProductImageDAO;
import mytmall.bean.*;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.util.HtmlUtils;
import mytmall.comparator.*;
/**
 * Servlet implementation class ForeServlet
 */
@WebServlet("/foreServlet")
public class ForeServlet extends BaseForeServlet {
	private static final long serialVersionUID = 1L;
	
    public String home(HttpServletRequest req,HttpServletResponse res,Page page)
    {
    	List<Category> cs= categoryDAO.list();
        productDAO.fill(cs);
        for(int i=0;i<cs.size();i++)
        	for(int j=0;j<cs.get(i).getProducts().size();j++)
        		productDAO.setFirstProductImage(cs.get(i).getProducts().get(j));
        productDAO.fillByRow(cs);
        req.setAttribute("cs", cs);
        return "home.jsp";
    }
    public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        name = HtmlUtils.htmlEscape(name);
        System.out.println(name);
        boolean exist = userDAO.get(name)!=null;
         
        if(exist){
            request.setAttribute("msg", "用户名已经被使用,不能使用");
            return "register.jsp"; 
        }
         
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        System.out.println(user.getName());
        System.out.println(user.getPassword());
        userDAO.add(user);
         
        return "@registerSuccess.jsp"; 
    } 
    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        name = HtmlUtils.htmlEscape(name);
        String password = request.getParameter("password");    
         
        User user = userDAO.get(name,password);
          
        if(null==user){
            request.setAttribute("msg", "账号密码错误");
            return "login.jsp";
        }
        request.getSession().setAttribute("user", user);
        return "@forehome";
    }  
    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
        request.getSession().removeAttribute("user");
        return "@forehome";
    } 
    public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);
        Category c = categoryDAO.get(p.getCid());
        List<ProductImage> productSingleImages = productImageDAO.list(pid, ProductImageDAO.type_single);
        List<ProductImage> productDetailImages = productImageDAO.list(pid, ProductImageDAO.type_detail);
        productDAO.setFirstProductImage(p);
        p.setProductSingleImages(productSingleImages);
        p.setProductDetailImages(productDetailImages);
        List<Property> ps = propertyDAO.list(c.getId());
        List<PropertyValue> pvs = propertyValueDAO.list(p.getId());      
     
        List<Review> reviews = reviewDAO.list(p.getId());
         
        productDAO.setSaleAndReviewNumber(p);
     
        request.setAttribute("reviews", reviews);
        request.setAttribute("c", c);
        request.setAttribute("p", p);
        request.setAttribute("ps", ps);
        request.setAttribute("pvs", pvs);
        return "product.jsp";      
    }  
    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user =(User) request.getSession().getAttribute("user");
        if(null!=user)
            return "%success";
        return "%fail";
    }
    public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");    
        User user = userDAO.get(name,password);
         
        if(null==user){
            return "%fail";
        }
        request.getSession().setAttribute("user", user);
        return "%success"; 
    }
    public String category(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
         
        Category c = categoryDAO.get(cid);
        productDAO.fill(c);
        productDAO.setSaleAndReviewNumber(c.getProducts());      
        for(int i=0;i<c.getProducts().size();i++)
        	productDAO.setFirstProductImage(c.getProducts().get(i));
        String sort = request.getParameter("sort");
        if(null!=sort){
        switch(sort){
            case "review":
                Collections.sort(c.getProducts(),new ProductReviewComparator());
                break;
            case "date" :
                Collections.sort(c.getProducts(),new ProductDateComparator());
                break;
                 
            case "saleCount" :
                Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                break;
                 
            case "price":
                Collections.sort(c.getProducts(),new ProductPriceComparator());
                break;
                 
            case "all":
                Collections.sort(c.getProducts(),new ProductAllComparator());
                break;
            }
        }
         
        request.setAttribute("c", c);
        return "category.jsp";     
    }
    public String search(HttpServletRequest request, HttpServletResponse response, Page page){
		String keyword = request.getParameter("keyword");
		System.out.println(keyword);
		List<Product> ps= productDAO.search(keyword,0,20);
		productDAO.setSaleAndReviewNumber(ps);
		request.setAttribute("ps",ps);
		return "searchResult.jsp";
	}
    public String buyone(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        int num = Integer.parseInt(request.getParameter("num"));
        Product p = productDAO.get(pid);
        int oiid = 0;
         
        User user =(User) request.getSession().getAttribute("user");
        boolean found = false;
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if(oi.getPid()==p.getId()){
                oi.setNumber(oi.getNumber()+num);
                orderItemDAO.update(oi);
                found = true;
                oiid = oi.getId();
                break;
            }
        }      
     
        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUid(user.getId());
            oi.setNumber(num);
            oi.setPid(pid);
            orderItemDAO.add(oi);
            oiid = oi.getId();
        }
        return "@forebuy?oiid="+oiid;
    }
    public String buy(HttpServletRequest request, HttpServletResponse response, Page page){
        String[] oiids=request.getParameterValues("oiid");
        List<OrderItem> ois = new ArrayList<>();
        float total = 0;
     
        for (String strid : oiids) {
            int oiid = Integer.parseInt(strid);
            OrderItem oi= orderItemDAO.get(oiid);
            oi.setProduct(productDAO.get(oi.getPid()));
            productDAO.setFirstProductImage(oi.getProduct());
            total +=oi.getProduct().getPromotePrice()*oi.getNumber();
            ois.add(oi);
        }
        request.getSession().setAttribute("ois", ois);
        request.setAttribute("total", total);
        return "buy.jsp";
    }  
    
    
    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);
        int num = Integer.parseInt(request.getParameter("num"));
         
        User user =(User) request.getSession().getAttribute("user");
        boolean found = false;
 
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if(oi.getPid()==p.getId()){
                oi.setNumber(oi.getNumber()+num);
                orderItemDAO.update(oi);
                found = true;
                break;
            }
        }      
         
        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUid(user.getId());;
            oi.setNumber(num);
            oi.setPid(pid);
            orderItemDAO.add(oi);
        }
        return "%success";
    }
    public String cart(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user =(User) request.getSession().getAttribute("user");
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        System.out.println(ois.size());
        for(int j=0;j<ois.size();j++)
		{
			Product p = productDAO.get(ois.get(j).getPid());
			ois.get(j).setProduct(p);
			List<ProductImage> ptis =productImageDAO.list(p.getId(),ProductImageDAO.type_single);
    		if(ptis.size()>0)
    		p.setFirstProductImage(productImageDAO.list(p.getId(),ProductImageDAO.type_single).get(0));
		}
        request.setAttribute("ois", ois);
        return "cart.jsp";
    }  
    public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user =(User) request.getSession().getAttribute("user");
        if(null==user)
            return "%fail";
     
        int pid = Integer.parseInt(request.getParameter("pid"));
        int number = Integer.parseInt(request.getParameter("number"));
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if(oi.getPid()==pid){
                oi.setNumber(number);
                orderItemDAO.update(oi);
                break;
            }
             
        }      
        return "%success";
    }
    public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page){
        User user =(User) request.getSession().getAttribute("user");
        if(null==user)
            return "%fail";
        int oiid = Integer.parseInt(request.getParameter("oiid"));
        orderItemDAO.delete(oiid);
        return "%success";
    }
    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page){
        User user =(User) request.getSession().getAttribute("user");
        List<OrderItem> ois= (List<OrderItem>) request.getSession().getAttribute("ois");
        if(ois.isEmpty())
            return "@login.jsp";
     
        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String receiver = request.getParameter("receiver");
        String mobile = request.getParameter("mobile");
        String userMessage = request.getParameter("userMessage");
        Order order = new Order();
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) +RandomUtils.nextInt(10000);
     
        order.setOrderCode(orderCode);
        order.setAddress(address);
        order.setPost(post);
        order.setReceiver(receiver);
        order.setMobile(mobile);
        order.setUserMessage(userMessage);
        order.setCreateDate(new Date());
        order.setUid(user.getId());
        order.setStatus(orderDAO.waitPay);
     
        orderDAO.add(order);
        float total =0;
        for (OrderItem oi: ois) {
            oi.setOid(order.getId());
            orderItemDAO.update(oi);
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
         
        return "@forealipay?oid="+order.getId() +"&total="+total;
    }
    public String alipay(HttpServletRequest request, HttpServletResponse response, Page page){
        return "alipay.jsp";
    }
    public String payed(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        order.setStatus(OrderDAO.waitDelivery);
        order.setPayDate(new Date());
        orderDAO.update(order);
        request.setAttribute("o", order);
        return "payed.jsp";    
    }  
    
    public String bought(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user =(User) request.getSession().getAttribute("user");
        List<Order> os= orderDAO.list(user.getId(),OrderDAO.delete);
         
        orderItemDAO.fill(os);
        for(int i=0;i<os.size();i++)
        	System.out.println(os.get(i).getOrderItems().size());
        request.setAttribute("os", os);
         
        return "bought.jsp";       
    }
    public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        orderItemDAO.fill(o);
        request.setAttribute("o", o);
        return "confirmPay.jsp";       
    }
    public String orderConfirmed(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.waitReview);
        o.setConfirmDate(new Date());
        orderDAO.update(o);
        return "orderConfirmed.jsp";
    }
    public String deleteOrder(HttpServletRequest request, HttpServletResponse response, Page page){
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.delete);
        orderDAO.update(o);
        return "%success";     
    }
    public String review(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        orderItemDAO.fill(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewDAO.list(p.getId());
        for(int i=0;i<reviews.size();i++)
        	reviews.get(i).setUser(userDAO.get(reviews.get(i).getUid()));
        productDAO.setSaleAndReviewNumber(p);
        request.setAttribute("p", p);
        request.setAttribute("o", o);
        request.setAttribute("reviews", reviews);
        return "review.jsp";       
    }
    public String doreview(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.finish);
        orderDAO.update(o);
        int pid = Integer.parseInt(request.getParameter("pid"));
         
        String content = request.getParameter("content");
         
        content = HtmlUtils.htmlEscape(content);
     
        User user =(User) request.getSession().getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setPtid(pid);
        review.setCreateDate(new Date());
        review.setUid(user.getId());
        reviewDAO.add(review);
         
        return "@forereview?oid="+oid+"&showonly=true";    
    }
}
