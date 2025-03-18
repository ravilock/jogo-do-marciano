import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordRepo {
  private static final String DATABASE_URL = "jdbc:sqlite:records.db"; // Database file path

  public RecordRepo() {
    this.initializeDatabase();
  }

  public void initializeDatabase() {
    // Try to connect to the SQLite database
    try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
      if (conn != null) {
        System.out.println("Connection to SQLite has been established.");

        // Create table if it doesn't exist
        createRecordsTable();
      }
    } catch (SQLException e) {
      System.out.println("An error occurred while connecting to the database.");
      e.printStackTrace();
    }
  }

  private void createRecordsTable() {
    String createTableSQL = "CREATE TABLE IF NOT EXISTS records ("
        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
        + "name TEXT NOT NULL, "
        + "guesses INTEGER NOT NULL,"
        + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP"
        + ");";

    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
        Statement stmt = conn.createStatement()) {
      stmt.execute(createTableSQL);
      System.out.println("Table 'records' has been created or already exists.");
    } catch (SQLException e) {
      System.out.println("An error occurred while creating the table.");
      e.printStackTrace();
    }
  }

  public void insertRecord(String name, int guesses) {
    String insertSQL = "INSERT INTO records (name, guesses) VALUES (?, ?)";

    try (Connection conn = DriverManager.getConnection(DATABASE_URL);
        PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
      pstmt.setString(1, name);
      pstmt.setInt(2, guesses);
      pstmt.executeUpdate();
      System.out.println("Record inserted successfully!");
    } catch (SQLException e) {
      System.out.println("An error occurred while inserting the record.");
      e.printStackTrace();
    }
  }

  public List<Record> getTopRecords() {
    List<Record> records = new ArrayList<>();

    String selectSQL = "SELECT id, name, guesses, timestamp FROM records ORDER BY guesses DESC LIMIT 10";

    try (
        Connection conn = DriverManager.getConnection(DATABASE_URL);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(selectSQL)) {

      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int guesses = rs.getInt("guesses");
        String timestamp = rs.getString("timestamp");

        records.add(new Record(id, name, guesses, timestamp));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return records;
  }
}
