package insertDigdata;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;

/*DAO���Ż��汾����ֱ�ӿ��������õ�*/
public class DAOyouhua {
/**/
	// insert��update��delete���������԰�������
	public void  update(String sql,Object...args){
		Connection connection = null;
		PreparedStatement preparedStatement =null;
		try{
			connection = jdbcTools.getConnection();
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
			jdbcTools.release(preparedStatement, connection);
			
		}
	}
	//��ѯһ����¼�����ض�Ӧ�Ķ���
	public <T> T get(Class<T> clazz,String sql,Object ...  args){
		List<T> result = getForList(clazz,sql,args);
		if(result.size()>0){
			return result.get(0);
		}
		return null;
	}
	//��ѯ������¼�����ض�Ӧ�Ķ���ļ���
	/**�ص��Ż��Ķ���
	 * ���裺1.��ȡһ����¼���ѱ�����ֵ����map�����У�Ȼ���map����list������
	 * 2.����ȡ�������е�mapȻ��ֵ�����󣬲�����Щ������ڼ����С�
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> List<T> getForList(Class<T>clazz,String sql,Object...args){
		
		List<T> list = new ArrayList<T>();/*��������ձ���ֵ�Ķ���ļ���*/
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		try {
			connection=jdbcTools.getConnection();
			preparedStatement=connection.prepareStatement(sql);
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1, args[i]);
			}
			resultSet=preparedStatement.executeQuery();
			/*ͨ��resultset��ȡ���˱�����ֵ������map�У�Ȼ���ÿ��map����list������*/
			List<Map<String, Object>> values = handleResultSetToMapList(resultSet);
			/*�Ѽ���list�е�map����ȡ����Ȼ��ֵ����Ӧ��bean�������Ѷ�����������list���Ϸ���*/
			list=tansforMapListToBeanList(clazz, values);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(preparedStatement, connection,resultSet);
		}
		
		return list;
	}
	
	
	//����ĳ����¼��ĳһ���ֶε�ֵ��һ��ͳһ��ֵ��һ����������¼�ȣ�
		/**
		 * ��һ�����ݻ����ֶ�
		 * @param sql
		 * @param args
		 * @return
		 */
		public <E> E getForValue(String sql,Object...args){
			Connection connection=null;
			PreparedStatement preparedStatement=null;
			ResultSet resultSet=null;
			try {
				connection=jdbcTools.getConnection();
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
		
		
		
	/**
	 * ��һ��װ����map��list�����е�mapȡ��������ֵ����bean��������
	 * @param clazz
	 * @param values
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private <T> List<T> tansforMapListToBeanList(Class<T> clazz, List<Map<String, Object>> values) throws InstantiationException, IllegalAccessException, InvocationTargetException 
	{
		List<T> result = new ArrayList<T>();/*����װbean����*/
		T bean = null;
		if(values.size()>0)
		{
			for(Map<String,Object> m:values)//����list����
			{
				bean = clazz.newInstance();
				for(Map.Entry<String, Object> entry:m.entrySet())
				{
					String propertyName = entry.getKey();
					Object value = entry.getValue();
					//������Ӧ��Object����
					BeanUtils.setProperty(bean, propertyName, value);
					
				}
				//13.��Object������뵽List�С�
				result.add(bean);
			}
		}
		return result;
	}
	/**
	 * �����������õ�һ��list ����װ�˶����Ѿ���ֵ��Map����
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private List<Map<String, Object>> handleResultSetToMapList(ResultSet resultSet) throws SQLException {
		List<Map<String, Object>> values = new ArrayList<Map<String,Object>>();
		
		
		List<String> columnLabels=getColumnLabels(resultSet);
		Map<String,Object> map = null;
		//7.����resultsetʹ��while
		while (resultSet.next())
		{
			map =new HashMap<String,Object>();
		    for(String columnLabel:columnLabels)
			{
		    	Object value = resultSet.getObject(columnLabel);/*ͨ��������ȡ��Ӧ�е�ֵ*/
		    	map.put(columnLabel, value);
			}	
			//11.�����õ�Map�������׼����List��
			values.add(map);
		}
		return values;
	}
	/**
	 * ��ȡ�����columnLabel��Ӧ��List
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private List<String> getColumnLabels(ResultSet rs) throws SQLException
	{
		List<String> labels = new ArrayList<String>();
		ResultSetMetaData resultSetMetaData = rs.getMetaData();
		for(int i=0;i<resultSetMetaData.getColumnCount();i++)
		{
			labels.add(resultSetMetaData.getColumnLabel(i+1));/*�����е��еı���ȫ���ŵ�һ��������*/
		}
		return labels;
	}
	
}
