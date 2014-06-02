package re.agiledesign.mp2.internal;

import static re.agiledesign.mp2.lexer.OperatorType.ADD;
import static re.agiledesign.mp2.lexer.OperatorType.AND;
import static re.agiledesign.mp2.lexer.OperatorType.BIT_NOT;
import static re.agiledesign.mp2.lexer.OperatorType.DIVIDE;
import static re.agiledesign.mp2.lexer.OperatorType.IS_EQUAL;
import static re.agiledesign.mp2.lexer.OperatorType.IS_GREATER;
import static re.agiledesign.mp2.lexer.OperatorType.IS_GREATER_OR_EQ;
import static re.agiledesign.mp2.lexer.OperatorType.IS_LESS;
import static re.agiledesign.mp2.lexer.OperatorType.IS_LESS_OR_EQ;
import static re.agiledesign.mp2.lexer.OperatorType.IS_NOT_EQUAL;
import static re.agiledesign.mp2.lexer.OperatorType.IS_REF_EQUAL;
import static re.agiledesign.mp2.lexer.OperatorType.IS_REF_NOT_EQUAL;
import static re.agiledesign.mp2.lexer.OperatorType.MODULO;
import static re.agiledesign.mp2.lexer.OperatorType.MULTIPLY;
import static re.agiledesign.mp2.lexer.OperatorType.NOT;
import static re.agiledesign.mp2.lexer.OperatorType.OR;
import static re.agiledesign.mp2.lexer.OperatorType.SHIFT_LEFT;
import static re.agiledesign.mp2.lexer.OperatorType.SHIFT_RIGHT;
import static re.agiledesign.mp2.lexer.OperatorType.SUBSTRACT;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.exception.UnimplementedOperatorException;
import re.agiledesign.mp2.internal.expressions.BinaryOperatorExpression;
import re.agiledesign.mp2.internal.expressions.Expression;
import re.agiledesign.mp2.internal.expressions.UnaryOperatorExpression;
import re.agiledesign.mp2.internal.expressions.operators.binary.Add;
import re.agiledesign.mp2.internal.expressions.operators.binary.And;
import re.agiledesign.mp2.internal.expressions.operators.binary.Divide;
import re.agiledesign.mp2.internal.expressions.operators.binary.Equal;
import re.agiledesign.mp2.internal.expressions.operators.binary.IsGreater;
import re.agiledesign.mp2.internal.expressions.operators.binary.IsGreaterOrEqual;
import re.agiledesign.mp2.internal.expressions.operators.binary.IsLess;
import re.agiledesign.mp2.internal.expressions.operators.binary.IsLessOrEqual;
import re.agiledesign.mp2.internal.expressions.operators.binary.Modulo;
import re.agiledesign.mp2.internal.expressions.operators.binary.Multiply;
import re.agiledesign.mp2.internal.expressions.operators.binary.NotEqual;
import re.agiledesign.mp2.internal.expressions.operators.binary.Or;
import re.agiledesign.mp2.internal.expressions.operators.binary.RefEqual;
import re.agiledesign.mp2.internal.expressions.operators.binary.RefNotEqual;
import re.agiledesign.mp2.internal.expressions.operators.binary.ShiftLeft;
import re.agiledesign.mp2.internal.expressions.operators.binary.ShiftRight;
import re.agiledesign.mp2.internal.expressions.operators.binary.Substract;
import re.agiledesign.mp2.internal.expressions.operators.unary.BitNot;
import re.agiledesign.mp2.internal.expressions.operators.unary.Negate;
import re.agiledesign.mp2.internal.expressions.operators.unary.Not;
import re.agiledesign.mp2.lexer.OperatorType;
import re.agiledesign.mp2.lexer.TokenConstants;

public class OperatorFactory {
	private static Map<OperatorType, Constructor<? extends UnaryOperatorExpression>> UNARY_OPERATORS = new HashMap<OperatorType, Constructor<? extends UnaryOperatorExpression>>(8);
	private static Map<OperatorType, Constructor<? extends BinaryOperatorExpression>> BINARY_OPERATORS = new HashMap<OperatorType, Constructor<? extends BinaryOperatorExpression>>(TokenConstants.OPERATOR_HASH_SIZE);

