package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 커뮤니티 버전이라서 연결하는 코드가 필요함..
public class MysqlConnect {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/bytearchive";
        String username = "root";
        String password = "1234";

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("MySQL 연결 성공");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
