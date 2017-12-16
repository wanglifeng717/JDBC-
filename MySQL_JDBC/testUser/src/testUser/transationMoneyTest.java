package testUser;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;



public class transationMoneyTest {

	
	/**��������ĸ��뼶��
	 * ��jdbc�����п���ͨ��connection��setTransationIsolation
	 * ����������ĸ��뼶��
	 * @author mdm
	 *
	 */
	@Test 
	public void testTransactionIsolationUpdate(){
		Connection connection =null;
		
		try {
			connection = jdbcTools.getConnection();
			connection.setAutoCommit(false);
			String sql ="update bank set balance=balance-500 where id=1";
			update(connection, sql);
			System.out.println("1");
			connection .commit();//���öϵ㣬Ȼ������������ȥ�����ֵ��ѡ��junit debug as �ó���ͣ������
			//Ȼ������testTransactionIsolationReadȥ�����ݣ�����ĸ��뼶��ͬ��Ч��ͬ��
			System.out.println("2");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			jdbcTools.release(null, connection);
		}
	}
	//���ڲ�ͬ�ĸ���Ȩ��ȥ�����ݿ⣬���Ƿ���Ч
	@Test
	public void testTransactionIsolationRead(){
		String sql ="select balance from bank where id=1";
	    Integer balance = getForValue(sql);
	    System.out.println(balance);
	}
	//����ĳ����¼��ĳһ���ֶε�ֵ��һ��ͳһ��ֵ��һ����������¼�ȣ�
	public <E> E getForValue(String sql,Object...args){
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			connection=jdbcTools.getConnection();
			//����������Ҫ�������ǰ�ߣ��㻹û�ύ���������������������µ����ݣ�����ܻ��ع��ء�
			//System.out.println(connection.getTransactionIsolation());//��ȡ��ǰ������뼶��
			//Ҳ����ֱ�������ݿ�������ȫ�ֵ�������뼶�𣬲����������ô���ȥд��
			//connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);//��δ�ύ������
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			preparedStatement=connection.prepareStatement(sql);
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1, args[i]);
			}
			resultSet=preparedStatement.executeQuery();
			if(resultSet.next())
			{
				return (E)resultSet.getObject(1);/*��ȡһ����¼��һ�е�ֵ�����֪���������ͺ�����Ҳ������getString("dname")*/
			}/*����д���͵��£���дsql����ʱ����Ҫ������������ǵ�һ����*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(preparedStatement, connection,resultSet);
		}
		return null;

	}
	
	/*tom��jerry���500Ԫ
	 * ��������
	 * 1.������������ÿ������ʹ�õ����Լ��ĵ��������ӣ��������޷���֤
	 * DAOyouhua  dao= new DAOyouhua();
		String sql="update bank set balance=balance-500 where id=1";
		dao.update(sql);
		//���ǹ����з�����һ���쳣��������λع�����ԭʼ��״̬
		int i = 10/0;
		System.out.println(i);
		
		sql="update bank set balance=balance+500 where id=2";
		dao.update(sql);
		
	 * ���岽�裺
	 * 1.��ʼ����ȡ��Ĭ�ϵ�Ĭ���ύ��Ϊconnection.setAutoCommit(false);
	 * 2.��������ɹ��ύ����connection.commit();
	 * 3.��������쳣�ع�����connection.rollback();
	 * 
	 * 
	 * */
	@Test
	public void testTransaction() {
		Connection connection=null;
		
		try {
			connection = jdbcTools.getConnection();
			//��ʼ����ȡ��Ĭ���ύ
			connection.setAutoCommit(false);
			
			String sql="update bank set balance=balance-500 where id=1";
			update(connection, sql);
			
			int i = 10/0;
//			System.out.println(i);
			
			sql="update bank set balance=balance+500 where id=2";
			update(connection, sql);
			//�ύ����
			connection.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			//�ع�����
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally{
			jdbcTools .release(null, connection);
		}
			
//		DAOyouhua  dao= new DAOyouhua();
//		String sql="update bank set balance=balance-500 where id=1";
//		dao.update(sql);
//		//���ǹ����з�����һ���쳣��������λع�����ԭʼ��״̬
//		int i = 10/0;
//		System.out.println(i);
//		
//		sql="update bank set balance=balance+500 where id=2";
//		dao.update(sql);
	}	
	//д���Ż��汾��update
	public void  update(Connection connection,String sql,Object...args)
	{
		PreparedStatement preparedStatement =null;
		try{
			preparedStatement = connection.prepareStatement(sql);
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1,args[i] );/*�����ɱ������Ȼ���ռλ����ֵ��ռλ����Ŵ�1��ʼ*/
			}
			preparedStatement.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(preparedStatement, null);
			
		}
	}

}
