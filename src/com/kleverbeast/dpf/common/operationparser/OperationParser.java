package com.kleverbeast.dpf.common.operationparser;

import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.ADD;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.AND;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.ASSIGN;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.BIT_AND;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.BIT_OR;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.BIT_XOR;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.DIVIDE;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.IS_EQUAL;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.IS_GREATER;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.IS_GREATER_OR_EQ;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.IS_LESS;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.IS_LESS_OR_EQ;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.IS_NOT_EQUAL;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.IS_REF_EQUAL;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.IS_REF_NOT_EQUAL;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.MODULO;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.MULTIPLY;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.OR;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.SHIFT_LEFT;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.SHIFT_RIGHT;
import static com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType.SUBSTRACT;

import java.util.ArrayList;
import java.util.List;

import com.kleverbeast.dpf.common.operationparser.exception.ParsingException;
import com.kleverbeast.dpf.common.operationparser.internal.CoercionUtil.CoercionType;
import com.kleverbeast.dpf.common.operationparser.internal.ConstantExpressionFactory;
import com.kleverbeast.dpf.common.operationparser.internal.OperatorFactory;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.AccessExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.AssignmentExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.CastExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.FunctionExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.ReflectedThisExpression;
import com.kleverbeast.dpf.common.operationparser.internal.statements.Block;
import com.kleverbeast.dpf.common.operationparser.internal.statements.BreakStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.ContinueStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.EmptyStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.ExpressionStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.ForStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.IfElseStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.Statement;
import com.kleverbeast.dpf.common.operationparser.tokenizer.Keywords;
import com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType;
import com.kleverbeast.dpf.common.operationparser.tokenizer.Token;
import com.kleverbeast.dpf.common.operationparser.tokenizer.TokenConstants;
import com.kleverbeast.dpf.common.operationparser.tokenizer.TokenTypes;
import com.kleverbeast.dpf.common.operationparser.tokenizer.Tokenizer;

public class OperationParser {
	private final Tokenizer mTokenizer;
	private final ConstantExpressionFactory mConstantFactory = new ConstantExpressionFactory();

	private static final Statement EMPTY_STATEMENT = new EmptyStatement();
	private static final Statement BREAK_STATEMENT = new BreakStatement();
	private static final Statement CONTINUE_STATEMENT = new ContinueStatement();

	private static final OperatorType PRECEDENCES[][] = {
	/*		*/{ OR },
	/*		*/{ AND },
	/*		*/{ BIT_OR },
	/*		*/{ BIT_XOR },
	/*		*/{ BIT_AND },
	/*		*/{ IS_EQUAL, IS_NOT_EQUAL, IS_REF_EQUAL, IS_REF_NOT_EQUAL },
	/*		*/{ IS_LESS, IS_LESS_OR_EQ, IS_GREATER, IS_GREATER_OR_EQ },
	/*		*/{ SHIFT_LEFT, SHIFT_RIGHT },
	/*		*/{ ADD, SUBSTRACT },
	/*		*/{ MULTIPLY, DIVIDE, MODULO },
	/*		*///{ NOT, BIT_NOT }
	/* */};

	public OperationParser(final String aSource) {
		mTokenizer = new Tokenizer(aSource);
	}

	public Statement parse() throws ParsingException {
		mTokenizer.tokenize();
		return parseBlock();
	}

	private Block parseBlock() throws ParsingException {
		final ArrayList<Statement> statements = new ArrayList<Statement>();

		while (mTokenizer.hasNext()) {
			final Statement statement = parseStatement();

			if (statement != EMPTY_STATEMENT) {
				statements.add(statement);
			}
		}

		return new Block(statements);
	}

	private void checkAndAdvance(final Token aToken) throws ParsingException {
		final Token token = mTokenizer.next();

		if (token != aToken) {
			throwExpectedFound(aToken, token);
		}
	}

	private boolean advanceIfNext(final Token aToken) throws ParsingException {
		final int position = mTokenizer.getPostion();

		if (mTokenizer.next() == aToken) {
			return true;
		}

		mTokenizer.restorePosition(position);
		return false;
	}

