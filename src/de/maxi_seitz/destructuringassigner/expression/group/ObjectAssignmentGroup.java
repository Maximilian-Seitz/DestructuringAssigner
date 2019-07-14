package de.maxi_seitz.destructuringassigner.expression.group;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;

import java.util.LinkedList;
import java.util.List;

public class ObjectAssignmentGroup extends AssignmentGroup {
	
	private List<AssignmentExpression> assignments = new LinkedList<>();
	
	private String objectIdentifier;
	
	private AssignmentExpression firstAssignment;
	
	
	public ObjectAssignmentGroup(String objectIdentifier) {
		super(Type.OBJECT);
		
		this.objectIdentifier = objectIdentifier;
	}
	
	public void addAssignment(AssignmentExpression assignment) {
		if(firstAssignment == null) {
			firstAssignment = assignment;
		}
		
		assignments.add(assignment);
	}
	
	@Override
	public void compressToDestructuringAssignment() {
		if(assignments.size() > 1) {
			Name destructuringTarget = new Name();
			
			StringBuilder targetBuilder = new StringBuilder();
			
			targetBuilder.append('{');
			
			boolean isFirstElement = true;
			for(AssignmentExpression assignment : assignments) {
				String property = assignment.getTargetNode().toSource();
				
				if(isFirstElement) {
					isFirstElement = false;
				} else {
					targetBuilder.append(", ");
				}
				
				targetBuilder.append(property);
				
				if(assignment != firstAssignment) {
					assignment.remove();
				}
			}
			
			targetBuilder.append('}');
			
			destructuringTarget.setIdentifier(targetBuilder.toString());
			
			
			Name destructuringSource = new Name();
			destructuringSource.setIdentifier(objectIdentifier);
			
			
			firstAssignment.setSourceNode(destructuringSource);
			firstAssignment.setTargetNode(destructuringTarget);
		}
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
