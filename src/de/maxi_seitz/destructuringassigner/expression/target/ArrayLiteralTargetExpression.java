package de.maxi_seitz.destructuringassigner.expression.target;

import org.mozilla.javascript.ast.ArrayLiteral;

class ArrayLiteralTargetExpression extends TargetExpression {
	
	private final ArrayLiteral node;
	
	ArrayLiteralTargetExpression(ArrayLiteral node) {
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
	
	@Override
	public String toString() {
		return node.toSource();
	}
	
}
