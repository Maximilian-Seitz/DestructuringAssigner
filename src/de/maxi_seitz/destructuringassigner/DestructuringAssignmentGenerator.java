package de.maxi_seitz.destructuringassigner;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;

import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstRoot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


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
		
		List<AssignmentExpression> firstListElements = new LinkedList<>();
		
		abstractSyntaxTree.visit(astNode -> {
			List<AssignmentExpression> assignments = AssignmentExpression.fromAstNode(astNode);
			
			for(AssignmentExpression assignment : assignments) {
				if(assignment.isConvertibleExpression()) {
					if(assignment.isFirstInList()) {
						firstListElements.add(assignment);
					}
				}
			}
			
			return true;
		});
		
		for(AssignmentExpression expression : firstListElements) {
			System.out.println(expression);
		}
		
		Files.writeString(Paths.get(outputFile), abstractSyntaxTree.toSource());
	}
	
}
