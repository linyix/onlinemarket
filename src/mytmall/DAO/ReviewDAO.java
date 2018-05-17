package mytmall.DAO;

import mytmall.bean.Category;
import mytmall.util.BeanHandler;
import mytmall.util.BeanListHandler;
import mytmall.util.DBUtil;
import mytmall.util.GetTotalHandler;
import mytmall.bean.Review;
import java.util.*;
import mytmall.bean.*;
import mytmall.comparator.*;
import mytmall.DAO.*;
import mytmall.util.*;
public class ReviewDAO {
	public int getTotal()
	{
		String sql = "select count(*) from Review";
		Object params[]= {};
		return (int)DBUtil.query(sql, params, new GetTotalHandler());
	}
	public void add(Review review)
	{
		String sql = "insert into Review values(null,?,?,?,?)";
		Object params[]= {review.getContent(),review.getUid(),review.getPtid(),review.getCreateDate()};
		review.setId((int)DBUtil.update(sql, params,new GetTotalHandler()));
				
	}
	public void delete(int id)
	{
		String sql ="delete from Review where id =?";
		Object params[]= {id};
		DBUtil.update(sql, params);
	}
	public void update(Review review)
	{
		String sql = "update Review set content = ?,uid=?,ptid=?createdate=?where id =?";
		Object params[]= {review.getContent(),review.getUid(),review.getPtid(),review.getCreateDate(),review.getId()};
		DBUtil.update(sql, params);
	}
	public Review get(int id)
	{
		String sql = "select * from Review where id =?";
		Object params[]= {id};
		return (Review) DBUtil.query(sql, params, new BeanHandler(Review.class));
	}
	@SuppressWarnings("unchecked")
	public List<Review> list(int start, int count)
	{
		String sql = "select * from Review order by id desc limit ?,?";
		Object params[]= {start,count};
		return (List<Review>) DBUtil.query(sql, params, new BeanListHandler(Review.class));
	}
	public List<Review> list()
	{
		return list(0,Short.MAX_VALUE);
	}
	
	@SuppressWarnings("unchecked")
	public List<Review> list(int ptid,int start, int count)
	{
		String sql = "select * from Review where ptid=? order by id desc limit ?,?";
		Object params[]= {ptid,start,count};
		return (List<Review>) DBUtil.query(sql, params, new BeanListHandler(Review.class));
	}
	public List<Review> list(int ptid)
	{
		return list(ptid,0,Short.MAX_VALUE);
	}
	public int getCount(int ptid) {
        String sql = "select count(*) from Review where ptid=? ";
		Object params[]= {ptid};
		return (int)DBUtil.query(sql, params, new GetTotalHandler());
    }
}
