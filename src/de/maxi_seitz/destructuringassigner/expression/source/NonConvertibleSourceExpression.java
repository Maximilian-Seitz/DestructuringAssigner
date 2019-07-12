package de.maxi_seitz.destructuringassigner.expression.source;

import org.mozilla.javascript.ast.AstNode;

import org.mozilla.javascript.Token;

class NonConvertibleSourceExpression extends SourceExpression {
	
	NonConvertibleSourceExpression(AstNode node) {
		if(node != null) {
			System.out.println(Token.typeToName(node.getType()) + " node (" + node.getClass().getName() + ") is no convertible source expression.");
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
