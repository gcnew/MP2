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

import re.agiledesign.mp2.VarInfo.Visibility;
import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.internal.ExpressionFactory;
import re.agiledesign.mp2.internal.OperatorFactory;
import re.agiledesign.mp2.internal.expressions.AccessExpression;
import re.agiledesign.mp2.internal.expressions.CastExpression;
import re.agiledesign.mp2.internal.expressions.ConstantExpression;
import re.agiledesign.mp2.internal.expressions.Expression;
import re.agiledesign.mp2.internal.expressions.FunctionCallExpression;
import re.agiledesign.mp2.internal.expressions.FunctionExpression;
import re.agiledesign.mp2.internal.expressions.GlobalAccessExpression;
import re.agiledesign.mp2.internal.expressions.IndexAccessExpression;
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
import re.agiledesign.mp2.tokenizer.Keyword;
import re.agiledesign.mp2.tokenizer.OperatorType;
import re.agiledesign.mp2.tokenizer.SyntaxToken;
import re.agiledesign.mp2.tokenizer.Token;
import re.agiledesign.mp2.tokenizer.TokenType;
import re.agiledesign.mp2.tokenizer.Tokenizer;
import re.agiledesign.mp2.util.AssertUtil;
import re.agiledesign.mp2.util.AssocEntry;
import re.agiledesign.mp2.util.CoercionUtil.CoercionType;
import re.agiledesign.mp2.util.Util;

public class MP2Parser {
	private final Tokenizer mTokenizer;
	private/*   */LexicalScope mLexicalScope = new LexicalScope(null);
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

	public MP2Parser(final String aSource) {
		this(aSource, /* legacy */true);
	}

