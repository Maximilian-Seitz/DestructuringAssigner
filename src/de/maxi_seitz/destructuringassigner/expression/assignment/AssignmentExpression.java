package de.maxi_seitz.destructuringassigner.expression.assignment;

import de.maxi_seitz.destructuringassigner.expression.DeclarationType;
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
		AssignmentExpression proceedingAssignment = getProceedingAssignment();
		
		if(proceedingAssignment != null) {
			//if proceeding assignment is convertible, this isn't the start of the group
			return !proceedingAssignment.isConvertibleExpression();
		} else {
			return true;
		}
	}
	
	public abstract void remove();
	
	public void groupFollowingAssignments() {
		AssignmentGroup group = makeGroup();
		
		//The assignment creating a group is always compatible with it, so no check is necessary
		this.addToGroup(group);
		
		boolean isCurrentElementInGroup = true;
		AssignmentExpression currentAssignment = this;
		while(isCurrentElementInGroup) {
			currentAssignment = currentAssignment.getFollowingAssignment();
			
			if(currentAssignment != null) {
				isCurrentElementInGroup = currentAssignment.isCompatibleWithGroup(group);
				
				if(isCurrentElementInGroup) {
					currentAssignment.addToGroup(group);
				} else {
					if(currentAssignment.isConvertibleExpression()) {
						currentAssignment.groupFollowingAssignments();
					}
				}
			} else {
				isCurrentElementInGroup = false;
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
	
	@Override
	public String toString() {
		return target.toString() + " = " + source.toString();
	}
	
	
	protected void setTargetExpression(AstNode node) {
		target = TargetExpression.fromAstNode(node);
	}
	
	protected void setSourceExpression(AstNode node) {
		source = SourceExpression.fromAstNode(node);
	}
	
	protected SourceExpression getSourceExpression() {
		return source;
	}
	
	protected AssignmentExpression getAssignmentProceedingGroup() {
		AstNode previousNode = (AstNode) getContainingAstNode().getChildBefore(getGroupAstNode());
		List<AssignmentExpression> proceedingAssignments = AssignmentExpression.fromAstNode(previousNode);
		int lastProceedingAssignmentId = proceedingAssignments.size() - 1;
		
		if(lastProceedingAssignmentId >= 0) {
			return proceedingAssignments.get(lastProceedingAssignmentId);
		} else {
			return null;
		}
	}
	
	protected AssignmentExpression getAssignmentFollowingGroup() {
		AstNode nextNode = (AstNode) getGroupAstNode().getNext();
		List<AssignmentExpression> followingAssignments = AssignmentExpression.fromAstNode(nextNode);
		
		if(followingAssignments.size() > 0) {
			return followingAssignments.get(0);
		} else {
			return null;
		}
	}
	
	
	protected abstract void replaceWithDestructuringAssignment(AstNode destructuringAssignment);
	
	protected abstract DeclarationType getDeclarationType();
	
	protected abstract AstNode getContainingAstNode();
	
	protected abstract AstNode getGroupAstNode();
	
	protected abstract AssignmentExpression getProceedingAssignment();
	
	protected abstract AssignmentExpression getFollowingAssignment();
	
	
	private AssignmentGroup makeGroup() {
		AssignmentGroup group = source.getAssignmentGroup();
		
		group.setDeclarationType(getDeclarationType());
		
		return group;
	}
	
	private boolean isCompatibleWithGroup(AssignmentGroup group) {
		if(getDeclarationType() == group.getDeclarationType()) {
			return getSourceExpression().isCompatibleWithGroup(group);
		}
		
		return false;
	}
	
	private void addToGroup(AssignmentGroup group) {
		source.addAssignmentToGroup(group, this);
	}
}
