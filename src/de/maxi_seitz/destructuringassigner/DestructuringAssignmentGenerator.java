package de.maxi_seitz.destructuringassigner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import de.maxi_seitz.destructuringassigner.expression.AssignmentExpression;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstRoot;


public class DestructuringAssignmentGenerator {
	
	private String inputFile;
	private String outputFile;
	
	
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	
	public void run() throws IOException {
		String sourceCode = Files.readString(Paths.get(inputFile));
		
		Parser parser = new Parser();
		AstRoot abstractSyntaxTree = parser.parse(sourceCode, inputFile, 0);
		
		abstractSyntaxTree.visit(astNode -> {
			AssignmentExpression statement = AssignmentExpression.fromAstNode(astNode);
			
			if(statement != null && statement.isSettingVariable()) {
				System.out.println(statement.getVariableName());
				//System.out.println(astNode.toSource());
			}
			
			return true;
		});
		
		Files.writeString(Paths.get(outputFile), abstractSyntaxTree.toSource());
	}
	
}
