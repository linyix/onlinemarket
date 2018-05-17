package mytmall.DAO;

import mytmall.bean.*;
import mytmall.util.BeanHandler;
import mytmall.util.BeanListHandler;
import mytmall.util.DBUtil;
import mytmall.util.GetTotalHandler;
import java.util.*;
import mytmall.bean.*;
import mytmall.comparator.*;
import mytmall.DAO.*;
import mytmall.util.*;
public class OrderItemDAO {
	public int getTotal()
	{
		String sql = "select count(*) from OrderItem";
		Object params[]= {};
		return (int)DBUtil.query(sql, params, new GetTotalHandler());
	}
	public void add(OrderItem orderItem)
	{
		String sql = "insert into OrderItem values(null,?,?,?,?)";
		Object params[]= {orderItem.getPid(),orderItem.getOid(),orderItem.getUid(),orderItem.getNumber()};
		orderItem.setId((int)DBUtil.update(sql, params,new GetTotalHandler()));
				
	}
	public void delete(int id)
	{
		String sql ="delete from OrderItem where id =?";
		Object params[]= {id};
		DBUtil.update(sql, params);
	}
	public void update(OrderItem orderItem)
	{
		String sql = "update OrderItem set pid = ?,oid = ?,uid =?,number=? where id =?";
		Object params[]= {orderItem.getPid(),orderItem.getOid(),orderItem.getUid(),orderItem.getNumber(),orderItem.getId()};
		DBUtil.update(sql, params);
	}
	public OrderItem get(int id)
	{
		String sql = "select * from OrderItem where id =?";
		Object params[]= {id};
		return (OrderItem) DBUtil.query(sql, params, new BeanHandler(OrderItem.class));
	}
	@SuppressWarnings("unchecked")
	public List<OrderItem> list(int oid, int start, int count)
	{
		String sql = "select * from OrderItem where oid =? order by id desc limit ?,?";
		Object params[]= {oid,start,count};
		return (List<OrderItem>) DBUtil.query(sql, params, new BeanListHandler(OrderItem.class));
	}
	public List<OrderItem> list(int oid)
	{
		return list(oid,0,Short.MAX_VALUE);
	}
	@SuppressWarnings("unchecked")
	public List<OrderItem> listByUser(int uid)
	{
		String sql = "select * from OrderItem where uid =? and oid=0";
		Object params[]= {uid};
		return (List<OrderItem>) DBUtil.query(sql, params, new BeanListHandler(OrderItem.class));
	}
	public int getSaleCount(int pid) {
	    String sql = "select sum(number) from OrderItem where pid =? ";
	    Object params[]= {pid};
	    return (int)DBUtil.query(sql, params, new GetTotalHandler());
	}
	public void fill(List<Order> os)
	{
		for(int i=0;i<os.size();i++)
		{
			fill(os.get(i));
			
		}
	}
	public void fill(Order o)
	{
		UserDAO userDAO = new UserDAO();
		ProductDAO productDAO = new ProductDAO();
		float total =0;
		int totalnum=0;
		List<OrderItem> ois = this.list(o.getId());
		o.setOrderItems(ois);
		o.setUser(userDAO.get(o.getUid()));
		for(int j=0;j<ois.size();j++)
		{
			totalnum +=ois.get(j).getNumber();
			Product p = productDAO.get(ois.get(j).getPid());
			total += ois.get(j).getNumber()*p.getPromotePrice();
			ois.get(j).setProduct(p);
			productDAO.setFirstProductImage(p);
		}
		o.setTotal(total);
		o.setTotalNumber(totalnum);
			
	}
}
