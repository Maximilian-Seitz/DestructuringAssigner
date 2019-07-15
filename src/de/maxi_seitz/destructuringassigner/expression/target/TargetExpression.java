package de.maxi_seitz.destructuringassigner.expression.target;

import de.maxi_seitz.destructuringassigner.expression.ExpressionWrapper;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.*;

/**
 * Wrapper for {@link AstNode},
 * to which data is assigned.
 * <br/>
 * Allows for easier analysis of an assignment.
 */
public abstract class TargetExpression extends ExpressionWrapper {
	
	private final AstNode node;
	
	
	protected TargetExpression(AstNode node) {
		this.node = node;
	}
	
	
	public static TargetExpression fromAstNode(AstNode node) {
		if(node != null) {
			int tokenType = node.getType();
			
			if (tokenType == Token.NAME) {
				return new VariableTargetExpression((Name) node);
			} else if (tokenType == Token.ARRAYLIT || tokenType == Token.GETELEM || tokenType == Token.GETPROP) {
				return new UnnamedTargetExpression(node);
			}
		}
		
		return new NonConvertibleTargetExpression(node);
	}
	
	/**
	 * Check if converting an assignment with this as a target makes sense.
	 * Checks for possible side-effects in the expression, among other things.
	 * @return <code>true</code> when target can be used for destructuring
	 */
	public abstract boolean isConvertibleExpression();
	
	/**
	 * @return Identifier of target
	 */
	public abstract String getName();
	
	public AstNode getNode() {
		return node;
	}
	
}
