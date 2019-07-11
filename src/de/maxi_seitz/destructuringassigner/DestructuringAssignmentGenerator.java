package de.maxi_seitz.destructuringassigner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DestructuringAssignmentGenerator {
	
	private String sourceCode;
	private String resultCode;
	
	
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
	public void setSourceCodeFromFile(Path filePath) throws IOException {
		setSourceCode(Files.readString(filePath));
	}
	
	public void setSourceCodeFromFile(String fileName) throws IOException {
		setSourceCodeFromFile(Paths.get(fileName));
	}
	
	public String getResultCode() {
		if(this.resultCode == null) {
			throw new IllegalStateException("Generator has not been run.");
		}
		
		return this.resultCode;
	}
	
	public void writeResultCodeToFile(String fileName) throws IOException {
		Files.writeString(Paths.get(fileName), getResultCode());
	}
	
	public void run() {
		resultCode = sourceCode;
	}
}
