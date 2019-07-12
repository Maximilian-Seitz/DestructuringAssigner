package de.maxi_seitz.destructuringassigner.expression.source;

import de.maxi_seitz.destructuringassigner.expression.target.TargetExpression;
import org.mozilla.javascript.ast.ElementGet;

class ElementSourceExpression extends SourceExpression {
	
	private final ElementGet node;
	
	ElementSourceExpression(ElementGet node) {
		this.node = node;
	}
	
	@Override
	public boolean isConvertibleExpression() {
		return isSideEffectFree(node);
	}
	
	@Override
	public boolean isTargetValidForDestructoring(TargetExpression target) {
		return true;
	}
	
	@Override
	public String toString() {
		return node.toSource();
	}
}
