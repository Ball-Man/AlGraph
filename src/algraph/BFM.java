package algraph;

import javafx.scene.paint.Color;
import queue.Queue;

public class BFM {
  // References graph
  private Graph _graph;

  // Root and destination nodes
  int _root;
  int _dest;

  // Temporary variables
  private int _out;
  private int _to;

  // Dynamic data used by algorithm
  // Queue of nodes
  private Queue<Integer> _queue;
  // Queue of adjacent nodes
  private Queue<Integer> _adj;
  // Boolean vector; true if the node is inside the queue
  // false if not
  private boolean _in[];
  // Distance vector; Contains the distances of the nodes from the
  // Chosen root
  private int _dist[];

  // Solution vector
  private int _path[];

  // Algorithm status
  private boolean _started;

  private boolean _found;

  public BFM(Graph graph) {
    _graph = graph;
    _in = new boolean[_graph.getNodesLength()];    // All false
    _dist = new int[_graph.getNodesLength()];      // All +inf
    for (int i = 0; i < _dist.length; i++)
      _dist[i] = Integer.MAX_VALUE;

    _out = -1;        // Placeholder
    _adj = new Queue<Integer>();
    _path = new int[_graph.getNodesLength()];      // All -1 as placeholder
    for (int i = 0; i < _path.length; i++)
      _path[i] = -1;
  }

  // true if the algorithm is over
  public int next() {
    if (_found) {
      analyzeAdj();
      _found = false;
      return 0;
    }
    else if (!_found && findAdj()) {
      _found = true;
      return 0;
    }
    else
      _found = false;

    if (findOut())
      return 0;
    
    return getPath();
  }

  // Starts the algorithm, call once
  public void start(int root, int dest) {
    _started = true;

    _root = root;
    _dest = dest;

    _dist[root] = 0;
    _queue = new Queue<Integer>();
    _queue.enqueue(root);

    // Make first step
    next();
  }

  // true if successfully completes(which means the algorithm isn't over yet)
  public boolean findOut() {
    if (_queue.getEmpty())
      return false;

    if (_out != -1)
      _graph.setColor(_out, Color.GRAY);

    _out = _queue.dequeue();
    _in [_out] = false;
    _adj = _graph.getAdj(_out);
    _graph.setColor(_out, Color.ORANGE);
    return true;
  }

  // true if still has nodes to analyze
  public boolean findAdj() {
    // If there are no more adjacent nodes to analyze
    if (_adj.getEmpty())
      return false;

    _to = _adj.dequeue();
    _graph.setColor(_to, Color.YELLOW);
    return true;
  }

  // Analyze nodes found by findAdj
  public void analyzeAdj() {
    // Check if a better path is found
    if (_dist[_out] + _graph.getWeight(_out, _to) < _dist[_to]) {
      if (!_in[_to]) {    // If not already enqueued
        _queue.enqueue(_to);
        _in[_to] = true;
      }

      // Update data
      _path[_to] = _out;
      _dist[_to] = _dist[_out] + _graph.getWeight(_out, _to);
    }

    _graph.setColor(_to, Color.GRAY);
  }

  public int getPath() {
    // Back to white
    for (int i = 0; i < _graph.getNodesLength(); i++)
      _graph.setColor(i, Color.WHITE);

    // Green for the shortest path
    int parent = _dest;
    do {
      _graph.setColor(parent, Color.GREEN);
      parent = _path[parent];
    } while (parent != _root && parent != -1);

    if (parent == -1) {  // If there's no path from root to dest
      for (int i = 0; i < _graph.getNodesLength(); i++)   // Back to white
        _graph.setColor(i, Color.WHITE);

      // Red color for root and dest
      _graph.setColor(_root, Color.RED);
      _graph.setColor(_dest, Color.RED);
      return 2;
    }
    else {              // If the shortest path is found
      _graph.setColor(parent, Color.GREEN);
      return 1;
    }
  }

  // Get status
  public boolean getStarted() {
    return _started;
  }
}