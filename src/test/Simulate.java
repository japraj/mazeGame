import model.generator.MazeGenerator;
import model.maze.Maze;
import model.path.Path;
import model.solver.AStar;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Simulate {

    private static final int SIZE = 81;
    private Map<Double, Integer> tickCount;

    @Test
    public void findBestC() {
        tickCount = new HashMap<>();
        MazeGenerator generator = new MazeGenerator(SIZE);
        Maze maze;
        AStar solver;
        int ticks;
        for (double i = 0; i <= 1; i += 0.01) {
            tickCount.put(i, 0);
        }

        for (int iteration = 0; iteration < 1000; iteration++) {
            maze = generator.generateMaze();
            for (double i = 0; i <= 1; i += 0.01) {
                solver = new AStar(maze, i);
                ticks = 0;
                for (Path p : solver) {
                    ticks++;
                }
                tickCount.put(i, tickCount.get(i) + ticks);
            }
        }

        double bestC = -1;
        int min = Integer.MAX_VALUE;
        int count;
        for (Double d : tickCount.keySet()) {
            count = tickCount.get(d);
            System.out.println(d + ": " + count);
            if (count < min) {
                bestC = d;
                min = count;
            }
        }
        System.out.println("Best C:\n" + bestC + "tick-count: " + min);
    }

}
