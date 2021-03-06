package re.agiledesign.mp2.internal.expressions;

import java.util.List;

import javax.script.ScriptException;

import re.agiledesign.mp2.collection.ConsList;
import re.agiledesign.mp2.collection.ConsListWrapper;
import re.agiledesign.mp2.collection.ImmutableList;
import re.agiledesign.mp2.collection.SequentialList;
import re.agiledesign.mp2.internal.Scope;

public class ConsListExpression extends Expression {
	private final List<Expression> mElements;

	public ConsListExpression(final List<Expression> aElements) {
		mElements = aElements;
	}

	public Object execute(final Scope aScope) throws Exception {
		int idx = mElements.size() - 1;

		SequentialList<Object> retval = null;
		final Object last = mElements.get(idx).execute(aScope);
		if (last != null) {
			if (!(last instanceof List)) {
				throw new ScriptException("Only lists can be consed");
			}

			@SuppressWarnings("unchecked")
			final List<Object> l = (List<Object>) last;
			if (!l.isEmpty()) {
				if (!(l instanceof ImmutableList)) {
					throw new ScriptException("Only immutable lists can be consed");
				}

				if (l instanceof SequentialList) {
					retval = (SequentialList<Object>) l;
				} else {
					retval = new ConsListWrapper<Object>((ImmutableList<Object>) l);
				}
			}
		}

		while (--idx >= 0) {
			final Object value = mElements.get(idx).execute(aScope);
			retval = new ConsList<Object>(value, retval);
		}

		return retval;
	}
}
