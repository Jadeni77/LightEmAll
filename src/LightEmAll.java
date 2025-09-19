import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.*;

//GamePiece is one of the many box in the game
// a board is all the GamePiece of the game
//a node is the line inside the box (GamePiece)

// represents the LightEmAllPart2 game world
public class LightEmAll extends World {
  // a list of columns of GamePieces, i.e., represents the board in column-major order
  ArrayList<ArrayList<GamePiece>> board;
  // a list of all nodes
  ArrayList<GamePiece> nodes;
  // the width and height of the board
  int width;
  int height;
  // the current location of the power station, as well as its effective radius
  int powerRow;
  int powerCol;
  int radius;
  Random rand;
  //a list of edges of the minimum spanning tree
  ArrayList<Edge> mst;

  public LightEmAll(int width, int height, Random rand) {
    this.width = width;
    this.height = height;
    this.board = new ArrayList<ArrayList<GamePiece>>();
    this.nodes = new ArrayList<GamePiece>();
    this.mst = new ArrayList<Edge>();
    this.rand = rand;

    //Initialize the board field with all connections to be false initially
    for (int col = 0; col < this.width; col++) {
      ArrayList<GamePiece> columns = new ArrayList<GamePiece>();
      for (int row = 0; row < this.height; row++) {
        GamePiece gp = new GamePiece(row, col, false, false, false, false);
        columns.add(gp);
        this.nodes.add(gp);
      }
      this.board.add(columns);
    }

    ArrayList<Edge> edges = new ArrayList<Edge>();
    //set up edges with random weights
    this.randomEdge(edges);

    //Kruskal's algorithm
    edges.sort((ed1, ed2) -> ed1.weight - ed2.weight);
    HashClass hi = new HashClass(this.nodes);

    for (Edge e : edges) {
      //from != to which should be always
      if (hi.find(e.from) != hi.find(e.to)) {
        this.mst.add(e);
        hi.union(e.from, e.to);
      }
    }
    
    //connect nodes base on mst
    for (Edge e : this.mst) {
      int x = e.changeInColOrRow("col");
      int y = e.changeInColOrRow("row");

      if (x == 1) { //the right
        e.getFromOrToMakeTrue("from", "right");
        e.getFromOrToMakeTrue("to", "left");
      }
      else if (x == -1) { //left
        e.getFromOrToMakeTrue("from", "left");
        e.getFromOrToMakeTrue("to", "right");
      }
      else if (y == 1) { //top
        e.getFromOrToMakeTrue("from", "bottom");
        e.getFromOrToMakeTrue("to", "top");
      }
      else if (y == -1) { //bottom
        e.getFromOrToMakeTrue("from", "top");
        e.getFromOrToMakeTrue("to", "bottom");
      }
    }

    this.powerRow = 0;
    this.powerCol = 0;
    if (this.powerCol <= this.width 
        && this.powerCol >= 0
        && this.powerRow <= this.height
        && this.powerRow >= 0) {
      this.board.get(this.powerCol).get(this.powerRow).makeTrue("ps");
    }

    //creates connected board
    buildMST();
    // generate the random game pieces
    makeRandomNode(this.rand);
    
    this.radius = calculateRadius();
    // check how many wires are connected to the power station
    computeIsPowered();
  }

  // creates a random edge for the minimum spanning tree
  // EFFECT: alters the game board to create random game pieces
  protected void randomEdge(ArrayList<Edge> edges) {
    for (int col = 0; col < this.width; col++) {
      for (int row = 0; row < this.height; row++) {
        GamePiece current = this.board.get(col).get(row);
        if (col < this.width - 1) { //from left to right,the right neighbor
          edges.add(new Edge(current, 
              this.board.get(col + 1).get(row), 
              this.rand.nextInt(1000)));
        }
        if (row < this.height - 1) { //from top to bottom, bottom neighbor
          edges.add(new Edge(current, 
              this.board.get(col).get(row + 1), 
              this.rand.nextInt(1000)));
        }
      }
    }
  }

