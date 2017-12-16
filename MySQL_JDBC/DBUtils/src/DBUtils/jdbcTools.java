package DBUtils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;



public class jdbcTools {
	
	//һ����Ŀһ�����ݿ����ӳؾ�����
	//���ݿ����ӳ��õ���connection����رյ�ʱ�򣬲�������Ĺرգ����ǰ��������ӹ黹�����ݿ����ӳ���
	private static DataSource dataSource=null;
	static 
	{
		dataSource=new ComboPooledDataSource("helloc3p0");
	}
//����������ݿ�Ĺ��߷�������������˺ܶ����ݿ�����ķ�ʽ
//	1.��ȡ����
//	2.�ر���Դ
//  3.��������ķ�����
	
	public static Connection getConnection() throws Exception
	{
		
		return dataSource.getConnection();
//		//1.׼���������ݿ��4���ַ���
//				String driverClass =null;
//				String jdbcUrl=null;
//				String user=null;
//				String password=null;
//		//��ȡ��·���µ�jdbc.propertise�ļ�
//				InputStream in =
//						jdbcTools.class.getClassLoader().getResourceAsStream("jdbc.properties");
//				Properties properties = new Properties();
//				properties.load(in);
//				driverClass= properties.getProperty("driver");
//				jdbcUrl = properties.getProperty("jdbcUrl");
//				user = properties.getProperty("user");
//				password = properties.getProperty("password");
////				�������ݿ���������ע������,��Ӧ��Driverʵ��������ע�������ľ�̬�����,���Բ�ͬд�ˣ�
////				����ע��ķ�ʽ�ŵ��ǿ���������ע����������
//				Class.forName(driverClass);
////				DriverManager.registerDriver((Driver) Class.forName(driverClass).newInstance());
////				ͨ��DriverManagerde��getConnection()������ȡ���ݿ����� 
//				Connection connection =
//						DriverManager.getConnection(jdbcUrl,user,password);
//				return connection;
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
	//�ر����ݿ�ķ��������صķ�����
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
//		���µķ���,ִ��SQL��������Ҫ��insert,update,delete.����select
//		Ŀǰ�Ǵ�������ģ���Ϊ����ÿ�β�����ʱ��Ҫ���ӺͶϿ�����
		public static void update(String sql)
		{
			Connection conn = null;
			Statement statement=null;
			try{
				conn= jdbcTools.getConnection();
				
				statement = conn.createStatement();
				
				statement.executeUpdate(sql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				jdbcTools.release(statement,conn);
			}
		
		}
		/**
		 * ��preparedstatement����ȥ�������ݣ�����ƴ��sql�ַ�����ʹ��
		 * 1.ִ��sql��䣬ʹ��preparedstatement
		 * @param sql�������Ӧ���ǿɱ��������ʽ�����ɷ���������,
		 * �������������ʽ�����Ǵ�������ʱ���㻹Ҫ��װ�����飬�������봫�����ͼ�����
		 * ���Ҵ��������ҿ��Ե��������ã����Ա������
		 * @param objects
		 */
		public static void update(String sql,Object ... args)
		{
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			try
			{
				connection = jdbcTools.getConnection();
				preparedStatement = connection.prepareStatement(sql);
				for(int i=0;i<args.length;i++)//�ò�������ķ�ʽ����ռλ����ֵ
				{
					preparedStatement.setObject(i+1,args[i]);
				}
				preparedStatement.executeUpdate();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				jdbcTools.release(preparedStatement, connection);
			}
		}
		
		//�������ݿ�����
		public static void commit(Connection connection){
			if(connection!=null)
				try {
					connection.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		//��������ع�
		public static void rollback(Connection connection)
		{
			if(connection!=null)
				try {
					connection.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		//��ʼ����ȡ���Զ��ύ
		public static void beginTx(Connection connection)
		{
			if(connection!=null)
				try {
					connection.setAutoCommit(false);
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
	
}
