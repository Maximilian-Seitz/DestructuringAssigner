package de.maxi_seitz.destructuringassigner.expression.source;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;
import de.maxi_seitz.destructuringassigner.expression.group.ArrayAssignmentGroup;
import de.maxi_seitz.destructuringassigner.expression.group.AssignmentGroup;
import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.NumberLiteral;

/**
 * Wrapper for {@link AstNode}, which accesses array element,
 * and is used as a source for an assignment.
 */
class ElementSourceExpression extends SourceExpression {
	
	private final ElementGet node;
	
	protected ElementSourceExpression(ElementGet node) {
		this.node = node;
	}
	
	@Override
	public boolean isConvertibleExpression() {
		boolean hasIntegerElementId = false;
		
		AstNode arrayElement = node.getElement();
		if(arrayElement instanceof NumberLiteral) {
			NumberLiteral arrayElementNumber = (NumberLiteral) arrayElement;
			hasIntegerElementId = arrayElementNumber.getNumber() % 1.0f == 0.0f;
		}
		
		return hasIntegerElementId && isSideEffectFree(node);
	}
	
	@Override
	public boolean isTargetValidForDestructuring(TargetExpression target) {
		return true;
	}
	
	@Override
	public AssignmentGroup getAssignmentGroup() {
		return new ArrayAssignmentGroup(getArrayIdentifier());
	}
	
	@Override
	public boolean isCompatibleWithGroup(AssignmentGroup group) {
		if(group.getType() == AssignmentGroup.Type.ARRAY) {
			ArrayAssignmentGroup arrayAssignmentGroup = (ArrayAssignmentGroup) group;
			String arrayIdentifier = arrayAssignmentGroup.getIdentifier();
			
			if(getArrayIdentifier().equals(arrayIdentifier)) {
				return !arrayAssignmentGroup.isElementIdPresent(getArrayElementNumber());
			}
		}
		
		return false;
	}
	
	@Override
	public void addAssignmentToGroup(AssignmentGroup group, AssignmentExpression assignment) {
		ArrayAssignmentGroup arrayAssignmentGroup = (ArrayAssignmentGroup) group;
		arrayAssignmentGroup.addAssignment(assignment, getArrayElementNumber());
	}
	
	private String getArrayIdentifier() {
		return node.getTarget().toSource();
	}
	
	private int getArrayElementNumber() {
		NumberLiteral arrayElement = (NumberLiteral) node.getElement();
		return Integer.parseInt(arrayElement.getValue());
	}
}
