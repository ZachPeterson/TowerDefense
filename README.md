# TowerDefense
A simple tower defense game made entirely in stock Java (no special libraries)

This game was made for a homework assignment in my entry level CS course. The idea was to use plain Java to show off some basic OO concepts as well as various other entry level Java concepts (such as generic types, queue data structure, etc). While it doesn't include sound, I threw in a few things that I thought were pretty cool such as fading the lasers out, animating the monsters, bosses and self-upgrading towers. Processing of the control panel and game are done in separate threads and communicate between eachother using a message queue. Towers are not placeable on the path, get a little bit stronger with each kill, and their lasers fade away after being fired. Enemies spawn in waves with some bosses in there as well. The level system is also extensible.

# Playing TowerDefense
TowerDefense comes prebuilt in the repository and ready to play. In order to play it, launch it by double-clicking TowerDefense.jar, or manually from the command line in the 'Current Build' directory with:
```
java -jar TowerDefense.jar
```
If you would like to compile it for yourself, you can simply use the ```javac``` command to compile the source code files and run it.

# The Level System
The levels are decently extensible, but this was done in my first CS class and a bit before I learned some proper game design principles, so it's not the best thought out. Levels are defined in the level.cfg file. Each level has a background image, path image, and a file containing the level's path nodes. The one provided is as follows:
```
<0>
backgroundImage menuBackground.png
</0>
<1>
backgroundImage background1.png
pathImage path1.png
pathNodeListFile lvl1PNodes.nodes
</1>
<2>
backgroundImage background2.png
pathImage path2.png
pathNodeListFile lvl2PNodes.nodes
</2>
```
The menu is a special level defined as 0 and only has a background image.

The path node files define the path nodes for the enemies to travel to. It is simple a list of points in the format: x,y. The first node is the node enemies will be spawned on and the last node is the node at which enemies will be considered dead and action will be taken accordingly. For example, the node file for the first level is simply:
```
620,281
400,281
400,100
101,100
101,281
-100,281
```
