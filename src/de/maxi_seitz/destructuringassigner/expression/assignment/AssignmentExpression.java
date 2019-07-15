package de.maxi_seitz.destructuringassigner.expression.assignment;

import de.maxi_seitz.destructuringassigner.expression.DeclarationType;
import de.maxi_seitz.destructuringassigner.expression.ExpressionWrapper;
import de.maxi_seitz.destructuringassigner.expression.group.AssignmentGroup;
import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;
import de.maxi_seitz.destructuringassigner.expression.source.SourceExpression;

import org.mozilla.javascript.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for {@link AstNode} representing an assignment.
 * Allows for higher level analysis and processing.
 */
public abstract class AssignmentExpression extends ExpressionWrapper {
	
	private TargetExpression target;
	private SourceExpression source;
	
	/**
	 * Check if direct children of {@link AstNode} are assignments,
	 * returning an {@link AssignmentExpression} for each one that is.
	 * <br/>
	 * Compatible with {@link VariableDeclaration} and {@link ExpressionStatement},
	 * converting the single declarations and assignments within.
	 * @param node {@link AstNode} in AST
	 * @return {@link List} of {@link AssignmentExpression assignment expressions}
	 * directly contained within {@link AstNode}.
	 */
	public static List<AssignmentExpression> fromAstNode(AstNode node) {
		List<AssignmentExpression> assignments = new ArrayList<>();
		
		if(node != null) {
			if(node instanceof VariableDeclaration) {
				//A declaration can contain a number of assignments (e.g. var a = 0, b = 3;)
				List<VariableInitializer> initializers = ((VariableDeclaration) node).getVariables();
				
				for(VariableInitializer initializer : initializers) {
					assignments.add(new InitializerExpression(initializer));
				}
			}
			
			if(node instanceof ExpressionStatement) {
				//An expression can only contain a single assignment
				AstNode expression = ((ExpressionStatement) node).getExpression();
				
				if(expression instanceof Assignment) {
					assignments.add(new SetterExpression((Assignment) expression));
				}
			}
		}
		
		return assignments;
	}
	
	/**
	 * Check if assignment, without considering the context,
	 * can be compressed into destructuring assignment.
	 * @return <code>true</code>, when expression can be converted.
	 */
	public boolean isConvertibleExpression() {
		return source.isConvertibleExpression() &&
				   target.isConvertibleExpression() &&
				   source.isTargetValidForDestructuring(target) &&
				   getContainingAstNode() != null &&	//is within a "container"
				   getContainingAstNode().hasChildren();//"container" has list of children (excludes inline expressions, like in first line of a for-loop
	}
	
	/**
	 * Check if this assignment is the first in a list of assignments,
	 * making it possible to define this list by this element.
	 * @return <code>true</code> if proceeding expression is no assignment,
	 * capable of being compressed
	 */
	public boolean isFirstInList() {
		AssignmentExpression proceedingAssignment = getProceedingAssignment();
		
		if(proceedingAssignment != null) {
			//if proceeding assignment is convertible, this isn't the start of the group
			return !proceedingAssignment.isConvertibleExpression();
		} else {
			return true;
		}
	}
	
	/**
	 * Cleanly removes assignment from AST,
	 * putting this {@link AssignmentExpression} into a state
	 * where it should not be kept/used anymore.
	 */
	public abstract void remove();
	
	/**
	 * Goes through list of following assignments,
	 * compressing them into destructuring assignments,
	 * where possible.
	 */
	public void groupFollowingAssignments() {
		//Generate new group, which holds assignments which can be compressed to a single expression
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
						//if next assignment isn't within this group,
						//but itself convertible, it'll start it's own group
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
	
	/**
	 * @return The {@link AstNode} containing "top level"
	 * expressions, like {@link VariableDeclaration},
	 * which, themselves, contain assignments
	 */
	protected abstract AstNode getContainingAstNode();
	
	/**
	 * @return The {@link AstNode} containing the assignment node itself
	 */
	protected abstract AstNode getGroupAstNode();
	
	/**
	 * @return Assignment directly proceeding this one
	 */
	protected abstract AssignmentExpression getProceedingAssignment();
	
	/**
	 * @return Assignment directly following this one
	 */
	protected abstract AssignmentExpression getFollowingAssignment();
	
	
	/**
	 * @return New {@link AssignmentGroup} compatible with this assignment
	 */
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
