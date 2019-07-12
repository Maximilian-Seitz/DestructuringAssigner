package de.maxi_seitz.destructuringassigner.expression;

import org.mozilla.javascript.ast.AstNode;

public class ExpressionWrapper {
	
	private boolean isSideEffectFree = true;
	
	protected boolean isSideEffectFree(AstNode node) {
		isSideEffectFree = true;
		
		node.visit(childNode -> {
			isSideEffectFree &= !childNode.hasSideEffects();
			return isSideEffectFree; //only check children for side effects, if none have been found so far
		});
		
		return isSideEffectFree;
	}
	
}
