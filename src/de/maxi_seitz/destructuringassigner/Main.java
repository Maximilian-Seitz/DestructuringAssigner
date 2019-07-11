package de.maxi_seitz.destructuringassigner;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
		if(args.length != 2) {
			showHelp();
			return;
		}
		
		try {
			DestructuringAssignmentGenerator generator = new DestructuringAssignmentGenerator();
			generator.setInputFile(args[0]);
			generator.setOutputFile(args[1]);
			generator.run();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void showHelp() {
		System.out.println("Usage:");
	}
	
}
