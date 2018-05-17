package mytmall.DAO;

import mytmall.util.BeanHandler;
import mytmall.util.BeanListHandler;
import mytmall.util.DBUtil;
import mytmall.util.DateUtil;
import mytmall.util.GetTotalHandler;
import mytmall.DAO.CategoryDAO;
import mytmall.DAO.OrderItemDAO;
import mytmall.DAO.ReviewDAO;
import mytmall.bean.ProductImage;
import mytmall.DAO.ProductImageDAO;
import mytmall.bean.Category;
import mytmall.bean.Product;
import java.util.*;
import mytmall.bean.*;
import mytmall.comparator.*;
import mytmall.DAO.*;
import mytmall.util.*;
public class ProductDAO {
	public int getTotal(int cid)
	{
		String sql = "select count(*) from Property where cid =?";
		Object params[]= {cid};
		return (int)DBUtil.query(sql, params, new GetTotalHandler());
	}
	public void add(Product product)
	{
		String sql = "insert into Product values(null,?,?,?,?,?,?,?)";
		Object params[]= {product.getCid(),product.getName(),product.getSubTitle(),product.getStock(),product.getOriginalPrice(),
				product.getPromotePrice(),DateUtil.d2t(product.getCreateDate())};
		product.setId((int)DBUtil.update(sql, params,new GetTotalHandler()));
				
	}
	public void delete(int id)		
	{
		String sql ="delete from Product where id =?";
		Object params[]= {id};
		DBUtil.update(sql, params);
	}
	public void update(Product product)
	{
		String sql = "update Product set name = ?,cid=?,subTitle=?,originalPrice=?,promotePrice=?,createDate=?,stock=? where id =?";
		Object params[]= {product.getName(),product.getCid(),product.getSubTitle(),product.getOriginalPrice(),product.getPromotePrice(),
				DateUtil.d2t(product.getCreateDate()),product.getStock(),product.getId()};
		DBUtil.update(sql, params);
	}
	public Product get(int id)
	{
		String sql = "select * from Product where id =?";
		Object params[]= {id};
		return (Product) DBUtil.query(sql, params, new BeanHandler(Product.class));
	}
	@SuppressWarnings("unchecked")
	public List<Product> list(int cid,int start, int count)
	{
		String sql = "select * from Product where cid=? order by id desc limit ?,?";
		Object params[]= {cid,start,count};
		return (List<Product>) DBUtil.query(sql, params, new BeanListHandler(Product.class));
	}
	public List<Product> list(int cid)
	{
		return list(cid,0,Short.MAX_VALUE);
	}
	public void fill(List<Category> cs) {
        for (Category c : cs) 
            fill(c);
    }
    public void fill(Category c) {
            List<Product> ps = this.list(c.getId());
            c.setProducts(ps);
    }
 
    public void fillByRow(List<Category> cs) {
        int productNumberEachRow = 8;
        for (Category c : cs) {
            List<Product> products =  c.getProducts();
            List<List<Product>> productsByRow =  new ArrayList<>();
            for (int i = 0; i < products.size(); i+=productNumberEachRow) {
                int size = i+productNumberEachRow;
                size= size>products.size()?products.size():size;
                List<Product> productsOfEachRow =products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            c.setProductsByRow(productsByRow);
        }
    }
    public void setFirstProductImage(Product p) {
        List<ProductImage> pis= new ProductImageDAO().list(p.getId(), ProductImageDAO.type_single);
        if(!pis.isEmpty())
            p.setFirstProductImage(pis.get(0));     
    }
    public void setSaleAndReviewNumber(Product p) {
        int saleCount = new OrderItemDAO().getSaleCount(p.getId());
        p.setSaleCount(saleCount);          
 
        int reviewCount = new ReviewDAO().getCount(p.getId());
        p.setReviewCount(reviewCount);
         
    }
 
    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product p : products) {
            setSaleAndReviewNumber(p);
        }
    }
    @SuppressWarnings("unchecked")
    public List<Product> search(String keyword, int start, int count) {        
         if(null==keyword||0==keyword.trim().length())
             return  new ArrayList<Product>();
         String sql = "select * from Product where name like ? limit ?,? ";
         Object params[]= {"%"+keyword+"%",start,count};
         return (List<Product>)DBUtil.query(sql, params, new BeanListHandler(Product.class));
            
    }
}
