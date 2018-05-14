package algraph;

import java.util.concurrent.ThreadLocalRandom;
import vector.Vector;
import list.Listable;
import javafx.scene.layout.*;

public class Graph {
  // Adjacency list
  private Vector<Node> _graph;

  // Layouts
  private static Pane _edgesCanvas;
  private static Pane _nodesCanvas;

  public Graph() {
    _graph = new Vector<Node>();
  }

  public boolean randomize(int nodes, int edges, int wStart, int wEnd) {
    // If nodes or edges are negative, or edges are more than the maximum amount
    // return false
    if (nodes < 0 || edges < 0 || edges > nodes * (nodes - 1))
      return false;

    _graph = new Vector<Node>();
    for (int i = 0; i < nodes; i++)
      addNode();

    // Compute all the possible edges
    Vector<Vector<Integer> > adj = new Vector<Vector<Integer> >(nodes);
    for (int i = 0; i < adj.getLength(); i++)
      adj.setAt(i, new Vector<Integer>(nodes - 1));

    for (int i = 0; i < adj.getLength(); i++) {
      int to = 0;
      for (int j = 0; j < adj.at(i).getLength(); j++) {
        if (to == i)
          to++;
        adj.at(i).setAt(j, to);
        to++;
      }
    }

    // Choose randomly between all the possible edges, removing them so that
    // the same one is never chosen twice
    for (int i = 0; i < edges; i++) {
      int node = ThreadLocalRandom.current().nextInt(0, adj.getLength());
      int edge = ThreadLocalRandom.current().nextInt(0, adj.at(node).getLength());

      // Create edge according to the given data
      addEdge(node, adj.at(node).at(edge), wStart, wEnd);
      System.out.println("Was here!");

      // Remove the chosen data so that it can't be chosen again
      adj.at(node).remove(edge);
      if (adj.at(node).getLength() == 0)  // If no edges remain for a specific node
        adj.remove(node);                 // remove the node
      System.out.println("And here!");
    }

    System.out.println("Yay");

    return true;
  }

  // Dynamic management
  public void addNode() {
    Node newNode = new Node(_graph.getLength(), this);
    _graph.push(newNode);

    // Random positioning
    int randX = ThreadLocalRandom.current().nextInt(0, (int)_nodesCanvas.getBoundsInParent().getWidth());
    int randY = ThreadLocalRandom.current().nextInt(0, (int)_nodesCanvas.getBoundsInParent().getHeight());
    newNode.setPosition(randX, randY);
  }

  public boolean removeNode(int id) {
    // If the given node doesn't exist, return false
    if (id >= _graph.getLength() || id < 0)
      return false;

    // Remove the given node and edges from the GUI and from the graph
    _graph.at(id).remove();
    _graph.remove(id);

    // Remove edges going towards the removed node(from the GUI and the graph)
    for (Node node : _graph) {
      Listable head = node;
      while (!head.getFinished()) {
        if (((Edge)head.getNext()).getTo() == id) {
          ((Edge)head.getNext()).remove();
          head.removeNext();
        }

        // I'm checking since I could have removed from the list
        // the last node, so that the next would be null
        if (!head.getFinished())
          head = head.getNext();
      }
    }

    // Renaming all the nodes and edges since one has been removed
    for (Node node : _graph) {
      // Rename nodes
      if (node.getID() > id)
        node.setID(node.getID() - 1);

      // Rename edges
      Listable head = node;
      while (!head.getFinished()) {
        if (((Edge)head.getNext()).getFrom() > id)
          ((Edge)head.getNext()).setFrom(((Edge)head.getNext()).getFrom() - 1);
        if (((Edge)head.getNext()).getTo() > id)
          ((Edge)head.getNext()).setTo(((Edge)head.getNext()).getTo() - 1);

        head = head.getNext();
      }
    }

    return true;
  }

  // Creates an edge with random weight from given range
  public boolean addEdge(int from, int to, int weightStart, int weightEnd) {
    // If the one of the given nodes doesn't exist or the node already exists,
    // return false
    if (from >= _graph.getLength() || from < 0 || to >= _graph.getLength() || to < 0
      || from == to || edgeExists(from, to))
      return false;

    // If random is set to true, choose randomly the weight based on the given range
    // (Inclusive range)
    int randWeight = ThreadLocalRandom.current().nextInt(weightStart, weightEnd + 1);

    _graph.at(from).insertNext(new Edge(from, to, randWeight));
    return true;
  }

  // Simplyfied overload; Creates an edge with the given weight
  public boolean addEdge(int from, int to, int w) {
    return addEdge(from, to, w, w);
  }

  // Update edges going towards a specific node
  public void updateEdgesTo(int to, int x, int y) {
    for (Node node : _graph) {
      Listable head = node;
      while (!head.getFinished()) {
        if (((Edge)head.getNext()).getTo() == to)
          ((Edge)head.getNext()).setLineEnd(x, y);
        head = head.getNext();
      }
    }
  }

  // Check if an edge already exists
  public boolean edgeExists(int from, int to) {
    Listable head = _graph.at(from);
    while (!head.getFinished()) {
      if (((Edge)head.getNext()).getTo() == to)
        return true;
      head = head.getNext();
    }

    return false;
  }

  // Update all the edges
  public void updateEdges() {
    for (Node node : _graph) {
      node.updateEdges();   // Update edges from the specific node
      updateEdgesTo(node.getID(), node.getX(), node.getY());
    }
  }

  // Layout management
  public static void setEdgesLayout(Pane canvas) {
    _edgesCanvas = canvas;
    Edge.setLayout(canvas);
  }

  public static void setNodesLayout(Pane canvas) {
    _nodesCanvas = canvas;
    Node.setLayout(canvas);
  }
}
