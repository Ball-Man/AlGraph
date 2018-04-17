# AlGraph #

A project from the Computer Science's degree course of Bologna. The project should show, thanks to a user-friendly GUI, how a Shortest Path Algorithm works.

## Bellman-Ford-Moore ##

My specific version is focused on the Bellman-Ford-Moore algorithm(1958) which is one of the first Shortest Path Algorithms conceived. In fact the BFM isn't the most efficient, but it still defends itself. Also, the notorious ones which came after, heavily took inspiration from the original BFM(the Dijkstra/Johnson etc. algorithms are in fact some sort of enhancement of the Bellman's).

## Compile and run ##

In order to compile the program, just navigate to the root of the project with any terminal and run:

```shell
# On Linux
make
```

or:
```shell
# On Windows
# (Requires MinGW)
mingw32-make
```

In order to run the program, from the project root execute the following command:
```shell
# Should work on both Linux and Windows
# (Requires java, obv) 
java -jar build/AlGraph.jar
``` 

Double clicking on the .jar file found in the build/ directory should also work(common on Windows systems).
