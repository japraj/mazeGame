# Maze Simulator

## Proposal

We will operate on square mazes which start at the top left and terminate at the
bottom right. The Maze Simulator will have 3 main features:
- Maze Generation: generate random square mazes of arbitrary size (with some constraints) based on an algorithm from
Wikipedia
- Maze Solving: animate the first path solution, starting from the top left corner, or the
user's current position 
- Controllable Character: the user can control a character to traverse and manually solve the maze.
 The goal is to complete the maze in the shortest possible time

 ## Questions

- Who will use this application? g*mers
- Why is this project of interest to you? when I was first learning to code, I made a very primitive maze game for
 fun. After studying recursive traversal of mazes in CS110, I was inspired to redo this old project from scratch using
 the new skills I have picked up over the past few years. The maze game was very simple (just a hardcoded maze that 
 you could walk around in, with a GUI - see bottom for an image). The scope of this project is larger - I will add a lot of features that I dreamed 
 about when I made the first game :)
 
 ## User Stories
 
 As a user, I want to be able to...
 
 - use MazeGenerator to produce a random Maze of the specified size
 - use Canvas and an implementation of MazeSolver to see the solution Path for the Maze
 - use Canvas to watch an animation of intermediate Paths produced by a MazeSolver
 - add a sequence of Moves to my current Path to traverse the Maze using the keyboard
 - have the current Maze and my current Player implicitly saved when I quit the application
 - have my previous Maze and Player automatically loaded (if possible) when I launch the application
 - have a random maze produced when I launch the app if there is no previous state to load
 - use save and load buttons to access data persistence features
 - see an image when I finish the maze
 
 Phase 4 Task 2: I refactored the Path class to be robust; see the generateBranches method for a checked exception (the 
 other REQUIRES clauses were replaced with runtime exceptions and I eliminated the need for some requires clauses by 
 adding functionality that prevents invalid state in other methods)
 
 Phase 4 Task 3: If I were to refactor this project, I would separate Path into two classes because currently it is not
  very cohesive; some features of Path are only used by MazeSolver, so I would create a subclass SolverPath that extends
  Path. In addition, I would try to reduce coupling by removing the number of dependencies to ImmutableMaze so
 we only need to keep track of a single ref in MazeGame. Finally, I would refactor MoveableEntity and Path to eliminate
 unnecessary fields (MoveableEntity.position and Path.tail).
 
 ## Original Maze Game
 
 ![Image of my original maze game](https://i.imgur.com/9suks2k.png)