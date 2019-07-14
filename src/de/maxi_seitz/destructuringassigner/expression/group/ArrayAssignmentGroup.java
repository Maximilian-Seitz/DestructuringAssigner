package de.maxi_seitz.destructuringassigner.expression.group;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.Assignment;
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
	public boolean isCompressible() {
		return elementAssignments.size() > 1;
	}
	
	@Override
	protected AstNode generateAssignmentNode(AstNode sourceNode, AstNode targetNode) {
		Name targetAssignment = new Name();
		targetAssignment.setIdentifier(targetNode.toSource() + " = " + sourceNode.toSource());
		
		return targetAssignment;
	}
	
	@Override
	protected AstNode getSourceNode() {
		Name sourceNode = new Name();
		sourceNode.setIdentifier(arrayIdentifier);
		
		return sourceNode;
	}
	
	@Override
	protected AstNode groupDestructoringTargetNode() {
		ArrayLiteral target = new ArrayLiteral();
		target.setIsDestructuring(true);	//not sure if needed
		
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
		
		target.setElements(targets);
		
		return target;
	}
}
