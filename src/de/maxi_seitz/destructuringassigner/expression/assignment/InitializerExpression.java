package de.maxi_seitz.destructuringassigner.expression.assignment;

import de.maxi_seitz.destructuringassigner.expression.DeclarationType;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;

import java.util.List;

/**
 * Wrapper for assignment as part of a variable initialisation.
 */
class InitializerExpression extends AssignmentExpression {
	
	private VariableInitializer node;
	
	protected InitializerExpression(VariableInitializer node) {
		this.node = node;
		setTargetExpression(node.getTarget());
		setSourceExpression(node.getInitializer());
	}
	
	@Override
	public void remove() {
		VariableDeclaration declaration = (VariableDeclaration) getGroupAstNode();
		
		List<VariableInitializer> initializers = declaration.getVariables();
		
		if(initializers.size() > 1) {
			initializers.remove(node);
		} else {
			getContainingAstNode().removeChild(declaration);
		}
	}
	
	@Override
	protected DeclarationType getDeclarationType() {
		VariableDeclaration declaration = (VariableDeclaration) getGroupAstNode();
		
		if(declaration.isVar()) {
			return DeclarationType.VAR;
		} else if(declaration.isConst()) {
			return DeclarationType.CONST;
		} else if(declaration.isLet()) {
			return DeclarationType.LET;
		} else {
			throw new IllegalStateException("Invalid declaration type in line " + declaration.getLineno());
		}
	}
	
	@Override
	protected void replaceWithDestructuringAssignment(AstNode destructuringAssignment) {
		VariableDeclaration declaration = (VariableDeclaration) getGroupAstNode();
		
		List<VariableInitializer> initializers = declaration.getVariables();
		int assignmentNumber = initializers.indexOf(node);
		
		VariableInitializer initializer = new VariableInitializer();
		initializer.setTarget(destructuringAssignment);
		
		initializers.set(assignmentNumber, initializer);
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
	
	@Override
	protected AssignmentExpression getProceedingAssignment() {
		int previousAssignmentNumber = getIndexInDeclaration() - 1;
		
		if(previousAssignmentNumber >= 0) {
			List<AssignmentExpression> groupAssignments = AssignmentExpression.fromAstNode(getGroupAstNode());
			return groupAssignments.get(previousAssignmentNumber);
		} else {
			return getAssignmentProceedingGroup();
		}
	}
	
	@Override
	protected AssignmentExpression getFollowingAssignment() {
		int nextAssignmentNumber = getIndexInDeclaration() + 1;
		
		if(nextAssignmentNumber < getNumberOfAssignmentsInDeclaration()) {
			List<AssignmentExpression> groupAssignments = AssignmentExpression.fromAstNode(getGroupAstNode());
			return groupAssignments.get(nextAssignmentNumber);
		} else {
			return getAssignmentFollowingGroup();
		}
	}
	
	private int getIndexInDeclaration() {
		VariableDeclaration declaration = (VariableDeclaration) getGroupAstNode();
		
		List<VariableInitializer> initializers = declaration.getVariables();
		return initializers.indexOf(node);
	}
	
	private int getNumberOfAssignmentsInDeclaration() {
		return ((VariableDeclaration) getGroupAstNode()).getVariables().size();
	}
}
