package cosc561.checkers.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cosc561.checkers.evaluator.CompoundEvaluator;
import cosc561.checkers.evaluator.Evaluator;
import cosc561.checkers.model.BoardState;
import cosc561.checkers.model.PlayerColor;

@SuppressWarnings("serial")
public class EvaluationPanel extends JPanel implements ActionListener {
	
	private PlayerColor evaluationColor = PlayerColor.RED;
	
	private static final String FORMAT_PATTERN = "###,###,###,###.###";
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(FORMAT_PATTERN);
	
	private static final String[] COLUMNS  = {
		"Name", "Base", "Norm", "Weight", "Final"
	};
	
	private List<Evaluator> evaluators;
	
	private JTable table;
	private DefaultTableModel model;
	
	private BoardState lastState;
	
	public EvaluationPanel() {
		super(new BorderLayout());
		
		model = new DefaultTableModel(COLUMNS, 0);

		table = new JTable(model);
		table.setFillsViewportHeight(true);
		
		this.add(table.getTableHeader(), BorderLayout.PAGE_START);
		this.add(table, BorderLayout.CENTER);
		
		JPanel control = new JPanel();
		control.add(new JLabel("Evaluate as "));
		createColorToggle(control);
		
		this.add(control, BorderLayout.SOUTH);
		
		this.evaluators = new CompoundEvaluator().getEvaluators();
	}
	
	private void createColorToggle(JPanel control) {
		ButtonGroup group = new ButtonGroup();
		
		for (PlayerColor color : PlayerColor.values()) {
			JRadioButton button = new JRadioButton(color.name());
						
			if (color == evaluationColor) {
				button.setSelected(true);
			}
			
			button.addActionListener(this);
			button.setActionCommand(color.name());
			group.add(button);
			control.add(button);
		}
	}

	public void showEvaluation(BoardState state) {
		lastState = state;
		
		if (evaluationColor == null || state == null) {
			return;
		}
		
		model.setRowCount(0);
		
		double total = 0;
		
		for (Evaluator eval : evaluators) {
			String name = eval.getClass().getSimpleName();
			double score = eval.evaluate(state, evaluationColor);
			double normal = eval.normalize(score);
			double weight = eval.getWeight(state.getPieces().getPieceCount());
			double finalScore = normal * weight;
			
			model.addRow(new String[] { 
					name, 
					DECIMAL_FORMAT.format(score),
					DECIMAL_FORMAT.format(normal),
					DECIMAL_FORMAT.format(weight),
					DECIMAL_FORMAT.format(finalScore) 
			});
			
			total += finalScore;
		}
		
		model.addRow(new String[] { 
				"TOTAL", "-", "-", "-",
				DECIMAL_FORMAT.format(total)
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		evaluationColor = PlayerColor.valueOf(e.getActionCommand());
		
		showEvaluation(lastState);
	}

}
