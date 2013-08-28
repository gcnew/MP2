package re.agiledesign.mp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	@SuppressWarnings({ "unused", "boxing", "null" })
	public static class ABC {
		public void print(final Object l) {
			System.out.println(l);
		}

		public void doop(final int o, final Long a) {
			System.out.println(1);
		}

		public void doop(final int o, final Integer a) {
			System.out.println(2);
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

		public Object[] array() {
			return new Object[] { 1, "abc", new Object[] { 3.5 } };
		}

		public List<String> list() {
			final List<String> retval = new ArrayList<String>();
			retval.add("I am");
			retval.add("MC");
			retval.add("PSD");

			return retval;
		}

		public Map<Object, Object> map() {
			final Map<Object, Object> retval = new HashMap<Object, Object>();

			retval.put(null, "S(null)");
			retval.put("qwerty", "12345678");
			retval.put(1, 10);

			return retval;
		}
	}

	public static void main(final String[] args) {
		try {
			final Map<String, Object> vars = new HashMap<String, Object>();
			vars.put("a", new ABC());

			// final Interpreter interpreter = InterpreterFactory.createInterpreter("a.doop(3, (Long) null)", vars);
			// final Interpreter interpreter = InterpreterFactory.createInterpreter("function add3(a) { return a + 3; } function ex(f, a) { return f(a); } a.print(ex(add3, 4))",  vars);
			// final Interpreter interpreter = InterpreterFactory.createInterpreter("function q() { (() => a.print('lambda'))(); } q()", vars);
			final Interpreter interpreter = InterpreterFactory.createInterpreter("return 'hello'[1 -> 3]", vars);
			// final Interpreter interpreter = InterpreterFactory.createInterpreter("return a.array()[1]", vars);
			final Object retval = interpreter.eval();

			String.valueOf(retval);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
