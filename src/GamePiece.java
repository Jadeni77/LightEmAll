import java.awt.*;

import javalib.worldimages.AlignModeX;
import javalib.worldimages.AlignModeY;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.OverlayImage;
import javalib.worldimages.OverlayOffsetAlign;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.StarImage;
import javalib.worldimages.WorldImage;

// represents a game piece
public class GamePiece {
  // in logical coordinates, with the origin
  // at the top-left corner of the screen
  protected int row;
  protected int col;
  // whether this GamePiece is connected to the
  // adjacent left, right, top, or bottom pieces
  protected boolean left;
  protected boolean right;
  protected boolean top;
  protected boolean bottom;
  // whether the power station is on this piece
  protected  boolean powerStation;
  protected boolean powered;
  protected int distanceFromStation;

  public GamePiece(int row, int col, boolean left, boolean right, boolean top, boolean bottom) {
    this.row = row;
    this.col = col;
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;
    this.powerStation = false;
    this.powered = false;
    this.distanceFromStation = 0;
  }

  // Constructor for testing
  public GamePiece(int row, int col, boolean left, boolean right, boolean top, boolean bottom,
                 boolean ps, boolean p, int d) {
    this.row = row;
    this.col = col;
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;
    this.powerStation = ps;
    this.powered = p;
    this.distanceFromStation = d;
  }

  // makes the game piece tile have a field set to true
  // EFFECT: alters the piece to have a true field depending on the input
  public void makeTrue(String str) {
    if (str.equals("right")) {
      this.right = true;
    } else if (str.equals("left")) {
      this.left = true;
    } else if (str.equals("top")) {
      this.top = true;
    } else if (str.equals("bottom")) {
      this.bottom = true;
    } else if (str.equals("ps")) {
      this.powerStation = true;
    } else if (str.equals("powered")) {
      this.powered = true;
    }
  }

  // makes the game piece tile's power station be not activated
  // EFFECT: alters the power station to not be on the piece
  public void makePSFalse() {
    this.powerStation = false;
  }

  // makes the game piece be not powered
  // EFFECT: alters the piece to not be powered
  public void makePoweredFalse() {
    this.powered = false;
  }

  //rotates the game piece counterclockwise
  // EFFECT: rotates the pointing of the game piece direction
  public void rotate() {
    boolean tempe = this.left;
    this.left = this.bottom;
    this.bottom = this.right;
    this.right = this.top;
    this.top = tempe;
  }

  // determines the difference in column or row location
  public int difference(GamePiece other, String str) {
    int diff = 0;
    if (str.equals("col")) {
      diff = other.col - this.col;
    } else if (str.equals("row")) {
      diff = other.row - this.row;
    }
    return diff;
  }

  // calculates the updated distance after adding one
  public int addOneDist() {
    return this.distanceFromStation + 1;
  }

  // calculates the radius distance from the power station and adding one
  public int calculateDistPS() {
    return (this.distanceFromStation / 2) + 1;
  }

  // whether or not the game piece is within the radius distance
  // EFFECT: changes the distance from station by 1 and powers or unpowers the piece
  // depending on its distance and difference from the radius
  public boolean updatePowerFrom(GamePiece current, int radius) {
    if (this.distanceFromStation == -1) {
      this.distanceFromStation = current.distanceFromStation + 1;
      this.powered = this.distanceFromStation <= radius;
      return true;
    }
    return false;
  }

  // alters the game piece to check if it is powered depending on the boolean case given
  // EFFECT: changes the state of powered to be of the given boolean statement
  public void setPowered(boolean b) {
    this.powered = b;
  }

  // alters the distance of the game piece from the power station
  // EFFECT: changes the distance from the power station based on the given distance
  public void setDistanceFromPS(int i) {
    this.distanceFromStation = i;
  }

  //Generate an image of this, the given GamePiece.
  // - size: the size of the tile, in pixels
  // - wireWidth: the width of wires, in pixels
  // - wireColor: the Color to use for rendering wires on this
  // - hasPowerStation: if true, draws a fancy star on this tile to represent the power station
  public WorldImage tileImage(int size, int wireWidth, Color wireColor, boolean hasPowerStation) {
    // Start tile image off as a blue square with a wire-width square in the middle,
    // to make image "cleaner" (will look strange if tile has no wire, but that can't be)
    WorldImage image = new OverlayImage(
            new RectangleImage(wireWidth, wireWidth, OutlineMode.SOLID, wireColor),
            new RectangleImage(size, size, OutlineMode.SOLID, Color.DARK_GRAY));
    WorldImage vWire = new RectangleImage(wireWidth, (size + 1) / 2, OutlineMode.SOLID, wireColor);
    WorldImage hWire = new RectangleImage((size + 1) / 2, wireWidth, OutlineMode.SOLID, wireColor);

    if (this.top) {
      image = new OverlayOffsetAlign(AlignModeX.CENTER,
              AlignModeY.TOP, vWire, 0, 0, image);
    }
    if (this.right) {
      image = new OverlayOffsetAlign(AlignModeX.RIGHT,
              AlignModeY.MIDDLE, hWire, 0, 0, image);
    }
    if (this.bottom) {
      image = new OverlayOffsetAlign(AlignModeX.CENTER,
              AlignModeY.BOTTOM, vWire, 0, 0, image);
    }
    if (this.left) {
      image = new OverlayOffsetAlign(AlignModeX.LEFT,
              AlignModeY.MIDDLE, hWire, 0, 0, image);
    }
    if (hasPowerStation) {
      image = new OverlayImage(
              new OverlayImage(
                      new StarImage(size / 3, 7, OutlineMode.OUTLINE, new Color(255, 128, 0)),
                      new StarImage(size / 3, 7, OutlineMode.SOLID, new Color(0, 255, 255))),
              image);
    }
    return image;
  }
}