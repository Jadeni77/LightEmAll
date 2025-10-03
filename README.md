# LightEmAll

The LightEmAll game is an implementation of Java built for the Fundamentals of Computer Science II (Fundies 2) with the given documentation of ImpWorld and the unique tester library. It uses the special big-bang 
syntax to run the program on the local desktop with dependency on mutable states, built-in world classes, and event handlers (mouse events, keyboard events). Additionally, it is designed
with a functional programming style, which does not contain too many object-oriented programming styles.

## 🎮 GamePlay
* Upon running, the game will generate a 900x900 pixel grid
* The goal is to connect all the edges and move the given shape to a certain position to light up all the edges
* The player will be able to use the four navigation keys to move the shape
* The player will also be able to click on the edges to adjust their position
* The game ends if all edges are connected and light up

<img width="799" height="833" alt="Screenshot 2025-10-02 at 11 37 16 PM" src="https://github.com/user-attachments/assets/a142becd-4b67-48bf-a709-7bc893763cb7" />
<img width="796" height="824" alt="Screenshot 2025-10-02 at 11 43 19 PM" src="https://github.com/user-attachments/assets/56158b40-8c5a-41c3-888e-f89e3ffef993" />
<img width="798" height="825" alt="Screenshot 2025-10-02 at 11 51 25 PM" src="https://github.com/user-attachments/assets/d98a2522-9ee6-4cd0-abb8-264fa955c0c3" />


## 🛠️ How it works
* **java.awt.color** - Provides the color value and RGB
* **java.util.*** - Uses the ArrayList, Iterator, and Random to manage collections of edges, and to randomize them upon program execution
* **javalib.impworld** - Provides the global world states, such as **World**, **bigBang**, and **Event Handlers** to exeucte the program as well as handle user interactions
* **javalib.worldimages** - Provides world images for component rendering, such as RectangleImage, TextImage,...

## ⚙️ To run the game locally
* In terminal:
  * javac -cp lib/javalib.jar -d bin src/*.java
  * java -cp lib/javalib.jar:bin App
 
## 👥 Contributors
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/jadeni77" target="_blank">
        <img src="https://github.com/jadeni77.png" width="60px" style="border-radius:50%;" /><br />
        Jaden Mei
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/lxyan636" target="_blank">
        <img src="https://github.com/lxyan636.png" width="60px" style="border-radius:50%;" /><br />
        Dori Lin
      </a>
    </td>
    
  </tr>
</table>
