package testUser;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.junit.Test;



public class JDBCTest {
	
	
	
	@Test
	/*����Ԫ���ݵ�ʹ�÷�����
	 * ͨ������õ�һ��ѧ���������
	 * */
	public void testResultSetMetaData()
	{
		Connection connection = null;
		PreparedStatement preparedstatement = null;
		ResultSet resultSet = null;

		try {
			String sql="select flowid flowId,type_en type,idcard idCard,examcard examCard,studentname studentName,"+
				       "location ,grade from examstudent where flowid=?";
			connection = jdbcTools.getConnection();
			preparedstatement= connection.prepareStatement(sql);
			preparedstatement.setInt(1, 1);//����ռλ����ֵ��ռλ����1��ʼ
			resultSet = preparedstatement.executeQuery();
			
			Map<String,Object> values = new HashMap<String,Object>();
			//1.�õ�ResultSetMetaData����
			ResultSetMetaData rsmd = resultSet.getMetaData();
			while(resultSet.next())
			{
				//2.��ӡÿһ�е�������	
				for(int i=0;i<rsmd.getColumnCount();i++)//��ȡ�м���
				{
					String columnLabel=rsmd.getColumnLabel(i+1);//��ȡ��ǩ��Ҳ���Ǳ���������
					Object columnValue = resultSet.getObject(columnLabel);//��ȡ�е�ֵ
					values.put(columnLabel,columnValue);//�ѱ�ǩ��ֵ�ֱ����map������
				}
			}
			//System.out.println(values);
			Class clazz = Student.class;
			Object object = clazz.newInstance();//���������
			for(Map.Entry<String,Object> entry:values.entrySet())
			{//����map��ֵ�͸�ֵ����Ӧ�����ԡ�
				String fieldName = entry.getKey();
				Object fieldValue=entry.getValue();
				//System.out.println(fieldName+":"+fieldValue);
				ReflectionUtils.setFieldValue(object, fieldName, fieldValue);//���乤�߰���������ݣ����ڴ��ɣ������Ǻܶ���
			}
			System.out.println(object);
			}

		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			jdbcTools.release(preparedstatement, connection,resultSet);
		}

	}
	//�������ǵ�ͨ�ò�ѯ�����ܲ��ܻ�ȡ����������������������������ԡ�
	@Test
	public  void testGet()
	{
		//����Ϊ��ȡһ������
		String sql="select flowid flowId,type_en type,idcard idCard,examcard examCard,studentname studentName,"+
	       "location ,grade from examstudent where flowid=?";
		Student student = get(Student.class,sql,1);
		System.out.println(student);
		
		sql="select id ,name,email,birth "+
		"from customers where id =?";
		Customer customer = get(Customer.class,sql,1);
		System.out.println(customer);
	}
	
	
