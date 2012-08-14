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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kleverbeast.dpf.common.operationparser.exception.ArgumentAlreadyExists;
import com.kleverbeast.dpf.common.operationparser.exception.ParsingException;
import com.kleverbeast.dpf.common.operationparser.internal.ExpressionFactory;
import com.kleverbeast.dpf.common.operationparser.internal.OperatorFactory;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.AccessExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.AssignmentExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.CastExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.ConstantExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.Expression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.FunctionCallExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.FunctionExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.IndexExpression;
import com.kleverbeast.dpf.common.operationparser.internal.expressions.ReflectedThisExpression;
import com.kleverbeast.dpf.common.operationparser.internal.statements.Block;
import com.kleverbeast.dpf.common.operationparser.internal.statements.BreakStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.ContinueStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.DoStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.EmptyStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.ExpressionStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.ForStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.IfElseStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.ReturnStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.SequenceStatement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.Statement;
import com.kleverbeast.dpf.common.operationparser.internal.statements.WhileStatement;
import com.kleverbeast.dpf.common.operationparser.tokenizer.Keywords;
import com.kleverbeast.dpf.common.operationparser.tokenizer.OperatorType;
import com.kleverbeast.dpf.common.operationparser.tokenizer.Token;
import com.kleverbeast.dpf.common.operationparser.tokenizer.TokenConstants;
import com.kleverbeast.dpf.common.operationparser.tokenizer.TokenTypes;
import com.kleverbeast.dpf.common.operationparser.tokenizer.Tokenizer;
import com.kleverbeast.dpf.common.operationparser.util.CoercionUtil.CoercionType;

public class OperationParser {
	private final Tokenizer mTokenizer;
	private/*   */LexicalScope mLexicalScope = new LexicalScope();
	private final ExpressionFactory mExpressionFactory = new ExpressionFactory();
	private final Map<String, FunctionExpression> mFunctions = new HashMap<String, FunctionExpression>();

	private static final Statement EMPTY_STATEMENT = new EmptyStatement();
	private static final Statement BREAK_STATEMENT = new BreakStatement();
	private static final Statement CONTINUE_STATEMENT = new ContinueStatement();
	private static final Statement EMPTY_RETURN_STATEMENT = new ReturnStatement(ExpressionFactory.getNull());

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

	public ParsedScript parse() throws ParsingException {
		mTokenizer.tokenize();

		return new ParsedScript(parseBlock(), mFunctions);
	}

	private Block parseBlock() throws ParsingException {
		final ArrayList<Statement> statements = new ArrayList<Statement>();

		while (mTokenizer.hasNext()) {
			if (parseDefinitionStatement()) {
				continue;
			}

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
		if (token.getType() == TokenTypes.LITERAL) {
			final Expression assignment = parseAssignment0(token.getStringValue());

			if (assignment != null) {
				return assignment;
			}
		}

		mTokenizer.restorePosition(position);
		return parsePrecedenced(0);
	}

	private Expression parseAssignment0(final String aVarName) throws ParsingException {
		final Token token2 = mTokenizer.next();

		if (token2.getType() == TokenTypes.OPERATOR) {
			final OperatorType operator = token2.getValue();

			if (operator.hasAssignment()) {
				Expression right = parseAssignment();

				if (operator != ASSIGN) {
					final OperatorType basicOperator = operator.getBasicOperator();
					right = OperatorFactory.getBinaryOperator(basicOperator, new AccessExpression(aVarName), right);
				}

				int index = mLexicalScope.getArgumentIndex(aVarName);
				if (index >= 0) {
					return mExpressionFactory.getArgumentAssignmentExpression(index, right);
				}

				index = mLexicalScope.getLocalVariableIndex(aVarName);
				if (index >= 0) {
					mLexicalScope.setAssigned(aVarName);
					return mExpressionFactory.getLocalAssignmentExpression(index, right);
				}

				return new AssignmentExpression(aVarName, right);
			}
		}

		return null;
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
		case GRAMMAR_TOKEN: {
			if (token != TokenConstants.O_BRACK) {
				throwExpectedFound(TokenConstants.O_BRACK, token);
			}

			if (isLambda()) {
				retval = new ConstantExpression(parseFunctionDefinition0(true));
			} else {
				retval = parseAssignment();
				checkAndAdvance(TokenConstants.C_BRACK);
			}
		}
			break;
		case CONSTANT:
			retval = mExpressionFactory.getConstantExpression(token);
			break;
		case LITERAL:
			if (mTokenizer.hasNext() && advanceIfNext(TokenConstants.O_BRACK)) {
				retval = parseFunctionCall(token.getStringValue());
			} else {
				retval = parseAccessExpression(token.getStringValue());
			}

			break;
		}

		if (retval == null) {
			throwUnexpected(token);
		}

		while (mTokenizer.hasNext()) {
			if (advanceIfNext(TokenConstants.SCOPE)) {
				retval = parseThisExpression(retval);

				continue;
			}

			if (advanceIfNext(TokenConstants.O_INDEX)) {
				retval = parseIndexExpression(retval);

				continue;
			}

			if (advanceIfNext(TokenConstants.O_BRACK)) {
				final List<Expression> args = parseFunctionArgs();
				retval = new FunctionCallExpression(retval, args);

				continue;
			}

			break;
		}

		return retval;
	}

