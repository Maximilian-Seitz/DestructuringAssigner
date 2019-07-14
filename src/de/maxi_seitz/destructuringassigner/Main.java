package de.maxi_seitz.destructuringassigner;

import java.io.IOException;

public class Main {
	
	private static final String HELP_STRING = "Usage: destructoringAssignmentGenerator.jar source_path target_path";
	
	public static void main(String[] args) {
		if(args.length == 2) {
			convertFile(args[0], args[1]);
		} else {
			showHelp();
		}
	}
	
	private static void convertFile(String inputFile, String outputFile) {
		try {
			DestructuringAssignmentGenerator generator = new DestructuringAssignmentGenerator();
			generator.setInputFile(inputFile);
			generator.setOutputFile(outputFile);
			generator.run();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void showHelp() {
		System.out.println(HELP_STRING);
	}
	
}
