package Hospital;

import java.sql.*;

public class ConnectionClass implements AutoCloseable {
    public Connection con;
    public Statement stm;

    public ConnectionClass() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/hospital_1?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                    "root",
                    "Pritha123!"
            );
            stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("✅ Database connected successfully with scrollable statement.");
        } catch (Exception ex) {
            System.out.println("❌ Database connection failed.");
            ex.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
            System.out.println("🔒 Connection closed successfully.");
        } catch (Exception e) {
            System.out.println("⚠️ Error while closing connection.");
            e.printStackTrace();
        }
    }
}
