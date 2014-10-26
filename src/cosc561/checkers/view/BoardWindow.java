package cosc561.checkers.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import cosc561.checkers.model.BoardState;

public class BoardWindow {

	public static final int OUTPUT_WIDTH = 300;	
	public static final Dimension DIMENSIONS = new Dimension(800, 800);
	
	private JFrame window;
	Container content;
	
	JPanel boardPanel;
	JPanel controlPanel;
	JPanel outputPanel;
	
	BoardGraphics graphics;
	
	public BoardWindow() {
		
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
		
		graphics = new BoardGraphics(boardPanel);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	private JPanel createOutputPanel() {
		JPanel outputPanel = new JPanel();
		
		outputPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		outputPanel.setPreferredSize(new Dimension(OUTPUT_WIDTH, 0));
		outputPanel.setLayout(new GridLayout(0, 2));
		
		return outputPanel;
	}

	private JPanel createControlPanel() {
		JPanel controlPanel = new JPanel();
		controlPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		
		return controlPanel;
	}

	public void render(BoardState board) {
		graphics.initDraw();

		graphics.drawBoard(board);
		
		graphics.display();
	}

}


