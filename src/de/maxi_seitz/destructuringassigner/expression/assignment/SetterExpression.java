package de.maxi_seitz.destructuringassigner.expression.assignment;

import org.mozilla.javascript.ast.*;

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
	public void setSourceNode(AstNode sourceNode) {
		node.setRight(sourceNode);
		
		setSourceExpression(sourceNode);
	}
	
	@Override
	public void setTargetNode(AstNode targetNode) {
		node.setLeft(targetNode);
		
		setTargetExpression(targetNode);
	}
	
	@Override
	public void remove() {
		getContainingAstNode().removeChild(getGroupAstNode());
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
