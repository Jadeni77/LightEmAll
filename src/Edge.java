// represents edges for the minimum spanning tree
public class Edge {
  protected GamePiece from;
  protected GamePiece to;
  int weight;

  public Edge(GamePiece from, GamePiece to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  // finds the change in column or row with this edge
  public int changeInColOrRow(String str) {
    int dif = 0;
    if (str.equals("col")) {
      dif = this.from.difference(this.to, str);
    } else if (str.equals("row")) {
      dif = this.from.difference(this.to, str);
    }
    return dif;
  }

  // alters the pointers of the game piece's from or to depending on the given string
  // EFFECT: changes the pointing direction of this game piece's from or to
  public void getFromOrToMakeTrue(String str1, String str2) {
    if (str1.equals("from")) {
      if (str2.equals("right")) {
        this.from.makeTrue("right");
      } else if (str2.equals("left")) {
        this.from.makeTrue("left");
      } else if (str2.equals("top")) {
        this.from.makeTrue("top");
      } else if (str2.equals("bottom")) {
        this.from.makeTrue("bottom");
      }
    } else if (str1.equals("from")) {
      if (str2.equals("right")) {
        this.to.makeTrue("right");
      } else if (str2.equals("left")) {
        this.to.makeTrue("left");
      } else if (str2.equals("top")) {
        this.to.makeTrue("top");
      } else if (str2.equals("bottom")) {
        this.to.makeTrue("bottom");
      }
    }
  }
}