package de.maxi_seitz.destructuringassigner.expression.assignment;

import org.mozilla.javascript.ast.Assignment;

/**
 * Wrapper for {@link Assignment},
 * to allow easier analysis.
 */
class SetterExpression extends AssignmentExpression {
	
	private Assignment node;
	
	SetterExpression(Assignment node) {
		this.node = node;
		setTargetExpression(node.getLeft());
		setSourceExpression(node.getRight());
	}
	
}
