# quad-tree-puzzle-game

This is my submission for the Data Structures Assignment as part of the Winter Semester at McGill University. The assignment focuses on working with hierarchical data using trees and implementing recursive methods to manipulate the recursive structure. The main objective is to create a visual game where players apply operations such as rotations to a quad-tree to achieve specific goals.

Game Description:
The Quad-Tree Puzzle Game is an exciting and challenging program that demonstrates the application of data structures and recursion in an interactive game. In this game, players will be presented with a randomly-generated game board resembling a Mondrian painting. The board consists of squares of four different colors.

Goal Assignment:
At the start of the game, each player is randomly assigned one of two goals to achieve:

1. Create the Largest Connected "Blob": Players must work towards forming the largest connected area of a specific color on the game board.
2. Maximize Color on Outer Perimeter: Players must aim to place as much of a given color as possible on the outer perimeter of the game board.

Moves:
Players can perform three types of moves during the game:

1. Rotate Block: Players can rotate a block of the quad-tree either clockwise or counterclockwise.
2. Reflect Block: Blocks can be reflected horizontally or vertically (along the x-axis or y-axis, respectively, considering the center of the block as the origin).
3. Smash Block: Players have the option to "smash" a block, which divides it into four brand-new, randomly generated sub-blocks.

Scoring:
After each move, the player's score is determined based on how well they have achieved their assigned goal.

Key Learning Objectives:

- Implementing a quad-tree data structure to represent the game board and its hierarchical nature.
- Developing recursive methods to manipulate the quad-tree efficiently.
- Converting the quad-tree into a flat, two-dimensional structure for visualization and interaction.
- Enhancing understanding and comfort with inheritance in Java for building the game mechanics.
