package mytmall.util;
import java.util.*;
import java.lang.reflect.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
public class BeanListHandler implements ResultSetHandler {
	private Class<?> clazz ;
	public BeanListHandler(Class<?> clazz)
	{
		this.clazz = clazz;
	}
	@Override
	public Object handle(ResultSet rs)
	{
		try
		{
			List<Object> list = new ArrayList<Object>();
			while(rs.next())
			{
				Object bean = clazz.newInstance();
				ResultSetMetaData metadata= rs.getMetaData();
				int colCount = metadata.getColumnCount();
				for(int i = 0; i< colCount; i++)
				{
					String colName = metadata.getColumnName(i+1);
					Object colObject = rs.getObject(i+1);
					if(colObject instanceof java.sql.Timestamp)
						colObject = DateUtil.t2d((java.sql.Timestamp)colObject);
					Field f = clazz.getDeclaredField(colName);
					f.setAccessible(true);
					f.set(bean, colObject);
				}
				list.add(bean);
			}
			return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
}
