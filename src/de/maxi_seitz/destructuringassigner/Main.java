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
			generator.setSourceCodeFromFile(args[0]);
			generator.run();
			generator.writeResultCodeToFile(args[1]);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void showHelp() {
		System.out.println("Usage:");
	}
	
}
