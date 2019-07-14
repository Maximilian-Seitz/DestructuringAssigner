package de.maxi_seitz.destructuringassigner.expression.target;

import org.mozilla.javascript.ast.Name;

class VariableTargetExpression extends TargetExpression {
	
	private final Name node;
	
	protected VariableTargetExpression(Name node) {
		super(node);
		
		this.node = node;
	}
	
	@Override
	public boolean isConvertibleExpression() {
		return true;
	}
	
	@Override
	public String getName() {
		return node.getString();
	}
	
	@Override
	public String toString() {
		return node.getString();
	}
	
}
