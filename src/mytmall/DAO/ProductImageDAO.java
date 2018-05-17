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
public class ProductImageDAO {
	public static final String type_single = "type_single";
	public static final String type_detail = "type_detail";
	public int getTotal(int pid)
	{
		String sql = "select count(*) from productImage where pid =?";
		Object params[]= {pid};
		return (int)DBUtil.query(sql, params, new GetTotalHandler());
	}
	public void add(ProductImage productImage)
	{
		String sql = "insert into productImage values(null,?,?)";
		Object params[]= {productImage.getPtid(),productImage.getType()};
		productImage.setId((int)DBUtil.update(sql, params,new GetTotalHandler()));
				
	}
	public void delete(int id)
	{
		String sql ="delete from productImage where id =?";
		Object params[]= {id};
		DBUtil.update(sql, params);
	}
	public void update(ProductImage productImage)
	{
		String sql = "update productImage set ptid = ?,type=? where id =?";
		Object params[]= {productImage.getPtid(),productImage.getType()};
		DBUtil.update(sql, params);
	}
	public ProductImage get(int id)
	{
		String sql = "select * from productImage where id =?";
		Object params[]= {id};
		return (ProductImage) DBUtil.query(sql, params, new BeanHandler(ProductImage.class));
	}
	@SuppressWarnings("unchecked")
	public List<ProductImage> list(int ptid,int start, int count)
	{
		String sql = "select * from productImage where ptid=? order by id desc limit ?,?";
		Object params[]= {ptid,start,count};
		return (List<ProductImage>) DBUtil.query(sql, params, new BeanListHandler(ProductImage.class));
	}
	public List<ProductImage> list(int ptid,String type)
	{
		return list(ptid,type,0,Short.MAX_VALUE);
	}
	@SuppressWarnings("unchecked")
	public List<ProductImage> list(int ptid,String type,int start, int count)
	{
		String sql = "select * from productImage where ptid=? and type=? order by id desc limit ?,?";
		Object params[]= {ptid,type,start,count};
		return (List<ProductImage>) DBUtil.query(sql, params, new BeanListHandler(ProductImage.class));
	}
}