  // builds up the minimum spanning tree to sort
  // EFFECT: alters the edges of the game so that they would be able to connect in some way
  protected void buildMST() {
    ArrayList<Edge> edges = new ArrayList<Edge>();
    randomEdge(edges);

    //bubble sort
    for (int i = 0; i < edges.size(); i++) {
      for (int j = 0; j < edges.size() - 1 - i; j++) {
        if (edges.get(j).weight > edges.get(j + 1).weight) {
          //swap the two
          Edge temp = edges.get(j);
          edges.set(j, edges.get(j + 1));
          edges.set(j + 1, temp);
        }
      }
    }
    HashClass hi = new HashClass(this.nodes);
    for (Edge e : edges) {
      if (hi.find(e.from) != hi.find(e.to)) {
        connectPieces(e.from, e.to);
        hi.union(e.from, e.to);
      }
    }
  }

  // connects game pieces together to ensure a solution
  // EFFECT: Alters game pieces if it does not connect with one another
  protected void connectPieces(GamePiece a, GamePiece b) {
    //determine direction and connect them
    if (a.col == b.col) { //vertical connection
      a.makeTrue("bottom");
      b.makeTrue("top");
    }
    else { //horizontal connection
      a.makeTrue("right");
      b.makeTrue("left");
    }
  }

  // creates a random game piece node for the game
  // EFFECT: changes every node to another random direction of node
  protected void makeRandomNode(Random rand) {
    //manipulate every node 
    for (GamePiece gp : this.nodes) {
      //four directions so 4 
      int rotation = rand.nextInt(4);
      for (int i = 0; i < rotation; i++) {
        //rotate the gameNode
        gp.rotate();
      }
    }
  }

  // alters world state to power up the nodes if it is connected to power station
  // EFFECT: determines if pieces are connected together and powers the pieces if so
  protected void computeIsPowered() {
    //reset all nodes to be not powered
    for (GamePiece gp : this.nodes) {
      gp.setDistanceFromPS(-1);
      gp.makePoweredFalse();
    }
    // determine the power station node location on the board & make the node powered
    GamePiece start = this.board.get(this.powerCol).get(this.powerRow);
    start.setDistanceFromPS(0);
    start.setPowered(0 <= this.radius);
    // cannot use arrayList, but LinkedList will have a link to check if nodes are connected
    Queue<GamePiece> queue = new LinkedList<GamePiece>();
    // add the node with the power station to the first queue
    queue.add(start);

    // As long as there are still nodes we continue to search
    while (!queue.isEmpty()) {
      // the first out of the queue
      GamePiece current = queue.poll();

      for (GamePiece neighbor : getConnectedNeighbors(current)) {
        if (neighbor.updatePowerFrom(current, this.radius)) {
          queue.add(neighbor);
        }
      }
    }
  }

  // finds game pieces with its neighbors from the left, right, top and bottom
  protected ArrayList<GamePiece> getConnectedNeighbors(GamePiece current) {
    ArrayList<GamePiece> neighbors = new ArrayList<GamePiece>();
    // Check left
    //if the left points at right and vice versa
    if (current.left && current.col > 0) {
      GamePiece left = this.board.get(current.col - 1).get(current.row);
      if (left.right) {
        neighbors.add(left);
      }
    }
    // Check right
    if (current.right && current.col < this.width - 1) {
      GamePiece right = this.board.get(current.col + 1).get(current.row);
      if (right.left) {
        neighbors.add(right);
      }
    }
    // Check top
    if (current.top && current.row > 0) {
      GamePiece top = this.board.get(current.col).get(current.row - 1);
      if (top.bottom) {
        neighbors.add(top);
      }
    }
    // Check bottom
    if (current.bottom && current.row < this.height - 1) {
      GamePiece bottom = this.board.get(current.col).get(current.row + 1);
      if (bottom.top) {
        neighbors.add(bottom);
      }
    }
    return neighbors;
  }

