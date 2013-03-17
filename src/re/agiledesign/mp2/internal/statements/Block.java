package com.kleverbeast.dpf.common.operationparser.internal.statements;

import java.util.List;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;

public class Block extends Statement {
	private final List<Statement> mStatements;

	public Block(final List<Statement> aStatements) {
		mStatements = aStatements;
	}

	public void execute(final Scope aScope) throws Exception {
		for (final Statement s : mStatements) {
			s.execute(aScope);

			if (aScope.getControlFlow() != null) {
				// either break, continue or return - any of them terminates this block evaluation
				return;
			}
		}
	}
}
