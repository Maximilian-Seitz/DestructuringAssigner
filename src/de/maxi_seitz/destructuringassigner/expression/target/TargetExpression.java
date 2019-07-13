package de.maxi_seitz.destructuringassigner.expression.target;

import de.maxi_seitz.destructuringassigner.expression.ExpressionWrapper;
import de.maxi_seitz.destructuringassigner.expression.group.AssignmentGroup;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.*;

public abstract class TargetExpression extends ExpressionWrapper {
	
	public static TargetExpression fromAstNode(AstNode node) {
		if(node != null) {
			int tokenType = node.getType();
			
			if (tokenType == Token.NAME || tokenType == Token.ARRAYLIT || tokenType == Token.GETELEM || tokenType == Token.GETPROP) {
				if (node instanceof Name) {
					return new VariableTargetExpression((Name) node);
				}
				
				if (node instanceof ArrayLiteral) {
					return new ArrayLiteralTargetExpression((ArrayLiteral) node);
				}
				
				if (node instanceof ElementGet) {
					return new ElementTargetExpression((ElementGet) node);
				}
				
				if (node instanceof PropertyGet) {
					return new PropertyTargetExpression((PropertyGet) node);
				}
			}
		}
		
		return new NonConvertibleTargetExpression(node);
	}
	
	public abstract boolean isConvertibleExpression();
	public abstract String getName();
	
}
