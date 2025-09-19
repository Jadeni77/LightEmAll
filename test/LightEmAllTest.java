import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javalib.impworld.WorldScene;
import javalib.worldimages.FontStyle;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;
import tester.Tester;

// represents examples and tests for the game
class LightEmAllTest {

  LightEmAllPart2 game1;
  LightEmAllPart2 game2;
  LightEmAllPart2 game3;
  LightEmAllPart2 game4;
  LightEmAllPart2 game5;
  Random rand1;
  Random rand2;
  Random rand3;
  Random rand4;
  Random rand5;
  Random rand6;
  Random rand7;
  GamePiece p1;
  GamePiece p2;
  GamePiece p3;
  GamePiece p4;
  GamePiece p5;
  GamePiece p6;
  GamePiece p7;
  Edge e1;
  Edge e2;
  Edge e3;
  Edge e4;
  Edge e5;
  Edge e6;
  ArrayList<GamePiece> listOfGP;
  HashClass hc;

  void initData() {
    this.rand1 = new Random(1);
    this.rand2 = new Random(2);
    this.rand3 = new Random(3);
    this.rand4 = new Random(4);
    this.rand5 = new Random(5);
    this.rand6 = new Random(6);
    this.rand7 = new Random(7);
    this.game1 = new LightEmAllPart2(8, 8, this.rand1);
    this.game2 = new LightEmAllPart2(4, 4, this.rand2);
    this.game3 = new LightEmAllPart2(3, 3, this.rand3);
    this.game4 = new LightEmAllPart2(2, 2, this.rand6);
    this.game5 = new LightEmAllPart2(1, 1, this.rand7);
    this.p1 = new GamePiece(0, 0, false, true, false, true);
    this.p2 = new GamePiece(1, 0, false, false, true, true);
    this.p3 = new GamePiece(0, 1, true, false, false, true);
    this.p4 = new GamePiece(1, 1, false, false, true, false);
    this.p5 = new GamePiece(0, 0, false, false, false, false);
    this.p6 = new GamePiece(0, 1, false, false, false, false);
    this.p7 = new GamePiece(1, 1, false, false, false, false);
    this.e1 = new Edge(this.p1, this.p3, 5);
    this.e2 = new Edge(this.p2, this.p4, 10);
    this.e3 = new Edge(this.p3, this.p1, 3);
    this.e4 = new Edge(this.p1, this.p1, 0);
    this.e5 = new Edge(this.p1, this.p4, 8);
    this.e6 = new Edge(this.p3, this.p2, 2);
    this.listOfGP = new ArrayList<>(Arrays.asList(this.p5, this.p6, this.p7));
    this.hc = new HashClass(this.listOfGP);
  }

  // test the makeRandomNode method
  void testMakeRandomNode(Tester t) {
    this.initData();
    // tests a original game piece of the world
    t.checkExpect(this.game1.nodes.get(0), new GamePiece(0, 0, true, false, true,
            false, false, false, -1));
    // changes the game pieces to a random game piece for the same world
    this.game1.makeRandomNode(this.rand4);
    // tests that the same game piece now has a different pointing of wires
    t.checkExpect(this.game1.nodes.get(0), new GamePiece(0, 0, false, true, false,
            true, false, false, -1));
    // tests a original game piece of another world
    t.checkExpect(this.game2.nodes.get(1), new GamePiece(1, 0, true, false, true,
            true, false, false, -1));
    // changes the game pieces to a random game piece for the same world
    this.game2.makeRandomNode(this.rand6);
    // tests that the same game piece now has a different pointing of wires
    t.checkExpect(this.game2.nodes.get(1), new GamePiece(1, 0, false, true, true,
            true, false, false, -1));
  }

  // test the computeIsPowered method
  void testComputeIsPowered(Tester t) {
    this.initData();
    // make game pieces that point to different directions or no directions
    GamePiece piece1 = new GamePiece(0, 0, false, false, false, false);
    GamePiece pieceLeft = new GamePiece(0, 2, true, false, false, false);
    GamePiece pieceRight = new GamePiece(0, 1, false, true, false, false);
    GamePiece piece2 = new GamePiece(1, 0, false, false, false, false);
    GamePiece piece3 = new GamePiece(1, 1, false, false, false, false);
    GamePiece pieceBottom = new GamePiece(1, 2, false, false, false, true);
    GamePiece piece4 = new GamePiece(2, 0, false, false, false, false);
    GamePiece piece5 = new GamePiece(2, 1, false, false, false, false);
    GamePiece pieceTop = new GamePiece(2, 2, false, false, true, false);
    // make the power station be on the piece pointing left
    pieceLeft.powerStation = true;
    // create a board & nodes with the pieces made above
    ArrayList<ArrayList<GamePiece>> board = new ArrayList<>();
    board.add(new ArrayList<>(Arrays.asList(piece1, piece2, piece4)));
    board.add(new ArrayList<>(Arrays.asList(pieceRight, piece3, piece5)));
    board.add(new ArrayList<>(Arrays.asList(pieceLeft, pieceBottom, pieceTop)));
    ArrayList<GamePiece> nodes = new ArrayList<>(Arrays.asList(piece1, piece2, piece4,
            pieceRight, piece3, piece5, pieceLeft, pieceBottom, pieceTop));
    // make game 3 have the created pieces as its board and nodes
    this.game3.board = board;
    this.game3.nodes = nodes;
    // make power station be at right top
    this.game3.powerRow = 0;
    this.game3.powerCol = 2;

    // test that pieces are not powered
    t.checkExpect(pieceRight.powered, false);
    t.checkExpect(pieceLeft.powered, false);
    t.checkExpect(pieceBottom.powered, false);
    t.checkExpect(pieceTop.powered, false);

    // check to see if there are powered pieces
    this.game3.computeIsPowered();

    // test that the left right piece are powered
    t.checkExpect(pieceRight.powered, true);
    t.checkExpect(pieceLeft.powered, true);
    t.checkExpect(pieceBottom.powered, false);
    t.checkExpect(pieceTop.powered, false);

    // move power station to bottom right piece
    pieceLeft.powerStation = false;
    pieceLeft.powered = false;
    pieceRight.powered = false;
    pieceTop.powerStation = true;
    this.game3.powerRow = 2;
    this.game3.powerCol = 2;

    // check to see if there are powered pieces
    this.game3.computeIsPowered();

    // test that the top bottom piece are powered
    t.checkExpect(pieceBottom.powered, true);
    t.checkExpect(pieceTop.powered, true);
    t.checkExpect(pieceRight.powered, false);
    t.checkExpect(pieceLeft.powered, false);
  }