	private static <T> Constructor<T> getUnaryConstructor(final Class<T> aClass) throws Exception {
		return aClass.getConstructor(Expression.class);
	}

	private static <T> Constructor<T> getBinaryConstructor(final Class<T> aClass) throws Exception {
		return aClass.getConstructor(Expression.class, Expression.class);
	}

	static {
		try {
			UNARY_OPERATORS.put(SUBSTRACT, getUnaryConstructor(Negate.class));
			UNARY_OPERATORS.put(BIT_NOT, getUnaryConstructor(BitNot.class));
			UNARY_OPERATORS.put(NOT, getUnaryConstructor(Not.class));

			BINARY_OPERATORS.put(OR, getBinaryConstructor(Or.class));
			BINARY_OPERATORS.put(AND, getBinaryConstructor(And.class));
			BINARY_OPERATORS.put(ADD, getBinaryConstructor(Add.class));
			BINARY_OPERATORS.put(SUBSTRACT, getBinaryConstructor(Substract.class));
			BINARY_OPERATORS.put(MULTIPLY, getBinaryConstructor(Multiply.class));
			BINARY_OPERATORS.put(DIVIDE, getBinaryConstructor(Divide.class));
			BINARY_OPERATORS.put(MODULO, getBinaryConstructor(Modulo.class));
			BINARY_OPERATORS.put(SHIFT_LEFT, getBinaryConstructor(ShiftLeft.class));
			BINARY_OPERATORS.put(SHIFT_RIGHT, getBinaryConstructor(ShiftRight.class));
			BINARY_OPERATORS.put(IS_EQUAL, getBinaryConstructor(Equal.class));
			BINARY_OPERATORS.put(IS_NOT_EQUAL, getBinaryConstructor(NotEqual.class));
			BINARY_OPERATORS.put(IS_REF_EQUAL, getBinaryConstructor(RefEqual.class));
			BINARY_OPERATORS.put(IS_REF_NOT_EQUAL, getBinaryConstructor(RefNotEqual.class));
			BINARY_OPERATORS.put(IS_GREATER, getBinaryConstructor(IsGreater.class));
			BINARY_OPERATORS.put(IS_GREATER_OR_EQ, getBinaryConstructor(IsGreaterOrEqual.class));
			BINARY_OPERATORS.put(IS_LESS, getBinaryConstructor(IsLess.class));
			BINARY_OPERATORS.put(IS_LESS_OR_EQ, getBinaryConstructor(IsLessOrEqual.class));
		} catch (final Exception e) {
			throw new RuntimeException("Initializing unary operator map failed", e);
		}
	}

	public static UnaryOperatorExpression getUnaryOperator(final OperatorType aOperator, final Expression aRight)
			throws ParsingException {
		final Constructor<? extends UnaryOperatorExpression> opc = UNARY_OPERATORS.get(aOperator);

		if (opc == null) {
			throw new UnimplementedOperatorException("Operator " + aOperator + " is not yet implemented");
		}

		try {
			return opc.newInstance(aRight);
		} catch (final Exception e) {
			throw new ParsingException("Failed instantiating operator " + aOperator + ": " + e.getMessage(), e);
		}
	}

	public static BinaryOperatorExpression getBinaryOperator(final OperatorType aOperator,
			final Expression aLeft,
			final Expression aRight) throws ParsingException {
		final Constructor<? extends BinaryOperatorExpression> opc = BINARY_OPERATORS.get(aOperator);

		if (opc == null) {
			throw new UnimplementedOperatorException("Operator " + aOperator + " is not yet implemented");
		}

		try {
			return opc.newInstance(aLeft, aRight);
		} catch (final Exception e) {
			throw new ParsingException("Failed instantiating operator " + aOperator + ": " + e.getMessage(), e);
		}
	}
}
