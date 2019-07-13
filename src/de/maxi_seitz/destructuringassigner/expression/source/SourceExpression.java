package de.maxi_seitz.destructuringassigner.expression.source;

import de.maxi_seitz.destructuringassigner.expression.ExpressionWrapper;
import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;
import de.maxi_seitz.destructuringassigner.expression.group.AssignmentGroup;
import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.PropertyGet;

public abstract class SourceExpression extends ExpressionWrapper {
	
	public static SourceExpression fromAstNode(AstNode node) {
		if(node != null) {
			int tokenType = node.getType();
			
			if(tokenType == Token.GETELEM || tokenType == Token.GETPROP) {
				if(node instanceof ElementGet) {
					return new ElementSourceExpression((ElementGet) node);
				}
				
				if(node instanceof PropertyGet) {
					return new PropertySourceExpression((PropertyGet) node);
				}
			}
		}
		
		return new NonConvertibleSourceExpression(node);
	}
	
	public abstract boolean isConvertibleExpression();
	
	public abstract boolean isTargetValidForDestructoring(TargetExpression target);
	
	public abstract AssignmentGroup getAssignmentGroup();
	
	public abstract boolean isCompatibleWithGroup(AssignmentGroup group);
	
	public abstract void addAssignmentToGroup(AssignmentGroup group, AssignmentExpression assignment);
}
