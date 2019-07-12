package de.maxi_seitz.destructuringassigner.expression.assignment;

import org.mozilla.javascript.ast.AstNode;
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
