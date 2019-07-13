package de.maxi_seitz.destructuringassigner.expression.group;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;

import java.util.LinkedList;
import java.util.List;

public class ObjectAssignmentGroup extends AssignmentGroup {
	
	private List<AssignmentExpression> assignments = new LinkedList<>();
	
	public ObjectAssignmentGroup() {
		super(Type.OBJECT);
	}
	
	public void addAssignment(AssignmentExpression assignmentExpression) {
		assignments.add(assignmentExpression);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(AssignmentExpression propertyAssignment : assignments) {
			sb.append("\t");
			sb.append(propertyAssignment);
			sb.append('\n');
		}
		
		return sb.toString();
	}
}
