package _GUIProcessing;

import javax.swing.*;

import Data_Genome.Repository;
import Data_Genome.SNPAlignment;
import Data_User.Bioinformatician;
import Data_User.TeamLead;
import Data_User.TechnicalSupport;
import _RunMe.InputReader;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Point;
//import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;


public class GUIProcess extends JFrame implements ActionListener {
	//Panel Function
	private MainPanel mainPanel;
	private JButton nextButton;
	private int currentPanelIndex;

	//Program function
	private InputReader inp;
	private TeamLead X;
	private Bioinformatician A;
	private Bioinformatician B;
	private Bioinformatician C;
	private TechnicalSupport D;
	private SNPAlignment origin, current;
	private int initial;


	//Constructor
	public GUIProcess() {
		super("Team Bioinformatics");
		mainPanel = new MainPanel();
		mainPanel.pane1Button.addActionListener(this);
		mainPanel.pane2Button.addActionListener(this);
		mainPanel.pane3Button.addActionListener(this);
		mainPanel.exportButton.addActionListener(this);

		currentPanelIndex = 0;
		nextButton = new JButton("Next");
		nextButton.addActionListener(this);
		add(nextButton, BorderLayout.SOUTH);

		add(mainPanel);
		inp = new InputReader();

		try {
			X = inp.getTeamLead();
			A = inp.getBioinformatician(1);
			B = inp.getBioinformatician(2);
			C = inp.getBioinformatician(3);
			D = inp.getTechnicalSupport(4);

			origin = inp.readFASTA();
			current = X.getRepository().getStorage();
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}

		setSize(500, 300); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mainPanel.pane1Button) {
			loadAndDisplayTeam();
		} else if (e.getSource() == mainPanel.pane2Button) {
			loadSNP();
		} else if (e.getSource() == mainPanel.pane3Button) {
			try {
				replaceGenome();
			} catch (IllegalArgumentException | IOException e1) {
				e1.printStackTrace();
			}
		}  else if (e.getSource() == mainPanel.exportButton) {
			handleExport();
		} else if (e.getSource() == nextButton) {
		currentPanelIndex = (currentPanelIndex + 1) % 3; //Iterating between panels
		if (currentPanelIndex == 0) {
			mainPanel.showPanel("panel1");
		} else if (currentPanelIndex == 1){
			mainPanel.showPanel("panel2");
		} else if (currentPanelIndex == 2){
			mainPanel.showPanel("panel3");
		}
	}

}

public void loadAndDisplayTeam() {
	String outputText = String.format("<div style='text-align: center;'>\tThe team:<br>%s. <br>"
			+ "Bioinformaticians: %s, %s, %s.<br>"
			+ "Technical Support: %s.<br></div>", A.getLeadBy(), 
			A.getName(), B.getName(), C.getName(), D.getName()); 

	mainPanel.pane1Label.setText("<html>" + outputText + "</html>");
}

public void loadSNP() {
	initial = origin.alignmentScore();
	String outputText = String.format("SNP Alignment: <br>Alignment score: %d.<br>",initial);
	Set<String> IDs = origin.getAllIdentifiers();

	for (String id : IDs) {
		outputText += String.format("%s<br>%s<br>", id, origin.getSNiPMap().get(id));
	}
	mainPanel.pane2Label.setText("<html>" + outputText + "</html>");
}

public void replaceGenome() throws IllegalArgumentException, IOException {
	X.overwriteAlignment(current, A);
	SNPAlignment alignA = A.getPersonalAlignment();
	String idGene = mainPanel.inputField1.getText();
	String oldSeg = mainPanel.inputField2.getText();
	String newSeg = mainPanel.inputField3.getText();
	alignA.replaceOccurences(idGene, oldSeg, newSeg);
}

private void handleExport() {
	JFileChooser fileChooser = new JFileChooser();
	if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
		File outputFile = fileChooser.getSelectedFile();
		
		Set<String> IDs = A.getPersonalAlignment().getAllIdentifiers();
		try(PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));){
			pw.printf("\t GENOME ALIGNMENT %n "
					+ "Lead by: %s%n "
					+ "Made by: %s%n"
					+ "Standard Alignment: %n", A.getLeadBy(),this.getName());
			for (String id : IDs) {
				pw.printf("%s%n%s%n", id, A.getPersonalAlignment().getOneSequence(id));
			}
			pw.printf("%nSNiP Alignment: %n");
			for (String id : IDs) {
				pw.printf("%s%n%s%n", id, A.getPersonalAlignment().getSNiPMap().get(id));
			}
			pw.println();
		} catch (IOException e) {
			e.printStackTrace();	
		}
	}
}
}
