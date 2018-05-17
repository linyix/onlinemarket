package mytmall.DAO;
import mytmall.util.*;
import mytmall.bean.*;
import java.util.*;
import mytmall.bean.*;
import mytmall.comparator.*;
import mytmall.DAO.*;
import mytmall.util.*;
public class UserDAO {

	public int getTotal()
	{
		String sql = "select count(*) from User";
		Object params[]= {};
		return (int)DBUtil.query(sql, params, new GetTotalHandler());
	}
	public void add(User user)
	{
		String sql = "insert into user values(null,?,?)";
		Object params[]= {user.getName(),user.getPassword()};
		DBUtil.update(sql, params);
				
	}
	public void delete(int id)
	{
		String sql ="delete from user where id =?";
		Object params[]= {id};
		DBUtil.update(sql, params);
	}
	public void update(User user)
	{
		String sql = "update user set name = ?,password = ? where id =?";
		Object params[]= {user.getName(),user.getPassword(),user.getId()};
		DBUtil.update(sql, params);
	}
	public User get(int id)
	{
		String sql = "select * from user where id =?";
		Object params[]= {id};
		return (User) DBUtil.query(sql, params, new BeanHandler(User.class));
	}
	public User get(String name)
	{
		String sql = "select * from user where name=?";
		Object params[]= {name};
		return (User) DBUtil.query(sql, params, new BeanHandler(User.class));
	}
	public User get(String name,String password)
	{
		String sql = "select * from user where name=? and password =?";
		Object params[]= {name,password};
		return (User) DBUtil.query(sql, params, new BeanHandler(User.class));
	}
	@SuppressWarnings("unchecked")
	public List<User> list(int start, int count)
	{
		String sql = "select * from user order by id desc limit ?,?";
		Object params[]= {start,count};
		return (List<User>) DBUtil.query(sql, params, new BeanListHandler(User.class));
	}
	public List<User> list()
	{
		return list(0,Short.MAX_VALUE);
	}
	
	public static void main(String args[]) throws Exception
	{
		UserDAO us = new UserDAO();
		List<User> lu = us.list(0, 4);
		for(int i=0;i<lu.size();i++)
		System.out.println(lu.get(i).getName());
		
	}
}
	
