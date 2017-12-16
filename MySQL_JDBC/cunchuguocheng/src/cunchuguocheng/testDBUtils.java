package cunchuguocheng;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

public class testDBUtils {

	QueryRunner queryRunner = new QueryRunner();
	/**
	 * ����QueryRunner���Update����
	 * �ڲ�ʵ�ֺ������Լ�д��DAOʵ�ֶ��ǲ��ġ�
	 */
	@Test
    public void testQueryRunnerUpdate() {
		//1.����testQueryRunner��ʵ����
		//QueryRunner queryRunner = new QueryRunner();
		//2.ʹ����updata����,��ɾ�Ķ�ֻҪ��SQL�����ˡ�
		String sql ="delete from customers where id in(?,?)";
		Connection connection=null;
		try {
			connection=jdbcTools.getConnection();
			//System.out.println(connection);
			queryRunner.update(connection, sql, 3,4);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			jdbcTools.release(null, connection);
		}
	}
	/**
	 * ���Բ�ѯ�ķ���
	 */
	@Test
	public void testQuery() {
		Connection connection =null;
		try {
			connection=jdbcTools.getConnection();
			String sql = "select name,email from customers where id=9 ";
			Object object=queryRunner.query(connection, sql, new myResultSetHandler());
			System.out.println(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(null, connection);
		}
	}
	/**
	 * ����ķ�����ͨ�õģ�����Խ�����Ĵ����������Լ������hander�����Լ�����ġ�
	 * @author mdm
	 *
	 */
	class myResultSetHandler implements ResultSetHandler{
		public Object handle(ResultSet resultSet) throws SQLException
		{
			List<Customer> customers=new ArrayList<Customer>() ;
				
			while(resultSet.next())
			{
				String name = resultSet.getString(1);
				String email=resultSet.getString(2);
				Customer customer = new Customer(3,name,email,null);
				customers.add(customer);
			}
			//System.out.println("handle...");
			return customers;
		}
	}
	/**
	 * ���c�W��һ��handler �@Щhandler �ǳ����ã������҂��Լ��a̎����a��
	 * �ѵ�һ����¼תΪ����beanHanlder������class������Ӧ�Ķ���
	 * @author mdm
	 *
	 */
	@Test
	public void testBeanHandler() {
		Connection connection =null;
		try {
			connection=jdbcTools.getConnection();
			String sql = "select name,email from customers where id=? ";
			@SuppressWarnings("unchecked")
			Customer customer= (Customer) queryRunner.query(connection, sql, new BeanHandler(Customer.class),9);
			System.out.println(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(null, connection);
		}
	}
	/**
	 * BeanListHanlder �Ѽǹ�ת��Ϊһ��list����list��Ϊnull �������ǿռ��� size()��������0
	 * ��sql����ܹ���ѯ����¼��List�д�ŵ�ÿ����¼��ֵ�����ԵĶ���
	 * ������ֵ�ķ�ʽ:��set��get������������Ǹ�����
	 * �ǵ���set������ֵ�ġ�����������ݿ��ֶ��ѳ��������ԶԲ��ϣ����������ݿ��ֶ�ȡ��������
	 * ���������ҵ���Ӧ��set��������Ӧ�����Ը�ֵ����Ȼ����
	 * ���ݿ�������ֶ��������������ֶ���һ���Ŀ��ԣ����Դ�Сд���Զ���ֵ
	 * 
	 * @author mdm
	 *
	 */
	@Test
	public void testBeanListHandler() {
		Connection connection =null;
		try {
			connection=jdbcTools.getConnection();
			
			String sql = "select id  ID_EN,name,email from customers where name=? ";
			
			List<Customer> customer= (List<Customer>) queryRunner.query(connection, sql, new BeanListHandler(Customer.class),"name_9");
			System.out.println(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(null, connection);
		}
	}
	/**
	 * MapHandler :����sql��Ӧ�ĵ�һ����¼��Ӧ��Map����
	 * ����sql��ѯ�������������еı�������ֵ���е�ֵ 
	 * @author mdm
	 *
	 */
	@Test
	public void testMapHandler() {
		Connection connection =null;
		try {
			connection=jdbcTools.getConnection();
			String sql = "select name,email from customers where id=? ";
			@SuppressWarnings("unchecked")
			Map<String, Object> customer= (Map<String, Object>) queryRunner.query(connection, sql, new MapHandler(),9);
			System.out.println(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(null, connection);
		}
	}
	/**
	 * ListMapHandler :����Map����
	 * һ����¼��Ӧ��Map����
	 * ����sql��ѯ�������������еı�������ֵ���е�ֵ 
	 * @author mdm
	 *
	 */
	@Test
	public void testListMapHandler() {
		Connection connection =null;
		try {
			connection=jdbcTools.getConnection();
			String sql = "select name,email from customers where name=? ";
			
			List<Map<String, Object>> customer= (List<Map<String, Object>>) queryRunner.query(connection, sql, new MapListHandler(),"name_9");
			System.out.println(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(null, connection);
		}
	}
	
	/**
	 * ScalarHanlder:�ѽ����תΪһ����ֵ���أ������������������͡�
	 * �����Ѻܶ��кܶ��࣬���Ǿͷ��ص�һ�е�һ��
	 */
	@Test
	public void testScalarHandler() {
		Connection connection =null;
		try {
			connection=jdbcTools.getConnection();
			String sql = "select name ,email from customers where name=? ";
			
			String customer= (String) queryRunner.query(connection, sql, new ScalarHandler(),"name_9");
			System.out.println(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(null, connection);
		}
	}

}
