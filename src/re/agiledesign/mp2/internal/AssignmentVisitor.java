package re.agiledesign.mp2.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import re.agiledesign.mp2.LexicalScope;
import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.internal.expressions.AccessExpression;
import re.agiledesign.mp2.internal.expressions.ArgumentAssignmentExpression;
import re.agiledesign.mp2.internal.expressions.Expression;
import re.agiledesign.mp2.internal.expressions.GlobalAssignmentExpression;
import re.agiledesign.mp2.internal.expressions.IndexAccessExpression;
import re.agiledesign.mp2.internal.expressions.IndexAssignmentExpression;
import re.agiledesign.mp2.internal.expressions.LocalAssignmentExpression;
import re.agiledesign.mp2.internal.expressions.SequenceExpression;
import re.agiledesign.mp2.lexer.OperatorType;
import re.agiledesign.mp2.util.Util;

public class AssignmentVisitor {
	private final Expression mRight;
	private final AccessExpression mAccessor;

	private final boolean mPost;
	private final LexicalScope mScope;
	private final OperatorType mOperator;

	private Expression mAssignment;

	private AssignmentVisitor(
			final AccessExpression aLeft,
			final Expression aRight,
			final OperatorType aOperator,
			final boolean aPost,
			final LexicalScope aScope) {
		mRight = aRight;
		mAccessor = aLeft;

		mPost = aPost;
		mScope = aScope;
		mOperator = aOperator;
	}

	public static Expression asAssignment(final AccessExpression aLeft, final Expression aRight)
			throws ParsingException {
		return asAssignment(aLeft, aRight, null, false, null);
	}

	public static Expression asAssignment(final AccessExpression aLeft,
			final Expression aRight,
			final OperatorType aOperator,
			final boolean aPost,
			final LexicalScope aScope) throws ParsingException {
		return new AssignmentVisitor(aLeft, aRight, aOperator, aPost, aScope).assignment();
	}

	public void visitLocalAssignment(final int aIndex) {
		mAssignment = fix(new LocalAssignmentExpression(aIndex, combine(mAccessor)));
	}

	public void visitGlobalAssignment(final String aVariableName) {
		mAssignment = fix(new GlobalAssignmentExpression(aVariableName, combine(mAccessor)));
	}

	public void visitArgumentAssignment(final int aIndex) {
		mAssignment = fix(new ArgumentAssignmentExpression(aIndex, combine(mAccessor)));
	}

	public void visitIndexAssignment(final Expression aThis, final Expression aIndex) {
		if (mOperator == null) {
			mAssignment = new IndexAssignmentExpression(aThis, aIndex, mRight);

			return;
		}

		final int thisIdx = mScope.getUniqueVariable().getIndex();
		final int indexIdx = mScope.getUniqueVariable().getIndex();

		final List<Expression> seq = new ArrayList<Expression>();

		seq.add(new LocalAssignmentExpression(thisIdx, aThis));
		seq.add(new LocalAssignmentExpression(indexIdx, aIndex));

		Expression access = new IndexAccessExpression(
			ExpressionFactory.getLocalAccessExpression(thisIdx),
			ExpressionFactory.getLocalAccessExpression(indexIdx));

		if (mPost) {
			final int tmpIdx = mScope.getUniqueVariable().getIndex();

			seq.add(new LocalAssignmentExpression(tmpIdx, access));
			access = ExpressionFactory.getLocalAccessExpression(tmpIdx);
		}

		seq.add(new IndexAssignmentExpression( //
			ExpressionFactory.getLocalAccessExpression(thisIdx), //
			ExpressionFactory.getLocalAccessExpression(indexIdx), //
			combine(access)));

		if (mPost) {
			seq.add(access);
		}

		mAssignment = new SequenceExpression(Util.immutableList(seq));
	}

	private Expression combine(final Expression aLeft) {
		if (mOperator == null) {
			return mRight;
		}

		try {
			return OperatorFactory.getBinaryOperator(mOperator, aLeft, mRight);
		} catch (final ParsingException e) {
			throw Util.rethrowUnchecked(e);
		}
	}

	private Expression fix(final Expression aExpression) {
		if (!mPost) {
			return aExpression;
		}

		final int tempIdx = mScope.getUniqueVariable().getIndex();

		return new SequenceExpression(Util.immutableList(Arrays.asList(
			new LocalAssignmentExpression(tempIdx, mAccessor),
			aExpression,
			ExpressionFactory.getLocalAccessExpression(tempIdx))));
	}

	private Expression assignment() throws ParsingException {
		mAccessor.visit(this);

		return mAssignment;
	}
}
