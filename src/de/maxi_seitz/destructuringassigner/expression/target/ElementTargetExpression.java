package de.maxi_seitz.destructuringassigner.expression.target;

import org.mozilla.javascript.ast.ElementGet;

class ElementTargetExpression extends TargetExpression {
	
	private final ElementGet node;
	
	ElementTargetExpression(ElementGet node) {
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