	public MP2Parser(final String aSource, final boolean aInt32) {
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

	private Expression parseAssignment() throws ParsingException {
		final Expression varAssign = parseVarAssign();
		if (varAssign != null) {
			return varAssign;
		}

		final Expression left = parseTernaryConditional();
		if (mTokenizer.hasNext()) {
			final int position = mTokenizer.getPostion();
			final Token token = mTokenizer.next();

			if (token.getType() == TokenType.OPERATOR) {
				final OperatorType operator = token.getValue();

				if (operator.hasAssignment()) {
					Expression right = parseAssignment();

					if (operator != ASSIGN) {
						final OperatorType basicOperator = operator.getBasicOperator();
						right = OperatorFactory.getBinaryOperator(basicOperator, left, right);
					}

					if (left instanceof AccessExpression) {
						return ((AccessExpression) left).asAssignment(right);
					}

					throw new ParsingException("LHS of assignment not assignable: " + left);
				}
			}

			mTokenizer.restorePosition(position);
		}

		return left;
	}

	private Expression parseVarAssign() throws ParsingException {
		final int position = mTokenizer.getPostion();
		final Token next = mTokenizer.next();

		if ((next.getType() == TokenType.IDENTIFIER) && (advanceIfNext(TokenType.OPERATOR, ASSIGN) != null)) {
			final VarInfo var = mLexicalScope.getVariable(next.getStringValue());

			if ((var != null) && (var.isLocal() || var.isVar())) {
				final Expression right = parseAssignment();

				var.assign();

				// TODO: handle var properly
				return new LocalAssignmentExpression(var.getIndex(), right);
			}
		}

		mTokenizer.restorePosition(position);
		return null;
	}

	private Expression parseTernaryConditional() throws ParsingException {
		final Expression exp = parsePrecedenced(0);

		if (mTokenizer.hasNext() && advanceIfNext(SyntaxToken.QUEST)) {
			final Expression t = parseAssignment();
			checkAndAdvance(SyntaxToken.COLON);
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
			final OperatorType operator = advanceIfNext(TokenType.OPERATOR, operators);

			if (operator == null) {
				break;
			}

			retval = OperatorFactory.getBinaryOperator(operator, retval, parsePrecedenced(aIndex + 1));
		}

		return retval;
	}

	private Expression parseCast() throws ParsingException {
		final int position = mTokenizer.getPostion();

		if (advanceIfNext(SyntaxToken.O_BRACK)) {
			final Token token = mTokenizer.next();

			if (token.getType() == TokenType.TYPE) {
				final CoercionType type = token.getValue();
				checkAndAdvance(SyntaxToken.C_BRACK);

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

			if (token.getType() == TokenType.OPERATOR) {
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
		case SYNTAX_TOKEN:
			retval = parseGrammarExpression(token);
			break;
		case CONSTANT:
			retval = mExpressionFactory.getConstantExpression(token);
			break;
		case IDENTIFIER:
			if (mTokenizer.hasNext() && advanceIfNext(SyntaxToken.O_BRACK)) {
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
			if (advanceIfNext(SyntaxToken.SCOPE)) {
				retval = parseThisExpression(retval);

				continue;
			}

			if (advanceIfNext(SyntaxToken.O_INDEX)) {
				retval = parseIndexExpression(retval);

				continue;
			}

			if (advanceIfNext(SyntaxToken.O_BRACK)) {
				final List<Expression> args = parseFunctionArgs();
				retval = new FunctionCallExpression(retval, args);

				continue;
			}

			break;
		}

		return retval;
	}

	private Expression parseGrammarExpression(final Token aCurrent) throws ParsingException {
		if (is(aCurrent, SyntaxToken.O_INDEX)) {
			return parseInlineList(false);
		}

		if (!is(aCurrent, SyntaxToken.O_BRACK)) {
			throwExpectedFound(SyntaxToken.O_BRACK, aCurrent);
		}

		if (isLambda()) {
			return new ConstantExpression(parseFunctionDefinition0(true));
		}

		final int position = mTokenizer.getPostion();
		final Expression simpleExpression = parseAssignment();
		if (advanceIfNext(SyntaxToken.C_BRACK)) {
			return simpleExpression;
		}

		mTokenizer.restorePosition(position);
		return parseInlineList(true);
	}

	private boolean isComprehension(final boolean aImmutable) throws ParsingException {
		int brack = aImmutable ? 1 : 0;
		int index = 1 - brack;
		final int position = mTokenizer.getPostion();

		// TODO: check for ( x ? 1 : 0; ...)
		while (true) {
			final Token token = mTokenizer.next();

			if (token.getType() == TokenType.SYNTAX_TOKEN) {
				switch (token.<SyntaxToken> getValue()) {
				case O_BRACK:
					++brack;
					break;
				case O_INDEX:
					++index;
					break;
				case C_BRACK:
					--brack;

					if ((aImmutable ? brack : index) == 0) {
						mTokenizer.restorePosition(position);
						return false;
					}
					break;
				case C_INDEX:
					--index;

					if ((aImmutable ? brack : index) == 0) {
						mTokenizer.restorePosition(position);
						return false;
					}
					break;
				case COMMA:
				case COLON:
				case SEMICOL:
					if (((aImmutable ? brack : index) == 1) && ((aImmutable ? index : brack) == 0)) {
						mTokenizer.restorePosition(position);
						return is(token, SyntaxToken.SEMICOL);
					}
					break;
				}
			}
		}
	}

	private Expression parseInlineList(final boolean aImmutable) throws ParsingException {
		final List<Expression> elements;
		final SyntaxToken expected = aImmutable ? SyntaxToken.C_BRACK : SyntaxToken.C_INDEX;

		if (advanceIfNext(expected)) {
			elements = Collections.emptyList();
		} else {
			final Expression first = parseAssignment();

			if (advanceIfNext(SyntaxToken.RANGE)) {
				return parseRangeList(first, aImmutable);
			}

			if (advanceIfNext(expected)) {
				elements = Collections.singletonList(first);
			} else {
				final int position = mTokenizer.getPostion();
				final Token separator = mTokenizer.next();

				if (is(separator, SyntaxToken.ARROW)) {
					if (advanceIfNext(expected)) {
						// set with a single element
						return new InlineSetExpression(Collections.singletonList(first), aImmutable);
					}

					if (advanceIfNext(SyntaxToken.COMMA)) {
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
				if (is(separator, SyntaxToken.COLON)) {
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
		final SyntaxToken expected = aImmutable ? SyntaxToken.C_BRACK : SyntaxToken.C_INDEX;

		final ArrayList<Expression> retval = new ArrayList<Expression>();
		retval.add(aFirst);

		while (true) {
			if (!advanceIfNext(SyntaxToken.COMMA)) {
				checkAndAdvance(expected);
				break;
			}

			if (advanceIfNext(expected)) {
				break;
			}

			retval.add(parseAssignment());

			if (aSet) {
				checkAndAdvance(SyntaxToken.ARROW);
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
			checkAndAdvance(SyntaxToken.COLON);
			retval.add(parseAssignment());
		} while (!advanceIfNext(SyntaxToken.C_BRACK));

		return retval;
	}

	private Expression parseRangeList(final Expression aFrom, final boolean aConsList) throws ParsingException {
		final SyntaxToken expected = aConsList ? SyntaxToken.C_BRACK : SyntaxToken.C_INDEX;
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
		final SyntaxToken expected = aImmutable ? SyntaxToken.C_BRACK : SyntaxToken.C_INDEX;

		final ArrayList<AssocEntry<Expression, Expression>> retval = new ArrayList<AssocEntry<Expression, Expression>>();
		retval.add(aFirst);

		while (true) {
			if (!advanceIfNext(SyntaxToken.COMMA)) {
				checkAndAdvance(expected);
				break;
			}

			if (advanceIfNext(expected)) {
				break;
			}

			final Expression key = parseAssignment();
			checkAndAdvance(SyntaxToken.ARROW);
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

			if (is(token, SyntaxToken.O_BRACK)) {
				++balance;
				continue;
			}

			if (is(token, SyntaxToken.C_BRACK)) {
				--balance;
			}
		} while (balance != 0);

		final boolean retval = mTokenizer.hasNext() && is(mTokenizer.next(), SyntaxToken.LAMBDA);
		mTokenizer.restorePosition(position);

		return retval;
	}

	private Expression parseAccessExpression(final String aVarName) throws ParsingException {
		final VarInfo var = mLexicalScope.getVariable(aVarName);

		if (var != null) {
			if (var.isArgument()) {
				return mExpressionFactory.getArgumentAccessExpression(var.getIndex());
			}

			if (var.isLocal()) {
				if (!var.isAssigned()) {
					throw new ParsingException("Local variable '" + aVarName + "' is used without being initialized");
				}

				return mExpressionFactory.getLocalAccessExpression(var.getIndex());
			}
		}

		return new GlobalAccessExpression(aVarName);
	}

	private Expression parseThisExpression(final Expression aThis) throws ParsingException {
		final Token token = mTokenizer.next();

		if (token.getType() != TokenType.IDENTIFIER) {
			throwExpectedFound(TokenType.IDENTIFIER, token);
		}

		checkAndAdvance(SyntaxToken.O_BRACK);
		final List<Expression> args = parseFunctionArgs();
		return new ReflectedThisExpression(aThis, token.getStringValue(), args);
	}

	private Expression parseIndexExpression(final Expression aThis) throws ParsingException {
		final Expression index = parseAssignment();

		if (advanceIfNext(SyntaxToken.ARROW)) {
			final Expression toIndex = parseAssignment();

			checkAndAdvance(SyntaxToken.C_INDEX);
			return new SublistExpression(aThis, index, toIndex);
		}

		checkAndAdvance(SyntaxToken.C_INDEX);
		return new IndexAccessExpression(aThis, index);
	}

	private Expression parseFunctionCall(final String aFunctionName) throws ParsingException {
		final List<Expression> args = parseFunctionArgs();
		final Expression functionAccessor = parseAccessExpression(aFunctionName);

		return new FunctionCallExpression(functionAccessor, args);
	}

	private List<Expression> parseFunctionArgs() throws ParsingException {
		final List<Expression> args = new ArrayList<Expression>(4);

		if (!advanceIfNext(SyntaxToken.C_BRACK)) {
			while (true) {
				args.add(parseAssignment());

				if (advanceIfNext(SyntaxToken.C_BRACK)) {
					break;
				}

				checkAndAdvance(SyntaxToken.COMMA);
			}
		}

		return Util.immutableList(args);
	}

	private boolean parseDefinitionStatement() throws ParsingException {
		final int position = mTokenizer.getPostion();
		final Token token = mTokenizer.next();

		if (token.getType() == TokenType.KEYWORD) {
			switch (token.<Keyword> getValue()) {
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

	private void newLexicalScope() {
		mLexicalScope = new LexicalScope(mLexicalScope);
	}

	private void restoreLexicalScope() {
		mLexicalScope = mLexicalScope.getParentScope();
	}

	private void parseFunctionDefinition() throws ParsingException {
		final Token nameToken = mTokenizer.next();
		if (nameToken.getType() != TokenType.IDENTIFIER) {
			throwExpectedFound(TokenType.IDENTIFIER, nameToken);
		}

		checkAndAdvance(SyntaxToken.O_BRACK);
		final FunctionExpression function = parseFunctionDefinition0(false);
		mFunctions.put(nameToken.getStringValue(), function);
	}

	private FunctionExpression parseFunctionDefinition0(final boolean aLambda) throws ParsingException {
		newLexicalScope();
		if (!advanceIfNext(SyntaxToken.C_BRACK)) {
			while (true) {
				final Token argName = mTokenizer.next();
				if (argName.getType() != TokenType.IDENTIFIER) {
					throwExpectedFound(TokenType.IDENTIFIER, argName);
				}

				mLexicalScope.addVariable(argName.getStringValue(), Visibility.ARGUMENT);
				if (advanceIfNext(SyntaxToken.C_BRACK)) {
					break;
				}

				checkAndAdvance(SyntaxToken.COMMA);
			}
		}

		if (aLambda) {
			checkAndAdvance(SyntaxToken.LAMBDA);
		}

		final Statement body = parseStatementOrBlock();
		final int localsCount = mLexicalScope.getCountOf(Visibility.LOCAL);
		final List<String> argsArray = mLexicalScope.getArgumentsArray();

		restoreLexicalScope();
		return new FunctionExpression(body, localsCount, Util.immutableList(argsArray));
	}

	private Statement parseStatement() throws ParsingException {
		if (advanceIfNext(SyntaxToken.SEMICOL)) {
			return EMPTY_STATEMENT;
		}

		final int position = mTokenizer.getPostion();
		final Token token = mTokenizer.next();

		Statement retval = null;
		if (token.getType() == TokenType.KEYWORD) {
			switch (token.<Keyword> getValue()) {
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
				retval = parseVarStatement(Visibility.LOCAL);
				break;
			case VAR:
				retval = parseVarStatement(Visibility.VAR);
				break;
			default:
				throw new ParsingException("Keyword not yet implemented: " + token);
			}
		} else {
			mTokenizer.restorePosition(position);
			retval = new ExpressionStatement(parseAssignment());
		}

		if (mTokenizer.hasNext()) {
			advanceIfNext(SyntaxToken.SEMICOL);
		}

		return retval;
	}

	private Statement parseIfElse() throws ParsingException {
		checkAndAdvance(SyntaxToken.O_BRACK);
		final Expression condition = parseAssignment();
		checkAndAdvance(SyntaxToken.C_BRACK);

		final Statement trueStatement = parseStatementOrBlock();

		if (mTokenizer.hasNext() && advanceIfNext(Keyword.ELSE)) {
			return new IfElseStatement(condition, trueStatement, parseStatementOrBlock());
		}

		return new IfElseStatement(condition, trueStatement);
	}

	private Statement parseFor() throws ParsingException {
		checkAndAdvance(SyntaxToken.O_BRACK);

		final Expression expr1;
		if (advanceIfNext(SyntaxToken.SEMICOL)) {
			expr1 = mExpressionFactory.getConstantExpression((Object) null);
		} else {
			expr1 = parseAssignment();
			checkAndAdvance(SyntaxToken.SEMICOL);
		}

		final Expression expr2;
		if (advanceIfNext(SyntaxToken.SEMICOL)) {
			expr2 = mExpressionFactory.getConstantExpression((Object) null);
		} else {
			expr2 = parseAssignment();
			checkAndAdvance(SyntaxToken.SEMICOL);
		}

		final Expression expr3;
		if (advanceIfNext(SyntaxToken.C_BRACK)) {
			expr3 = mExpressionFactory.getConstantExpression((Object) null);
		} else {
			expr3 = parseAssignment();
			checkAndAdvance(SyntaxToken.C_BRACK);
		}

		final Statement body = parseStatementOrBlock();

		return new ForStatement(expr1, expr2, expr3, body);
	}

	private Statement parseWhile() throws ParsingException {
		checkAndAdvance(SyntaxToken.O_BRACK);
		final Expression condition = parseAssignment();
		checkAndAdvance(SyntaxToken.C_BRACK);

		final Statement body = parseStatementOrBlock();
		return new WhileStatement(condition, body);
	}

	private Statement parseDo() throws ParsingException {
		final Statement body = parseStatementOrBlock();

		checkAndAdvance(Keyword.WHILE);
		checkAndAdvance(SyntaxToken.O_BRACK);
		final Expression condition = parseAssignment();
		checkAndAdvance(SyntaxToken.C_BRACK);

		return new DoStatement(condition, body);
	}

	private Statement parseReturn() throws ParsingException {
		if (advanceIfNext(SyntaxToken.SEMICOL)) {
			return EMPTY_RETURN_STATEMENT;
		}

		final Expression expression = parseAssignment();
		return new ReturnStatement(expression);
	}

	private Statement parseVarStatement(final Visibility aVisibility) throws ParsingException {
		AssertUtil.runtimeAssert(aVisibility == Visibility.LOCAL || aVisibility == Visibility.VAR);

		final ArrayList<Expression> expressions = new ArrayList<Expression>(4);

		do {
			final Token token = mTokenizer.next();

			if (token.getType() != TokenType.IDENTIFIER) {
				throwExpectedFound(TokenType.IDENTIFIER, token);
			}

			final String varName = token.getStringValue();
			mLexicalScope.addVariable(varName, aVisibility);

			if (advanceIfNext(TokenType.OPERATOR, OperatorType.ASSIGN) != null) {
				mTokenizer.restorePosition(mTokenizer.getPostion() - 2);

				final Expression init = parseVarAssign();
				AssertUtil.notNull(init);

				expressions.add(init);
			}
		} while (mTokenizer.hasNext() && advanceIfNext(SyntaxToken.COMMA));

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

		if (advanceIfNext(SyntaxToken.O_BLOCK)) {
			final ArrayList<Statement> statements = new ArrayList<Statement>();

			while (!advanceIfNext(SyntaxToken.C_BLOCK)) {
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

	private boolean is(final Token aToken, final Keyword aObject) {
		return aToken.getValue() == aObject;
	}

	private boolean is(final Token aToken, final SyntaxToken aObject) {
		return aToken.getValue() == aObject;
	}

	private void checkAndAdvance(final SyntaxToken aValue) throws ParsingException {
		final Token token = mTokenizer.next();

		if (!is(token, aValue)) {
			throwExpectedFound(aValue, token);
		}
	}

	private void checkAndAdvance(final Keyword aValue) throws ParsingException {
		final Token token = mTokenizer.next();

		if (!is(token, aValue)) {
			throwExpectedFound(aValue, token);
		}
	}

	private boolean advanceIfNext(final SyntaxToken aValue) throws ParsingException {
		final int position = mTokenizer.getPostion();

		if (is(mTokenizer.next(), aValue)) {
			return true;
		}

		mTokenizer.restorePosition(position);
		return false;
	}

	private boolean advanceIfNext(final Keyword aValue) throws ParsingException {
		final int position = mTokenizer.getPostion();

		if (is(mTokenizer.next(), aValue)) {
			return true;
		}

		mTokenizer.restorePosition(position);
		return false;
	}

	private <T> T advanceIfNext(final TokenType aType, final T... aValues) throws ParsingException {
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

	private void throwExpectedFound(final Keyword aExpected, final Token aFound) throws ParsingException {
		throw new ParsingException("Expected token: " + aExpected + ", found: " + aFound);
	}

	private void throwExpectedFound(final SyntaxToken aExpected, final Token aFound) throws ParsingException {
		throw new ParsingException("Expected token: " + aExpected + ", found: " + aFound);
	}

	private void throwExpectedFound(final TokenType aExpected, final Token aFound) throws ParsingException {
		throw new ParsingException("Expected token of type: " + aExpected + ", found: " + aFound);
	}

	private void throwUnexpected(final Token aToken) throws ParsingException {
		throw new ParsingException("Unexpected token: " + aToken);
	}
}
