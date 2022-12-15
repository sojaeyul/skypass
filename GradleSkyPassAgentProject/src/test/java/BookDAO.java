

import java.util.List;


import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
 
public class BookDAO {
 
    private SqlSessionFactory sqlSessionFactory = null;
 
    public BookDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }
 
    public  List<Book> selectAll(){
        List<Book> list = null;
        SqlSession session = sqlSessionFactory.openSession();
 
        try {
            list = session.selectList("Book.selectAll");
        } finally {
            session.close();
        }
 
        return list;
    }
 
    public Book selectById(int id){
        Book book = null;
        SqlSession session = sqlSessionFactory.openSession();
        try {
            book = session.selectOne("Book.selectById", id);
        } finally {
            session.close();
        }
 
        return book;
    }
 
    public int insert(Book book){
        int id = -1;
        SqlSession session = sqlSessionFactory.openSession();
 
        try {
            id = session.insert("Book.insert", book);
        } finally {
            session.commit();
            session.close();
        }
 
        return id;
    }
 
    public void update(Book book){
 
        SqlSession session = sqlSessionFactory.openSession();
 
        try {
            session.update("Book.update", book);
        } finally {
            session.commit();
            session.close();
        }
    }
 
    /**
     * Delete an instance of book from the database.
     * @param id value of the instance to be deleted.
     */
    public void delete(int id){
 
        SqlSession session = sqlSessionFactory.openSession();
 
        try {
            session.delete("Book.delete", id);
        } finally {
            session.commit();
            session.close();
        }
    }
}
