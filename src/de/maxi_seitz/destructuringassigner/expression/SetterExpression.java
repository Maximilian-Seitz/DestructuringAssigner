package de.maxi_seitz.destructuringassigner.expression;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;

public class SetterExpression extends AssignmentExpression {
	
	private Assignment node;
	
	SetterExpression(Assignment node) {
		this.node = node;
	}
	
	@Override
	public boolean isSettingVariable() {
		AstNode setExpression = node.getLeft();
		return setExpression.getType() == Token.NAME;
	}
	
	@Override
	public String getVariableName() {
		return node.getLeft().getString();
	}
}
