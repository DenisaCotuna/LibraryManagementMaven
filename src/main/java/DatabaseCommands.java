import java.sql.*;
public class DatabaseCommands {
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public void createNewTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Inventory (\n"
                + " id integer PRIMARY KEY,\n"
                + " idItem text NOT NULL,\n"
                + " quantity integer\n"
                + ");";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            // creating a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String idItem, int quantity) {
        String sql = "INSERT INTO Inventory(idItem, quantity) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idItem);
            pstmt.setDouble(2, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(int id, String idItem, int quantity) {
        String sql = "UPDATE Inventory SET idItem = ? , "
                + "quantity = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, idItem);
            pstmt.setDouble(2, quantity);
            pstmt.setInt(3, id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Inventory WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void executeCustomQuery(String sql) {
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            // execute the custom query
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printAllContent() {
        String sql = "SELECT * FROM Inventory";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {


            // loop through the result set
            if (!rs.isBeforeFirst()) {
                System.out.println("No data found.");
                return;
            }
            System.out.println("------------ Database data ------------");
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("idItem") + "\t" +
                        rs.getInt("quantity"));
            }
            System.out.println("---------------------------------------");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM Warehouse";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            int rowsDeleted = stmt.executeUpdate(sql);
            System.out.println("Rows deleted: " + rowsDeleted);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
