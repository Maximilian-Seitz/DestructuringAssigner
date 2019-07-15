package de.maxi_seitz.destructuringassigner.expression.target;

import org.mozilla.javascript.ast.AstNode;

/**
 * Represents data target which is not suitable for destructuring.
 */
class NonConvertibleTargetExpression extends TargetExpression {
	
	protected NonConvertibleTargetExpression(AstNode node) {
		super(node);
	}
	
	@Override
	public boolean isConvertibleExpression() {
		return false;
	}
	
	@Override
	public String getName() {
		return null;
	}
	
}
