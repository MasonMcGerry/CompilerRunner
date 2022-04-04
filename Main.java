//Main Class
/*
compiles all files in a directory
runs a specific file in a directory
 
to make this a jar file to execute like a program I had to make sure the Environment Variable in Windows 10 was set to the bin's path containing jar.exe
put all the class files in a folder
then in that folder make a manifest.txt containing "Main-Class: Compiler", press reenvturn and save the txt
then run jar -cvmf manifest.txt CompilerRunner.jar *.class
to make the jar an exe
https://fullstackdeveloper.guru/2020/06/17/how-to-create-a-windows-native-java-application-generating-exe-file/
TO DO:
make a runner class
compiler class with another button compile all (in target directory) or compile target file
runner class to run target file
finding a way to new way to do a deprecated method is coming up with dead ends and is frustrating, I'd like a good workflow for finding a non deprecated replacement, I'll make a video
I think I finally found it https://download.java.net/java/early_access/loom/docs/api/deprecated-list.html
the trick seems to be finding
https://download.java.net/java/early_access/loom/docs/api/java.base/java/lang/Runtime.html
*/

public class Main {
	public static void main(String[] args) {
// run app
		CompilerRunner cr = new CompilerRunner();
		cr.window();
	}
}