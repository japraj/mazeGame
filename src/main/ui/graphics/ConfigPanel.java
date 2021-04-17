package ui.graphics;

import model.maze.ImmutableMaze;
import model.maze.Maze;
import model.solver.AStar;
import model.solver.MazeSolver;
import model.solver.backtracker.Backtracker;
import ui.controller.MazeGame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

// A Menu that allows the user to configure various aspects of the simulation
public class ConfigPanel extends JPanel {

    // all values in px
    public static final int WIDTH = 150;

    private MazeGame mazeGame;
    // genPanel
    private JSpinner sizeSpinner;
    //    private JCheckBox animateGeneration;
    private JButton generateMaze;
    private JButton blankMaze;
    // solvePanel
    private JCheckBox animateSolve;
    private JButton solveMaze;
    // algoPanel
    private ButtonGroup algorithm;
    private JRadioButton backtracker;
    private JRadioButton astar;
    // miscPanel
    private JButton save;
    private JButton load;
//    private JCheckBox drawPlayerPath;

    // EFFECTS: initialize/configure this & panels, construct subcomponents and add action listeners
    public ConfigPanel(MazeGame mazeGame, int height, int mazeSize) {
        this.mazeGame = mazeGame;
        setPreferredSize(new Dimension(WIDTH, height));
        setBackground(MazeGame.BACKGROUND);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setFocusable(false);

        add(getGenerationPanel(mazeSize));
        add(Box.createVerticalStrut(15));
        add(getSolverPanel());
        add(Box.createVerticalStrut(15));
        add(getAlgorithmsPanel());
        add(Box.createVerticalStrut(15));
        add(getMiscPanel());

        addActionHandlers();
    }


    // MODIFIES: this
    // EFFECTS: produces the generation panel and initializes associated variables
    private Component getGenerationPanel(int size) {
        JPanel genPanel = makePanel("Generate Maze", 120); // 120 w/ animateGeneration
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;

        c.weighty = 0.5;
        c.weightx = 0.1;

        // Size Spinner
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        sizeSpinner = new JSpinner(new SpinnerNumberModel(size, Maze.MIN_SIZE, Maze.MAX_SIZE, 2));
        addLabelledComponent(genPanel, sizeSpinner, "Size: ", c);
        sizeSpinner.setFocusable(false);
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) sizeSpinner.getEditor();
        editor.getTextField().setFocusable(false);
        editor.getTextField().setEditable(false);

        c.weightx = 0.5;

        // Generation Button
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        generateMaze = new JButton("Generate Maze");
        genPanel.add(generateMaze, c);
        generateMaze.setFocusable(false);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        blankMaze = new JButton("Blank Canvas");
        genPanel.add(blankMaze, c);
        blankMaze.setFocusable(false);


