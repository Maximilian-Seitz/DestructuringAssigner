package de.maxi_seitz.destructuringassigner.expression;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.VariableInitializer;

/**
 * Wrapper for {@link AstNode},
 * to allow easier analysis.
 */
public abstract class AssignmentExpression {
	
	public static AssignmentExpression fromAstNode(AstNode node) {
		int tokenType = node.getType();
		
		if(tokenType == Token.ASSIGN || tokenType == Token.VAR) {
			if(node instanceof VariableInitializer) {
				return new InitializerExpression((VariableInitializer) node);
			}
			
			if(node instanceof Assignment) {
				return new SetterExpression((Assignment) node);
			}
		}
		
		return null;
	}
	
	public abstract boolean isSettingVariable();
	public abstract String getVariableName();
	
}
