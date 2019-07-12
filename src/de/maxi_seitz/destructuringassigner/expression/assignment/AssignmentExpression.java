package de.maxi_seitz.destructuringassigner.expression.assignment;

import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;
import de.maxi_seitz.destructuringassigner.expression.source.SourceExpression;

import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.VariableInitializer;

import org.mozilla.javascript.Token;

/**
 * Wrapper for {@link AstNode},
 * to allow easier analysis.
 */
public abstract class AssignmentExpression {
	
	private TargetExpression target;
	private SourceExpression source;
	
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
	

	public boolean isConvertibleExpression() {
		return source.isConvertibleExpression() && target.isConvertibleExpression();
	}
	
	public String getTargetName() {
		return target.getName();
	}
	
	public String getSourceName() {
		return source.getName();
	}
	
	
	protected void setTargetExpression(AstNode node) {
		target = TargetExpression.fromAstNode(node);
	}
	
	protected void setSourceExpression(AstNode node) {
		source = SourceExpression.fromAstNode(node);
	}
	
}
