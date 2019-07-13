package de.maxi_seitz.destructuringassigner.expression.source;

import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;
import de.maxi_seitz.destructuringassigner.expression.group.AssignmentGroup;
import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;
import org.mozilla.javascript.ast.AstNode;

class NonConvertibleSourceExpression extends SourceExpression {
	
	NonConvertibleSourceExpression(AstNode node) {
		if(node != null) {
			//System.out.println(Token.typeToName(node.getType()) + " node (" + node.getClass().getName() + ") is no convertible source expression.");
		}
	}
	
	@Override
	public boolean isConvertibleExpression() {
		return false;
	}
	
	@Override
	public boolean isTargetValidForDestructoring(TargetExpression target) {
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
	public void addAssignmentToGroup(AssignmentGroup group, AssignmentExpression assignment) {
	
	}
	
}
