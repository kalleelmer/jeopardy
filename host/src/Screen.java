import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.OverlayLayout;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Screen extends JFrame {
	private static final long serialVersionUID = 1L;

	JPanel outerFrame = new JPanel();
	JPanel playingField = new JPanel();
	JLabel[] categoryLabels;
	JLabel[][] questionLabels;
	StyledDocument mainText;
	JTextPane textDisplay = new JTextPane();
	SimpleAttributeSet center;

	public Screen() {

		LayoutManager overlay = new OverlayLayout(outerFrame);
		outerFrame.setLayout(overlay);

		mainText = textDisplay.getStyledDocument();
		center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontFamily(center, "Sans-serif");
		StyleConstants.setFontSize(center, 50);
		StyleConstants.setForeground(center, Color.WHITE);
		mainText.setParagraphAttributes(0, mainText.getLength(), center, false);
		textDisplay.setBackground(new Color(50, 50, 255));
		textDisplay.setAlignmentX(0.5f);
		textDisplay.setAlignmentY(0.5f);
		textDisplay.setEditable(false);
		textDisplay.setVisible(false);
		outerFrame.add(textDisplay);

		playingField.setBackground(new Color(0, 0, 150));
		outerFrame.add(playingField);
		getContentPane().add(outerFrame, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void display(String text) {
		try {
			mainText.remove(0, mainText.getLength());
			mainText.insertString(0, text, center);
			textDisplay.setVisible(true);
		} catch (BadLocationException gurka) {
		}
	}
	
	public void hide() {
		textDisplay.setVisible(false);
	}
}