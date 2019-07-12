package de.maxi_seitz.destructuringassigner.expression.source;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;

import org.mozilla.javascript.Token;

public abstract class SourceExpression {
	
	public static SourceExpression fromAstNode(AstNode node) {
		if(node != null) {
			int tokenType = node.getType();
			
			if(tokenType == Token.GETELEM) {
				if(node instanceof ElementGet) {
					return new ArrayElementSourceExpression((ElementGet) node);
				}
			}
		}
		
		return new NonConvertibleSourceExpression(node);
	}
	
	public abstract boolean isConvertibleExpression();
	public abstract String getName();
	
}
