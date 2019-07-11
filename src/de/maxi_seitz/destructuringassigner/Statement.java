package de.maxi_seitz.destructuringassigner;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;

/**
 * Wrapper for {@link AstNode},
 * to allow easier analysis.
 */
public class Statement {
	
	private AstNode node;
	private AstNode parentNode;
	
	public Statement(AstNode node) {
		this.node = node;
		this.parentNode = node.getParent();
	}
	
	public boolean isAssignment() {
		if(node != null) {
			int tokenType = node.getType();
			
			if(tokenType == Token.ASSIGN) {
				return true;
			}
			
			if(tokenType == Token.VAR && parentNode != null) {
				return parentNode.getType() == Token.VAR;
			}
		}
		
		return false;
	}
	
}
