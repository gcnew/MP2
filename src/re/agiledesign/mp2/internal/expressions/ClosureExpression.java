package re.agiledesign.mp2.internal.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import re.agiledesign.mp2.LexicalScope.CaptureMapping;
import re.agiledesign.mp2.VarInfo.Visibility;
import re.agiledesign.mp2.internal.ExpressionFactory;
import re.agiledesign.mp2.internal.FunctionScope;
import re.agiledesign.mp2.internal.Scope;
import re.agiledesign.mp2.internal.statements.Block;
import re.agiledesign.mp2.internal.statements.SequenceStatement;
import re.agiledesign.mp2.internal.statements.Statement;

public class ClosureExpression extends Expression {
	private final Statement mBody;
	private final int mLocalsCount;
	private final List<String> mArguments;
	private final List<CaptureMapping> mCaptures;

	public ClosureExpression(
			final Statement aBody,
			final int aLocalsCount,
			final List<String> aArguments,
			final List<CaptureMapping> aCaptures
	) {
		mBody = aBody;
		mLocalsCount = aLocalsCount;
		mArguments = aArguments;
		mCaptures = aCaptures;
	}

	public Object execute(final Scope aScope) throws Exception {
		final List<Expression> captureSetters = new ArrayList<Expression>();

		for (final CaptureMapping m : mCaptures) {
			final Object value;

			if (m.visibility == Visibility.ARGUMENT) {
				value = ((FunctionScope) aScope).getArgument(m.captureIndex);
			} else {
				value = aScope.getLocalVariable(m.captureIndex);
			}

			final Expression valueExpr = ExpressionFactory.getConstantExpression(value);
			captureSetters.add(new LocalAssignmentExpression(m.localIndex, valueExpr));
		}

		final Statement body = new Block(Arrays.asList(new SequenceStatement(captureSetters), mBody));

		return new FunctionExpression(body, mLocalsCount, mArguments);
	}
}
