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
		String sourceCode = readSourceCode();
		
		AstRoot abstractSyntaxTree = parseSourceCode(sourceCode);
		
		if(abstractSyntaxTree != null) {
			List<AssignmentExpression> firstListElements = getAssignmentsStartingListsInAst(abstractSyntaxTree);
			
			for(AssignmentExpression assignment : firstListElements) {
				assignment.groupFollowingAssignments();
			}
			
			String convertedSourceCode = abstractSyntaxTree.toSource();
			
			writeSourceCode(convertedSourceCode);
		}
	}
	
	
	private String readSourceCode() throws IOException {
		return Files.readString(Paths.get(inputFile));
	}
	
	private void writeSourceCode(String sourceCode) throws IOException {
		Files.writeString(Paths.get(outputFile), sourceCode);
	}
	
	private AstRoot parseSourceCode(String sourceCode) {
		Parser parser = new Parser();
		
		try {
			return parser.parse(sourceCode, inputFile, 0);
		} catch(EvaluatorException e) {
			System.err.println("Failed to parse file. Following error found: " + e.getLocalizedMessage());
			return null;
		}
	}
	
	private List<AssignmentExpression> getAssignmentsStartingListsInAst(AstRoot abstractSyntaxTree) {
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
		
		return firstListElements;
	}
	
	
}