  // tests the makeScene method
  void testMakeScene(Tester t) {
    this.initData();
    // draws images of the tiles for a expected scene for a game of 3x3
    GamePiece p1 = this.game2.board.get(0).get(0);
    GamePiece p2 = this.game3.board.get(0).get(1);
    GamePiece p3 = this.game3.board.get(0).get(2);
    GamePiece p4 = this.game3.board.get(1).get(0);
    GamePiece p5 = this.game3.board.get(1).get(1);
    GamePiece p6 = this.game3.board.get(1).get(2);
    GamePiece p7 = this.game3.board.get(2).get(0);
    GamePiece p8 = this.game3.board.get(2).get(1);
    GamePiece p9 = this.game3.board.get(2).get(2);
    WorldImage pImage1 = p1.tileImage(100, 10, new Color(255, 215, 0), p1.powerStation);
    WorldImage pImage2 = p2.tileImage(100, 10, new Color(100, 100, 100), p2.powerStation);
    WorldImage pImage3 = p3.tileImage(100, 10, new Color(100, 100, 100), p3.powerStation);
    WorldImage pImage4 = p4.tileImage(100, 10, new Color(255, 215, 0), p4.powerStation);
    WorldImage pImage5 = p5.tileImage(100, 10, new Color(255, 215, 0), p5.powerStation);
    WorldImage pImage6 = p6.tileImage(100, 10, new Color(100, 100, 100), p6.powerStation);
    WorldImage pImage7 = p7.tileImage(100, 10, new Color(255, 215, 0), p7.powerStation);
    WorldImage pImage8 = p8.tileImage(100, 10, new Color(100, 100, 100), p8.powerStation);
    WorldImage pImage9 = p9.tileImage(100, 10, new Color(100, 100, 100), p9.powerStation);
    // draw the expected scene
    WorldScene expectedGame = new WorldScene(1000, 1000);
    expectedGame.placeImageXY(pImage1, 50, 50);
    expectedGame.placeImageXY(pImage2, 50, 150);
    expectedGame.placeImageXY(pImage3, 50, 250);
    expectedGame.placeImageXY(pImage4, 150, 50);
    expectedGame.placeImageXY(pImage5, 150, 150);
    expectedGame.placeImageXY(pImage6, 150, 250);
    expectedGame.placeImageXY(pImage7, 250, 50);
    expectedGame.placeImageXY(pImage8, 250, 150);
    expectedGame.placeImageXY(pImage9, 250, 250);
    // test that the game scene of game 3 is the same as the expected scene
    t.checkExpect(this.game3.makeScene(), expectedGame);

    // draws images of the tiles for a expected scene for a game of 2x2
    GamePiece g4p1 = this.game4.board.get(0).get(0);
    GamePiece g4p2 = this.game4.board.get(0).get(1);
    GamePiece g4p3 = this.game4.board.get(1).get(0);
    GamePiece g4p4 = this.game4.board.get(1).get(1);
    WorldImage p4i1 = g4p1.tileImage(100, 10, new Color(255, 215, 0), g4p1.powerStation);
    WorldImage p4i2 = g4p2.tileImage(100, 10, new Color(100, 100, 100), g4p2.powerStation);
    WorldImage p4i3 = g4p3.tileImage(100, 10, new Color(100, 100, 100), g4p3.powerStation);
    WorldImage p4i4 = g4p4.tileImage(100, 10, new Color(100, 100, 100), g4p4.powerStation);
    // draw the expected scene
    WorldScene expected2 = new WorldScene(1000, 1000);
    expected2.placeImageXY(p4i1, 50, 50);
    expected2.placeImageXY(p4i2, 50, 150);
    expected2.placeImageXY(p4i3, 150, 50);
    expected2.placeImageXY(p4i4, 150, 150);
    // test that the game scene of game 2 is the same as the expected scene
    t.checkExpect(this.game4.makeScene(), expected2);

    // draws images of the tiles for a expected scene for a game of 1x1
    GamePiece g5p = this.game5.board.get(0).get(0);
    WorldImage g5i = g5p.tileImage(100, 10, new Color(255, 215, 0), g5p.powerStation);
    // draw the expected scene
    WorldScene expected3 = new WorldScene(1000, 1000);
    expected3.placeImageXY(g5i, 50, 50);
    // test that the game scene of game 5 is the same as the expected scene
    t.checkExpect(this.game5.makeScene(), expected3);
  }

