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
public class PropertyDAO {
	public int getTotal(int cid)
	{
		String sql = "select count(*) from Property where cid =?";
		Object params[]= {cid};
		return (int)DBUtil.query(sql, params, new GetTotalHandler());
	}
	public void add(Property property)
	{
		String sql = "insert into Property values(null,?,?)";
		Object params[]= {property.getName(),property.getCid()};
		property.setId((int)DBUtil.update(sql, params,new GetTotalHandler()));
				
	}
	public void delete(int id)
	{
		String sql ="delete from Property where id =?";
		Object params[]= {id};
		DBUtil.update(sql, params);
	}
	public void update(Property property)
	{
		String sql = "update Property set name = ?,cid=? where id =?";
		Object params[]= {property.getName(),property.getCid(),property.getId()};
		DBUtil.update(sql, params);
	}
	public Property get(int id)
	{
		String sql = "select * from Property where id =?";
		Object params[]= {id};
		return (Property) DBUtil.query(sql, params, new BeanHandler(Property.class));
	}
	@SuppressWarnings("unchecked")
	public List<Property> list(int cid,int start, int count)
	{
		String sql = "select * from Property where cid=? order by id desc limit ?,?";
		Object params[]= {cid,start,count};
		return (List<Property>) DBUtil.query(sql, params, new BeanListHandler(Property.class));
	}
	public List<Property> list(int cid)
	{
		return list(cid,0,Short.MAX_VALUE);
	}
}
