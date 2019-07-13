package de.maxi_seitz.destructuringassigner.expression.group;

public abstract class AssignmentGroup {
	
	private Type type;
	
	protected AssignmentGroup(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public enum Type {
		ARRAY,
		OBJECT
	}
	
}
