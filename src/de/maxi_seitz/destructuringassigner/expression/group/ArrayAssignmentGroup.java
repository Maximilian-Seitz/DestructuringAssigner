package de.maxi_seitz.destructuringassigner.expression.group;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;

import java.util.Map;
import java.util.TreeMap;

public class ArrayAssignmentGroup extends AssignmentGroup {
	
	private Map<Integer, AssignmentExpression> elementAssignments = new TreeMap<>();
	
	private String arrayIdentifier;
	
	
	public ArrayAssignmentGroup(String arrayIdentifier) {
		super(Type.ARRAY);
		
		this.arrayIdentifier = arrayIdentifier;
	}
	
	public String getArrayIdentifier() {
		return arrayIdentifier;
	}
	
	public boolean isElementIdPresent(int elementId) {
		return elementAssignments.containsKey(elementId);
	}
	
	public void addAssignment(AssignmentExpression assignment, int elementNumber) {
		elementAssignments.put(elementNumber, assignment);
	}
}
