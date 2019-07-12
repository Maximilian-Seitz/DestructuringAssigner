package de.maxi_seitz.destructuringassigner.expression.source;

import org.mozilla.javascript.ast.ElementGet;

class ArrayElementSourceExpression extends SourceExpression {
	
	private final ElementGet node;
	
	ArrayElementSourceExpression(ElementGet node) {
		this.node = node;
	}
	
	@Override
	public boolean isConvertibleExpression() {
		return true;
	}
	
	@Override
	public String getName() {
		return node.toSource();
	}
}