  // test the onKeyEvent method
  void testOnKeyEvent(Tester t) {
    this.initData();
    // test the original location of the power station
    t.checkExpect(this.game1.powerCol, 0);
    t.checkExpect(this.game1.powerRow, 0);
    // press the right buttom
    this.game1.onKeyEvent("right");
    // test that there is no shift as the wires are not powered
    t.checkExpect(this.game1.powerCol, 0);
    t.checkExpect(this.game1.powerRow, 0);

    // create a board that is powered
    this.p1.powerStation = true;
    this.p1.powered = false;
    this.p2.powered = false;
    this.p3.powered = false;
    this.p4.powered = false;
    ArrayList<ArrayList<GamePiece>> board = new ArrayList<>();
    board.add(new ArrayList<>(Arrays.asList(this.p1, this.p2)));
    board.add(new ArrayList<>(Arrays.asList(this.p3, this.p4)));
    ArrayList<GamePiece> nodes = new ArrayList<>(Arrays.asList(this.p1, this.p2,
            this.p3, this.p4));
    this.game4.board = board;
    this.game4.nodes = nodes;
    this.game4.powerRow = 0;
    this.game4.powerCol = 0;
    // to power up the board with wires connected
    this.game4.computeIsPowered();

    // check the starting point of the power station
    t.checkExpect(this.game4.powerRow, 0);
    t.checkExpect(this.game4.powerCol, 0);

    // press the right button
    this.game4.onKeyEvent("right");
    // test that the power station has moved once to the right
    t.checkExpect(this.game4.powerRow, 0);
    t.checkExpect(this.game4.powerCol, 1);

    // press the down button
    this.game4.onKeyEvent("down");
    // test that the power station has moved down one
    t.checkExpect(this.game4.powerRow, 1);
    t.checkExpect(this.game4.powerCol, 1);

    // press the up button
    this.game4.onKeyEvent("up");
    // test that the power station has moved up one
    t.checkExpect(this.game4.powerRow, 0);
    t.checkExpect(this.game4.powerCol, 1);

    // press the button left
    this.game4.onKeyEvent("left");
    // test that the power station has moved once to the left
    t.checkExpect(this.game4.powerRow, 0);
    t.checkExpect(this.game4.powerCol, 0);
  }

  // test the move method
  void testMove(Tester t) {
    this.initData();
    // set up a connected powered game board
    this.p1.powerStation = true;
    this.p2.powerStation = false;
    this.p3.powerStation = false;
    this.p4.powerStation = false;
    this.p1.powered = false;
    this.p2.powered = false;
    this.p3.powered = false;
    this.p4.powered = false;
    ArrayList<ArrayList<GamePiece>> board = new ArrayList<>();
    board.add(new ArrayList<>(Arrays.asList(this.p1, this.p2)));
    board.add(new ArrayList<>(Arrays.asList(this.p3, this.p4)));
    ArrayList<GamePiece> nodes = new ArrayList<>(Arrays.asList(this.p1, this.p2,
            this.p3, this.p4));
    this.game4.board = board;
    this.game4.nodes = nodes;
    this.game4.powerRow = 0;
    this.game4.powerCol = 0;
    // power up the game pieces that are connected
    this.game4.computeIsPowered();

    // test the starting point of the power station
    t.checkExpect(this.game4.powerRow, 0);
    t.checkExpect(this.game4.powerCol, 0);

    // move the power station to the right
    this.game4.move(0, 1);
    // test that the power station has shifted right
    t.checkExpect(this.game4.powerRow, 0);
    t.checkExpect(this.game4.powerCol, 1);

    // move the power station to down
    this.game4.move(1, 1);
    // test that the power station has shifted down
    t.checkExpect(this.game4.powerRow, 1);
    t.checkExpect(this.game4.powerCol, 1);

    // move the power station up
    this.game4.move(0, 1);
    // test that the power station has shifted up
    t.checkExpect(this.game4.powerRow, 0);
    t.checkExpect(this.game4.powerCol, 1);

    // Move the power station left
    this.game4.move(0, 0);
    // test that the power station has shifted left
    t.checkExpect(this.game4.powerRow, 0);
    t.checkExpect(this.game4.powerCol, 0);
  }

