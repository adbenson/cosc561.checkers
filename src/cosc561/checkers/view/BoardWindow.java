package cosc561.checkers.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import cosc561.checkers.Checkers;
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;
import cosc561.checkers.model.PlayerColor;
import cosc561.checkers.model.Space;

public class BoardWindow {

	public static final int OUTPUT_WIDTH = 300;	
	public static final Dimension DIMENSIONS = new Dimension(800, 800);
	
	private JFrame window;
	private Container content;
	
	private JPanel boardPanel;
	private JPanel controlPanel;
	private JPanel outputPanel;
	
	private BoardGraphics graphics;
	
	private ControlHandler controlHandler;
	
	private Checkers game;
	
	private Grid grid = Grid.getInstance();
	
	private JTextArea logArea;
	
	public BoardWindow(Checkers game) throws InvocationTargetException, InterruptedException {
		
		this.game = game;
		
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				initialize();
			};
		});
	}

	protected void initialize() {
		window = new JFrame();
		
		controlHandler = new ControlHandler(this);
		
		content = window.getContentPane();
		content.setLayout(new BorderLayout());
		
		boardPanel = new JPanel();
		boardPanel.setPreferredSize(DIMENSIONS);
		boardPanel.addMouseListener(controlHandler);
		boardPanel.addMouseMotionListener(controlHandler);
		content.add(boardPanel, BorderLayout.CENTER);
		
		controlPanel = createControlPanel();		
		content.add(controlPanel, BorderLayout.SOUTH);

		outputPanel = createOutputPanel();
		content.add(outputPanel, BorderLayout.EAST);

		window.pack();
		window.setVisible(true);
		
		graphics = new BoardGraphics(boardPanel, grid);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	private JPanel createOutputPanel() {
		JPanel outputPanel = new JPanel();
		
		outputPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		outputPanel.setPreferredSize(new Dimension(OUTPUT_WIDTH, 0));
		outputPanel.setLayout(new BorderLayout());
		
		JScrollPane scroll = new JScrollPane(logArea);
		scroll.setPreferredSize(new Dimension(OUTPUT_WIDTH, 0));
		scroll.setBackground(Color.yellow);
		outputPanel.add(scroll, BorderLayout.CENTER);
		
		logArea = new JTextArea();
		logArea.setPreferredSize(new Dimension(OUTPUT_WIDTH, 0));
		logArea.setEditable(false);
		logArea.setWrapStyleWord(true);
		logArea.setLineWrap(true);
		logArea.setBackground(Color.white);
		
		outputPanel.add(logArea);
		
		return outputPanel;
	}

	private JPanel createControlPanel() {
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		return controlPanel;
	}

	private PlayerColor inquireColor() {
		
		int n = JOptionPane.showOptionDialog(new JFrame(),
		    "What color are we playing as today?",
		    "Choose our color",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    PlayerColor.values(),
		    null);
		
		if (n < 0) {
			throw new RuntimeException("No color choice made");
		}
		
		return PlayerColor.values()[n];
	}
	

	public void logAction(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				logArea.append(message);
			};
		});
	}
	
	public void render(final BoardState board) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				graphics.init();

				graphics.drawGrid();
				graphics.drawBoard(board);
				
				graphics.display();
			};
		});
	}
	public void movePiece(Space from, Space to) {
		
	}
	
	public void dragPiece(Space from, Point to) {
	}

	public void hover(Space space) {
		
	}
	
	public void selectPiece(Space space) {
		
	}

	public Space getSpaceAt(Point point) {
		return graphics.getSpaceAt(point);
	}

}


