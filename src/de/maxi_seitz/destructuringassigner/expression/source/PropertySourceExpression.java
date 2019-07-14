package de.maxi_seitz.destructuringassigner.expression.source;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;
import de.maxi_seitz.destructuringassigner.expression.group.AssignmentGroup;
import de.maxi_seitz.destructuringassigner.expression.group.ObjectAssignmentGroup;
import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;
import org.mozilla.javascript.ast.PropertyGet;

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
	public boolean isTargetValidForDestructoring(TargetExpression target) {
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
		return group.getType() == AssignmentGroup.Type.OBJECT;
	}
	
	@Override
	public void addAssignmentToGroup(AssignmentGroup group, AssignmentExpression assignment) {
		ObjectAssignmentGroup objectAssignmentGroup = (ObjectAssignmentGroup) group;
		objectAssignmentGroup.addAssignment(assignment);
	}
	
	@Override
	public String toString() {
		return node.toSource();
	}
	
	private String getObjectIdentifier() {
		return node.getTarget().toSource();
	}
	
}
