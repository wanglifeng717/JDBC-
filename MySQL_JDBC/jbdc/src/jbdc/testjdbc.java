package jbdc;


import java.io.InputStream;
import java.sql.*;
import java.util.*;

import org.junit.Test;

public class testjdbc {

	
	public static void main1(String[] args) throws Exception {

		/* Driver�����ݿ⳧�̸�����ʵ�ֵ� �࣬���ǿ���ͨ��Driver��ʵ��������ȡ���ݿ�����
		 * 1.����MYSQL����
		 * 2.��ѹ��������ӵ���jar�ļ�
		 * 3.�ڵ�ǰ��Ŀ���½�lib,��������jar�ļ�
		 * 4.�Ҽ���build-path
		 * */
		//1.����һ��Driverʵ����Ķ���
		Driver driver = new com.mysql.jdbc.Driver();
		//2.׼���������ݿ�Ļ�����Ϣ��url,user,passworld
		String url = "jdbc:mysql://localhost:3306/mydata";
		Properties info = new Properties();
		info.put("user", "root");
		info.put("password", "root");
		//3.����Driver�ӿڵ�connect(url,info)��ȡ���ݿ�����
		Connection connection =  driver.connect(url, info);
		System.out.println(connection);
	}
	
	/*��дһ��ͨ�õķ������ڲ��޸�Դ���������£����Ի�ȡ�κ����ݿ������
	 * ��������������ݿ������Driverʵ�����ȫ����URL��user��password����һ�������ļ���
	 * ͨ���޸������ļ��ķ�ʽʵ�ֺ;������ݿ�Ľ���*/

	public static void main(String[] args) throws Exception
	{
		new testjdbc().getConnection();
		System.out.println(222);
	}
	
	@Test
	public  void testGetConnection()throws Exception
	{
		System.out.println(111);
		System.out.println(getConnection());
	}
	
	public  Connection getConnection()throws Exception
	{
		String driverClass =null;
		String jdbcUrl=null;
		String user=null;
		String password=null;
		//��ȡ·���µ�jdbc.properties�ļ�
		//���ַ�ʽ�����ļ�����ŵ�bin�ļ����£���Ȼ���Ҳ���
		InputStream in =
				getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		//System.out.println(getClass().getClassLoader());
		Properties properties = new Properties();
		properties.load(in);
		driverClass= properties.getProperty("driver");
		jdbcUrl = properties.getProperty("jdbcUrl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		Driver driver =
				(Driver)Class.forName(driverClass).newInstance();
		Properties info = new Properties();
		info.put("user", user);
		info.put("password", password);
		Connection connection = driver.connect(jdbcUrl,info);
		//System.out.println(connection);
		return connection;
		
	}
}

 