  // test the onMousePressed method
  void testOnMousePressed(Tester t) {
    this.initData();
    // create the board for game 4
    this.p1.powerStation = true;
    this.p1.powered = false;
    this.p2.powered = false;
    this.p3.powered = false;
    this.p4.powered = false;
    ArrayList<ArrayList<GamePiece>> board = new ArrayList<>();
    board.add(new ArrayList<>(Arrays.asList(this.p1, this.p2)));
    board.add(new ArrayList<>(Arrays.asList(this.p3, this.p4)));
    ArrayList<GamePiece> nodes = new ArrayList<>(Arrays.asList(this.p1, this.p2,
            this.p3, this.p4));
    this.game4.board = board;
    this.game4.nodes = nodes;
    // check to power up those wires that are connected
    this.game4.computeIsPowered();

    // test the pointing of a game piece on the board
    t.checkExpect(this.p2.left, false);
    t.checkExpect(this.p2.right, false);
    t.checkExpect(this.p2.top, true);
    t.checkExpect(this.p2.bottom, true);

    // click on that game piece
    this.game4.onMousePressed(new Posn(50, 150));

    // test that the same game piece is now rotated
    t.checkExpect(this.p2.left, true);
    t.checkExpect(this.p2.right, true);
    t.checkExpect(this.p2.top, false);
    t.checkExpect(this.p2.bottom, false);

    // click somewhere outside the game board
    this.game4.onMousePressed(new Posn(-100, 50));
    // test that the same game piece has no change
    t.checkExpect(this.p2.left, true);
    t.checkExpect(this.p2.right, true);
    t.checkExpect(this.p2.top, false);
    t.checkExpect(this.p2.bottom, false);
  }

  // test the lastScene method
  void testLastScene(Tester t) {
    this.initData();
    // create a powered board
    this.p1.powered = true;
    this.p2.powered = true;
    this.p3.powered = true;
    this.p4.powered = true;
    this.p1.powerStation = true;
    ArrayList<ArrayList<GamePiece>> board = new ArrayList<>();
    board.add(new ArrayList<>(Arrays.asList(this.p1, this.p2)));
    board.add(new ArrayList<>(Arrays.asList(this.p3, this.p4)));
    ArrayList<GamePiece> nodes =
            new ArrayList<>(Arrays.asList(this.p1, this.p2, this.p3, this.p4));
    this.game4.board = board;
    this.game4.nodes = nodes;

    // expected last scene
    WorldScene ws = this.game4.makeScene();
    WorldImage msg = new TextImage("You Win!", 30, FontStyle.BOLD, Color.green);
    ws.placeImageXY(msg, 100, 100);

    // test that a all powered board displays the same expected winning scene
    t.checkExpect(this.game4.lastScene("You Win!"), ws);

    // create last scene with a different message
    WorldScene ws2 = this.game4.makeScene();
    ws2.placeImageXY(new TextImage("Game Over", 30, FontStyle.BOLD, Color.green), 100, 100);
    // test that the scene is now the same as a expected last same saying game over
    t.checkExpect(this.game4.lastScene("Game Over"), ws2);

    // create last scene with a different message
    WorldScene ws3 = this.game4.makeScene();
    ws3.placeImageXY(new TextImage("blabla", 30, FontStyle.BOLD, Color.green), 100, 100);
    // test that the scene is now the same as a expected last same saying blabla
    t.checkExpect(this.game4.lastScene("blabla"), ws3);
  }

  // test the checkWin method
  void testCheckWin(Tester t) {
    this.initData();
    // create a powered 1 x 1 board
    this.p1.powerStation = true;
    this.p1.powered = true;
    ArrayList<ArrayList<GamePiece>> board = new ArrayList<>();
    board.add(new ArrayList<>(Arrays.asList(this.p1)));
    ArrayList<GamePiece> nodes = new ArrayList<>(Arrays.asList(this.p1));
    this.game5.board = board;
    this.game5.nodes = nodes;

    // expected scene of a powered board that should trigger a win
    WorldScene expected = this.game5.makeScene();
    expected.placeImageXY(new TextImage("You Win!", 30, FontStyle.BOLD, Color.green), 50, 50);
    // check to see if the game has been won
    this.game5.checkWin();

    // test that the scene for the game is the same as the expected scene created
    t.checkExpect(this.game5.lastScene("You Win!"), expected);

    // make the board not be all powered up
    this.p1.powered = false;
    // create a expected scene with no win message
    WorldScene expected2 = this.game5.makeScene();
    // check to see if the game has been won
    this.game5.checkWin();

    // test that the scene for the game has no winning message
    t.checkExpect(this.game5.makeScene(), expected2);

    // create a expected scene with a different win message
    WorldScene expected3 = this.game4.makeScene();
    expected.placeImageXY(new TextImage("Congrats!", 30, FontStyle.BOLD, Color.green), 50, 50);
    // check to see if the game has been won
    this.game4.checkWin();

    // test that the scene for the game is the same as the expected scene created
    t.checkExpect(this.game4.makeScene(), expected3);
  }

