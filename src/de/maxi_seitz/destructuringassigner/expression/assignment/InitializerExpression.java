package de.maxi_seitz.destructuringassigner.expression.assignment;

import org.mozilla.javascript.ast.VariableInitializer;

/**
 * Wrapper for {@link VariableInitializer},
 * to allow easier analysis.
 */
class InitializerExpression extends AssignmentExpression {
	
	private VariableInitializer node;
	
	InitializerExpression(VariableInitializer node) {
		this.node = node;
		setTargetExpression(node.getTarget());
		setSourceExpression(node.getInitializer());
	}
	
}
