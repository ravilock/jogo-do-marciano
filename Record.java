public class Record {
  public int ID;
  public String name;
  public int guesses;
  public String timestamp;

  public Record(int ID, String name, int guesses, String timestamp) {
    this.ID = ID;
    this.name = name;
    this.guesses = guesses;
    this.timestamp = timestamp;
  }
}
