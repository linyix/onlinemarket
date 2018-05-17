package mytmall.comparator;
import mytmall.bean.*;
import java.util.*;
import mytmall.bean.*;
import mytmall.comparator.*;
import mytmall.DAO.*;
import mytmall.util.*;
public class ProductAllComparator implements Comparator<Product> {
	
	@Override
    public int compare(Product p1, Product p2)  {
        return p2.getReviewCount()*p2.getSaleCount()-p1.getReviewCount()*p1.getSaleCount();
    }
	
}
