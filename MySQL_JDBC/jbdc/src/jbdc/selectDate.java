package jbdc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class selectDate {

	public static void main(String[] args) {
		Connection conn = null;
		Statement statement=null;
		ResultSet rs=null;
		try{
//			1.��ȡ���ݿ�����
			conn= jdbcTools.getConnection();
			String sql =null; 
//			2.׼��SQL���
			sql="select * from dept ";
//			3.��ȡstatement����
			statement = conn.createStatement();
//			4.ִ�в�ѯ���õ�ResultSet
			rs=statement.executeQuery(sql);
//			5.����ResultSet
			while(rs.next())
			{
				int id = rs.getInt(1);
				String name = rs.getString("dname");
				String loc = rs.getString(3);
				System.out.println(id+":"+name+":"+loc);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			jdbcTools.release(statement,conn,rs);
		}

	}

}
