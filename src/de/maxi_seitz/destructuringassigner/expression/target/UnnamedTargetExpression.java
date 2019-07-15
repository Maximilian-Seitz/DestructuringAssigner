package de.maxi_seitz.destructuringassigner.expression.target;

import org.mozilla.javascript.ast.AstNode;

/**
 * Represents data target which can not be represented as a simple name.
 */
class UnnamedTargetExpression extends TargetExpression {
	
	private final AstNode node;
	
	protected UnnamedTargetExpression(AstNode node) {
		super(node);
		
		this.node = node;
	}
	
	@Override
	public boolean isConvertibleExpression() {
		return isSideEffectFree(node);
	}
	
	@Override
	public String getName() {
		return null;
	}
	
}
