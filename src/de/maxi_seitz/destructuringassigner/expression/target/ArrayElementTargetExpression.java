package de.maxi_seitz.destructuringassigner.expression.target;

import org.mozilla.javascript.ast.ElementGet;

class ArrayElementTargetExpression extends TargetExpression {
	
	private final ElementGet node;
	
	ArrayElementTargetExpression(ElementGet node) {
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
