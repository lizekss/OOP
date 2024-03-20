import javax.swing.table.AbstractTableModel;
import java.sql.*;

public class MtpTableModel extends AbstractTableModel {
    private static final String DB_NAME = "my_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String SERVER_AND_PORT = "localhost:3306";

    private static final int NUM_COLS = 3;

    private Statement st;
    private ResultSet rs;

    public MtpTableModel() {
        st = null;
        rs = null;
        try {
            String url = "jdbc:mysql://" + SERVER_AND_PORT + "/" + DB_NAME + "?user=" + USER + "&password=" + PASSWORD;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url);
            st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (ClassNotFoundException | SQLException e) {e.printStackTrace(); }
    }

    @Override
    public int getRowCount() {
        int result = 0;
        if (rs != null) {
            try {
                rs.last();
                result = rs.getRow();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public int getColumnCount() {
        return NUM_COLS;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            rs.beforeFirst();
            while (rs.next() && rs.getRow() < rowIndex + 1) {}
            return rs.getString(columnIndex + 1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Metropolis";
            case 1:
                return "Continent";
            case 2:
                return "Population";
            default: break;
        }
        return null;
    }

    public void executeUpdate(String sql) {
        try {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void executeQuery(String sql) {
        try {
            rs = st.executeQuery(sql);
            fireTableDataChanged();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
