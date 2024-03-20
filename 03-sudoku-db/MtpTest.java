import junit.framework.TestCase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class MtpTest extends TestCase {
    private MtpTableModel model;
    private Statement st;
    private ResultSet rs;

    protected void setUp() throws Exception {
        super.setUp();
        model = new MtpTableModel();
        try {
            String url = "jdbc:mysql://localhost:3306/my_db?user=test&password=test";
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void testMtpColumns() {
        assertEquals(3, model.getColumnCount());
        assertEquals("metropolis", model.getColumnName(0).toLowerCase());
        assertEquals("continent", model.getColumnName(1).toLowerCase());
        assertEquals("population", model.getColumnName(2).toLowerCase());
        assertNull(model.getColumnName(model.getColumnCount() + 1));
    }

    public void testMtpModelUpdate() throws SQLException {
        rs = st.executeQuery("SELECT * FROM metropolises WHERE metropolis = 'X' AND continent = 'Y' AND population = 100");
        rs.last();
        int before = rs.getRow();
        model.executeUpdate("INSERT INTO metropolises VALUES ('X', 'Y', 100)");
        rs = st.executeQuery("SELECT * FROM metropolises WHERE metropolis = 'X' AND continent = 'Y' AND population = 100");
        rs.last();
        int after = rs.getRow();
        st.executeUpdate("DELETE FROM metropolises WHERE metropolis = 'X' AND continent = 'Y' AND population = 100");
        assertEquals(1, after - before);

        rs = st.executeQuery("SELECT * FROM metropolises");
        rs.last();
        int beforeWrong = rs.getRow();
        model.executeUpdate("Wrong syntax");
        rs = st.executeQuery("SELECT * FROM metropolises");
        rs.last();
        int afterWrong = rs.getRow();
        assertEquals(beforeWrong, afterWrong);
    }

    public void testMtpModelQuery() throws SQLException {
        rs = st.executeQuery("SELECT * FROM metropolises");
        ArrayList<String> before = new ArrayList<>();
        while (rs.next()) {
            for (int i = 1; i <= 3; i++) {
                before.add(rs.getString(i));
            }
        }
        Collections.sort(before);
        model.executeQuery("SELECT * FROM metropolises");
        ArrayList <String> after = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                after.add((String)model.getValueAt(i, j));
            }
        }
        assertNull(model.getValueAt(model.getRowCount() + 1, model.getColumnCount() + 1));
        Collections.sort(after);
        assertEquals(before, after);

        model.executeQuery("Wrong syntax");
        assertEquals(0, model.getRowCount());
    }
}
