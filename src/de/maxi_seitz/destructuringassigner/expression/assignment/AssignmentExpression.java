package de.maxi_seitz.destructuringassigner.expression.assignment;

import de.maxi_seitz.destructuringassigner.expression.ExpressionWrapper;
import de.maxi_seitz.destructuringassigner.expression.group.AssignmentGroup;
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
	
	public boolean isConvertibleExpression() {
		return source.isConvertibleExpression() &&
				   target.isConvertibleExpression() &&
				   source.isTargetValidForDestructoring(target) &&
				   getContainingAstNode() != null &&	//is within a "container"
				   getContainingAstNode().hasChildren();//"container" has list of children (excludes inline expressions, like in first line of a for-loop
	}
	
	public boolean isFirstInList() {
		List<AssignmentExpression> proceedingAssignments = getProceedingAssignments();
		int lastProceedingAssignmentId = proceedingAssignments.size() - 1;
		
		if(lastProceedingAssignmentId >= 0) {
			AssignmentExpression lastProceedingAssignment = proceedingAssignments.get(lastProceedingAssignmentId);
			
			//if directly proceeding assignment is convertible, this isn't the start of the group
			return !lastProceedingAssignment.isConvertibleExpression();
		} else {
			return true;
		}
	}
	
	public abstract void remove();
	
	public void groupFollowingAssignments() {
		AssignmentGroup group = source.getAssignmentGroup();
		
		//The assignment creating a group is always compatible with it, so no check is necessary
		addToGroup(group);
		
		boolean isCurrentElementInGroup = true;
		AssignmentExpression currentAssignment = this;
		while(isCurrentElementInGroup) {
			List<AssignmentExpression> nextAssignments = currentAssignment.getFollowingAssignments();
			
			for(AssignmentExpression nextAssignment : nextAssignments) {
				isCurrentElementInGroup = nextAssignment.isCompatibleWithGroup(group);
				
				if(isCurrentElementInGroup) {
					nextAssignment.addToGroup(group);
					currentAssignment = nextAssignment;
				} else {
					if(nextAssignment.isConvertibleExpression()) {
						nextAssignment.groupFollowingAssignments();
					}
					
					break;
				}
			}
		}
		
		if(group.isCompressible()) {
			AstNode destructuringAssignment = group.compressToDestructuringAssignment();
			
			this.replaceWithDestructuringAssignment(destructuringAssignment);
		}
	}
	
	public AstNode getTargetNode() {
		return target.getNode();
	}
	
	
	protected void setTargetExpression(AstNode node) {
		target = TargetExpression.fromAstNode(node);
	}
	
	protected void setSourceExpression(AstNode node) {
		source = SourceExpression.fromAstNode(node);
	}
	
	
	protected abstract void replaceWithDestructuringAssignment(AstNode destructuringAssignment);
	
	protected abstract AstNode getContainingAstNode();
	
	protected abstract AstNode getGroupAstNode();
	
	
	private List<AssignmentExpression> getProceedingAssignments() {
		AstNode previousNode = (AstNode) getContainingAstNode().getChildBefore(getGroupAstNode());
		return AssignmentExpression.fromAstNode(previousNode);
	}
	
	private List<AssignmentExpression> getFollowingAssignments() {
		AstNode nextNode = (AstNode) getGroupAstNode().getNext();
		return AssignmentExpression.fromAstNode(nextNode);
	}
	
	private boolean isCompatibleWithGroup(AssignmentGroup group) {
		return source.isCompatibleWithGroup(group);
	}
	
	private void addToGroup(AssignmentGroup group) {
		source.addAssignmentToGroup(group, this);
	}
}
