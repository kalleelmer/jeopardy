import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ControlPanel extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JButton quitButton = new JButton("Avsluta");
	JButton pointsFontUp = new JButton("Större");
	JButton pointsFontDown = new JButton("Mindre");
	JButton categoryFontUp = new JButton("Större");
	JButton categoryFontDown = new JButton("Mindre");
	JButton hostConnect = new JButton("Programledare");
	JButton restartButton = new JButton("Ny omgång");
	JCheckBox myCheckBox = new JCheckBox("Helskärmsläge");
	JLabel statusText = new JLabel("Programmet startat");
	JPanel bottomPanel = new JPanel();
	JPanel outerFrame = new JPanel();
	JPanel playingField = new JPanel();
	HostInterface host;

	Screen screen;
	Category[] categories;
	JButton[][] controlButtons;

	public ControlPanel(Screen screen, Category[] categories, HostInterface host) {
		this.screen = screen;
		this.host = host;
		reload(categories);
	}

	public void reload(Category[] categories) {
		this.categories = categories;

		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(restartButton);
		bottomPanel.add(hostConnect);
		bottomPanel.add(new JLabel("Kategorier:"));
		bottomPanel.add(categoryFontUp);
		bottomPanel.add(categoryFontDown);
		bottomPanel.add(new JLabel("Poäng:"));
		bottomPanel.add(pointsFontUp);
		bottomPanel.add(pointsFontDown);
		bottomPanel.add(myCheckBox);
		bottomPanel.add(myCheckBox);
		bottomPanel.add(quitButton);

		playingField = new JPanel();
		playingField.setLayout(new GridLayout(6, categories.length));
		for (int i = 0; i < categories.length; i++) {
			if (categories[i] == null) {
				playingField.add(new JLabel());
				continue;
			}
			String titleString = "<html><center>"
					+ categories[i].getTitle().replace("\\n", "<br>")
					+ "</center></html>";
			JLabel l = new JLabel(titleString);
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
			playingField.add(l);
		}
		controlButtons = new JButton[5][categories.length];
		for (int q = 0; q < 5; q++) {
			for (int c = 0; c < categories.length; c++) {
				if (categories[c] != null
						&& categories[c].getQuestion(q) != null) {
					controlButtons[q][c] = new JButton(q + 1 + "00");
					controlButtons[q][c].addActionListener(this);
					controlButtons[q][c].setFont(new Font("Sans-Serif",
							Font.BOLD, 25));
				} else {
					controlButtons[q][c] = new JButton();
					controlButtons[q][c].setVisible(false);
				}
				playingField.add(controlButtons[q][c]);
			}
		}

		outerFrame.setLayout(new BorderLayout());
		outerFrame.add(bottomPanel, BorderLayout.SOUTH);
		outerFrame.add(playingField, BorderLayout.CENTER);

		getContentPane().add(outerFrame, BorderLayout.CENTER);

		restartButton.addActionListener(this);
		hostConnect.addActionListener(this);
		quitButton.addActionListener(this);
		pointsFontUp.addActionListener(this);
		pointsFontDown.addActionListener(this);
		categoryFontUp.addActionListener(this);
		categoryFontDown.addActionListener(this);
		myCheckBox.addActionListener(this);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == quitButton) {
			Object[] options = { "Ja", "Nej" };
			int n = JOptionPane
					.showOptionDialog(
							this,
							"Vill du verkligen avsluta programmet och förlora nuvarande spelomgång?",
							"Avsluta?", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);
			if (n == 0) {
				shutdown();
			}
		} else if (e.getSource() == restartButton) {
			Object[] options = { "Ja", "Nej" };
			int n = JOptionPane
					.showOptionDialog(
							this,
							"Vill du verkligen starta om och förlora nuvarande spelomgång?",
							"Starta om?", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[1]);
			if (n == 0) {
				reload(categories);
			}
		} else if (e.getSource() == hostConnect) {
			String hostIP = JOptionPane.showInputDialog(null, "Adress: ",
					"Anlut till programledarskärm", 1);
			if (host != null) {
				host.close();
			}
			host = new HostInterface(hostIP, this);
		} else if (e.getSource() == pointsFontUp) {
			screen.changePointsFontSize(1);
		} else if (e.getSource() == pointsFontDown) {
			screen.changePointsFontSize(-1);
		} else if (e.getSource() == categoryFontUp) {
			screen.changeCategoryFontSize(1);
		} else if (e.getSource() == categoryFontDown) {
			screen.changeCategoryFontSize(-1);
		} else if (e.getSource() == myCheckBox) {
			if (myCheckBox.isSelected()) {
				screen.dispose();
				screen.setUndecorated(true);
				screen.setVisible(true);
			} else {
				screen.dispose();
				screen.setUndecorated(false);
				screen.setVisible(true);
			}
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < categories.length; j++) {
				if (e.getSource() == controlButtons[i][j]) {
					screen.showQuestion(i, j);
					if (host != null) {
						host.display(categories[j].getQuestion(i).text
								+ "\\n\\n"
								+ categories[j].getQuestion(i).answer);
					}
					Object[] options = { "Ja", "Nej", "Avbryt" };
					int n = JOptionPane.showOptionDialog(this,
							categories[j].getQuestion(i).text + "\n\n"
									+ categories[j].getQuestion(i).answer,
							"Korrekt svar?", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[2]);
					if (n == 0) {// Korrekt svar
						screen.removeQuestion(i, j);
						controlButtons[i][j].setVisible(false);
					} else if (n == 1) {// Fel svar
						screen.removeQuestion(i, j);
						controlButtons[i][j].setVisible(false);
					} else {// Avbryt
						// Ändra inget på spelplanen
					}
					if (host != null) {
						host.hide();
					}
					screen.hideQuestion();
				}
			}
		}
	}

	public void displayInfo(String info) {
		String date = new SimpleDateFormat("H:m:s").format(System
				.currentTimeMillis());
		String title = "" + date + ": " + info;
		setTitle(title);
	}

	private void shutdown() {
		if (host != null) {
			host.close();
		}
		System.exit(0);
	}
}