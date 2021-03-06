package algraph;

import java.util.concurrent.ThreadLocalRandom;
import vector.Vector;
import javafx.scene.paint.Color;
import list.Listable;
import javafx.scene.layout.*;
import queue.Queue;

/**
 * Graph class containing a vector of Nodes.
 * Can be serialized.
 */
class Graph implements java.io.Serializable {
  // Adjacency list
  private Vector<Node> _graph;

  // Layout
  private int _wLimit;
  private int _hLimit;

  public Graph(int w, int h) {
    _graph = new Vector<Node>();

    _wLimit = w;
    _hLimit = h;
  }

  /**
   * Remove all the nodes and edges from the graph.
   */
  public void clear() {
    for (Node node : _graph)
      node.remove();
    _graph = new Vector<Node>();
  }

  public int getNodesLength() {
    return _graph.getLength();
  }

  /**
   * Create the graph randomly from the given data.
   */
  public boolean randomize(int nodes, int edges, int wStart, int wEnd) {
    // If nodes or edges are negative, or edges are more than the maximum amount
    // return false
    if (nodes < 0 || edges < 0 || edges > nodes * (nodes - 1))
      return false;

    clear();
    for (int i = 0; i < nodes; i++)
      addNode();

    // adj contains all the possible existing direct edges
    Vector<Vector<Integer> > adj = new Vector<Vector<Integer> >(nodes);

    // contains all the nodes that still can have edges coming from them
    Vector<Integer> availableNodes = new Vector<Integer>(nodes);
    for (int i = 0; i < availableNodes.getLength(); i++)
      availableNodes.setAt(i, i);

    // Compute all the possible edges
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
      int node = ThreadLocalRandom.current().nextInt(0, availableNodes.getLength());
      int edge = ThreadLocalRandom.current().nextInt(0, adj.at(availableNodes.at(node)).getLength());

      // Create edge according to the given data
      addEdge(availableNodes.at(node), adj.at(availableNodes.at(node)).at(edge), wStart, wEnd);

      // Remove the chosen data so that it can't be chosen again
      adj.at(availableNodes.at(node)).remove(edge);
      if (adj.at(availableNodes.at(node)).getLength() == 0)  // If no edges remain for a specific node
        availableNodes.remove(node);                         // remove the node
    }

    return true;
  }

  // Data getters
  /**
   * Get nodes adjacent to a given one.
   */
  public Queue<Integer> getAdj(int node) {
    Queue<Integer> adj = new Queue<Integer>();

    Listable head = _graph.at(node);
    while (!head.getFinished()) {
      adj.enqueue(((Edge)head.getNext()).getTo());
      head = head.getNext();
    }

    return adj;
  }

  /**
   * Get weight of a specific edge.
   * This assumes the given edge exists.
   */
  public int getWeight(int from, int to) {
    Listable head = _graph.at(from);
    while (!head.getFinished()) {
      if (((Edge)head.getNext()).getTo() == to)
        return ((Edge)head.getNext()).getWeight();
      head = head.getNext();
    }

    // When used by the algorithm, this should never happen
    return Integer.MIN_VALUE;
  }

  // Dynamic management
  /**
   * Add a new node.
   */
  public void addNode() {
    Node newNode = new Node(_graph.getLength(), this);
    _graph.push(newNode);

    // Random positioning
    int randX = ThreadLocalRandom.current().nextInt(0, _wLimit);
    int randY = ThreadLocalRandom.current().nextInt(0, _hLimit);
    newNode.setPosition(randX, randY);
  }

  /**
   * Remove the given node if exists.
   * If it doesn't it returns false.
   * This also removes all the edges starting from
   * / ending on the removed node.
   */
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

  /**
   * Creates an edge with a random weight(from the given range) if possible.
   * if one of the given nodes(starting/ending) doesn't exist or the specific
   * edge already exists, returns false.
   */
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

  /**
   * Creates an edge with the given weight if possible.
   * if one of the given nodes(starting/ending) doesn't exist or the specific
   * edge already exists, returns false.
   */
  public boolean addEdge(int from, int to, int w) {
    return addEdge(from, to, w, w);
  }

  // Remove an edge if exists
  public boolean removeEdge(int from, int to) {
    // If one of the given nodes doesn't exist, return false
    if (from < 0 || to < 0 || from >= _graph.getLength() || to >= _graph.getLength())
      return false;

    Listable head = _graph.at(from);
    while (!head.getFinished()) {
      if (((Edge)head.getNext()).getTo() == to) {
        ((Edge)head.getNext()).remove();
        head.removeNext();
        return true;
      }
      head = head.getNext();
    }

    // If the edge isn't found, return false
    return false;
  }

  /**
   * Edit an edge's weight if possible. If not, return false.
   */
  public boolean editEdge(int from, int to, int newWeight) {
    if (removeEdge(from, to))
      return addEdge(from, to, newWeight);

    return false;
  }

  /**
   * Update line positioning for edges ending in the given node.
   */
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

  /**
   * Returns true if the given edge already exists.
   */
  public boolean edgeExists(int from, int to) {
    Listable head = _graph.at(from);
    while (!head.getFinished()) {
      if (((Edge)head.getNext()).getTo() == to)
        return true;
      head = head.getNext();
    }

    return false;
  }

  /**
   * Update line positioning for all the existing edges.
   */
  public void updateEdges() {
    for (Node node : _graph) {
      node.updateEdges();   // Update edges from the specific node
      updateEdgesTo(node.getID(), node.getX(), node.getY());
    }
  }

  /**
   * Generate GUI from the internal data.
   * Suitable for serialization.
   */
  public void generateGUI() {
    for (Node node : _graph)
      node.generateGUI();
  }

  /**
   * Set fill color of the given node(circle).
   */
  public void setColor(int node, Color color) {
    _graph.at(node).setColor(color);
  }
}