	private boolean isLambda() throws ParsingException {
		int balance = 1;
		final int position = mTokenizer.getPostion();
		do {
			final Token token = mTokenizer.next();

			if (token == TokenConstants.O_BRACK) {
				++balance;
				continue;
			}

			if (token == TokenConstants.C_BRACK) {
				--balance;
			}
		} while (balance != 0);

		final boolean retval = mTokenizer.hasNext() && (mTokenizer.next() == TokenConstants.LAMBDA);
		mTokenizer.restorePosition(position);

		return retval;
	}

	private Expression parseAccessExpression(final String aVarName) throws ParsingException {
		int index = mLexicalScope.getArgumentIndex(aVarName);
		if (index >= 0) {
			return mExpressionFactory.getArgumentAccessExpression(index);
		}

		index = mLexicalScope.getLocalVariableIndex(aVarName);
		if (index >= 0) {
			if (!mLexicalScope.isAssigned(aVarName)) {
				throw new ParsingException("Local variable '" + aVarName + "' is used without being initialized");
			}

			return mExpressionFactory.getLocalAccessExpression(index);
		}

		return new AccessExpression(aVarName);
	}

	private Expression parseThisExpression(final Expression aThis) throws ParsingException {
		final Token token = mTokenizer.next();

		if (token.getType() != TokenTypes.LITERAL) {
			throwExpectedFound(TokenTypes.LITERAL, token);
		}

		checkAndAdvance(TokenConstants.O_BRACK);
		final List<Expression> args = parseFunctionArgs();
		return new ReflectedThisExpression(aThis, token.getStringValue(), args);
	}

	private Expression parseIndexExpression(final Expression aThis) throws ParsingException {
		final Expression index = parseAssignment();

		checkAndAdvance(TokenConstants.C_INDEX);
		return new IndexExpression(aThis, index);
	}

	private Expression parseFunctionCall(final String aFunctionName) throws ParsingException {
		final List<Expression> args = parseFunctionArgs();
		final Expression functionAccessor = parseAccessExpression(aFunctionName);

		return new FunctionCallExpression(functionAccessor, args);
	}

	private List<Expression> parseFunctionArgs() throws ParsingException {
		final List<Expression> args = new ArrayList<Expression>(4);

		if (!advanceIfNext(TokenConstants.C_BRACK)) {
			while (true) {
				args.add(parseAssignment());

				if (advanceIfNext(TokenConstants.C_BRACK)) {
					break;
				}

				checkAndAdvance(TokenConstants.COMMA);
			}
		}

		return args;
	}

	private boolean parseDefinitionStatement() throws ParsingException {
		final int position = mTokenizer.getPostion();
		final Token token = mTokenizer.next();

		if (token.getType() == TokenTypes.KEYWORD) {
			switch (token.<Keywords> getValue()) {
			case FUNCTION:
				parseFunctionDefinition();
				return true;
			case CLASS:
				throw new ParsingException("Classes are not yet implemented");
			}
		}

		mTokenizer.restorePosition(position);
		return false;
	}

	private LexicalScope newLexicalScope() {
		final LexicalScope oldScope = mLexicalScope;

		mLexicalScope = new LexicalScope();
		return oldScope;
	}

	private void restoreLexicalScope(final LexicalScope aLexicalScope) {
		mLexicalScope = aLexicalScope;
	}

	private void parseFunctionDefinition() throws ParsingException {
		final Token nameToken = mTokenizer.next();
		if (nameToken.getType() != TokenTypes.LITERAL) {
			throwExpectedFound(TokenTypes.LITERAL, nameToken);
		}

		checkAndAdvance(TokenConstants.O_BRACK);
		final FunctionExpression function = parseFunctionDefinition0(false);
		mFunctions.put(nameToken.getStringValue(), function);
	}

