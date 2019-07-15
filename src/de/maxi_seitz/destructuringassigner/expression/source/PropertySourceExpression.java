package de.maxi_seitz.destructuringassigner.expression.source;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;
import de.maxi_seitz.destructuringassigner.expression.group.AssignmentGroup;
import de.maxi_seitz.destructuringassigner.expression.group.ObjectAssignmentGroup;
import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.PropertyGet;

/**
 * Wrapper for {@link AstNode}, which accesses object property,
 * and is used as a source for an assignment.
 */
class PropertySourceExpression extends SourceExpression {
	
	private final PropertyGet node;
	
	protected PropertySourceExpression(PropertyGet node) {
		this.node = node;
	}
	
	@Override
	public boolean isConvertibleExpression() {
		return isSideEffectFree(node);
	}
	
	@Override
	public boolean isTargetValidForDestructuring(TargetExpression target) {
		if(target != null) {
			String targetName = target.getName();
			String propertyName = node.getProperty().getIdentifier();
			
			return propertyName.equals(targetName);
		}
		
		return false;
	}
	
	@Override
	public AssignmentGroup getAssignmentGroup() {
		return new ObjectAssignmentGroup(getObjectIdentifier());
	}
	
	@Override
	public boolean isCompatibleWithGroup(AssignmentGroup group) {
		if(group.getType() == AssignmentGroup.Type.OBJECT) {
			ObjectAssignmentGroup objectAssignmentGroup = (ObjectAssignmentGroup) group;
			String arrayIdentifier = objectAssignmentGroup.getIdentifier();
			
			return getObjectIdentifier().equals(arrayIdentifier);
		}
		
		return false;
	}
	
	@Override
	public void addAssignmentToGroup(AssignmentGroup group, AssignmentExpression assignment) {
		ObjectAssignmentGroup objectAssignmentGroup = (ObjectAssignmentGroup) group;
		objectAssignmentGroup.addAssignment(assignment);
	}
	
	private String getObjectIdentifier() {
		return node.getTarget().toSource();
	}
	
}
