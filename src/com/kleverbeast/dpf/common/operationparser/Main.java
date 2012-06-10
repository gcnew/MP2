package com.kleverbeast.dpf.common.operationparser;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.statements.Statement;

public class Main {
	public static class ABC {
		public void print(final long l) {
			System.out.println(l);
		}

		public long l() {
			return 1;
		}
	}

	public static void main(final String[] args) {
		try {
			// final OperationParser parser = new OperationParser("$q = (boolean) 1; $l = $a.l(); for ($i = 32; $i; $i = $i - 1) { $l <<= 1; $a.print($l); }");
			final OperationParser parser = new OperationParser("$z = ((1 + 2).toString() + 12) + (string) null + 'qwerty'");
			final Statement s = parser.parse();

			final Scope scope = new Scope(null, null);
			scope.setVariable("$a", new ABC());
			s.execute(scope);

			scope.getVariable("");
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
