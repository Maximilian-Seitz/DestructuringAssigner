package de.maxi_seitz.destructuringassigner.expression.target;

import org.mozilla.javascript.ast.ArrayLiteral;

class ArrayLiteralTargetExpression extends TargetExpression {
	
	private final ArrayLiteral node;
	
	ArrayLiteralTargetExpression(ArrayLiteral node) {
		this.node = node;
	}
	
	@Override
	public boolean isConvertibleExpression() {
		return true;
	}
	
	@Override
	public String getName() {
		return node.toSource();
	}
	
}
