package mytmall.comparator;

import mytmall.bean.*;
import java.util.*;
import mytmall.bean.*;
import mytmall.comparator.*;
import mytmall.DAO.*;
import mytmall.util.*;
public class ProductPriceComparator implements Comparator<Product>{

	@Override
	public int compare(Product p1, Product p2) {
		return (int) (p1.getPromotePrice()-p2.getPromotePrice());
	}

}

/**
* 模仿天猫整站j2ee 教程 为how2j.cn 版权所有
* 本教程仅用于学习使用，切勿用于非法用途，由此引起一切后果与本站无关
* 供购买者学习，请勿私自传播，否则自行承担相关法律责任
*/	
