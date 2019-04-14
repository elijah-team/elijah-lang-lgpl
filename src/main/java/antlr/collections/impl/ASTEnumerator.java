package antlr.collections.impl;

import java.util.NoSuchElementException;

import antlr.collections.AST;

public class ASTEnumerator implements antlr.collections.ASTEnumeration {
	/** The list of root nodes for subtrees that match */
	VectorEnumerator nodes;
	int i = 0;


public ASTEnumerator(Vector v) {
		nodes = new VectorEnumerator(v);
}
public boolean hasMoreNodes() {
	synchronized (nodes) {
		return i <= nodes.vector.lastElement;
	}
}
public antlr.collections.AST nextNode() {
	synchronized (nodes) {
		if (i <= nodes.vector.lastElement) {
			return (AST)nodes.vector.data[i++];
		}
		throw new NoSuchElementException("ASTEnumerator");
	}
}
}
