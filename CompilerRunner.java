//CompilerRunner Class

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.lang.Process;
import java.lang.Runtime;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FilenameFilter;

public class CompilerRunner {
	JFrame frame = new JFrame();

	JPanel panel = new JPanel(); // holds all panels
	JPanel buttonPanel = new JPanel(); // holds buttons
	JPanel textPanel = new JPanel(); // holds text
	JPanel labelPanel = new JPanel(); // holds labels
	JPanel textLabelPanel = new JPanel(); // holds labels and text
	JLabel targetFileLabel = new JLabel("Target File"); // label showing the user which text field is the target file
	JLabel directoryLabel = new JLabel("Target Directory"); // label showing the user which text field is the directory
	JLabel runLabel = new JLabel("Run File");
	JTextArea compileOutput = new JTextArea("console output"); // holds output of compilation from system
	JScrollPane scrollpane = new JScrollPane(compileOutput);
	JFileChooser fcd = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // https://mkyong.com/swing/java-swing-jfilechooser-example/
	JFileChooser fcf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	JFileChooser fcfr = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	JButton compileAll = new JButton("Compile All"); // compiles code in target directory
	JButton setCompileAllPath = new JButton("Set Compile Directory"); // sets path to target directory
	JButton compileTarget = new JButton("Compile Target File"); // compiles file targeted by compileTargetFile
	JButton setCompileTarget = new JButton("Set Compile Filepath"); // sets path to target file
	JButton setRunFile = new JButton("Set Target Run File");
	JButton runTargetFile = new JButton("Run Target File");
	int textLength = 400;
	int textHeight = 20;
	JTextField compileAllPath = new JTextField(); // gets path to target directory triggered and set by
													// setCompileAllPath using fileChooser
	JTextField compileTargetFile = new JTextField(); // gets path of target file triggered and set by setCompileTarget
	JTextField runFile = new JTextField();
	BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
	BoxLayout textBoxylayout = new BoxLayout(textPanel, BoxLayout.PAGE_AXIS);
	BoxLayout labelBoxylayout = new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS);
	GridLayout gridlayout = new GridLayout(0, 2);

	public void window() {
		fcd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // make fcd only see directories
																	// https://stackoverflow.com/questions/10083447/selecting-folder-destination-in-java
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(600, 400)); // had to do this in order to keep the minimum size
		compileOutput.setEditable(false);
		compileAllPath.setEditable(false);
		compileTargetFile.setEditable(false);
		runFile.setEditable(false);
		// trigger button for getting compile all path
		setCompileAllPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int returnVal = fcd.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					String file = fcd.getSelectedFile().getPath();
					compileAllPath.setText(file);
				}
			}
		});
		// trigger button to compile all files in the directory
		compileAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fcd.getSelectedFile() == null) {
					compileOutput.setText("No Path Selected");
				} else {
					File dir = fcd.getSelectedFile();
					String[] cmdarray = { "javac", "*.java" }; // absolutely vital that the command is a single element
																// of the array and the subsequent arguments are as well
					String reader = null;
					String output = "";
					try {
						Process process = Runtime.getRuntime().exec(cmdarray, null, dir);
						// standard and error output displayed on a JTextArea when compilation is
						// complete
						BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
						BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						int count = 0; // counts
						while ((reader = stdInput.readLine()) != null) {
							output += reader + "\n";
							count++;
						}
						while ((reader = stdErr.readLine()) != null) {
							output += reader + "\n";
							count++;
						}
						compileOutput.setText(output);
						if (count == 0) {
							// make it so it lists the compiled files
							// https://stackabuse.com/java-list-files-in-a-directory/
							// I don't fully understand this function, I get that it's the creation of a
							// method then calling it but I should study this more
							FilenameFilter filter = new FilenameFilter() {
								public boolean accept(File dir, String name) {
									return name.endsWith(".class");
								}
							};
							String[] files = dir.list(filter);
							String allFiles = "";
							for (String f : files) {
								allFiles += f + "\n";
							}
							compileOutput.setText("Compilation complete\n" + allFiles); /*
																						 * there is a bug, when compiled
																						 * without setting
																						 * compiledirectory (and
																						 * presumably compiletargetfile)
																						 * it compiles so it's probably
																						 * dropping into the directory
																						 * I'm in or something will need
																						 * to get that value
																						 * and put it in the JTextField
																						 * for targetdirectory and
																						 * targetfile so the user knows
																						 * where it's pointed to
																						 */
						}

					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		// trigger button to set target file path
		setCompileTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(null, "java"); // filters for only
																								// .java
				fcf.setFileFilter(fileFilter); // filters for only .java
				int returnVal = fcf.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String file = fcf.getSelectedFile().getPath();
					compileTargetFile.setText(file);
				}
			}
		});
		// trigger button to compile target file
		compileTarget.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fcf.getSelectedFile() == null) {
					compileOutput.setText("No Path Selected");
				} else {
					String file = fcf.getSelectedFile().getPath(); // get path returns a string for the File object
					String[] cmdarray = { "javac", file }; // absolutely vital that the command is a single element of
															// the array and the subsequent arguments are as well
					String reader = null;
					String output = "";
					try {
						Process process = Runtime.getRuntime().exec(cmdarray);
						// standard and error output displayed on a JTextArea when compilation is
						// complete
						BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
						BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						int count = 0; // counts
						while ((reader = stdInput.readLine()) != null) {
							output += reader + "\n";
							count++;
						}
						while ((reader = stdErr.readLine()) != null) {
							output += reader + "\n";
							count++;
						}
						compileOutput.setText(output);
						if (count == 0) {
							compileOutput.setText("Compilation complete"); /*
																			 * there is a bug, when compiled without
																			 * setting compiledirectory (and presumably
																			 * compiletargetfile)
																			 * it compiles so it's probably dropping
																			 * into the directory I'm in or something
																			 * will need to get that value
																			 * and put it in the JTextField for
																			 * targetdirectory and targetfile so the
																			 * user knows where it's pointed to
																			 */
						}

					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		// trigger button to set target run file path
		setRunFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(null, "class"); // filters for only
																									// .class
				fcfr.setFileFilter(fileFilter); // filters for only .class
				int returnVal = fcfr.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String file = fcfr.getSelectedFile().getPath();
					runFile.setText(file);
				}
			}
		});
		// trigger button to run class, usually Main but whichever the user chooses
		runTargetFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if getName() is null then read out no path selected, still doesn't work it
				// seems, maybe a try catch
				if (fcfr.getSelectedFile() == null) {
					compileOutput.setText("No Path Selected");
				} else {
					String file = fcfr.getSelectedFile().getName(); // get file name with extension
					String path = fcfr.getSelectedFile().getPath(); // get full file path with file and extension
					file = file.split("\\.class")[0]; // split at . and assign the filename only which is the first
														// element of the array
					// was a headache
					// https://javarevisited.blogspot.com/2016/02/2-ways-to-split-string-with-dot-in-java-using-regular-expression.html#axzz7PcHimjFy,
					// had to espace "."
					path = path.split(file)[0]; // split at file so only obtain the path so it can be used below in
												// exec() function
					File dir = new File(path);
					String[] cmdarray = { "java", file };
					String reader = null;
					String output = "";
					try {
						Process process = Runtime.getRuntime().exec(cmdarray, null, dir);
						// standard and error output displayed on a JTextArea when compilation is
						// complete
						BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
						BufferedReader stdErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						int count = 0; // counts
						while ((reader = stdInput.readLine()) != null) {
							output += reader + "\n";
							count++;
						}
						while ((reader = stdErr.readLine()) != null) {
							output += reader + "\n";
							count++;
						}
						compileOutput.setText(output);
						if (count == 0) {
							compileOutput.setText("File ran without error, and no console output"); /*
																									 * there is a bug,
																									 * when compiled
																									 * without setting
																									 * compiledirectory
																									 * (and presumably
																									 * compiletargetfile)
																									 * it compiles so
																									 * it's probably
																									 * dropping into the
																									 * directory I'm in
																									 * or something will
																									 * need to get that
																									 * value
																									 * and put it in the
																									 * JTextField for
																									 * targetdirectory
																									 * and targetfile so
																									 * the user knows
																									 * where it's
																									 * pointed to
																									 * caught this by
																									 * using if
																									 * statement so if
																									 * it's null it
																									 * won't run the cmd
																									 */
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		// layouts
		panel.setLayout(boxlayout);
		buttonPanel.setLayout(gridlayout);
		textPanel.setLayout(textBoxylayout);
		labelPanel.setLayout(labelBoxylayout);

		// maintain panel sizes
		buttonPanel.setMaximumSize(new Dimension(400, 100));
		// set size of text fields, maybe unecessary?
		compileAllPath.setPreferredSize(new Dimension(textLength, textHeight));
		compileTargetFile.setPreferredSize(new Dimension(textLength, textHeight));
		panel.setPreferredSize(new Dimension(600, 200));
		panel.setMinimumSize(new Dimension(600, 200)); // doesn't do anything?
		labelPanel.add(directoryLabel);
		labelPanel.add(targetFileLabel);
		labelPanel.add(runLabel);
		textPanel.add(compileAllPath);
		textPanel.add(compileTargetFile);
		textPanel.add(runFile);
		textLabelPanel.add(labelPanel);
		textLabelPanel.add(textPanel);
		buttonPanel.add(setCompileAllPath);
		buttonPanel.add(compileAll);
		buttonPanel.add(setCompileTarget);
		buttonPanel.add(compileTarget);
		buttonPanel.add(setRunFile);
		buttonPanel.add(runTargetFile);
		panel.add(textLabelPanel);
		panel.add(scrollpane);
		panel.add(buttonPanel);

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

}

/*
 * SOURCES:
 * https://mkyong.com/swing/java-swing-jfilechooser-example/
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
 * error-codes
 * https://www.tutorialspoint.com/java/lang/runtime_exec_dir.htm
 * https://stackoverflow.com/questions/31776546/why-does-runtime-execstring-work
 * -for-some-but-not-all-commands
 * https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/lang/
 * Runtime.html
 * https://docs.oracle.com/javase/tutorial/uiswing/components/textarea.html
 * https://stackabuse.com/java-list-files-in-a-directory/
 * https://javarevisited.blogspot.com/2016/02/2-ways-to-split-string-with-dot-in
 * -java-using-regular-expression.html#axzz7PcHimjFy
 */