	private FunctionExpression parseFunctionDefinition0(final boolean aLambda) throws ParsingException,
			ArgumentAlreadyExists {
		final LexicalScope oldScope = newLexicalScope();
		if (!advanceIfNext(TokenConstants.C_BRACK)) {
			while (true) {
				final Token argName = mTokenizer.next();
				if (argName.getType() != TokenTypes.LITERAL) {
					throwExpectedFound(TokenTypes.LITERAL, argName);
				}

				mLexicalScope.addArgument(argName.getStringValue());
				if (advanceIfNext(TokenConstants.C_BRACK)) {
					break;
				}

				checkAndAdvance(TokenConstants.COMMA);
			}
		}

		if (aLambda) {
			checkAndAdvance(TokenConstants.LAMBDA);
		}

		final Statement body = parseStatementOrBlock();
		final int localsCount = mLexicalScope.getLocalsCount();
		final List<String> argsArray = mLexicalScope.getArgumentsArray();

		restoreLexicalScope(oldScope);
		return new FunctionExpression(body, localsCount, argsArray);
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
			case WHILE:
				retval = parseWhile();
				break;
			case DO:
				retval = parseDo();
				break;
			case BREAK:
				retval = BREAK_STATEMENT;
				break;
			case CONTINUE:
				retval = CONTINUE_STATEMENT;
				break;
			case RETURN:
				retval = parseReturn();
				break;
			case LOCAL:
				retval = parseLocalStatement();
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
			expr1 = mExpressionFactory.getConstantExpression((Object) null);
		} else {
			expr1 = parseAssignment();
			checkAndAdvance(TokenConstants.SEMICOL);
		}

		final Expression expr2;
		if (advanceIfNext(TokenConstants.SEMICOL)) {
			expr2 = mExpressionFactory.getConstantExpression((Object) null);
		} else {
			expr2 = parseAssignment();
			checkAndAdvance(TokenConstants.SEMICOL);
		}

		final Expression expr3;
		if (advanceIfNext(TokenConstants.C_BRACK)) {
			expr3 = mExpressionFactory.getConstantExpression((Object) null);
		} else {
			expr3 = parseAssignment();
			checkAndAdvance(TokenConstants.C_BRACK);
		}

		final Statement body = parseStatementOrBlock();

		return new ForStatement(expr1, expr2, expr3, body);
	}

	private Statement parseWhile() throws ParsingException {
		checkAndAdvance(TokenConstants.O_BRACK);
		final Expression condition = parseAssignment();
		checkAndAdvance(TokenConstants.C_BRACK);

		final Statement body = parseStatementOrBlock();
		return new WhileStatement(condition, body);
	}

	private Statement parseDo() throws ParsingException {
		final Statement body = parseStatementOrBlock();

		checkAndAdvance(TokenConstants.WHILE);
		checkAndAdvance(TokenConstants.O_BRACK);
		final Expression condition = parseAssignment();
		checkAndAdvance(TokenConstants.C_BRACK);

		return new DoStatement(condition, body);
	}

	private Statement parseReturn() throws ParsingException {
		if (advanceIfNext(TokenConstants.SEMICOL)) {
			return EMPTY_RETURN_STATEMENT;
		}

		final Expression expression = parseAssignment();
		return new ReturnStatement(expression);
	}

	private Statement parseLocalStatement() throws ParsingException {
		final List<Expression> expressions = new ArrayList<Expression>(4);

		do {
			final Token token = mTokenizer.next();

			if (token.getType() != TokenTypes.LITERAL) {
				throwExpectedFound(TokenTypes.LITERAL, token);
			}

			final String varName = token.getStringValue();
			mLexicalScope.addLocalVariable(varName);

			if (mTokenizer.hasNext()) {
				final int position = mTokenizer.getPostion();
				final Expression assignment = parseAssignment0(varName);

				if (assignment != null) {
					expressions.add(assignment);
				} else {
					mTokenizer.restorePosition(position);
				}
			}
		} while (mTokenizer.hasNext() && advanceIfNext(TokenConstants.COMMA));

		switch (expressions.size()) {
		case 0:
			return EMPTY_STATEMENT;
		case 1:
			return new ExpressionStatement(expressions.get(0));
		default:
			return new SequenceStatement(expressions);
		}
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