	private <T> T advanceIfNext(final TokenTypes aType, final T aValues[]) throws ParsingException {
		final int position = mTokenizer.getPostion();
		final Token token = mTokenizer.next();

		if (token.getType() == aType) {
			for (int i = 0; i < aValues.length; i++) {
				if (token.getValue() == aValues[i]) {
					return aValues[i];
				}
			}
		}

		mTokenizer.restorePosition(position);
		return null;
	}

	private Expression parseAssignment() throws ParsingException {
		final int position = mTokenizer.getPostion();
		final Token token = mTokenizer.next();

		// TODO: handle index ($a[$i] = X)
		if (token.getType() == TokenTypes.VARIABLE) {
			final Token token2 = mTokenizer.next();

			if (token2.getType() == TokenTypes.OPERATOR) {
				final OperatorType operator = token2.getValue();

				if (operator.hasAssignment()) {
					Expression right = parseAssignment();
					final String varName = token.getStringValue();

					if (operator != ASSIGN) {
						final OperatorType basicOperator = operator.getBasicOperator();
						right = OperatorFactory.getBinaryOperator(basicOperator, new AccessExpression(varName), right);
					}

					return new AssignmentExpression(varName, right);
				}
			}
		}

		mTokenizer.restorePosition(position);
		return parsePrecedenced(0);
	}

	private Expression parsePrecedenced(final int aIndex) throws ParsingException {
		if (aIndex == PRECEDENCES.length) {
			return parseCast();
		}

		Expression retval = parsePrecedenced(aIndex + 1);
		final OperatorType operators[] = PRECEDENCES[aIndex];
		while (mTokenizer.hasNext()) {
			final OperatorType operator = advanceIfNext(TokenTypes.OPERATOR, operators);

			if (operator == null) {
				break;
			}

			retval = OperatorFactory.getBinaryOperator(operator, retval, parsePrecedenced(aIndex + 1));
		}

		return retval;
	}

	private Expression parseCast() throws ParsingException {
		final int position = mTokenizer.getPostion();

		if (advanceIfNext(TokenConstants.O_BRACK)) {
			final Token token = mTokenizer.next();

			if (token.getType() == TokenTypes.TYPE) {
				final CoercionType type = token.getValue();
				checkAndAdvance(TokenConstants.C_BRACK);

				return new CastExpression(type, parseCast());
			}
		}

		mTokenizer.restorePosition(position);
		return parseUnary();
	}

	private Expression parseUnary() throws ParsingException {
		Expression retval = null;
		OperatorType operator = null;

		do {
			final int position = mTokenizer.getPostion();
			final Token token = mTokenizer.next();

			if (token.getType() == TokenTypes.OPERATOR) {
				if (token.getValue() == ADD) {
					continue;
				}

				if (operator == null) {
					operator = token.getValue();
				} else if (token.getValue() == operator) {
					operator = null;
				} else {
					mTokenizer.restorePosition(position);
					retval = parseUnary();
				}
			} else {
				mTokenizer.restorePosition(position);
				retval = parseExpression();
			}
		} while (retval == null);

		return (operator == null) ? retval : OperatorFactory.getUnaryOperator(operator, retval);
	}

	private Expression parseExpression() throws ParsingException {
		Expression retval = null;
		final Token token = mTokenizer.next();

		switch (token.getType()) {
		case GRAMMAR_TOKEN:
			if (token != TokenConstants.O_BRACK) {
				throwExpectedFound(TokenConstants.O_BRACK, token);
			}

			retval = parseAssignment();
			checkAndAdvance(TokenConstants.C_BRACK);
			break;
		case CONSTANT:
			retval = mConstantFactory.getExpression(token);
			break;
		case VARIABLE:
			retval = new AccessExpression(token.getStringValue());
			break;
		case LITERAL:
			retval = parseFunctionCall(token.getStringValue());
			break;
		}

		if (retval == null) {
			throwUnexpected(token);
		}

		while (mTokenizer.hasNext() && advanceIfNext(TokenConstants.SCOPE)) {
			retval = parseThisExpression(retval);
		}

		return retval;
	}

	private Expression parseThisExpression(final Expression aThis) throws ParsingException {
		final Token token = mTokenizer.next();

		if (token.getType() != TokenTypes.LITERAL) {
			throwExpectedFound(TokenTypes.LITERAL, token);
		}

		final List<Expression> args = parseFunctionArgs();
		return new ReflectedThisExpression(aThis, token.getStringValue(), args);
	}

