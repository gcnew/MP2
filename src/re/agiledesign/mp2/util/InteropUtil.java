package re.agiledesign.mp2.util;

import java.util.Collections;

import re.agiledesign.mp2.internal.ExpressionFactory;
import re.agiledesign.mp2.internal.expressions.FunctionExpression;
import re.agiledesign.mp2.internal.expressions.ReflectedApplyExpression;
import re.agiledesign.mp2.internal.statements.ReturnStatement;

public class InteropUtil {
	public static FunctionExpression proxy(final Object aService, final String aName) {
		// @formatter:off
		return new FunctionExpression(
			new ReturnStatement(
				new ReflectedApplyExpression(ExpressionFactory.getConstantExpression(aService), aName)
			),
			0,
			Collections.<String> emptyList()
		);
		// @formatter:on
	}
}
