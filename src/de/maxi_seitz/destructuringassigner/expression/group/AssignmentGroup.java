package de.maxi_seitz.destructuringassigner.expression.group;

import de.maxi_seitz.destructuringassigner.expression.DeclarationType;
import org.mozilla.javascript.ast.AstNode;

public abstract class AssignmentGroup {
	
	private Type type;
	private DeclarationType declarationType;
	
	protected AssignmentGroup(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public AstNode compressToDestructuringAssignment() {
		AstNode destructuringTarget = groupDestructoringTargetNode();
		AstNode destructuringSource = getSourceNode();
		
		return generateAssignmentNode(destructuringSource, destructuringTarget);
	}
	
	public abstract boolean isCompressible();
	
	public void setDeclarationType(DeclarationType declarationType) {
		this.declarationType = declarationType;
	}
	
	public DeclarationType getDeclarationType() {
		return declarationType;
	}
	
	
	protected abstract AstNode generateAssignmentNode(AstNode sourceNode, AstNode targetNode);
	protected abstract AstNode getSourceNode();
	protected abstract AstNode groupDestructoringTargetNode();
	
	public enum Type {
		ARRAY,
		OBJECT
	}
	
}
