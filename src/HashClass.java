import java.util.ArrayList;
import java.util.HashMap;

// represents a hash class comparing two nodes
public class HashClass {
  protected HashMap<GamePiece, GamePiece> parent;

  public HashClass(ArrayList<GamePiece> node) {
    this.parent = new HashMap<GamePiece, GamePiece>();
    for (GamePiece nodes : node) {
      //assign hashcode
      //the name == the hashcode
      this.parent.put(nodes, nodes);
    }
  }

  // to find the parent/root of the node
  public GamePiece find(GamePiece node) {
    if (this.parent.get(node) != node) {
      this.parent.put(node, find(this.parent.get(node)));
    }
    return this.parent.get(node);
  }

  // Merges two game pieces and updating the parent of a root to the other parent
  // EFFECT: Updates the parent so that the root of a given game piece points to the parent
  // of another given game piece
  public void union(GamePiece a, GamePiece b) {
    GamePiece rA = find(a);
    GamePiece rB = find(b);
    this.parent.put(rB, rA);
  }
}
