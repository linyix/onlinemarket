package mytmall.util;
import java.sql.ResultSet;
public class GetTotalHandler  implements ResultSetHandler{

	@Override
	public Object handle(ResultSet rs)
	{
		try
		{
			if(!rs.next())
				return 0;
			return rs.getInt(1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
}