  // tests the randomeEdge method
  void testRandomEdge(Tester t) {
    this.initData();
    ArrayList<Edge> edges = new ArrayList<Edge>();
    // test the sizes originally before randomizing edges
    t.checkExpect(edges.size(), 0);
    // create random edges in the board
    this.game4.randomEdge(edges);
    // since we have randomized edges there will be 4 edges as its a 4 game piece game
    t.checkExpect(edges.size(), 4);

    // get values of the game pieces from the game board
    GamePiece n1 = this.game4.board.get(0).get(0);
    GamePiece n2 = this.game4.board.get(1).get(0);
    GamePiece n3 = this.game4.board.get(0).get(1);
    GamePiece n4 = this.game4.board.get(1).get(1);
    // get values of the edges after randomizing
    Edge e1 = edges.get(0);
    Edge e2 = edges.get(1);
    Edge e3 = edges.get(2);
    Edge e4 = edges.get(3);
    // test that the to and from pieces of edge 1 is the same as nodes 1 and 2
    t.checkExpect(e1.from, n1);
    t.checkExpect(e1.to, n2);
    // test that the to and from pieces of edge 2 is the same as nodes 1 and 2
    t.checkExpect(e2.from, n1);
    t.checkExpect(e2.to, n3);
    // test that the to and from pieces of edge 3 is the same as nodes 3 and 4
    t.checkExpect(e3.from, n3);
    t.checkExpect(e3.to, n4);
    // test that the to and from pieces of edge 4 is the same as nodes 2 and 4
    t.checkExpect(e4.from, n2);
    t.checkExpect(e4.to, n4);
  }

  // test the buildMST method
  void testBuildMST(Tester t) {
    this.initData();
    // make all game pieces have no connections
    this.p1 = new GamePiece(0, 0, false, false, false, false);
    this.p2 = new GamePiece(1, 0, false, false, false, false);
    this.p3 = new GamePiece(0, 1, false, false, false, false);
    this.p4 = new GamePiece(1, 1, false, false, false, false);
    // make game 4 have no connections
    ArrayList<ArrayList<GamePiece>> board = new ArrayList<>();
    board.add(new ArrayList<>(Arrays.asList(p1, p2)));
    board.add(new ArrayList<>(Arrays.asList(p3, p4)));
    ArrayList<GamePiece> nodes = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
    this.game4.board = board;
    this.game4.nodes = nodes;
    // Check that a piece is all pointing false
    t.checkExpect(this.p1.left, false);
    t.checkExpect(this.p1.right, false);
    t.checkExpect(this.p1.top, false);
    t.checkExpect(this.p1.bottom, false);

    // build the minimum spanning tree that connects pieces together
    this.game4.buildMST();

    // Check that a piece is now connecting/pointing true somewhere
    t.checkExpect(this.p1.left, false);
    t.checkExpect(this.p1.right, true);
    t.checkExpect(this.p1.top, false);
    t.checkExpect(this.p1.bottom, false);
    // Check that another piece is now connecting/pointing true somewhere
    t.checkExpect(this.p2.left, false);
    t.checkExpect(this.p2.right, true);
    t.checkExpect(this.p2.top, false);
    t.checkExpect(this.p2.bottom, false);
    // Check that another piece is now connecting/pointing true somewhere
    t.checkExpect(this.p3.left, true);
    t.checkExpect(this.p3.right, false);
    t.checkExpect(this.p3.top, false);
    t.checkExpect(this.p3.bottom, true);
    // Check that another piece is now connecting/pointing true somewhere
    t.checkExpect(this.p4.left, true);
    t.checkExpect(this.p4.right, false);
    t.checkExpect(this.p4.top, true);
    t.checkExpect(this.p4.bottom, false);
  }

  // test the connectPieces method
  void testConnectPieces(Tester t) {
    this.initData();
    this.p1 = new GamePiece(this.p1.row, this.p1.col, false, false, false, false);
    this.p2 = new GamePiece(this.p2.row, 4, true, false, false, false);
    this.p3 = new GamePiece(this.p2.row, 1, true, false, false, false);
    // test piece 1 has no wire powered on
    t.checkExpect(this.p1.top, false);
    t.checkExpect(this.p1.bottom, false);
    t.checkExpect(this.p1.left, false);
    t.checkExpect(this.p1.right, false);
    // connect pieces with 2 same pieces
    this.game4.connectPieces(this.p1, this.p1);
    // tests that two same pieces have a vertical connection
    t.checkExpect(this.p1.top, true);
    t.checkExpect(this.p1.bottom, true);
    t.checkExpect(this.p1.left, false);
    t.checkExpect(this.p1.right, false);
    // test that there is a horizontal connection
    this.game4.connectPieces(this.p2, this.p3);
    t.checkExpect(this.p2.top, false);
    t.checkExpect(this.p2.bottom, false);
    t.checkExpect(this.p2.left, true);
    t.checkExpect(this.p2.right, true);
  }

  void testGetConnectedNeighbors(Tester t) {
    this.initData();
    this.p1.right = true;
    this.p2.left = true;
    this.p1.bottom = true;
    this.p3.top = true;
    // Set board for game 4
    ArrayList<ArrayList<GamePiece>> board = new ArrayList<>();
    board.add(new ArrayList<>(Arrays.asList(this.p1, this.p3)));
    board.add(new ArrayList<>(Arrays.asList(this.p2, this.p4)));
    this.game4.board = board;

    // test that the neighbors of piece 1 has piece 2
    t.checkExpect(this.game4.getConnectedNeighbors(this.p1).contains(this.p2), true);
    // test that the neighbors of piece 1 has piece 3
    t.checkExpect(this.game4.getConnectedNeighbors(this.p1).contains(this.p3), true);
    // test that the neighbors of piece 2 has piece 1
    t.checkExpect(this.game4.getConnectedNeighbors(this.p2).contains(this.p1), true);
    // test that the neighbors of piece 1 are only piece 2 and 3
    t.checkExpect(this.game4.getConnectedNeighbors(this.p1),
            new ArrayList<GamePiece>(Arrays.asList(this.p2, this.p3)));
  }

