package de.maxi_seitz.destructuringassigner.expression.group;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Name;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ArrayAssignmentGroup extends AssignmentGroup {
	
	private final Map<Integer, AssignmentExpression> elementAssignments = new TreeMap<>();
	
	private final String arrayIdentifier;
	
	private AssignmentExpression firstAssignment;
	
	private int biggestElementNumber = 0;
	
	
	public ArrayAssignmentGroup(String arrayIdentifier) {
		super(Type.ARRAY);
		
		this.arrayIdentifier = arrayIdentifier;
	}
	
	public String getIdentifier() {
		return arrayIdentifier;
	}
	
	public boolean isElementIdPresent(int elementId) {
		return elementAssignments.containsKey(elementId);
	}
	
	public void addAssignment(AssignmentExpression assignment, int elementNumber) {
		if(firstAssignment == null) {
			firstAssignment = assignment;
		}
		
		if(elementNumber > biggestElementNumber) {
			biggestElementNumber = elementNumber;
		}
		
		elementAssignments.put(elementNumber, assignment);
	}
	
	@Override
	public void compressToDestructuringAssignment() {
		if(elementAssignments.size() > 1) {
			ArrayLiteral destructuringTarget = new ArrayLiteral();
			destructuringTarget.setIsDestructuring(true);	//not sure if needed
			
			List<AstNode> targets = new ArrayList<>(biggestElementNumber);
			
			for(int i = 0; i <= biggestElementNumber; i++) {
				AssignmentExpression assignment = elementAssignments.get(i);
				
				if(assignment != null) {
					targets.add(assignment.getTargetNode());
					
					if(assignment != firstAssignment) {
						assignment.remove();
					}
				} else {
					Name skipElement = new Name();
					skipElement.setIdentifier("");
					targets.add(skipElement);
				}
			}
			
			destructuringTarget.setElements(targets);
			
			
			Name destructuringSource = new Name();
			destructuringSource.setIdentifier(arrayIdentifier);
			
			
			firstAssignment.setSourceNode(destructuringSource);
			firstAssignment.setTargetNode(destructuringTarget);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(Map.Entry<Integer, AssignmentExpression> elementAssignment : elementAssignments.entrySet()) {
			sb.append("\t");
			sb.append(elementAssignment.getKey());
			sb.append(": ");
			sb.append(elementAssignment.getValue());
			sb.append('\n');
		}
		
		return sb.toString();
	}
}
