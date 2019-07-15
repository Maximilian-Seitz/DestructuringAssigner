package de.maxi_seitz.destructuringassigner;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;

import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstRoot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * Allows compression of assignments in JavaScript file,
 * where destructuring assignments can be used.
 * <br/>
 * Writes new source code to specified output file.
 * <br/>
 * Requires source and target files to be set via
 * {@link #setInputFile(String)} and {@link #setOutputFile(String)}.
 * Then, to generate output, use {@link #run()}.
 */
public class DestructuringAssignmentGenerator {
	
	private String inputFile;
	private String outputFile;
	
	/**
	 * Set file to compress
	 * @param inputFile Path to JavaScript file
	 */
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	/**
	 * Set file for output
	 * @param outputFile Path to JavaScript file
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	
	/**
	 * Run conversion, taking input JavaScript file,
	 * compressing lists of assignments, where possible,
	 * and writing the result to the output file.
	 * @throws IOException when failing to read input, or write to output file.
	 */
	public void run() throws IOException {
		String sourceCode = readSourceCode();
		
		AstRoot abstractSyntaxTree = parseSourceCode(sourceCode);
		
		if(abstractSyntaxTree != null) {
			List<AssignmentExpression> firstListElements = getAssignmentsStartingListsInAst(abstractSyntaxTree);
			
			// for each list of assignments, take the first assignment,
			// and group/compress the list from there
			for(AssignmentExpression assignment : firstListElements) {
				//go through following assignments, check for compatibility, and compress if possible
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
	
	/**
	 * Find {@link AssignmentExpression assignments},
	 * which are the first in a list of assignments.
	 * These lists can potentially be grouped into
	 * destructuring assignments.
	 * @param abstractSyntaxTree Root of AST of source code
	 * @return A {@link List list} of {@link AssignmentExpression assignments},
	 * that are each the first in a "list" of assignments in the source code.
	 */
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