  // test the calculateRadius method
  void testCalculateRadius(Tester t) {
    this.initData();
    // test the radius range for a game with 1 game piece
    t.checkExpect(this.game5.calculateRadius(), 1);
    // test the radius range for a game with 64 game pieces
    t.checkExpect(this.game1.calculateRadius(), 1);
    // test the radius range for a game with 16 game piece
    t.checkExpect(this.game2.calculateRadius(), 5);
  }

  // test the bfs method
  void testBfs(Tester t) {
    this.initData();
    // searches for the farthest piece in a game with 4 pieces from the given piece
    t.checkExpect(this.game4.bfs(this.game4.nodes.get(0)),
            new GamePiece(0, 0, true, false, false, true, true, true, 0));
    // searches for the farthest piece in a game with 64 pieces from the given piece
    t.checkExpect(this.game1.bfs(this.game1.nodes.get(5)),
            new GamePiece(5, 0, true, true, false, false, true, true, 0));
    // searches for the farthest piece in a game with 16 pieces from the given piece
    t.checkExpect(this.game2.bfs(this.game2.nodes.get(3)),
            new GamePiece(3, 0, true, false, false, true, true, true, 0));
  }

  // test the makeTrue method
  void testMakeRightTrue(Tester t) {
    this.initData();
    // tests if the right side is originally true
    t.checkExpect(this.p1.right, true);
    // tests if the right side is originally true
    t.checkExpect(this.p2.right, false);
    // makes the right be true when it is already true
    this.p1.makeTrue("right");
    // makes the right be true when it is originally false
    this.p2.makeTrue("right");
    // test that the values for right are true now
    t.checkExpect(this.p1.right, true);
    // test that the values for right are true now
    t.checkExpect(this.p2.right, true);

    this.initData();
    // tests if the left side is originally true
    t.checkExpect(this.p1.left, false);
    // tests if the left side is originally true
    t.checkExpect(this.p3.left, true);
    // makes the left be true when it is false
    this.p1.makeTrue("left");
    // makes the left be true when it is originally true
    this.p3.makeTrue("left");
    // test that the values for left are true now
    t.checkExpect(this.p1.left, true);
    // test that the values for left are true now
    t.checkExpect(this.p3.left, true);

    this.initData();
    // tests if the top side is originally true
    t.checkExpect(this.p1.top, false);
    // tests if the top side is originally true
    t.checkExpect(this.p2.top, true);
    // makes the top be true when it is false
    this.p1.makeTrue("top");
    // makes the top be true when it is originally true
    this.p2.makeTrue("top");
    // test that the values for top are true now
    t.checkExpect(this.p1.top, true);
    // test that the values for top are true now
    t.checkExpect(this.p2.top, true);

    this.initData();
    // tests if the bottom side is originally true
    t.checkExpect(this.p4.bottom, false);
    // tests if the bottom side is originally true
    t.checkExpect(this.p1.bottom, true);
    // makes the bottom be true when it is false
    this.p4.makeTrue("bottom");
    // makes the bottom be true when it is originally true
    this.p1.makeTrue("bottom");
    // test that the values for bottom are true now
    t.checkExpect(this.p4.bottom, true);
    // test that the values for bottom are true now
    t.checkExpect(this.p1.bottom, true);

    this.initData();
    this.p2.powerStation = true;
    // test the initial value of a power station for the game piece
    t.checkExpect(this.p1.powerStation, false);
    // test the initial value of a power station for the game piece
    t.checkExpect(this.p2.powerStation, true);
    // makes the power station be true
    this.p1.makeTrue("ps");
    // makes the power station be true
    this.p2.makeTrue("ps");
    // test that the power station is altered to true when originally false
    t.checkExpect(this.p1.powerStation, true);
    // test that the power station is altered to true when originally true
    t.checkExpect(this.p2.powerStation, true);

    this.initData();
    this.p2.powered = true;
    // test the initial value of powered for the game piece
    t.checkExpect(this.p1.powered, false);
    // test the initial value of powered for the game piece
    t.checkExpect(this.p2.powered, true);
    // makes the powered field be true
    this.p1.makeTrue("powered");
    // makes the powered field be true
    this.p2.makeTrue("powered");
    // test that the powered is altered to true when originally false
    t.checkExpect(this.p1.powered, true);
    // test that the powered is altered to true when originally true
    t.checkExpect(this.p2.powered, true);
  }

