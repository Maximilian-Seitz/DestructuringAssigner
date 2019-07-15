package de.maxi_seitz.destructuringassigner.expression.source;

import de.maxi_seitz.destructuringassigner.expression.ExpressionWrapper;
import de.maxi_seitz.destructuringassigner.expression.assignment.AssignmentExpression;
import de.maxi_seitz.destructuringassigner.expression.group.AssignmentGroup;
import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.PropertyGet;

/**
 * Wrapper for {@link AstNode},
 * from which data is taken to be stored in a variable.
 * <br/>
 * Allows for easier analysis of an assignment.
 */
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
		
		return new NonConvertibleSourceExpression();
	}
	
	/**
	 * Check if converting an assignment with this as a source makes sense.
	 * Checks for possible side-effects in the expression, among other things.
	 * @return <code>true</code> when source can be destructured
	 */
	public abstract boolean isConvertibleExpression();
	
	/**
	 * Check if target is compatible with this source for destructuring.
	 * @param target {@link TargetExpression}
	 * @return <code>true</code> if source and target are compatible
	 */
	public abstract boolean isTargetValidForDestructuring(TargetExpression target);
	
	/**
	 * Generate new {@link AssignmentGroup}, to destructure this source.
	 * @return New {@link AssignmentGroup}
	 */
	public abstract AssignmentGroup getAssignmentGroup();
	
	/**
	 * Check if group is destructuring the same source as this expression.
	 * @param group {@link AssignmentGroup}
	 * @return <code>true</code> if group makes sense with this source
	 */
	public abstract boolean isCompatibleWithGroup(AssignmentGroup group);
	
	public abstract void addAssignmentToGroup(AssignmentGroup group, AssignmentExpression assignment);
}
