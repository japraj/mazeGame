package model.solver;

import model.maze.ImmutableMaze;
import model.moveable.Move;
import model.path.Path;
import model.path.PathNode;
import model.path.Position;

import java.util.*;

// An implementation of the A* search algorithm; pseudocode: https://en.wikipedia.org/wiki/A*_search_algorithm
public final class AStar extends MazeSolver {

    // goal weight is the sum of the x and y of the goal node
    private int goalWeight;
    private PriorityQueue<PathNode> openSet;
    private Map<PathNode, PathNode> cameFrom;
    private Map<PathNode, Integer> gScore;
    private Map<PathNode, Integer> fScore;
    // added and removed keep track of PathNodes that were added to/removed from openSet in the most recent tick
    // note that we only remove one PathNode each tick
    private LinkedList<Position> added;
    private PathNode removed;

    public AStar(ImmutableMaze maze) {
        super(maze);
        goalWeight = (2 * maze.getSize()) - 4;
    }

    // a guess of how short a path would be if it contained p; uses Manhattan distance
    // EFFECTS: produces distance between p and goal (bottom right corner of maze) using Manhattan distance
    private int heuristic(Position p) {
        // since the goal is the bottom right corner, we know that no node could have a greater x + y than the goal so
        // no need for abs value
        return goalWeight - (p.getPosX() + p.getPosY());
    }

    // Choose the node in openSet that has the lowest f-score
    private PathNode getBestCandidate() {
        PathNode best = null;
        int bestFScore = Integer.MAX_VALUE;
        for (PathNode p : openSet) {
            if (fScore.getOrDefault(p, Integer.MAX_VALUE) < bestFScore) {
                best = p;
                bestFScore = fScore.get(p);
            }
        }
        return best;
    }

    // Produce a path from origin to p using cameFrom
    private Path reconstructPath(PathNode current) {
        LinkedList<PathNode> nodes = new LinkedList<>();
        nodes.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            nodes.addFirst(current);
        }

        path = new Path();
        for (PathNode p : nodes) {
            if (p.getDirection() != null) {
                path.addNode(p.getPosX(), p.getPosY());
            }
        }
        return path;
    }

    private boolean isGoal(Position p) {
        return heuristic(p) == 0;
    }

    private List<PathNode> getNeighbours(PathNode p) {
        List<PathNode> neighbours = new ArrayList<>(2);
        for (Move move : MOVES) {
            PathNode neighbour = p.applyMove(move);
            if (maze.isPath(neighbour)
                    && (p.getDirection() == null || !neighbour.applyMove(p.getDirection()).equals(p)))
                neighbours.add(neighbour);
        }
        return neighbours;
    }

    public List<Position> getAdded() {
        return added;
    }

    public PathNode getRemoved() {
        return removed;
    }

    @Override
    protected void init() {
        path = null;
        PathNode origin = new PathNode(1, 1, null);

        openSet = new PriorityQueue<>(maze.getSize() * maze.getSize(), new Comparator<PathNode>() {
            @Override
            public int compare(PathNode p1, PathNode p2) {
                int compare = fScore.getOrDefault(p1, Integer.MAX_VALUE) - fScore.getOrDefault(p2, Integer.MAX_VALUE);
                return compare != 0 ? compare : heuristic(p1) - heuristic(p2);
            }
        });
        openSet.add(origin);

        cameFrom = new HashMap<>();

        gScore = new HashMap<>();
        gScore.put(origin, 0);

        fScore = new HashMap<>();
        fScore.put(origin, heuristic(origin));

        added = new LinkedList<>();
        removed = null;
    }

    @Override
    protected boolean isSolved() {
        return path != null || openSet.isEmpty();
    }

    // main loop of algorithm
    // EFFECTS: produces null if not yet finished, Path if it is finished
    @Override
    protected Path tick() {
        PathNode current = openSet.poll();

        if (isGoal(current))
            return reconstructPath(current);

        removed = current;
        added.clear();
        openSet.remove(current);
        for (PathNode neighbour : getNeighbours(current)) {
            int tentativeGScore = gScore.get(current) + 1; // distance travelled is always 1
            if (tentativeGScore < gScore.getOrDefault(neighbour, Integer.MAX_VALUE)) {
                cameFrom.put(neighbour, current);
                gScore.put(neighbour, tentativeGScore);
                fScore.put(neighbour, tentativeGScore + heuristic(neighbour));
                if (!openSet.contains(neighbour)) {
                    openSet.add(neighbour);
                    added.add(neighbour);
                }
            }
        }

        return null;
    }

}
