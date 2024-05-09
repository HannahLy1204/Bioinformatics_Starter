package _GUIProcessing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainPanel extends JPanel{
	private JPanel currentPanel;
	//Panel 1
	public JButton pane1Button;
	public JLabel pane1Label;
	//Panel 2
	public JButton pane2Button;
	public JLabel pane2Label;
	//Panel 3
	public JTextField inputField1;
	public JTextField inputField2; 
	public JTextField inputField3;
	public JButton pane3Button;
	public JButton exportButton;
	
	public MainPanel() {
		setLayout(new CardLayout());

		JPanel panel1 = drawPanel1();
		JPanel panel2 = drawPanel2();
		JPanel panel3 = drawPanel3();
		
		add(panel1, "panel1");
		add(panel2, "panel2");
		add(panel3, "panel3");
		
		//Initial panel
		currentPanel = panel1;
	}

	private JPanel drawPanel1() {
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		pane1Button = new JButton("Load Input");
		pane1Label = new JLabel("Team Information");

		panel1.add(pane1Button);
		panel1.add(pane1Label);
		
		pane1Button.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane1Button.setHorizontalAlignment(SwingConstants.CENTER);
		pane1Label.setHorizontalAlignment(SwingConstants.CENTER);
		return panel1;
	}
	
	private JPanel drawPanel2() {
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		pane2Button = new JButton("Load SNP");
		pane2Label = new JLabel("SNP Alignment");
		
		panel2.add(pane2Button);
		panel2.add(pane2Label);

		pane2Button.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
		return panel2;
	}
	
	private JPanel drawPanel3() {
		JPanel panel3 = new JPanel(new BorderLayout());
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
		inputField1 = new JTextField(30);
		inputField2 = new JTextField(30);
		inputField3 = new JTextField(30);
		pane3Button = new JButton("Replace Genome");
		exportButton = new JButton("Export");
		
		panel3.add(new JLabel("Genome Identifiers, start with '>':"));
		panel3.add(inputField1);
		panel3.add(new JLabel("Segments to be replaced:"));
		panel3.add(inputField2);
		panel3.add(new JLabel("New segments:"));
		panel3.add(inputField3);
		panel3.add(pane3Button);
		panel3.add(exportButton);
		
		pane3Button.setAlignmentX(Component.LEFT_ALIGNMENT);
		exportButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return panel3;
	}

	public void showPanel(String panelName) {
		CardLayout layout = (CardLayout) getLayout();
		layout.show(this, panelName);

		for (Component comp : this.getComponents()) {
			if (comp.isVisible() == true) {
				currentPanel = (JPanel) comp;
				break; 
			}
		}
	}

}
