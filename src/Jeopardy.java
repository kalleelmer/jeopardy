import java.io.File;
import javax.swing.JFileChooser;


public class Jeopardy {
	public static void main(String[] args) {
		
		final JFileChooser fc = new JFileChooser();
		Category[] categories = null;
		do{
			int result = fc.showOpenDialog(fc);
	        File inputFile = fc.getSelectedFile();
	        if(result != JFileChooser.APPROVE_OPTION){
	        	System.exit(0);
	        	System.out.println("Cancel or error. Aborting.");
	        }
	        categories = XMLQuestions.getCategoryList(inputFile);
		}
		while(categories == null);
		HostInterface host = null;
		
	    Screen screen = new Screen(categories);
	    screen.setLocation(1290, 10);
	    screen.setSize(1024, 768);
	    screen.setVisible(true);
	    
	    ControlPanel controlPanel = new ControlPanel(screen, categories, host);
	    controlPanel.setLocation(10, 10);
	    controlPanel.setSize(1024, 768);
	    controlPanel.setVisible(true);
	}
}