  // test the makePSFalse method
  void testMakePSFalse(Tester t) {
    this.initData();
    this.p2.powerStation = true;
    // test the initial value of a power station for the game piece
    t.checkExpect(this.p1.powerStation, false);
    // test the initial value of a power station for the game piece
    t.checkExpect(this.p2.powerStation, true);
    // makes the power station be false
    this.p1.makePSFalse();
    // makes the power station be false
    this.p2.makePSFalse();
    // test that the power station is altered to false when originally false
    t.checkExpect(this.p1.powerStation, false);
    // test that the power station is altered to false when originally true
    t.checkExpect(this.p2.powerStation, false);
  }

  // test the makePoweredFalse method
  void testMakePoweredFalse(Tester t) {
    this.initData();
    this.p2.powered = true;
    // test the initial value of powered for the game piece
    t.checkExpect(this.p1.powered, false);
    // test the initial value of powered for the game piece
    t.checkExpect(this.p2.powered, true);
    // makes the powered field be true
    this.p1.makePoweredFalse();
    // makes the powered field be true
    this.p2.makePoweredFalse();
    // test that the powered is altered to true when originally false
    t.checkExpect(this.p1.powered, false);
    // test that the powered is altered to true when originally true
    t.checkExpect(this.p2.powered, false);
  }

  // test the rotate method
  void testRotate(Tester t) {
    this.initData();
    // test the initial pointers of a game piece
    t.checkExpect(this.p1.left, false);
    t.checkExpect(this.p1.right, true);
    t.checkExpect(this.p1.top, false);
    t.checkExpect(this.p1.bottom, true);

    // rotate the game piece
    this.p1.rotate();

    // test that the game piece is rotated counter clockwise
    t.checkExpect(this.p1.left, true);
    t.checkExpect(this.p1.right, false);
    t.checkExpect(this.p1.top, false);
    t.checkExpect(this.p1.bottom, true);

    // rotate the game piece again
    this.p1.rotate();

    // test that the game piece is rotated counter clockwise
    t.checkExpect(this.p1.left, true);
    t.checkExpect(this.p1.right, false);
    t.checkExpect(this.p1.top, true);
    t.checkExpect(this.p1.bottom, false);
  }

  // test the difference method
  void testColDifference(Tester t) {
    this.initData();
    // test the column difference between two pieces
    t.checkExpect(this.p1.difference(this.p3, "col"), 1);
    // test the column difference between two pieces
    t.checkExpect(this.p1.difference(this.p2, "col"), 0);
    // test the column difference between two pieces
    t.checkExpect(this.p1.difference(this.p1, "col"), 0);
    // test the column difference between two pieces
    t.checkExpect(this.p2.difference(this.p3, "col"), 1);
    // test the row difference between two pieces
    t.checkExpect(this.p1.difference(this.p3, "row"), 0);
    // test the row difference between two pieces
    t.checkExpect(this.p1.difference(this.p2, "row"), 1);
    // test the row difference between two pieces
    t.checkExpect(this.p3.difference(this.p4, "row"), 1);
    // test the row difference between two pieces
    t.checkExpect(this.p2.difference(this.p3, "row"), -1);
  }

  // test the addOneDist method
  void testAddOneDist(Tester t) {
    this.initData();
    this.p2.distanceFromStation = 3;
    this.p3.distanceFromStation = -2;
    this.p4.distanceFromStation = 100;
    // test the distance for piece 1 after adding one to the distance from station
    t.checkExpect(this.p1.addOneDist(), 1);
    // test the distance for piece 2 after adding one to the distance from station
    t.checkExpect(this.p2.addOneDist(), 4);
    // test the distance for piece 3 after adding one to the distance from station
    t.checkExpect(this.p3.addOneDist(), -1);
    // test the distance for piece 4 after adding one to the distance from station
    t.checkExpect(this.p4.addOneDist(), 101);
  }

  // test the calculateDistPS method
  void testCalculateDistPS(Tester t) {
    this.initData();
    this.p2.distanceFromStation = 3;
    this.p3.distanceFromStation = -2;
    this.p4.distanceFromStation = 100;
    // test the radius distance for piece 1 after adding one to the distance from station
    t.checkExpect(this.p1.calculateDistPS(), 1);
    // test the radius distance for piece 2 after adding one to the distance from station
    t.checkExpect(this.p2.calculateDistPS(), 2);
    // test the radius distance for piece 3 after adding one to the distance from station
    t.checkExpect(this.p3.calculateDistPS(), 0);
    // test the radius distance for piece 4 after adding one to the distance from station
    t.checkExpect(this.p4.calculateDistPS(), 51);
  }

  // test the updatePowerFrom method
  void testUpdatePowerFrom(Tester t) {
    this.initData();
    this.p1.distanceFromStation = -1;
    // test that the piece is within the radius distance
    t.checkExpect(this.p1.updatePowerFrom(this.p2, 5), true);
    // test that the piece is not within the radius distance
    t.checkExpect(this.p1.updatePowerFrom(this.p3, 1), false);
    // test that the piece is not within the radius distance
    t.checkExpect(this.p1.updatePowerFrom(this.p4, 2), false);
  }

