package com.kleverbeast.dpf.common.operationparser;

import com.kleverbeast.dpf.common.operationparser.internal.Scope;
import com.kleverbeast.dpf.common.operationparser.internal.statements.Statement;

public class Main {
	public static class ABC {
		public void print(final Object l) {
			System.out.println(l);
		}

		public void doop(final int o, final long a) {
		}

		public void doop(final long o, final int a) {
		}

		public void qq(final Long... args) {
			if (args == null) {
				System.out.println("(null)");
			}

			for (Long l : args) {
				System.out.println(l);
			}
		}

		public long l() {
			return 1;
		}
	}

	public static void main(final String[] args) {
		try {
			// final OperationParser parser = new OperationParser("$q = (boolean) 1; $l = $a.l(); for ($i = 32; $i; $i = $i - 1) { $l <<= 1; $a.print($l); }");
			// final OperationParser parser = new OperationParser("$z = ((1 + 2).toString() + 12) + (string) null + 'qwerty'");
			final OperationParser parser = new OperationParser("$z = 0; for (;true;) if ($z === 3) break; else { $z += 1; $a.print((short) $z); $a.doop(0, (long) 0); } ");
			final Statement s = parser.parse();

			final Scope scope = new Scope(null, null);
			scope.setVariable("$a", new ABC());
			s.execute(scope);

			new ABC().qq();
			ABC.class.getMethod("qq", Long[].class).invoke(new ABC(),
					new Object[] { new Long[] { Long.valueOf(1), 2l } });

			scope.getVariable("");
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
