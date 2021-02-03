# Maze Simulator

## Proposal

We will operate on square mazes which start at the top left and terminate at the
bottom right. The Maze Simulator will have 3 main features:
- Maze Generation: generate random square mazes of arbitrary size (with some constraints) based on an algorithm from
Wikipedia
- Maze Solving: animate the shortest path or first path solution, starting from the top left corner, or the
user's current position 
- Controllable Character: the user can control a character to traverse and manually solve the maze, with options to
spice up the game. The goal is to complete the maze in the shortest possible time

 ## Questions

- Who will use this application? g*mers
- Why is this project of interest to you? when I was first learning to code, I made a very primitive maze game for
 fun. After studying recursive traversal of mazes in CS110, I was inspired to redo this old project from scratch using
 the new skills I have picked up over the past few years. The maze game was *very* simple (just a hardcoded maze that 
 you could walk around in, with a GUI - see bottom for an image). The scope of this project is larger - I will add a lot of features that I dreamed 
 about when I made the first game :)
 
 ## User Stories
 
 As a user, I want to be able to...
 
 - use MazeGenerator to produce a random Maze of the specified size
 - use an implementation of MazeSolver to see the Path from my current Position to the end of the Maze
 - use Printer to watch an animation of intermediate Paths produced by a MazeSolver
 - add a sequence of Moves to my current Path to traverse the Maze
 
 ## Original Maze Game
 
 ![Image of my original maze game](https://i.imgur.com/9suks2k.png)