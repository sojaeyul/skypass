

import java.util.List;

import com.koreanair.common.db.MyBatisConnectionFactory;

public class MyBatisTestService {
	
    public static void main(String[] args) {
    	 
        BookDAO bookDAO = new BookDAO(MyBatisConnectionFactory.getSqlSessionFactory());
 
        Book book = new Book();
 
        //Creat
        //2건을 입력한다.
        System.out.println("==INSERT==");
 
        book.setId(1);
        book.setName("홍길동전");
        bookDAO.insert(book);
 
        book.setId(2);
        book.setName("레미제라블"); 
        bookDAO.insert(book);
 
        //Read
        //입력한 리스트를 보여준다.
        List<Book> bookList = bookDAO.selectAll();
        for(Book bookInfo: bookList){
            System.out.println("BOOK ID: "+bookInfo.getId());
            System.out.println("BOOK NAME: "+bookInfo.getName());
        }
 
        //Update
        System.out.println("");
        System.out.println("==UPDATE==");
 
        
        //ID 2번의 이름을 업데이트 한다.
        book.setId(2);
        book.setName("해저 2만리");
        bookDAO.update(book);
 
        //Read
        //변경한 ID 2번의 이름을 보여준다ㅏ.
        book = bookDAO.selectById(2);
        System.out.println("BOOK ID: "+book.getId());
        System.out.println("BOOK NAME: "+book.getName());
 
        //Delete
        System.out.println("");
        System.out.println("==DELETE==");
 
        //2번을 삭제한다.
        bookDAO.delete(2);
 
        //Read
        //전체 리스트를 보여준다.
        bookList.clear();
        bookList = bookDAO.selectAll();
        for(Book bookInfo: bookList){
            System.out.println("BOOK ID: "+bookInfo.getId());
            System.out.println("BOOK NAME: "+bookInfo.getName());
        }
 
    }
 
	
	
	
/*	
	public static void main(String[] args) {

		SqlSession sqlSession = null;
		try {
			sqlSession = MyBatisConnectionFactory.getSqlSessionFactory().openSession(false);

			System.out.println(sqlSession);
			//Mapper 사용하는 방법
			
			TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
			List<Map<String, Object>> progrmList = testMapper.getProgrmList();
			
			//Sqlsession을 사용하는 방법
			List<Map<String, Object>> progrmList = (List<Map<String,Object>>)sqlSession.selectList("getProgrmList");
			
			
			for (Iterator iterator = progrmList.iterator(); iterator.hasNext();) {
				Map<String, Object> map = (Map<String, Object>) iterator.next();
				System.out.println(map);				
			}
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("id", "test8");
			param.put("name", "test8 name");
			param.put("age", 88);
			
			testMapper.insertTest(param);
			
			Map<String, Object> param2 = new HashMap<String, Object>();
			param2.put("id", "test5");
			param2.put("name", "test5 update name");
			param2.put("age", 28);
			testMapper.updateTest(param2);
		
			// 커밋
			sqlSession.commit();
		} catch (Exception e) {
			// 롤백
			sqlSession.rollback();
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
	}
	*/
}
