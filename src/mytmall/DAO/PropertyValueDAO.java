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
public class PropertyValueDAO {
	public int getTotal()
	{
		String sql = "select count(*) from PropertyValue";
		Object params[]= {};
		return (int)DBUtil.query(sql, params, new GetTotalHandler());
	}
	public void add(PropertyValue propertyValue)
	{
		String sql = "insert into PropertyValue values(null,?,?,?)";
		Object params[]= {propertyValue.getPid(),propertyValue.getPtid(),propertyValue.getValue()};
		propertyValue.setId((int)DBUtil.update(sql, params,new GetTotalHandler()));
				
	}
	public void delete(int id)
	{
		String sql ="delete from PropertyValue where id =?";
		Object params[]= {id};
		DBUtil.update(sql, params);
	}
	public void update(PropertyValue propertyValue)
	{
		String sql = "update PropertyValue set pid = ?,ptid =?,value=? where id =?";
		Object params[]= {propertyValue.getPid(),propertyValue.getPtid(),propertyValue.getValue(),propertyValue.getId()};
		DBUtil.update(sql, params);
	}
	public PropertyValue get(int pid,int ptid)
	{
		String sql = "select * from PropertyValue where pid =? and ptid =?";
		Object params[]= {pid,ptid};
		return (PropertyValue) DBUtil.query(sql, params, new BeanHandler(PropertyValue.class));
	}
	public PropertyValue get(int id)
	{
		String sql = "select * from PropertyValue where id=?";
		Object params[]= {id};
		return (PropertyValue) DBUtil.query(sql, params, new BeanHandler(PropertyValue.class));
	}
	@SuppressWarnings("unchecked")
	public List<PropertyValue> list(int ptid)
	{
		String sql = "select * from PropertyValue where ptid =? order by pid desc ";
		Object params[]= {ptid};
		return (List<PropertyValue>) DBUtil.query(sql, params, new BeanListHandler(PropertyValue.class));
	}
	public void init(Product p)
	{
		List<Property> pts= new PropertyDAO().list(p.getCid());
        
        for (Property pt: pts) {
            PropertyValue pv = get(pt.getId(),p.getId());
            if(null==pv){
                pv = new PropertyValue();
                pv.setPid(pt.getId());
                pv.setPtid(p.getId());;
                this.add(pv);
            }
        }
	}
}
