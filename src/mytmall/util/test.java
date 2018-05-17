package mytmall.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class test {
		public static void main(String[] args) {
	        
			String sql ="insert into xxx values(?)";
			Object p[]= {3};
			DBUtil.update(sql,p);
					
			
	    }
		public static void update(String sql,Object params[]) 
		{
			Connection conn=null;
			PreparedStatement st=null;
			ResultSet rs=null;
			try
			{
				conn = DBUtil.getConnection();
				st = conn.prepareStatement(sql);
					st.setObject(1, params[0],java.sql.Types.DATE);
				System.out.println(st.toString());
				st.executeUpdate();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			
		}
}
