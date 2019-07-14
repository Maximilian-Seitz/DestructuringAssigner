package de.maxi_seitz.destructuringassigner.expression.assignment;

import de.maxi_seitz.destructuringassigner.expression.DeclarationType;
import org.mozilla.javascript.ast.*;

/**
 * Wrapper for {@link Assignment},
 * to allow easier analysis.
 */
class SetterExpression extends AssignmentExpression {
	
	private Assignment node;
	
	protected SetterExpression(Assignment node) {
		this.node = node;
		setTargetExpression(node.getLeft());
		setSourceExpression(node.getRight());
	}
	
	@Override
	public void remove() {
		getContainingAstNode().removeChild(getGroupAstNode());
	}
	
	@Override
	protected DeclarationType getDeclarationType() {
		return DeclarationType.NONE;
	}
	
	@Override
	protected void replaceWithDestructuringAssignment(AstNode destructuringAssignment) {
		ExpressionStatement expressionStatement = (ExpressionStatement) getGroupAstNode();
		expressionStatement.setExpression(destructuringAssignment);
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
