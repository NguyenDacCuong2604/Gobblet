package AI;

import java.awt.GridBagConstraints; 
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

//  The interface for the game
public class GameFrame extends JFrame {
	private BoardPanel board;
	private JButton startButton;
	private JTextField msgTextField;
	private JButton settingButton;
	private DefaultMutableTreeNode root;
	private JTree gameTree;
	private JScrollPane scrollPane;
	private JPanel settingPanel;
	private JButton minimaxButton, vsButton, alphabetaButton;
	public GameFrame() {
		// other
		setTitle("gobblet chess");
		setSize(780, 450);
		setResizable(false);
		setLocationRelativeTo(null); // center
		
		//panel setting player
		minimaxButton = new JButton("Ai-Minimax");
		alphabetaButton = new JButton("Ai-Alphabeta");
		vsButton = new JButton("Human");
		settingPanel = new JPanel();
		settingPanel.setLayout(new GridLayout(1, 3));
		settingPanel.add(minimaxButton);
		settingPanel.add(alphabetaButton);
		settingPanel.add(vsButton);
		this.getContentPane().add(settingPanel);
		
		msgTextField = new JTextField();
		msgTextField.setEditable(false);
		
		minimaxButton.addActionListener(new ActionListener() {
			@Override
				public void actionPerformed(ActionEvent e) {
					root = new DefaultMutableTreeNode("max-min tree");
					settingPanel.setVisible(false);
					settingPanel(1);
			}
		});
		alphabetaButton.addActionListener(new ActionListener() {
			@Override
				public void actionPerformed(ActionEvent e) {
					root = new DefaultMutableTreeNode("alpha-beta tree");
					settingPanel.setVisible(false);
					settingPanel(2);
				}
			});
		vsButton.addActionListener(new ActionListener() {
			@Override
				public void actionPerformed(ActionEvent e) {
					root = new DefaultMutableTreeNode("human-human");
					settingPanel.setVisible(false);
					settingPanel(3);
				}
			});
		
		
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);		
	}
	
	public void settingPanel(int i) {
		board = new BoardPanel(this, root, i);

		gameTree = new JTree(root);

		startButton = new JButton("play again");
		startButton.setEnabled(false);
		settingButton = new JButton("setting player");

		// the layout for the interface 
		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		c.insets = new Insets(5, 5, 5, 5);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		add(board, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(msgTextField, c);

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		add(startButton, c);
		
		c.gridx = 2; 
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.NONE;
		add(settingButton, c);

		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0.3;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		scrollPane = new JScrollPane(gameTree,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPane, c);

		//when you click the bottom the game start again 
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				restart();
			}

		});
		updateMessage("please, black player to move");
		settingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new GameFrame();
				reload();
			}

		});
		
	}
	public void reload() {
		this.dispose();
	}
//	  refresh the new below the user interface
	public void updateMessage(String message) {
		msgTextField.setText(message);
	}

//	  stop using" player again button 
	public void disableStartButton() {
		startButton.setEnabled(false);
	}

//	  start using  player again button
	public void enableStartButton() {
		startButton.setEnabled(true);
	}

//	  start again
	public void restart() {
		board.restart();
		disableStartButton();
		DefaultTreeModel model = (DefaultTreeModel) gameTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		root.removeAllChildren();
		model.reload(root);
	}

//	  refresh the tree
	public void refreshTree() {
		DefaultTreeModel model = (DefaultTreeModel) gameTree.getModel();
		model.reload();
	}

//	  program start from here
	public static void main(String[] args) {
		JFrame frame = new GameFrame();
	}

}
