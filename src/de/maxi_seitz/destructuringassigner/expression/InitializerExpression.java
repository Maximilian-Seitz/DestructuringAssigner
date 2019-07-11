package de.maxi_seitz.destructuringassigner.expression;

import org.mozilla.javascript.ast.VariableInitializer;

/**
 * Wrapper for {@link VariableInitializer},
 * to allow easier analysis.
 */
public class InitializerExpression extends AssignmentExpression {
	
	private VariableInitializer node;
	
	InitializerExpression(VariableInitializer node) {
		this.node = node;
	}
	
	@Override
	public boolean isSettingVariable() {
		return !node.isDestructuring();
	}
	
	@Override
	public String getVariableName() {
		return node.getTarget().getString();
	}
	
}
