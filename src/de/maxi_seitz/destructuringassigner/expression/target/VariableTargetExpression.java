package de.maxi_seitz.destructuringassigner.expression.target;

import org.mozilla.javascript.ast.Name;

class VariableTargetExpression extends TargetExpression {
	
	private final Name node;
	
	VariableTargetExpression(Name node) {
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
	
}
