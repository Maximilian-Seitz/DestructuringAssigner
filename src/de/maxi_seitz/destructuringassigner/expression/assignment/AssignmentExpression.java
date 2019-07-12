package de.maxi_seitz.destructuringassigner.expression.assignment;

import de.maxi_seitz.destructuringassigner.expression.ExpressionWrapper;
import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;
import de.maxi_seitz.destructuringassigner.expression.source.SourceExpression;

import org.mozilla.javascript.ast.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Wrapper for {@link AstNode},
 * to allow easier analysis.
 */
public abstract class AssignmentExpression extends ExpressionWrapper {
	
	private TargetExpression target;
	private SourceExpression source;
	
	public static List<AssignmentExpression> fromAstNode(AstNode node) {
		List<AssignmentExpression> assignments = new LinkedList<>();
		
		if(node != null) {
			if(node instanceof VariableDeclaration) {
				List<VariableInitializer> initializers = ((VariableDeclaration) node).getVariables();
				
				for(VariableInitializer initializer : initializers) {
					assignments.add(new InitializerExpression(initializer));
				}
			}
			
			if(node instanceof ExpressionStatement) {
				AstNode expression = ((ExpressionStatement) node).getExpression();
				
				if(expression instanceof Assignment) {
					assignments.add(new SetterExpression((Assignment) expression));
				} else {
					System.out.println("Test: " + expression.shortName());
				}
			}
		}
		
		return assignments;
	}
	
	public List<AssignmentExpression> getProceedingAssignments() {
		AstNode container = getContainingAstNode();
		
		if(container != null) {
			if(container.getFirstChild() != null) {
				AstNode previousNode = (AstNode) container.getChildBefore(getGroupAstNode());
				return AssignmentExpression.fromAstNode(previousNode);
			}
		}
		
		return null;
	}
	
	public boolean isConvertibleExpression() {
		return source.isConvertibleExpression() &&
				   target.isConvertibleExpression() &&
				   source.isTargetValidForDestructoring(target) &&
				   getContainingAstNode() != null &&
				   getContainingAstNode().getFirstChild() != null;
	}
	
	public boolean isFirstInList() {
		List<AssignmentExpression> proceedingAssignments = getProceedingAssignments();
		
		for(AssignmentExpression proceedingAssignment : proceedingAssignments) {
			if(proceedingAssignment.isConvertibleExpression()) {
				return false;
			}
		}
		
		return true;
	}
	
	public abstract void remove();
	
	public String getTargetString() {
		return target.toString();
	}
	
	public String getSourceString() {
		return source.toString();
	}
	
	@Override
	public String toString() {
		return getTargetString() + " = " + getSourceString();
	}
	
	
	protected void setTargetExpression(AstNode node) {
		target = TargetExpression.fromAstNode(node);
	}
	
	protected void setSourceExpression(AstNode node) {
		source = SourceExpression.fromAstNode(node);
	}
	
	
	protected abstract AstNode getContainingAstNode();
	
	protected abstract AstNode getGroupAstNode();
}