  // creates the world scene of the game
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(1000,1000);
    // goes through each game piece on the board
    for (int col = 0; col < this.width; col++) {
      for (int row = 0; row < this.height; row++) {
        GamePiece gp = this.board.get(col).get(row);
        Color wireColor;
        // change the color of the game piece if it is powered
        if (gp.powered) {
          wireColor = new Color(255, 215, 0); //gold
        }
        else {
          wireColor = new Color(100, 100, 100); //dark grey
        }
        WorldImage img = gp.tileImage(100, 10, wireColor, gp.powerStation);
        int x = col * 100 + 50;
        int y = row * 100 + 50;
        scene.placeImageXY(img, x, y);
      }
    }
    return scene;
  }

  // shifts the power station based on key presses
  // EFFECT: changes the location of the power station
  public void onKeyEvent(String key) {
    if (key.equals("left")) {
      // shifts the power station to left
      move(this.powerRow, this.powerCol - 1);
    }
    else if (key.equals("right")) {
      // shifts the power station to right
      move(this.powerRow, this.powerCol + 1);
    }
    else if (key.equals("up")) {
      // shifts the power station to top
      move(this.powerRow - 1, this.powerCol);
    }
    else if (key.equals("down")) {
      // shifts the power station to bottom
      move(this.powerRow + 1, this.powerCol);
    }
    computeIsPowered();
    checkWin();
  }

  // shifts the power station
  // EFFECT: changes the location of the power station given the location
  public void move(int newRow, int newCol) {
    if (newRow < 0 || newRow >= this.height || newCol < 0 || newCol >= this.width) {
      return ;
    }
    GamePiece current = this.board.get(this.powerCol).get(this.powerRow);
    GamePiece target = this.board.get(newCol).get(newRow);
    boolean temp = false;
    if (newCol == this.powerCol - 1) {
      temp = current.left && target.right;
    }
    else if (newCol == this.powerCol + 1) {
      temp = current.right && target.left;
    }
    else if (newRow == this.powerRow - 1) {
      temp = current.top && target.bottom;
    }
    else if (newRow == this.powerRow + 1) {
      temp = current.bottom && target.top;
    }
    if (temp) {
      current.makePSFalse();
      target.makeTrue("ps");
      this.powerRow = newRow;
      this.powerCol = newCol;

      this.radius = this.calculateRadius();
      computeIsPowered();
    }
  }

  // alters the world based on mouse presses
  // EFFECT: rotates a game piece on the board and powers it if correctly connected
  public void onMousePressed(Posn pos) {
    int col = pos.x / 100;
    int row = pos.y / 100;
    // checks if the mouse press is on the board and rotates the game piece where the press is
    if (col >= 0 && col < this.width && row >= 0 && row < this.height) {
      GamePiece gp = this.board.get(col).get(row);
      gp.rotate();

      computeIsPowered();
      checkWin();
    }
  }

  // determines if all the nodes have been powered
  // EFFECT: displays a winning scene onto the scene if all nodes are powered
  public void checkWin() {
    boolean allConnected = true;
    boolean allWithinR = true;

    for (GamePiece gp : this.nodes) {
      if (!gp.powered) {
        allConnected = false;
      }
      if (gp.distanceFromStation == -1) {
        allWithinR = false;
      }
    }
    if (allConnected) {
      //recalculate radius if fully connected
      this.radius = calculateRadius();
    }
    if (allWithinR && allConnected) {
      this.endOfWorld("You Win!");
    }
  }

  // draws the ending scene with a message
  public WorldScene lastScene(String msg) {
    WorldScene scene = this.makeScene(); //keep current game visible 
    WorldImage windowText = new TextImage(msg, 30, FontStyle.BOLD, Color.green);
    scene.placeImageXY(windowText, (this.width * 100) / 2, (this.height * 100) / 2);
    return scene;
  }
  
  // calculates the radius for where the power station is at and powering
  public int calculateRadius() {
    GamePiece start = this.board.get(this.powerCol).get(this.powerRow);
    GamePiece firstBFSend =  bfs(start);
    GamePiece secondBFSend = bfs(firstBFSend);
    
    return (secondBFSend.calculateDistPS());
  }
  
  // searches and finds the farthest game piece
  // EFFECT: resets the distance of all game pieces from the power station
  public GamePiece bfs(GamePiece start) {
    //reset distance
    for (GamePiece gp : this.nodes) {
      gp.setDistanceFromPS(-1);
    }
 
    Queue<GamePiece> queue = new LinkedList<GamePiece>();
    start.setDistanceFromPS(0);
    queue.add(start);
    GamePiece furthest = start;
    
    while (!queue.isEmpty()) {
      GamePiece current = queue.poll();
      for (GamePiece neighbor : getConnectedNeighbors(current)) {
        if (neighbor.distanceFromStation == -1) {
          neighbor.setDistanceFromPS(current.addOneDist());
          queue.add(neighbor);
          if (neighbor.distanceFromStation > furthest.distanceFromStation) {
            furthest = neighbor;
          }
        }
      }
    }
    return furthest;
  }
}

