package de.maxi_seitz.destructuringassigner.expression.source;

import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;
import org.mozilla.javascript.ast.PropertyGet;

class PropertySourceExpression extends SourceExpression {
	
	private final PropertyGet node;
	
	PropertySourceExpression(PropertyGet node) {
		this.node = node;
	}
	
	@Override
	public boolean isConvertibleExpression() {
		return !node.getTarget().hasSideEffects();
	}
	
	@Override
	public boolean isTargetValidForDestructoring(TargetExpression target) {
		if(target != null) {
			String targetName = target.getName();
			String propertyName = node.getProperty().getIdentifier();
			
			return propertyName.equals(targetName);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return node.toSource();
	}
	
}
