package de.maxi_seitz.destructuringassigner.expression.group;

import de.maxi_seitz.destructuringassigner.expression.DeclarationType;
import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Name;

import java.util.LinkedList;
import java.util.List;

/**
 * Group of assignments with object as their source.
 */
public class ObjectAssignmentGroup extends AssignmentGroup {
	
	private List<AssignmentExpression> assignments = new LinkedList<>();
	
	private String objectIdentifier;
	
	
	public ObjectAssignmentGroup(String objectIdentifier) {
		super(Type.OBJECT);
		
		this.objectIdentifier = objectIdentifier;
	}
	
	public String getIdentifier() {
		return objectIdentifier;
	}
	
	public void addAssignment(AssignmentExpression assignment) {
		assignments.add(assignment);
	}
	
	@Override
	public boolean isCompressible() {
		return assignments.size() > 1;
	}
	
	@Override
	protected AstNode generateAssignmentNode(AstNode sourceNode, AstNode targetNode) {
		Name targetAssignment = new Name();
		
		if(getDeclarationType() == DeclarationType.NONE) {
			targetAssignment.setIdentifier("(" + targetNode.toSource() + " = " + sourceNode.toSource() + ")");
		} else {
			targetAssignment.setIdentifier(targetNode.toSource() + " = " + sourceNode.toSource());
		}
		
		return targetAssignment;
	}
	
	@Override
	protected AstNode getSourceNode() {
		Name sourceNode = new Name();
		sourceNode.setIdentifier(objectIdentifier);
		
		return sourceNode;
	}
	
	@Override
	protected AstNode groupDestructuringTargetNode() {
		Name targetNode = new Name();
		
		StringBuilder targetBuilder = new StringBuilder();
		targetBuilder.append('{');
		
		boolean isFirstAssignment = true;
		for(AssignmentExpression assignment : assignments) {
			String property = assignment.getTargetNode().toSource();
			
			if(isFirstAssignment) {
				isFirstAssignment = false;
			} else {
				assignment.remove();
				
				targetBuilder.append(", ");
			}
			
			targetBuilder.append(property);
		}
		
		targetBuilder.append('}');
		
		targetNode.setIdentifier(targetBuilder.toString());
		
		return targetNode;
	}
}
