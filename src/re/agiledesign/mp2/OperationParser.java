package re.agiledesign.mp2;

import static re.agiledesign.mp2.tokenizer.OperatorType.ADD;
import static re.agiledesign.mp2.tokenizer.OperatorType.AND;
import static re.agiledesign.mp2.tokenizer.OperatorType.ASSIGN;
import static re.agiledesign.mp2.tokenizer.OperatorType.BIT_AND;
import static re.agiledesign.mp2.tokenizer.OperatorType.BIT_OR;
import static re.agiledesign.mp2.tokenizer.OperatorType.BIT_XOR;
import static re.agiledesign.mp2.tokenizer.OperatorType.DIVIDE;
import static re.agiledesign.mp2.tokenizer.OperatorType.IS_EQUAL;
import static re.agiledesign.mp2.tokenizer.OperatorType.IS_GREATER;
import static re.agiledesign.mp2.tokenizer.OperatorType.IS_GREATER_OR_EQ;
import static re.agiledesign.mp2.tokenizer.OperatorType.IS_LESS;
import static re.agiledesign.mp2.tokenizer.OperatorType.IS_LESS_OR_EQ;
import static re.agiledesign.mp2.tokenizer.OperatorType.IS_NOT_EQUAL;
import static re.agiledesign.mp2.tokenizer.OperatorType.IS_REF_EQUAL;
import static re.agiledesign.mp2.tokenizer.OperatorType.IS_REF_NOT_EQUAL;
import static re.agiledesign.mp2.tokenizer.OperatorType.MODULO;
import static re.agiledesign.mp2.tokenizer.OperatorType.MULTIPLY;
import static re.agiledesign.mp2.tokenizer.OperatorType.OR;
import static re.agiledesign.mp2.tokenizer.OperatorType.SHIFT_LEFT;
import static re.agiledesign.mp2.tokenizer.OperatorType.SHIFT_RIGHT;
import static re.agiledesign.mp2.tokenizer.OperatorType.SUBSTRACT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import re.agiledesign.mp2.exception.ArgumentAlreadyExists;
import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.internal.ExpressionFactory;
import re.agiledesign.mp2.internal.OperatorFactory;
import re.agiledesign.mp2.internal.expressions.AccessExpression;
import re.agiledesign.mp2.internal.expressions.AssignmentExpression;
import re.agiledesign.mp2.internal.expressions.CastExpression;
import re.agiledesign.mp2.internal.expressions.ConstantExpression;
import re.agiledesign.mp2.internal.expressions.Expression;
import re.agiledesign.mp2.internal.expressions.FunctionCallExpression;
import re.agiledesign.mp2.internal.expressions.FunctionExpression;
import re.agiledesign.mp2.internal.expressions.IndexExpression;
import re.agiledesign.mp2.internal.expressions.InlineListExpression;
import re.agiledesign.mp2.internal.expressions.InlineMapExpression;
import re.agiledesign.mp2.internal.expressions.InlineSetExpression;
import re.agiledesign.mp2.internal.expressions.LocalAssignmentExpression;
import re.agiledesign.mp2.internal.expressions.RangeListExpression;
import re.agiledesign.mp2.internal.expressions.ReflectedThisExpression;
import re.agiledesign.mp2.internal.expressions.SublistExpression;
import re.agiledesign.mp2.internal.expressions.TernaryExpression;
import re.agiledesign.mp2.internal.statements.Block;
import re.agiledesign.mp2.internal.statements.BreakStatement;
import re.agiledesign.mp2.internal.statements.ContinueStatement;
import re.agiledesign.mp2.internal.statements.DoStatement;
import re.agiledesign.mp2.internal.statements.EmptyStatement;
import re.agiledesign.mp2.internal.statements.ExpressionStatement;
import re.agiledesign.mp2.internal.statements.ForStatement;
import re.agiledesign.mp2.internal.statements.IfElseStatement;
import re.agiledesign.mp2.internal.statements.ReturnStatement;
import re.agiledesign.mp2.internal.statements.SequenceStatement;
import re.agiledesign.mp2.internal.statements.Statement;
import re.agiledesign.mp2.internal.statements.WhileStatement;
import re.agiledesign.mp2.tokenizer.Keywords;
import re.agiledesign.mp2.tokenizer.OperatorType;
import re.agiledesign.mp2.tokenizer.Token;
import re.agiledesign.mp2.tokenizer.TokenConstants;
import re.agiledesign.mp2.tokenizer.TokenTypes;
import re.agiledesign.mp2.tokenizer.Tokenizer;
import re.agiledesign.mp2.util.AssocEntry;
import re.agiledesign.mp2.util.Util;
import re.agiledesign.mp2.util.CoercionUtil.CoercionType;


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
		this(aSource, /* legacy */true);
	}

	public OperationParser(final String aSource, final boolean aInt32) {
		mTokenizer = new Tokenizer(aSource, aInt32);
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

		return new Block(Util.immutableList(statements));
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
		if ((token.getType() == TokenTypes.IDENTIFIER) && mTokenizer.hasNext()) {
			final Expression assignment = parseAssignment0(token.getStringValue());

			if (assignment != null) {
				return assignment;
			}
		}

		mTokenizer.restorePosition(position);
		return parseTernaryConditional();
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
					return new LocalAssignmentExpression(index, right);
				}

				index = mLexicalScope.getLocalVariableIndex(aVarName);
				if (index >= 0) {
					mLexicalScope.setAssigned(aVarName);
					return new LocalAssignmentExpression(index, right);
				}

				return new AssignmentExpression(aVarName, right);
			}
		}

		return null;
	}

	private Expression parseTernaryConditional() throws ParsingException {
		final Expression exp = parsePrecedenced(0);

		if (mTokenizer.hasNext() && advanceIfNext(TokenConstants.QUEST)) {
			final Expression t = parseAssignment();
			checkAndAdvance(TokenConstants.COLON);
			final Expression f = parseAssignment();

			return new TernaryExpression(exp, t, f);
		}

		return exp;
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
			retval = parseGrammarExpression(token);
			break;
		case CONSTANT:
			retval = mExpressionFactory.getConstantExpression(token);
			break;
		case IDENTIFIER:
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

	private Expression parseGrammarExpression(final Token aCurrent) throws ParsingException {
		if (aCurrent == TokenConstants.O_INDEX) {
			return parseInlineList(false);
		}

		if (aCurrent != TokenConstants.O_BRACK) {
			throwExpectedFound(TokenConstants.O_BRACK, aCurrent);
		}

		if (isLambda()) {
			return new ConstantExpression(parseFunctionDefinition0(true));
		}

		final int position = mTokenizer.getPostion();
		final Expression simpleExpression = parseAssignment();
		if (advanceIfNext(TokenConstants.C_BRACK)) {
			return simpleExpression;
		}

		mTokenizer.restorePosition(position);
		return parseInlineList(true);
	}

	private Expression parseInlineList(final boolean aImmutable) throws ParsingException {
		final List<Expression> elements;
		final Token expected = aImmutable ? TokenConstants.C_BRACK : TokenConstants.C_INDEX;

		if (advanceIfNext(expected)) {
			elements = Collections.emptyList();
		} else {
			final Expression first = parseAssignment();

			if (advanceIfNext(TokenConstants.RANGE)) {
				return parseRangeList(first, aImmutable);
			}

			if (advanceIfNext(expected)) {
				elements = Collections.singletonList(first);
			} else {
				final int position = mTokenizer.getPostion();
				final Token separator = mTokenizer.next();

				if (separator == TokenConstants.ARROW) {
					if (advanceIfNext(expected)) {
						// set with a single element
						return new InlineSetExpression(Collections.singletonList(first), aImmutable);
					}

					if (advanceIfNext(TokenConstants.COMMA)) {
						// parse set
						elements = parseInlineList0(first, aImmutable, true);
						return new InlineSetExpression(Util.immutableList(elements), aImmutable);
					}

					final Expression value = parseAssignment();
					final AssocEntry<Expression, Expression> firstEntry = new AssocEntry<Expression, Expression>(first,
							value);
					final List<AssocEntry<Expression, Expression>> entries = parseInlineMap(firstEntry, aImmutable);
					return new InlineMapExpression(entries, aImmutable);
				}

				mTokenizer.restorePosition(position);
				final List<Expression> temp;
				if (separator == TokenConstants.COLON) {
					temp = parseConsList(first);
				} else {
					temp = parseInlineList0(first, aImmutable, false);
				}

				elements = Util.immutableList(temp);
			}
		}

		return new InlineListExpression(elements, aImmutable);
	}

	private List<Expression> parseInlineList0(final Expression aFirst, final boolean aImmutable, final boolean aSet)
			throws ParsingException {
		final Token expected = aImmutable ? TokenConstants.C_BRACK : TokenConstants.C_INDEX;

		final ArrayList<Expression> retval = new ArrayList<Expression>();
		retval.add(aFirst);

		while (true) {
			if (!advanceIfNext(TokenConstants.COMMA)) {
				checkAndAdvance(expected);
				break;
			}

			if (advanceIfNext(expected)) {
				break;
			}

			retval.add(parseAssignment());

			if (aSet) {
				checkAndAdvance(TokenConstants.ARROW);
			}
		}

		if (aImmutable && !aSet) {
			// add a null to reuse cons list methods
			retval.add(ExpressionFactory.getNull());
		}

		return retval;
	}

	private List<Expression> parseConsList(final Expression aFirst) throws ParsingException {
		final ArrayList<Expression> retval = new ArrayList<Expression>();
		retval.add(aFirst);

		do {
			checkAndAdvance(TokenConstants.COLON);
			retval.add(parseAssignment());
		} while (!advanceIfNext(TokenConstants.C_BRACK));

		return retval;
	}

	private Expression parseRangeList(final Expression aFrom, final boolean aConsList) throws ParsingException {
		final Token expected = aConsList ? TokenConstants.C_BRACK : TokenConstants.C_INDEX;
		if (advanceIfNext(expected)) {
			if (!aConsList) {
				throw new ParsingException("Infinite strict list found");
			}

			return new RangeListExpression(aFrom);
		}

		final Expression to = parseExpression();
		checkAndAdvance(expected);

		return new RangeListExpression(aFrom, to, aConsList);
	}

	private List<AssocEntry<Expression, Expression>> parseInlineMap(final AssocEntry<Expression, Expression> aFirst,
			final boolean aImmutable) throws ParsingException {
		final Token expected = aImmutable ? TokenConstants.C_BRACK : TokenConstants.C_INDEX;

		final ArrayList<AssocEntry<Expression, Expression>> retval = new ArrayList<AssocEntry<Expression, Expression>>();
		retval.add(aFirst);

		while (true) {
			if (!advanceIfNext(TokenConstants.COMMA)) {
				checkAndAdvance(expected);
				break;
			}

			if (advanceIfNext(expected)) {
				break;
			}

			final Expression key = parseAssignment();
			checkAndAdvance(TokenConstants.ARROW);
			final Expression value = parseAssignment();

			retval.add(new AssocEntry<Expression, Expression>(key, value));
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

		if (token.getType() != TokenTypes.IDENTIFIER) {
			throwExpectedFound(TokenTypes.IDENTIFIER, token);
		}

		checkAndAdvance(TokenConstants.O_BRACK);
		final List<Expression> args = parseFunctionArgs();
		return new ReflectedThisExpression(aThis, token.getStringValue(), args);
	}

	private Expression parseIndexExpression(final Expression aThis) throws ParsingException {
		final Expression index = parseAssignment();

		if (advanceIfNext(TokenConstants.ARROW)) {
			final Expression toIndex = parseAssignment();

			checkAndAdvance(TokenConstants.C_INDEX);
			return new SublistExpression(aThis, index, toIndex);
		}

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

		return Util.immutableList(args);
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
		if (nameToken.getType() != TokenTypes.IDENTIFIER) {
			throwExpectedFound(TokenTypes.IDENTIFIER, nameToken);
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
				if (argName.getType() != TokenTypes.IDENTIFIER) {
					throwExpectedFound(TokenTypes.IDENTIFIER, argName);
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
		final ArrayList<Expression> expressions = new ArrayList<Expression>(4);

		do {
			final Token token = mTokenizer.next();

			if (token.getType() != TokenTypes.IDENTIFIER) {
				throwExpectedFound(TokenTypes.IDENTIFIER, token);
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
			return new SequenceStatement(Util.immutableList(expressions));
		}
	}

	private Statement parseStatementOrBlock() throws ParsingException {
		final int position = mTokenizer.getPostion();

		if (advanceIfNext(TokenConstants.O_BLOCK)) {
			final ArrayList<Statement> statements = new ArrayList<Statement>();

			while (!advanceIfNext(TokenConstants.C_BLOCK)) {
				final Statement statement = parseStatement();

				if (statement != EMPTY_STATEMENT) {
					statements.add(statement);
				}
			}

			if (statements.isEmpty()) {
				return EMPTY_STATEMENT;
			}

			return new Block(Util.immutableList(statements));
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
