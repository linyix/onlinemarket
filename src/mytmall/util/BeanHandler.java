package mytmall.util;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
public class BeanHandler implements ResultSetHandler {
	private Class<?> clazz;
	public BeanHandler(Class<?> clazz)
	{
		this.clazz = clazz;
	}
	@Override
	public Object handle(ResultSet rs)
	{
		try
		{
			if(!rs.next())
				return null;
			Object bean = clazz.newInstance();
			ResultSetMetaData metadata = rs.getMetaData();
			int colCount = metadata.getColumnCount();
			for(int i =0 ; i < colCount; i++)
			{
				String colName = metadata.getColumnName(i+1);
				Object colObject = rs.getObject(i+1);
				if(colObject instanceof java.sql.Timestamp)
					colObject = DateUtil.t2d((java.sql.Timestamp)colObject);
				Field f = clazz.getDeclaredField(colName);
				f.setAccessible(true);
				f.set(bean, colObject);
				
			}
			return bean;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
}