        // AnimateGeneration CheckBox
        c.gridy = 2;
//        animateGeneration = new JCheckBox("Animate");
//        animateGeneration.setBackground(MazeGame.BACKGROUND);
//        animateGeneration.setForeground(MazeGame.TEXT_COLOR);
//        genPanel.add(animateGeneration, c);
        return genPanel;
    }

    // MODIFIES: this
    // EFFECTS: produces the solver panel and initializes associated variables
    private Component getSolverPanel() {
        JPanel solverPanel = makePanel("Solve Maze", 90);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.weighty = 0.5;
        c.weightx = 0.5;

        // Solve Button
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        solveMaze = new JButton("Solve Maze");
        solveMaze.setFocusable(false);
        solverPanel.add(solveMaze, c);

        // Animate Solve Checkbox
        c.gridy = 1;
        animateSolve = new JCheckBox("Animate");
        animateSolve.setBackground(MazeGame.BACKGROUND);
        animateSolve.setForeground(MazeGame.TEXT_COLOR);
        animateSolve.setFocusable(false);
        solverPanel.add(animateSolve, c);

        return solverPanel;
    }

    // MODIFIES: this
    // EFFECTS: produces a JPanel wrapping the SolverPanel button group & JLabel and initializes associated fields
    private Component getButtonGroup() {
        algorithm = new ButtonGroup();

        JPanel container = new JPanel();
        container.setFocusable(false);
        container.setBackground(MazeGame.BACKGROUND);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        backtracker = makeAlgorithmButton(container, "Backtracker", true);
        astar = makeAlgorithmButton(container, "A*", false);

        return container;
    }

    // MODIFIES: this
    // EFFECTS: adds a RadioButton with specified text to container and to the algorithm ButtonGroup, sets its
    //          foreground/background values to go w/ app theme, and sets its selected attribute as specified
    private JRadioButton makeAlgorithmButton(Container c, String text, boolean isSelected) {
        JRadioButton b = new JRadioButton(text);
        b.setSelected(isSelected);
        b.setBackground(MazeGame.BACKGROUND);
        b.setForeground(MazeGame.TEXT_COLOR);
        b.setFocusable(false);
        c.add(b);
        algorithm.add(b);
        return b;
    }

    // MODIFIES: this
    // EFFECTS: produces the algorithms panel and initializes associated variables
    private Component getAlgorithmsPanel() {
        JPanel algoPanel = makePanel("Solving Algorithm", 80);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.weighty = 0.5;
        c.weightx = 0.5;

        // Algorithm RadioButton Group
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        algoPanel.add(getButtonGroup(), c);

        return algoPanel;
    }

    // MODIFIES: this
    // EFFECTS: produces the miscellaneous panel and initializes associated variables
    private Component getMiscPanel() {
        JPanel miscPanel = makePanel("Miscellaneous", 90);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.weighty = 0.5;
        c.weightx = 0.5;

        // Save/load
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        save = new JButton("Save");
        save.setFocusable(false);
        miscPanel.add(save, c);
        load = new JButton("Load");
        load.setFocusable(false);
        c.gridy = 1;
        miscPanel.add(load, c);

        // Draw Player Path
//        c.gridy = 2;
//        drawPlayerPath = new JCheckBox("Draw Path");
//        drawPlayerPath.setBackground(MazeGame.BACKGROUND);
//        drawPlayerPath.setForeground(MazeGame.TEXT_COLOR);
//        drawPlayerPath.setFocusable(false);
//        miscPanel.add(drawPlayerPath, c);

        return miscPanel;
    }

    // EFFECTS: creates a new JPanel with GridBagLayout and a TitledBorder of the specified title, and app-specific
    //          color/font
    private JPanel makePanel(String title, int height) {
        JPanel panel = new JPanel();
        panel.setMaximumSize(new Dimension(WIDTH, height));
        panel.setLayout(new GridBagLayout());
        panel.setBackground(MazeGame.BACKGROUND);
        panel.setFocusable(false);
        panel.setBorder(
                new TitledBorder(new LineBorder(MazeGame.ACCENTS, 1, false),
                        title,
                        TitledBorder.LEADING,
                        TitledBorder.TOP,
                        MazeGame.HEADER,
                        MazeGame.TEXT_COLOR));
        return panel;
    }

    // EFFECTS: produces a JLabel with specified text and a default color/font
    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(MazeGame.TEXT_FONT);
        label.setForeground(MazeGame.TEXT_COLOR);
        label.setFocusable(false);
        return label;
    }

    // REQUIRES: parent must have GridBagLayout
    // MODIFIES: parent
    // EFFECTS: adds a JLabel with specified text on gridx=0, and given component c on gridx=1 (to parent)
    private void addLabelledComponent(Container parent, Component c, String text, GridBagConstraints g) {
        JLabel label = makeLabel(text);
        parent.add(label, g);
        g.gridx = 1;
        parent.add(c, g);
    }

    // REQUIRES: all fields must have been initialized
    // MODIFIES: this
    // EFFECTS: adds action handlers to all interactive GUI elements
    private void addActionHandlers() {
        sizeSpinner.addChangeListener(e -> mazeGame.setSize((int) sizeSpinner.getValue()));

        generateMaze.addActionListener(e -> mazeGame.generateNewMaze(false));

        blankMaze.addActionListener(e -> mazeGame.generateNewMaze(true));

        solveMaze.addActionListener(e -> mazeGame.solve(animateSolve.isSelected()));

        save.addActionListener(e -> mazeGame.save());

        load.addActionListener(e -> {
            try {
                mazeGame.load(true);
            } catch (Exception exception) {
                System.out.println("Failed to load");
            }
        });
    }

    // EFFECTS: return solver of selected type
    public MazeSolver getSelectedSolver(ImmutableMaze maze) {
        return backtracker.isSelected() ? new Backtracker(maze) : new AStar(maze);
    }

    public void setSize(int size) {
        sizeSpinner.setValue(size);
    }

}
