package re.agiledesign.mp2.internal.statements;

import java.util.List;

import re.agiledesign.mp2.internal.Scope;


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