	private Expression parseFunctionCall(final String aFunctionName) throws ParsingException {
		final List<Expression> args = parseFunctionArgs();
		return new FunctionExpression(aFunctionName, args);
	}

	private List<Expression> parseFunctionArgs() throws ParsingException {
		checkAndAdvance(TokenConstants.O_BRACK);

		final List<Expression> args = new ArrayList<Expression>(4);
		while (true) {
			if (advanceIfNext(TokenConstants.C_BRACK)) {
				break;
			}

			args.add(parseAssignment());
			advanceIfNext(TokenConstants.COMMA);
		}

		return args;
	}

	private Statement parseStatement() throws ParsingException {
		if (advanceIfNext(TokenConstants.SEMICOL)) {
			return EMPTY_STATEMENT;
		}

		final int position = mTokenizer.getPostion();
		final Token token = mTokenizer.next();

		Statement retval = null;
		if (token.getType() == TokenTypes.KEYWORD) {
			switch (token.<Keywords> getValue()) {
			case IF:
				retval = parseIfElse();
				break;
			case FOR:
				retval = parseFor();
				break;
			case BREAK:
				retval = BREAK_STATEMENT;
				break;
			case CONTINUE:
				retval = CONTINUE_STATEMENT;
				break;
			default:
				throw new ParsingException("Keyword not yet implemented" + token);
			}
		} else {
			mTokenizer.restorePosition(position);
			retval = new ExpressionStatement(parseAssignment());
		}

		if (mTokenizer.hasNext()) {
			advanceIfNext(TokenConstants.SEMICOL);
		}

		return retval;
	}

	private Statement parseIfElse() throws ParsingException {
		checkAndAdvance(TokenConstants.O_BRACK);
		final Expression condition = parseAssignment();
		checkAndAdvance(TokenConstants.C_BRACK);

		final Statement trueStatement = parseStatementOrBlock();

		if (mTokenizer.hasNext() && advanceIfNext(TokenConstants.ELSE)) {
			return new IfElseStatement(condition, trueStatement, parseStatementOrBlock());
		}

		return new IfElseStatement(condition, trueStatement);
	}

	private Statement parseFor() throws ParsingException {
		checkAndAdvance(TokenConstants.O_BRACK);

		final Expression expr1;
		if (advanceIfNext(TokenConstants.SEMICOL)) {
			expr1 = mConstantFactory.getExpression((Object) null);
		} else {
			expr1 = parseAssignment();
			checkAndAdvance(TokenConstants.SEMICOL);
		}

		final Expression expr2;
		if (advanceIfNext(TokenConstants.SEMICOL)) {
			expr2 = mConstantFactory.getExpression((Object) null);
		} else {
			expr2 = parseAssignment();
			checkAndAdvance(TokenConstants.SEMICOL);
		}

		final Expression expr3;
		if (advanceIfNext(TokenConstants.C_BRACK)) {
			expr3 = mConstantFactory.getExpression((Object) null);
		} else {
			expr3 = parseAssignment();
			checkAndAdvance(TokenConstants.C_BRACK);
		}

		final Statement body = parseStatementOrBlock();

		return new ForStatement(expr1, expr2, expr3, body);
	}

	private Statement parseStatementOrBlock() throws ParsingException {
		final int position = mTokenizer.getPostion();

		if (advanceIfNext(TokenConstants.O_BLOCK)) {
			final List<Statement> statements = new ArrayList<Statement>();

			while (!advanceIfNext(TokenConstants.C_BLOCK)) {
				final Statement statement = parseStatement();

				if (statement != EMPTY_STATEMENT) {
					statements.add(statement);
				}
			}

			if (statements.isEmpty()) {
				return EMPTY_STATEMENT;
			}

			return new Block(statements);
		}

		mTokenizer.restorePosition(position);
		return parseStatement();
	}

	private void throwExpectedFound(final Token aExpected, final Token aFound) throws ParsingException {
		throw new ParsingException("Expected token: " + aExpected + ", found: " + aFound);
	}

	private void throwExpectedFound(final TokenTypes aExpected, final Token aFound) throws ParsingException {
		throw new ParsingException("Expected token of type: " + aExpected + ", found: " + aFound);
	}

	private void throwUnexpected(final Token aToken) throws ParsingException {
		throw new ParsingException("Unexpected token: " + aToken);
	}
}
