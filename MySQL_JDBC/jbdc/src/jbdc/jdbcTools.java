package jbdc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class jdbcTools {
//����������ݿ�Ĺ��߷�������������˺ܶ����ݿ�����ķ�ʽ
//	1.��ȡ����
//	2.�ر���Դ
	
	public static Connection getConnection() throws Exception
	{
		//1.׼���������ݿ��4���ַ���
				String driverClass =null;
				String jdbcUrl=null;
				String user=null;
				String password=null;
		//��ȡ��·���µ�jdbc.propertise�ļ�
				InputStream in =
						jdbcTools.class.getClassLoader().getResourceAsStream("jdbc.properties");
				Properties properties = new Properties();
				properties.load(in);
				driverClass= properties.getProperty("driver");
				jdbcUrl = properties.getProperty("jdbcUrl");
				user = properties.getProperty("user");
				password = properties.getProperty("password");
//				�������ݿ���������ע������,��Ӧ��Driverʵ��������ע�������ľ�̬�����,���Բ�ͬд�ˣ�
//				����ע��ķ�ʽ�ŵ��ǿ���������ע����������
				Class.forName(driverClass);
//				DriverManager.registerDriver((Driver) Class.forName(driverClass).newInstance());
//				ͨ��DriverManagerde��getConnection()������ȡ���ݿ����� 
				Connection connection =
						DriverManager.getConnection(jdbcUrl,user,password);
				return connection;
	}
//	�ر����ݿ����ӵķ���
	public static void release(Statement statement,Connection conn){
		if(statement!=null){
			try{
				statement.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		if(conn !=null){
			try{
				conn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
		public static void release(Statement statement,Connection conn,ResultSet rs)
		{
			if(statement!=null){
				try{
					statement.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			if(conn !=null){
				try{
					conn.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			if(rs !=null){
				try{
					rs.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
}
