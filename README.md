# Maze Simulator

![Image of Maze Game](https://i.imgur.com/NWIM7C0.png)

![Image of maze game](https://i.imgur.com/320vQJT.png)

# Video Demo

<a href="https://www.dropbox.com/s/k8k4n2dgoh0zqpm/2021-04-16%2020-48-23.mp4?dl=0">Click here to see a video demo of 
the main features</a>

## Features

We operate on square mazes which start at the top left and terminate at the bottom right. The simulator has 4 main
 features:
- Maze Generation: generate random square mazes of arbitrary size (with some constraints) based on an algorithm from
Wikipedia
- Maze Solving: animate the shortest path or first path solution (using a backtracking search algo or A* search),
 starting from the top left corner
- Editable Canvas: can remove walls with right-click and place them with left-click
- Controllable Character: the user can control a character to traverse and manually solve the maze. The goal is to
 complete the maze in the shortest possible time
- Data Persistence: the user can save and load their data (stored in JSON)

## Motivation

When I was first learning to code, I made a very primitive maze game for fun. After studying recursive traversal of 
graphs in an intro CS class, I was inspired to redo this old project from scratch using the new skills I have picked up
over the past few years. The maze game was *very* simple (just a hardcoded maze that you could walk around in, with a
GUI). The scope of this project is larger - I have added a lot of features that I dreamed 
about when I made the first game and this time I have unit tests :)
 
 
 ## Original Maze Game
 
 ![Image of my original maze game](https://i.imgur.com/9suks2k.png)