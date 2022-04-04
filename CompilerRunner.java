//CompilerRunner Class

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.lang.Process;
import java.lang.Runtime;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class CompilerRunner {
	JFrame frame = new JFrame();

	JPanel panel = new JPanel(); // holds all panels
	JPanel buttonPanel = new JPanel(); // holds buttons
	JPanel textPanel = new JPanel(); // holds text
	JPanel labelPanel = new JPanel(); // holds labels
	JPanel textLabelPanel = new JPanel(); // holds labels and text
	JLabel targetFileLabel = new JLabel("Target File"); // label showing the user which text field is the target file
	JLabel directoryLabel = new JLabel("Target Directory"); // label showing the user which text field is the directory
	JLabel compileOutput = new JLabel("test"); // holds output of compilation from system
	JFileChooser fcd = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // https://mkyong.com/swing/java-swing-jfilechooser-example/
	JFileChooser fcf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	JButton compileAll = new JButton("Compile All"); // compiles code in target directory
	JButton setCompileAllPath = new JButton("Set Compile Directory"); // sets path to target directory
	JButton compileTarget = new JButton("Compile Target File"); // compiles file targeted by compileTargetFile
	JButton setCompileTarget = new JButton("Set Compile Filepath"); // sets path to target file
	int textLength = 400;
	int textHeight = 20;
	JTextField compileAllPath = new JTextField(); // gets path to target directory triggered and set by
													// setCompileAllPath using fileChooser
	JTextField compileTargetFile = new JTextField(); // gets path of target file triggered and set by setCompileTarget
	BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
	BoxLayout textBoxylayout = new BoxLayout(textPanel, BoxLayout.PAGE_AXIS);
	BoxLayout labelBoxylayout = new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS);
	GridLayout gridlayout = new GridLayout(0, 2);

	public void window() {
		fcd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // make fcd only see directories
																	// https://stackoverflow.com/questions/10083447/selecting-folder-destination-in-java
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		//trigger button for getting compile all path
		setCompileAllPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int returnVal = fcd.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					String file = fcd.getSelectedFile().getPath();
					compileAllPath.setText(file);
				}
			}
		});
		//trigger button to compile all files in the directory
		compileAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			// not working going to try cding into the dir and running cmd
			String command = "javac *.java --source-path " + compileAllPath.getText(); //getText turns compileAllPath's input field into a String
			//String command = "cd " + compileAllPath.getText() + " & javac *.java"; // this throws an error where I think it can't find CD, I read it needs the whole path to that cmd, may experiment later 
			//String command = "javac " + compileAllPath.getText() + "\\*.java"; // this works when used with deprecated Runtime.getRuntime().exec(String)
			//String command = "C:\\Program Files\\Java\\jdk-18\\bin\\javac.exe " + compileAllPath.getText() + "\\*.java";
			//String command = "C:\\Program Files\\Java\\jdk-18\\bin\\javac.exe ";
			//String command = "javac *.java"; // string of command to run to compile all code
			File dir = fcd.getSelectedFile(); // file directory in which to compile all code
			//System.out.println(dir.getName());
			String[] commandArray = { command }; // turns command string into an array
			//compileOutput.setText(sysOutput(commandArray[0]));
			//System.out.println(compileOutput.getText());
			String reader = null;
			String output = "";
				try {
					// process error=2 https://community.oracle.com/tech/developers/discussion/1245932/runtime-exec-error-codes
					//Process process = Runtime.getRuntime().exec(commandArray, null, dir);
					//Process process = Runtime.getRuntime().exec(commandArray);
					Process process = Runtime.getRuntime().exec(command);
					//std and err output displayed on a JLabel when compilation is complete
					///* output to compileOutput label works with commandArray, formatting is off though, will need to fix
					//unfinished, the output is coming out as one long string, need the label to be scrollable
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
					BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					while ((reader = stdInput.readLine()) != null) {
						output += reader + "\n";
					}
					while ((reader = stdErr.readLine()) != null) {
						output += reader + "\n";
					}
					compileOutput.setText(output);
					//System.out.println(dir);
					//*/
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		//trigger button to set target file path
		setCompileTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fcf.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String file = fcf.getSelectedFile().getPath();
					compileTargetFile.setText(file);
				}
			}
		});
		//trigger button to compile target file 
		//layouts
		panel.setLayout(boxlayout);
		buttonPanel.setLayout(gridlayout);
		textPanel.setLayout(textBoxylayout);
		labelPanel.setLayout(labelBoxylayout);
		// maintain panel sizes
		buttonPanel.setMaximumSize(new Dimension(400, 100));
		textPanel.setMaximumSize(new Dimension(500, 50));
		// set size of text fields
		compileAllPath.setPreferredSize(new Dimension(textLength, textHeight));
		compileTargetFile.setPreferredSize(new Dimension(textLength, textHeight));
		panel.setPreferredSize(new Dimension(600, 200));
		labelPanel.add(directoryLabel);
		labelPanel.add(targetFileLabel);
		textPanel.add(compileAllPath);
		textPanel.add(compileTargetFile);
		textLabelPanel.add(labelPanel);
		textLabelPanel.add(textPanel);
		buttonPanel.add(setCompileAllPath);
		buttonPanel.add(compileAll);
		buttonPanel.add(setCompileTarget);
		buttonPanel.add(compileTarget);
		panel.add(textLabelPanel);
		panel.add(compileOutput);
		panel.add(buttonPanel);

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	public String sysOutput(String[] cmd) {
		// read and return system output
		String reader = null;
		String output = "";
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((reader = stdInput.readLine()) != null) {
				output += reader;
			}
			while ((reader = stdErr.readLine()) != null) {
				output += reader;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return output;
	}
}

/*
 * SOURCES: https://mkyong.com/swing/java-swing-jfilechooser-example/
 * https://stackoverflow.com/questions/10083447/selecting-folder-destination-in-
 * java
 * https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
 * https://stackoverflow.com/questions/5752307/how-to-retrieve-value-from-
 * jtextfield-in-java-swing
 * https://stackoverflow.com/questions/147181/how-can-i-convert-my-java-program-
 * to-an-exe-file
 * https://fullstackdeveloper.guru/2020/06/17/how-to-create-a-windows-native-
 * java-application-generating-exe-file/
 * http://www.smartclass.co/2011/10/creating-package-with-multiple-classes.html
 * https://www.tabnine.com/code/java/methods/javax.swing.JPanel/setMaximumSize
 * https://docs.oracle.com/javase/7/docs/api/java/awt/Dimension.html
 * https://stackoverflow.com/questions/19621838/createprocess-error-2-the-system
 * -cannot-find-the-file-specified
 * https://community.oracle.com/tech/developers/discussion/1245932/runtime-exec-
 * error-codes https://www.tutorialspoint.com/java/lang/runtime_exec_dir.htm
 */
