import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.OverlayLayout;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Screen extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	JButton quitButton = new JButton("Avsluta");
	JCheckBox myCheckBox = new JCheckBox("Check");
	JLabel statusText = new JLabel("My text");
	JPanel outerFrame = new JPanel();
	JPanel playingField = new JPanel();
	JLabel[] categoryLabels;
	JLabel[][] questionLabels;
	StyledDocument mainText;
	JTextPane textDisplay = new JTextPane();
	SimpleAttributeSet center;

	Category[] categories;

	public Screen(Category[] categories) {
		reload(categories);
	}

	public void reload(Category[] categories) {

		this.categories = categories;

		LayoutManager overlay = new OverlayLayout(outerFrame);
		outerFrame.setLayout(overlay);

		mainText = textDisplay.getStyledDocument();
		center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontFamily(center, "Sans-serif");
		StyleConstants.setFontSize(center, 35);
		StyleConstants.setForeground(center, Color.WHITE);
		try {
			mainText.insertString(0,
					"Lång text som garanterat kommer att gå åt helvete. 666",
					center);
			mainText.remove(0, mainText.getLength());
			mainText.insertString(0,
					"Lång text som garanterat kommer att gå åt helvete.",
					center);
		} catch (BadLocationException gurka) {
		}
		mainText.setParagraphAttributes(0, mainText.getLength(), center, false);
		textDisplay.setMaximumSize(new Dimension(800, 200));
		textDisplay.setBackground(new Color(50, 50, 255));
		textDisplay.setAlignmentX(0.5f);
		textDisplay.setAlignmentY(0.5f);
		textDisplay.setEditable(false);
		textDisplay.setVisible(false);
		outerFrame.add(textDisplay);

		playingField = new JPanel();
		playingField.setLayout(new GridLayout(6, categories.length));

		categoryLabels = new JLabel[categories.length];
		for (int i = 0; i < categories.length; i++) {
			if (categories[i] != null) {
				String titleString = "<html><center>"
						+ categories[i].getTitle().replace("\\n", "<br>")
						+ "</center></html>";
				categoryLabels[i] = new JLabel(titleString);
				categoryLabels[i].setHorizontalAlignment(JLabel.CENTER);
				categoryLabels[i]
						.setFont(new Font("Sans-Serif", Font.PLAIN, 15));
				categoryLabels[i].setForeground(new Color(255, 255, 255));
			} else {
				categoryLabels[i] = new JLabel();
			}
			playingField.add(categoryLabels[i]);
		}
		questionLabels = new JLabel[5][categories.length];
		for (int q = 0; q < 5; q++) {
			for (int c = 0; c < categories.length; c++) {
				if (categories[c] != null
						&& categories[c].getQuestion(q) != null) {
					questionLabels[q][c] = new JLabel(q + 1 + "00");
					questionLabels[q][c].setHorizontalAlignment(JLabel.CENTER);
					questionLabels[q][c].setFont(new Font("Sans-Serif",
							Font.BOLD, 50));
					questionLabels[q][c]
							.setForeground(new Color(255, 255, 255));
				} else {
					questionLabels[q][c] = new JLabel();
				}
				playingField.add(questionLabels[q][c]);
			}
		}

		playingField.setBackground(new Color(0, 0, 150));
		outerFrame.add(playingField);
		getContentPane().add(outerFrame, BorderLayout.CENTER);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	public void showQuestion(int pointIndex, int categoryIndex) {
		try {
			mainText.remove(0, mainText.getLength());
			String qText = categories[categoryIndex].getQuestion(pointIndex).text;
			mainText.insertString(0, qText, center);
			textDisplay.setVisible(true);
		} catch (BadLocationException gurka) {

		}
	}

	public void changePointsFontSize(int change) {
		Font font = questionLabels[0][0].getFont();
		int fontSize = font.getSize() + change;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < categories.length; j++) {
				questionLabels[i][j].setFont(new Font("Sans-Serif", Font.BOLD,
						fontSize));
			}
		}
	}

	public void changeCategoryFontSize(int change) {
		Font font = categoryLabels[0].getFont();
		int fontSize = font.getSize() + change;
		for (int i = 0; i < categories.length; i++) {
			categoryLabels[i].setFont(new Font("Sans-Serif", Font.PLAIN,
					fontSize));
		}
	}

	public void hideQuestion() {
		textDisplay.setVisible(false);
	}

	public void removeQuestion(int pointIndex, int categoryIndex) {
		questionLabels[pointIndex][categoryIndex].setVisible(false);
	}

	public void actionPerformed(ActionEvent e) {
	}
}