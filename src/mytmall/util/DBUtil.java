package mytmall.util;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
public class DBUtil {
	static String loginName ="root";
	static String password = "admin";
	static String ip = "localhost";
	static String port = "3306";
	static String database = "mytmall";
	static String encoding = "utf-8";
	static 
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	public static Connection getConnection() throws SQLException
	{
		String url = String.format("jdbc:mysql://%s:%s/%s?characterEncoding=%s", ip, port, database, encoding);
        return DriverManager.getConnection(url, loginName, password);
	}
	public static void release(Connection conn,Statement st,ResultSet rs)
	{
		if(rs!=null)
		{
			try
			{
				rs.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			rs=null;
		}
		if(st!=null)
		{
			try
			{
				st.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			st=null;
		}
		if(conn!=null)
		{
			try
			{
				conn.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			conn=null;
		}
	}
	public static void update(String sql,Object params[]) 
	{
		Connection conn=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try
		{
			conn = getConnection();
			st = conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)
			{
				st.setObject(i+1, params[i]);
			}
			System.out.println(st.toString());
			st.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			release(conn,st,rs);
		}
		
	}
	public static Object update(String sql,Object params[],ResultSetHandler rsh ) 
	{
		Connection conn=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try
		{
			conn = getConnection();
			st = conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)
			{
				st.setObject(i+1, params[i]);
			}
			
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			return rsh.handle(rs);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			release(conn,st,rs);
		}
	}
	public static Object query(String sql,Object params[],ResultSetHandler rsh ) 
	{
		Connection conn=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		try
		{
			conn = getConnection();
			st = conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)
			{
				st.setObject(i+1, params[i]);
			}
			rs = st.executeQuery();
			return rsh.handle(rs);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			release(conn,st,rs);
		}
	}
	
}
