package de.maxi_seitz.destructuringassigner;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;

import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstRoot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


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
		AstRoot abstractSyntaxTree;
		
		try {
			abstractSyntaxTree = parser.parse(sourceCode, inputFile, 0);
		} catch(EvaluatorException e) {
			System.err.println("Failed to parse file. Following error found: " + e.getLocalizedMessage());
			return;
		}
		
		abstractSyntaxTree.visit(astNode -> {
			AssignmentExpression assignment = AssignmentExpression.fromAstNode(astNode);
			
			if(assignment != null && assignment.isConvertibleExpression()) {
				System.out.println(assignment.getSourceName() + " >> " + assignment.getTargetName());
				//System.out.println(astNode.toSource());
			}
			
			return true;
		});
		
		Files.writeString(Paths.get(outputFile), abstractSyntaxTree.toSource());
	}
	
}
