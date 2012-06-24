package com.kleverbeast.dpf.common.operationparser;

import java.util.HashMap;
import java.util.Map;

public class Main {
	public static class ABC {
		public void print(final Object l) {
			System.out.println(l);
		}

		public void doop(final int o, final Long a) {
		}

		public void doop(final int o, final Integer a) {
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
			final Map<String, Object> vars = new HashMap<String, Object>();
			vars.put("a", new ABC());

			final Interpreter interpreter = InterpreterFactory.createInterpreter("function add3(a) { return a + 3; } function ex(f, a) { return f(a); } a.print(ex(add3, 4))",
					vars);
			final Object retval = interpreter.eval();

			String.valueOf(retval);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
