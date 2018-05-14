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

  public boolean addEdge(int from, int to, int w) {
    // If the one of the given nodes doesn't exist, return false
    if (from >= _graph.getLength() || from < 0 || to >= _graph.getLength() || to < 0)
      return false;

    _graph.at(from).insertNext(new Edge(from, to, w));
    return true;
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
