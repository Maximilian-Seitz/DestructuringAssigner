package de.maxi_seitz.destructuringassigner.expression.assignment;

import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;

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
	
	@Override
	protected AstNode getContainingAstNode() {
		AstNode directParent = getGroupAstNode();
		
		if(directParent != null) {
			return directParent.getParent();
		}
		
		return null;
	}
	
	@Override
	protected AstNode getGroupAstNode() {
		return node.getParent();
	}
	
}
