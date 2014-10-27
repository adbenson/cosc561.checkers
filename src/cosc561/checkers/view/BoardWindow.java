package cosc561.checkers.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.Grid;

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
	
	
	private Grid grid = Grid.getInstance();
	
	private JTextArea logArea;
	
	public BoardWindow() throws InvocationTargetException, InterruptedException {
		
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				initialize();
			};
		});
	}

	protected void initialize() {
		window = new JFrame();
		
		content = window.getContentPane();
		content.setLayout(new BorderLayout());
		
		boardPanel = new JPanel();
		boardPanel.setPreferredSize(DIMENSIONS);
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

				graphics.drawGrid(grid);
				graphics.drawBoard(board);
				
				graphics.display();
			};
		});
	}

}

