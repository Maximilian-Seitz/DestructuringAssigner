package de.maxi_seitz.destructuringassigner.expression.target;

import org.mozilla.javascript.ast.AstNode;

import org.mozilla.javascript.Token;

class NonConvertibleTargetExpression extends TargetExpression {
	
	NonConvertibleTargetExpression(AstNode node) {
		if(node != null) {
			System.out.println(Token.typeToName(node.getType()) + " node (" + node.getClass().getName() + ") is no convertible target expression.");
		}
	}
	
	@Override
	public boolean isConvertibleExpression() {
		return false;
	}
	
	@Override
	public String getName() {
		return null;
	}
	
}
