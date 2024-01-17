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
                + " id text PRIMARY KEY,\n"
                + " title text NOT NULL,\n"
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

    public void createNewTableTransactions() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Transactions (\n"
                + " id integer PRIMARY KEY,\n"
                + " total real\n"
                + ");";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            // creating a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String idItem, String title, int quantity) {
        String sql = "INSERT INTO Inventory(id,title, quantity) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idItem);
            pstmt.setString(2, title);
            pstmt.setDouble(3, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertTransaction(double total) {
        String sql = "INSERT INTO Transactions(total) VALUES(?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, total);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(String id, String title, int quantity) {
        String sql = "UPDATE Inventory SET title = ? , "
                + "quantity = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, title);
            pstmt.setInt(2, quantity);
            pstmt.setString(3, id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(String id) {
        String sql = "DELETE FROM Inventory WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, id);
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
            System.out.println("------------ Inventory data ------------");
            System.out.println(" ID            Title      No. of copies");
            while (rs.next()) {
                System.out.println(rs.getString("id") +  "\t" +
                        rs.getString("title") + "\t" +
                        rs.getInt("quantity"));
            }
            System.out.println("---------------------------------------");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printAllContentTransaction() {
        String sql = "SELECT * FROM Transactions";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {


            // loop through the result set
            if (!rs.isBeforeFirst()) {
                System.out.println("No data found.");
                return;
            }
            System.out.println("------------ Transactions data ------------");
            System.out.println("ID  Total");
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getDouble("total"));
            }
            System.out.println("---------------------------------------");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAll() {
        String sql = "DELETE FROM Inventory";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            int rowsDeleted = stmt.executeUpdate(sql);
            System.out.println("Rows deleted: " + rowsDeleted);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAllTransaction() {
        String sql = "DELETE FROM Transactions";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            int rowsDeleted = stmt.executeUpdate(sql);
            System.out.println("Rows deleted: " + rowsDeleted);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
