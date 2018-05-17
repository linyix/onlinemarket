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
public class CategoryDAO {
	public int getTotal()
	{
		String sql = "select count(*) from Category";
		Object params[]= {};
		return (int)DBUtil.query(sql, params, new GetTotalHandler());
	}
	public void add(Category category)
	{
		String sql = "insert into Category values(null,?)";
		Object params[]= {category.getName()};
		category.setId((int)DBUtil.update(sql, params,new GetTotalHandler()));
				
	}
	public void delete(int id)
	{
		String sql ="delete from Category where id =?";
		Object params[]= {id};
		DBUtil.update(sql, params);
	}
	public void update(Category category)
	{
		String sql = "update Category set name = ? where id =?";
		Object params[]= {category.getName(),category.getId()};
		DBUtil.update(sql, params);
	}
	public Category get(int id)
	{
		String sql = "select * from Category where id =?";
		Object params[]= {id};
		return (Category) DBUtil.query(sql, params, new BeanHandler(Category.class));
	}
	@SuppressWarnings("unchecked")
	public List<Category> list(int start, int count)
	{
		String sql = "select * from Category order by id desc limit ?,?";
		Object params[]= {start,count};
		return (List<Category>) DBUtil.query(sql, params, new BeanListHandler(Category.class));
	}
	public List<Category> list()
	{
		return list(0,Short.MAX_VALUE);
	}
}