  // test the setPowered method
  void testSetPowered(Tester t) {
    this.initData();
    // test the initial powered state of the game piece
    t.checkExpect(this.p1.powered, false);
    // to set the powered state of the piece to false
    this.p1.setPowered(false);
    // test the state is still false as it is set to false
    t.checkExpect(this.p1.powered, false);
    // to set the powered state of the piece to false
    this.p1.setPowered(true);
    // test the state is true as it is set to true
    t.checkExpect(this.p1.powered, true);
  }

  // test the setDistanceFromPS method
  void testSetDistanceFromPS(Tester t) {
    this.initData();
    // test the initial distance from power station of the game piece
    t.checkExpect(this.p1.distanceFromStation, 0);
    // to set the distance from power station of the game piece to the same distance
    this.p1.setDistanceFromPS(0);
    // test the initial distance from power station of the game piece has no change
    t.checkExpect(this.p1.distanceFromStation, 0);
    // to set the distance from power station of the game piece to a different distance
    this.p1.setDistanceFromPS(100);
    // the initial distance from power station of the game piece had changed
    t.checkExpect(this.p1.distanceFromStation, 100);
  }

  // test the changeInColOrRow method
  void testChangeInColOrRow(Tester t) {
    this.initData();
    // test the change in column for edge 1
    t.checkExpect(this.e1.changeInColOrRow("col"), 1);
    // test the change in column for edge 2
    t.checkExpect(this.e2.changeInColOrRow("col"), 1);
    // test the change in column for edge 3
    t.checkExpect(this.e3.changeInColOrRow("col"), -1);
    // test the change in row for edge 4
    t.checkExpect(this.e4.changeInColOrRow("row"), 0);
    // test the change in row for edge 5
    t.checkExpect(this.e5.changeInColOrRow("row"), 1);
    // test the change in row for edge 6
    t.checkExpect(this.e6.changeInColOrRow("row"), 1);
  }

  // test the getFroOrTomMakeTrue method
  void testGetFromOrToMakeTrue(Tester t) {
    this.initData();
    // test the original pointings of the piece before this edge
    t.checkExpect(this.e1.from.right, true);
    t.checkExpect(this.e1.from.left, false);
    t.checkExpect(this.e1.from.top, false);
    t.checkExpect(this.e1.from.bottom, true);
    // to alter the left pointing to true
    this.e1.getFromOrToMakeTrue("from", "left");
    // to alter the right pointing which should have no change
    this.e1.getFromOrToMakeTrue("from", "right");
    // to alter the top pointing to true
    this.e1.getFromOrToMakeTrue("from", "top");
    // to alter the bottom pointing which should have no change
    this.e1.getFromOrToMakeTrue("from", "bottom");
    // test the new pointings of the piece before this edge
    t.checkExpect(this.e1.from.right, true);
    t.checkExpect(this.e1.from.left, true);
    t.checkExpect(this.e1.from.top, true);
    t.checkExpect(this.e1.from.bottom, true);

    this.initData();
    // test the original pointings of the piece after this edge
    t.checkExpect(this.e1.to.right, false);
    t.checkExpect(this.e1.to.left, true);
    t.checkExpect(this.e1.to.top, false);
    t.checkExpect(this.e1.to.bottom, true);
    // to alter the left pointing to true
    this.e1.getFromOrToMakeTrue("to", "left");
    // to alter the right pointing which should have no change
    this.e1.getFromOrToMakeTrue("to", "right");
    // to alter the top pointing to true
    this.e1.getFromOrToMakeTrue("to", "top");
    // to alter the bottom pointing which should have no change
    this.e1.getFromOrToMakeTrue("to", "bottom");
    // test the new pointings of the piece after this edge
    t.checkExpect(this.e1.to.right, false);
    t.checkExpect(this.e1.to.left, true);
    t.checkExpect(this.e1.to.top, false);
    t.checkExpect(this.e1.to.bottom, true);
  }

  // test the find method
  void testFind(Tester t) {
    this.initData();
    this.hc.parent.put(this.p7, this.p6);
    this.hc.parent.put(this.p6, this.p5);
    this.hc.parent.put(this.p5, this.p5);
    // test to find the root for piece 7
    t.checkExpect(this.hc.find(this.p7), this.p5);
    // test to find the root for piece 76
    t.checkExpect(this.hc.find(this.p6), this.p5);
    // test that the parent of piece 7 is now piece 5
    t.checkExpect(this.hc.parent.get(this.p7), this.p5);
    // test that the parent of piece 6 is now piece 5
    t.checkExpect(this.hc.parent.get(this.p6), this.p5);
  }

  // test the union method
  void testUnion(Tester t) {
    this.initData();
    // test to find the parent of piece 7 originally
    t.checkExpect(this.hc.parent.get(this.p7), this.p7);
    // updates the parent for piece 7 to be the given piece 6
    this.hc.union(this.p6, p7);
    // test to find the parent of piece 7 which should be piece 6
    t.checkExpect(this.hc.parent.get(this.p7), this.p6);
    // updates the parent for piece 5 to be the given piece 5
    this.hc.union(this.p5, p6);
    // test to find the parent of piece 6 which should be piece 5
    t.checkExpect(this.hc.parent.get(this.p6), this.p5);
    // test the root parent of piece 7 has become p5 now
    t.checkExpect(this.hc.find(this.p7), this.p5);
  }
}
