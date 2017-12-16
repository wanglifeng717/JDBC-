package cunchuguocheng;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import org.junit.Test;

//���c�е����c

public class testCallStatement {

	/**
	 * ���ʹ�� JDBC ���ô洢�����ݿ��еĺ�����洢����
	 */
	@Test
	public void testCallableStatement(){
		Connection connection = null;
		CallableStatement callableStatement=null;
	
		try {
			/*1.ͨ�^connection�����prepareCall������������һ��callableStatement�����ʵ��
			 * ��ʹ��connection�����preparedCall��������ʱ����Ҫ����һ��String���͵��ַ�����
			 * ���ַ�������ָ����ε��ô洢����*/
			//{?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
			  // {call <procedure-name>[(<arg1>,<arg2>, ...)]}

			connection=jdbcTools.getConnection();
			System.out.println(connection);
			String sql="{?=call subString(?,?,?)}";
			//String sql ="{call proc1(?)}";
			//callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement =  connection.prepareCall(sql);
			/*2.ͨ��callablestatement�����reisterOutParamter()����ע��OUt����*/
			callableStatement.registerOutParameter(1, Types.DATE);
			//�]���Еr����ʡ
			callableStatement.setString(2, "abdcd");
			callableStatement.setInt(3, 1);
			callableStatement.setInt(4, 3);
			
			// 3. ͨ�� CallableStatement ����� setXxx() �����趨 IN �� IN OUT ����. ���뽫����Ĭ��ֵ��Ϊ
		    // null, ����ʹ�� setNull() ����.
			//callableStatement.setInt(2, 80);
						
			// 4. ͨ�� CallableStatement ����� execute() ����ִ�д洢����
			callableStatement.execute();
						
			// 5. ��������õ��Ǵ����ز����Ĵ洢����, 
			//����Ҫͨ�� CallableStatement ����� getXxx() ������ȡ�䷵��ֵ.
			String sum = callableStatement.getString(1);
			//long empCount = callableStatement.getLong(3);
			System.out.println(sum);
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			jdbcTools.release(null, connection);
		}
	}
}
