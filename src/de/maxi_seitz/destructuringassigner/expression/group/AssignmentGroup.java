package de.maxi_seitz.destructuringassigner.expression.group;

import de.maxi_seitz.destructuringassigner.expression.DeclarationType;

import org.mozilla.javascript.ast.AstNode;

/**
 * Group of assignments, which can be converted into a single destructuring assignment.
 */
public abstract class AssignmentGroup {
	
	private Type type;
	private DeclarationType declarationType;
	
	protected AssignmentGroup(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	/**
	 * Generate {@link AstNode} containing the destructuring assignment.
	 * Removes all assignments in the process, except the first,
	 * since it needs to be kept as a reference for where to insert this assignment.
	 * @return {@link AstNode} containing destructuring assignment
	 */
	public AstNode compressToDestructuringAssignment() {
		AstNode destructuringTarget = groupDestructuringTargetNode();
		AstNode destructuringSource = getSourceNode();
		
		return generateAssignmentNode(destructuringSource, destructuringTarget);
	}
	
	/**
	 * @return <code>true</code> if this group can be compressed to a destructuring assignment
	 */
	public abstract boolean isCompressible();
	
	public void setDeclarationType(DeclarationType declarationType) {
		this.declarationType = declarationType;
	}
	
	public DeclarationType getDeclarationType() {
		return declarationType;
	}
	
	
	protected abstract AstNode generateAssignmentNode(AstNode sourceNode, AstNode targetNode);
	
	protected abstract AstNode getSourceNode();
	
	/**
	 * Generate {@link AstNode} containing the destructuring assignment target.
	 * Removes all assignments in the process, except the first,
	 * since it needs to be kept as a reference for where to insert the destructuring assignment.
	 * @return {@link AstNode} containing destructuring assignment target
	 */
	protected abstract AstNode groupDestructuringTargetNode();
	
	
	public enum Type {
		ARRAY,
		OBJECT
	}
	
}
