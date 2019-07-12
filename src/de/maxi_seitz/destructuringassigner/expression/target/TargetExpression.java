package de.maxi_seitz.destructuringassigner.expression.target;

import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.Name;

import org.mozilla.javascript.Token;

public abstract class TargetExpression {
	
	public static TargetExpression fromAstNode(AstNode node) {
		if(node != null) {
			int tokenType = node.getType();
			
			if (tokenType == Token.NAME || tokenType == Token.ARRAYLIT || tokenType == Token.GETELEM) {
				if (node instanceof Name) {
					return new VariableTargetExpression((Name) node);
				}
				
				if (node instanceof ArrayLiteral) {
					return new ArrayLiteralTargetExpression((ArrayLiteral) node);
				}
				
				if (node instanceof ElementGet) {
					return new ArrayElementTargetExpression((ElementGet) node);
				}
			}
		}
		
		return new NonConvertibleTargetExpression(node);
	}
	
	public abstract boolean isConvertibleExpression();
	public abstract String getName();
	
}
