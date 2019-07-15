package de.maxi_seitz.destructuringassigner.expression.source;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;
import de.maxi_seitz.destructuringassigner.expression.group.AssignmentGroup;
import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;

/**
 * Represents data source which is not suitable for destructuring.
 */
class NonConvertibleSourceExpression extends SourceExpression {
	
	@Override
	public boolean isConvertibleExpression() {
		return false;
	}
	
	@Override
	public boolean isTargetValidForDestructuring(TargetExpression target) {
		return false;
	}
	
	@Override
	public AssignmentGroup getAssignmentGroup() {
		return null;
	}
	
	@Override
	public boolean isCompatibleWithGroup(AssignmentGroup group) {
		return false;
	}
	
	@Override
	public void addAssignmentToGroup(AssignmentGroup group, AssignmentExpression assignment) { }
	
}
