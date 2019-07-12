package de.maxi_seitz.destructuringassigner.expression.target;

import org.mozilla.javascript.ast.PropertyGet;

class PropertyTargetExpression extends TargetExpression {
	
	private final PropertyGet node;
	
	PropertyTargetExpression(PropertyGet node) {
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
