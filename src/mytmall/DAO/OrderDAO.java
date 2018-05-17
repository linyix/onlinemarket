package mytmall.DAO;

import mytmall.bean.*;
import mytmall.util.BeanHandler;
import mytmall.util.BeanListHandler;
import mytmall.util.DBUtil;
import mytmall.util.DateUtil;
import mytmall.util.GetTotalHandler;
import java.util.*;
import mytmall.bean.*;
import mytmall.comparator.*;
import mytmall.DAO.*;
import mytmall.util.*;
public class OrderDAO {
	public static final String waitPay = "waitPay";
	public static final String waitDelivery = "waitDelivery";
	public static final String waitConfirm = "waitConfirm";
	public static final String waitReview = "waitReview";
	public static final String finish = "finish";
	public static final String delete = "delete";
	public int getTotal()
	{
		String sql = "select count(*) from Order_ ";
		Object params[]= {};
		return (int)DBUtil.query(sql, params, new GetTotalHandler());
	}
	public void add(Order bean)
	{
		String sql = "insert into Order_ values(null,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object params[]= {bean.getOrderCode(),bean.getAddress(),bean.getPost(),bean.getReceiver(),bean.getMobile(),
				bean.getUserMessage(),DateUtil.d2t(bean.getCreateDate()),DateUtil.d2t(bean.getPayDate()),DateUtil.d2t(bean.getDeliveryDate()),
				DateUtil.d2t(bean.getConfirmDate()),bean.getUid(),bean.getStatus()};
		bean.setId((int)DBUtil.update(sql, params,new GetTotalHandler()));
		
				
	}
	public void delete(int id)		
	{
		String sql ="delete from Order_ where id =?";
		Object params[]= {id};
		DBUtil.update(sql, params);
	}
	public void update(Order bean)
	{
		String sql = "update order_ set orderCode =?, address= ?, post=?, receiver=?,mobile=?,userMessage=? ,createDate = ? ,"+
				" payDate =? , deliveryDate =?, confirmDate = ? ,  uid=?, status=? where id = ?";
		Object params[]= {bean.getOrderCode(),bean.getAddress(),bean.getPost(),bean.getReceiver(),bean.getMobile(),
				bean.getUserMessage(),DateUtil.d2t(bean.getCreateDate()),DateUtil.d2t(bean.getPayDate()),DateUtil.d2t(bean.getDeliveryDate()),
				DateUtil.d2t(bean.getConfirmDate()),bean.getUid(),bean.getStatus(),bean.getId()};
		DBUtil.update(sql, params);
	}
	public Order get(int id)
	{
		String sql = "select * from Order_ where id =?";
		Object params[]= {id};
		return (Order) DBUtil.query(sql, params, new BeanHandler(Order.class));
	}
	@SuppressWarnings("unchecked")
	public List<Order> list(int uid,int start, int count)
	{
		String sql = "select * from Order_ where uid=? order by id desc limit ?,?";
		Object params[]= {uid,start,count};
		return (List<Order>) DBUtil.query(sql, params, new BeanListHandler(Order.class));
	}
	@SuppressWarnings("unchecked")
	public List<Order> list(int start, int count)
	{
		String sql = "select * from Order_   order by id desc limit ?,?";
		Object params[]= {start,count};
		return (List<Order>) DBUtil.query(sql, params, new BeanListHandler(Order.class));
	}
	public List<Order> list(int uid)
	{
		return list(uid,0,Short.MAX_VALUE);
	}
	@SuppressWarnings("unchecked")
	public List<Order> list(int uid,String status, int start, int count)
	{
		String sql = "select * from Order_   where uid =? and status <> ? order by id desc limit ?,?";
		Object params[]= {uid,status,start,count};
		return (List<Order>) DBUtil.query(sql, params, new BeanListHandler(Order.class));
	}
	public List<Order> list(int uid,String status)
	{
		return list(uid,status,0,Short.MAX_VALUE);
	}
	
}
