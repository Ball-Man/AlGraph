package algraph;

import javafx.scene.paint.Color;
import queue.Queue;

/**
 * Bellman-Ford algorithm manager. 
 */
class BFM {
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
  // Used to prevent negative cycles
  private int _times[];

  // Solution vector
  private int _path[];

  // Algorithm status
  private boolean _started;

  private boolean _found;

  private boolean _cycle;

  /**
   * First part of the algorithm's cycle: Dequeue a node from the main queue.
   * Returns true if the algorithm isn't over yet.
   */
  private boolean findOut() {
    if (_queue.getEmpty())
      return false;

    if (_out != -1)
      _graph.setColor(_out, Color.GRAY);

    _out = _queue.dequeue();
    _in[_out] = false;
    _adj = _graph.getAdj(_out);
    _graph.setColor(_out, Color.ORANGE);
    return true;
  }

  /**
   * Second part of the algorithm's cycle: Find the next adjacent node to
   * the given one.
   * Returns true if there are still more adjacent nodes which need to be
   * analyzed.
   */
  private boolean findAdj() {
    // If there are no more adjacent nodes to analyze
    if (_adj.getEmpty())
      return false;

    _to = _adj.dequeue();
    _times[_to]++;            // Used for negative cycles discrimination
    _graph.setColor(_to, Color.YELLOW);
    return true;
  }

  /**
   * Third part of the algorithm's cycle: Analyze the adjacent node.
   * This will update the node's distance from the root and eventually
   * enqueue it.
   */
  private void analyzeAdj() {
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

  /**
   * Called when the algorithm is over; Returns the correct
   * enum value according to the case(no path found/shortest path found).
   */
  private Results getPath() {
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
      return Results.NONE;
    }
    else {              // If the shortest path is found
      _graph.setColor(parent, Color.GREEN);
      return Results.OK;
    }
  }

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

    _times = new int[_graph.getNodesLength()];    // All 0
  }

  /**
   * Start the algorithm. Call once.
   */
  public void start(int root, int dest) {
    _started = true;

    _root = root;
    _dest = dest;

    _dist[root] = 0;
    _queue = new Queue<Integer>();
    _queue.enqueue(_root);
    _graph.setColor(_root, Color.YELLOW);
  }

  /**
   * Proceed with the step-by-step algorithm.
   * Returns:
   *  0 if the algorithm is still running.
   *  1 if the shortest path is found.
   *  2 if there's no path between the given nodes.
   *  3 if a negative cycle is found.
   */
  public Results next() {
    if (_found) {
      analyzeAdj();
      _found = false;
      return Results.NEXT;
    }
    else if (!_found && findAdj()) {
      _found = true;
      return Results.NEXT;
    }
    else
      _found = false;

    // Negative cycles
    for (int i : _times)
      if (i > _graph.getNodesLength())
        return Results.CYCLE;

    if (findOut())
      return Results.NEXT;
    
    return getPath();
  }

  /**
   * Returns true if the algorithm has started.
   */
  public boolean getStarted() {
    return _started;
  }

  /**
   * Returns the internal main queue used by the algorithm.
   */
  public Queue<Integer> getQueue() {
    return _queue;
  }

  /**
   * Returns the internal array of distances from the root
   * used by the algorithm.
   */
  public int[] getDistances() {
    return _dist;
  }
}