//ͨ�õĲ�ѯ������ͨ����ѯ�����ǰѲ�ѯ�������Ը�ֵ����Ӧ������󡣡�	
	public <T> T get(Class<T> clazz,String sql,Object ...args)
	{
		T entity = null;
		
		Connection connection = null;
		PreparedStatement preparedstatement = null;
		ResultSet resultSet = null;

		try {
			//1.�õ�resultSet����
			connection = jdbcTools.getConnection();
			preparedstatement= connection.prepareStatement(sql);
			for(int i=0;i<args.length;i++)
			{
				preparedstatement.setObject(i+1,args[i]);
			}
			
			resultSet = preparedstatement.executeQuery();
			//2.�õ�resultMetaData����
			ResultSetMetaData rsmd = resultSet.getMetaData();
			//3.����һ��map<String,Object>����;����sql��ѯ����ı�����ֵ���е�ֵ
			Map<String,Object> values= new HashMap<String,Object>();
			//4.��������������resultMetaData���map����
			if(resultSet.next())
			{
				for(int i=0;i<rsmd.getColumnCount();i++){
					String columnLabel = rsmd.getColumnLabel(i+1);
					Object columnValue=resultSet.getObject(i+1);
					values.put(columnLabel, columnValue);
				}
			}
			//5.���÷��䴴��clazz��Ӧ�Ķ���
			if(values.size()>0){
				entity = clazz.newInstance();
				//6.����map�����÷���Ϊclass����Ķ�Ӧ�����Ը�ֵ��
				for(Map.Entry<String, Object> entry:values.entrySet()){
					String fieldName = entry.getKey();
					Object value=entry.getValue();
					ReflectionUtils.setFieldValue(entity, fieldName, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jdbcTools.release(preparedstatement, connection,resultSet);
		}

		return entity;
	}
	
	
	
	
	
//ԭ����ʾ������ΪʲôҪдһ������ͨ�õķ�������Ϊ��ȡ��ͬ��������ͨ�õĵط�������дgetcustomerҲ�Ǽ�������ƣ������������ܲ���ͳһ��һ��
	public Student getStudent(String sql,Object ...args)
	{
		Student stu = null;//����Customer customer = null;

		Connection connection = null;
		PreparedStatement preparedstatement = null;
		ResultSet resultSet = null;

		try {
			connection = jdbcTools.getConnection();
			preparedstatement= connection.prepareStatement(sql);
			for(int i=0;i<args.length;i++)
			{
				preparedstatement.setObject(i+1,args[i]);
			}
			
			resultSet = preparedstatement.executeQuery();
			//�Ѳ�ѯ������Ϣ�����θ�ֵ��ѧ�������ԡ���ֵ��һ���õ�һ�м�¼�����԰������������ԣ�Ҳ���������е�����������
			if (resultSet.next()) {
				stu = new Student();
				stu.setFlowId(resultSet.getInt(1));
				stu.setType(resultSet.getInt(2));
				stu.setIdCard(resultSet.getString(6));
				stu.setExamCard(resultSet.getString(3));
				stu.setStudentName(resultSet.getString(4));
				stu.setLocation(resultSet.getString(5));
				stu.setGrade(resultSet.getInt(7));
				/*����
				 * customer = new Customer();
				 * customer.setId(resultSet.getInt(1));
				 * customer.setName(resultSet.getString(1));
				 * customer.setEmial(resultSet.getString(3));
				 * customer.setBirth(resultSet.getDate(4));
				 * */
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jdbcTools.release(preparedstatement, connection,resultSet);
		}

		return stu;
	}
	
	
	
	
	
	
	/*
	 * SQLע�� ����Ϊ���sql�����ƴ�������ġ���������ȫ����ƴ�������������߼�©��
	 * ���Ƕ���preparedstatement ���ַ�ʽ����Ч�ġ���Ϊ�������������ȥ������ȥƥ�䡣
	 * */
	@Test
	public void testSQLInjection()
	{
		String deptno="9 or dname='";//string deptno="1"����ʲô���Ͷ����ԣ�ȡ��������sql����Ƿ��С�����,�����յ���ʾ��SQL���Ϊ׼
		String dname="or'1'='1";
		String sql = "select * from dept where deptno="+ deptno +" AND dname='"+dname+"'";
		System.out.println(sql);
		//select * from dept where deptno=1 OR  dname=' AND dname=' OR  '1'='1' 
		Connection connection =null;
		Statement statement = null;
		ResultSet resultSet =null;
		try
		{
			connection = jdbcTools.getConnection();//��������
			statement = connection.createStatement();//��ȡstatement����
			resultSet = statement.executeQuery(sql);//ִ�в�ѯ��䣬��ȡresultset����
			if(resultSet.next())//���������һ�У�˵���������˽��
				System.out.println("success");
			else
				System.out.println("fail");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			jdbcTools.release(statement, connection,resultSet);//������������صģ����Թر�������Ҳ���Թر�������
		}
	}
	
	
	//��preparedstatement����sql������д��
	//д����Ԥ�����sql��䣬���Ǵ�ռλ�������
	//�ô��Ƿ�ֹsqlע��
	//1.�������� 2.дԤ����sql���3.����preparestatement����4.��ֵ��sql��5.ִ��sql���
   @Test
   public void testPreparedStatement()
   {
	   Connection connection=null;
	   PreparedStatement preparedStatement = null;//����ԭ����statement����
	   try
	   {
		   connection =jdbcTools.getConnection();
		   String sql="insert into examstudent"+ " values(?,?,?,?,?,?,?)";
		   preparedStatement = connection.prepareStatement(sql);
		   preparedStatement.setInt(1, 5);//���ռλ��������Ǵ�1��ʼ�ġ�
		   preparedStatement.setInt(2, 4);
		   preparedStatement.setString(3, "qq");
		   preparedStatement.setString(4, "qq");
		   preparedStatement.setString(5, "qq");
		   preparedStatement.setString(6, "qq");
		   preparedStatement.setInt(7, 1);
		   //preparedStatement.setDate(9, new Date(new java.util.Date().getTime()));
		   //���ݿ�ʱ��Ĳ��뷽ʽ��
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
   

  
   //���԰������֤����׼��֤��Ϣȥ���ݿ�����飬�Ƿ��ܲ鵽���ˣ����ѽ����ӡ����
	public void testGetStudent()
	{
		//1.�õ���ѯ����1����ѧ�Ų�ѯ��2�������֤��ѯ
		int searchType = getSearchTypeFromConsole();
		//2.�����ѯѧ����Ϣ����ѯ���ݿ⣬�Ѳ�ѯ�Ľ����ֵ������ĸ�������
		Student student = searchStudent(searchType);
		
		//3.��ӡѧ����Ϣ
		printStudent(student);
		
	}
	
	/**
	 * ��ӡѧ����Ϣ��ѧ�����ڣ���ӡ������Ϣ�������������ʾ���޴���
	 * @param student
	 */
	private void printStudent(Student student) {
		if (student != null) {
			System.out.println(student);
		} else {
			System.out.println("���޴���!");
		}
		
	}

	/**
	 * �����ѯѧ����Ϣ�ķ���������һ��student���󣬲����ڷ���null
	 * @param searchType ��1 �� 2
	 * @return
	 */
	private Student searchStudent(int searchType) {
		String sql = "SELECT flowid, type_en, idcard, examcard,"
				+ "studentname, location, grade " + "FROM examstudent "
				+ "WHERE ";
		Scanner scanner = new Scanner(System.in);
		//1.���������searchType����ʾ�û�������Ϣ
		//1.1�����1����ʾ�������֤�ţ������2����ʾ����׼��֤��
		//2.����searchType ȷ��SQL
		if (searchType == 1) {
			System.out.print("������׼��֤��:");
		
			String examCard = scanner.next();
			
			sql = sql + "examcard = '" + examCard + "'";
		} else {
			System.out.print("���������֤��:");
			String examCard = scanner.next();
			sql = sql + "idcard = '" + examCard + "'";//���������ֵȥƴ���sql���
		}
		//3.ִ�в�ѯ
		Student student = getStudent(sql);
		//4.�����ڲ�ѯ������Ѳ�ѯ�ṹ��װ��һ��student����
		scanner.close();
		return student;
	}

	/**
	 * ���ݴ����SQL����Student����
	 * @param sql
	 * @return
	 */
	private Student getStudent(String sql) {
		 
		Student stu = null;

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = jdbcTools.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			//�Ѳ�ѯ������Ϣ�����θ�ֵ��ѧ�������ԡ���ֵ��һ���õ�һ�м�¼�����԰������������ԣ�Ҳ���������е�����������
			if (resultSet.next()) {
				stu = new Student(resultSet.getInt(1), resultSet.getInt(2),
						resultSet.getString(3), resultSet.getString(4),
						resultSet.getString(5), resultSet.getString(6),
						resultSet.getInt(7));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jdbcTools.release(statement, connection,resultSet);
		}

		return stu;
	}

	/**
	 * �ӿ���̨����һ��������ȷ����ѯ������
	 * @return 1.�����֤��ѯ��2.��׼��֤��ѯ ������Ч����ʾ���û���������
	 */
	private int getSearchTypeFromConsole() {
	    System.out.print("�������ѯ����");
	    Scanner scanner = new Scanner(System.in);
	    int type = scanner.nextInt();
	    if(type!=1 && type!=2){
	    	System.out.println("����������������");
	    	scanner.close();
	    	throw new RuntimeException();
	    }
	    //scanner.close();���ﲻ���С���Ϊ
		return type;
	}

	/**
	 * ���Լ���ѧ���Ƿ�ɹ�
	 */
	@Test
	public void testAddNewStudent()
	{
		Student student=getStudentFromConsole();//�ӿ���̨�����Ը�ֵ��ѧ���ĸ�������
		addNewStudent2(student);//�¼�һ��ѧ�������ݿ���
		
	}
	/**
	 * �ӿ���̨����ѧ������
	 * @return
	 */
	private Student getStudentFromConsole() {
		Scanner scanner = new Scanner(System.in);

	    Student student = new Student();

		System.out.print("FlowId:");
		student.setFlowId(scanner.nextInt());

		System.out.print("Type: ");
		student.setType(scanner.nextInt());

		System.out.print("IdCard:");
		student.setIdCard(scanner.next());

		System.out.print("ExamCard:");
		student.setExamCard(scanner.next());

		System.out.print("StudentName:");
		student.setStudentName(scanner.next());

		System.out.print("Location:");
		student.setLocation(scanner.next());

		System.out.print("Grade:");
		student.setGrade(scanner.nextInt());
		scanner.close();
		return student;
	}
	
	/**
	 * ����һ��ѧ���ļ�¼
	 * @param student
	 */
	public void addNewStudent(Student student)
	{
//		׼��һ������SQL���
		String sql = "INSERT INTO examstudent VALUES(" + student.getFlowId()
		+ "," + student.getType() + ",'" + student.getIdCard() + "','"
		+ student.getExamCard() + "','" + student.getStudentName()
		+ "','" + student.getLocation() + "'," + student.getGrade()
		+ ")";
//		ִ�в���������ڲ������˽������ӣ�ִ��statement���رյȲ���
		jdbcTools.update(sql);
	}
	public void addNewStudent2(Student student)
	   {
		//�����õ���preparedstatement�������statement���õ���Ԥ�����sql���
		   String sql ="insert into examstudent(flowid,type_en,idcard,examcard,studentname,location,grade) "
		   		+ "values(?,?,?,?,?,?,?)";
		   //������������صĵġ���������Ԥ�����sql����ص����ԡ�
		   jdbcTools.update(sql,student.getFlowId(),student.getType(),student.getIdCard(),
				   student.getExamCard(), student.getStudentName(),student.getLocation(),student.getGrade());
	   }

 
}
