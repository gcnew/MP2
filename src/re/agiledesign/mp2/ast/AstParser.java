package re.agiledesign.mp2.ast;

import static re.agiledesign.mp2.ast.node.Node.DSL.Attribute;
import static re.agiledesign.mp2.ast.node.Node.DSL.Child;
import static re.agiledesign.mp2.ast.node.Node.DSL.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import re.agiledesign.mp2.ast.node.Node;
import re.agiledesign.mp2.collection.ConsList;
import re.agiledesign.mp2.collection.SequentialList;
import re.agiledesign.mp2.exception.ParsingException;
import re.agiledesign.mp2.lexer.Keyword;
import re.agiledesign.mp2.lexer.OperatorType;
import re.agiledesign.mp2.lexer.SourcePosition;
import re.agiledesign.mp2.lexer.SyntaxToken;
import re.agiledesign.mp2.lexer.Token;
import re.agiledesign.mp2.lexer.TokenConstants;
import re.agiledesign.mp2.lexer.TokenIterator;
import re.agiledesign.mp2.lexer.TokenType;
import re.agiledesign.mp2.lexer.Tokenizer;
import re.agiledesign.mp2.lexer.TokensBuffer;
import re.agiledesign.mp2.util.AssertUtil;
import re.agiledesign.mp2.util.Pair;
import re.agiledesign.mp2.util.StringUtil;

public class AstParser {
	private final String mSource;
	private TokenIterator mTokens;
	private SequentialList<TokenIterator> mSavePoints;

	private static final Node NULL = asNode(TokenConstants.getKeywordToken("null", SourcePosition.UNKNOWN));

	// private List<Error> mErrors = new ArrayList<Error>();

	public AstParser(final String aSource) {
		mSource = aSource;
	}

	public Node parse() throws ParsingException {
		mTokens = new TokensBuffer(new Tokenizer(mSource)).tokenIterator();

		return parseProgram();
	}

	private Node parseProgram() throws ParsingException {
		final SourcePosition start = position();
		final List<Node> body = new ArrayList<Node>();

		while (!mTokens.atEnd()) {
			body.add(parseStatement());
		}

		AssertUtil.runtimeAssert(mSavePoints == null);
		return Node("Program", position(start), Child("body", body));
	}

	private Node parseStatement() throws ParsingException {
		if (is(SyntaxToken.SEMICOL)) {
			final SourcePosition start = position();
			consume(SyntaxToken.SEMICOL);

			return Node("EmptyStatement", position(start));
		}

		if (is(SyntaxToken.O_BLOCK)) {
			final Node retval = parseBlock();
			tryConsume(SyntaxToken.SEMICOL);

			return retval;
		}

		return parseNonBlockStatement();
	}

	private Node parseNonBlockStatement() throws ParsingException {
		final Node definition = tryParseDefinition(false);

		final Node retval;
		if (definition != null) {
			retval = definition;
		} else {
			if (is(TokenType.KEYWORD)) {
				retval = parseKeywordStatement();
			} else {
				final Node expr = parseExpression();

				retval = Node("ExpressionStatement", expr.getPosition(), Child("body", expr));
			}
		}

		tryConsume(SyntaxToken.SEMICOL);

		return retval;
	}

	private Node parseKeywordStatement() throws ParsingException {
		AssertUtil.runtimeAssert(mTokens.current().getType() == TokenType.KEYWORD);

		final Node retval;
		switch (mTokens.current().<Keyword> getValue()) {
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
			retval = parseBreak();
			break;
		case CONTINUE:
			retval = parseContinue();
			break;
		case RETURN:
			retval = parseReturn();
			break;
		case LOCAL:
			//$FALL-THROUGH$
		case VAR:
			retval = parseVarDeclaration();
			break;
		default:
			throw new ParsingException("Keyword not yet implemented: " + mTokens.current());
		}

		return retval;
	}

	private Node parseVarDeclaration() throws ParsingException {
		final SourcePosition start = position();
		final String visibility = parseVisibility();

		final List<Node> declarations = new ArrayList<Node>();
		do {
			final Node name = parseIdentifier();

			Node init = null;
			if (tryConsume(OperatorType.ASSIGN)) {
				init = parseExpression();
			}

			declarations.add(Node(
				"VarDeclaration",
				position(name.getPosition()),
				Child("name", name),
				Child("init", init)));
		} while (tryConsume(SyntaxToken.COMMA));

		return Node("VarDeclarations", position(start), Child("visibility", visibility), Child("body", declarations));
	}

	private Node parseReturn() throws ParsingException {
		final SourcePosition start = position();

		consume(Keyword.RETURN);

		final Node expression;
		if (tryConsume(SyntaxToken.SEMICOL)) {
			expression = NULL;
		} else {
			expression = parseExpression();
		}

		return Node("Return", position(start), Child("right", expression));
	}

	private Node parseContinue() throws ParsingException {
		final SourcePosition start = position();

		consume(Keyword.CONTINUE);
		return Node("Continue", position(start));
	}

	private Node parseBreak() throws ParsingException {
		final SourcePosition start = position();

		consume(Keyword.BREAK);
		return Node("Break", position(start));

	}

	private Node parseDo() throws ParsingException {
		final SourcePosition start = position();

		consume(Keyword.DO);
		final Node body = parseStatement();

		consume(Keyword.WHILE);
		consume(SyntaxToken.O_BRACK);
		final Node condition = parseExpression();
		consume(SyntaxToken.C_BRACK);

		return Node("Do", position(start), Child("condition", condition), Child("body", body));
	}

	private Node parseWhile() throws ParsingException {
		final SourcePosition start = position();

		consume(Keyword.WHILE);
		consume(SyntaxToken.O_BRACK);
		final Node condition = parseExpression();
		consume(SyntaxToken.C_BRACK);

		final Node body = parseStatement();

		return Node("Do", position(start), Child("condition", condition), Child("body", body));
	}

	private Node parseFor() throws ParsingException {
		final SourcePosition start = position();

		consume(Keyword.FOR);
		consume(SyntaxToken.O_BRACK);

		final Node init; // TODO allow for variable creation
		if (tryConsume(SyntaxToken.SEMICOL)) {
			init = NULL;
		} else {
			init = parseExpression();
			consume(SyntaxToken.SEMICOL);
		}

		final Node condition;
		if (tryConsume(SyntaxToken.SEMICOL)) {
			condition = NULL;
		} else {
			condition = parseExpression();
			consume(SyntaxToken.SEMICOL);
		}

		final Node advance;
		if (is(SyntaxToken.C_BRACK)) {
			advance = NULL;
		} else {
			advance = parseExpression();
		}

		consume(SyntaxToken.C_BRACK);
		final Node body = parseStatement();

		return Node(
			"For",
			position(start),
			Child("init", init),
			Child("condition", condition),
			Child("advance", advance),
			Child("body", body));
	}

	private Node parseIfElse() throws ParsingException {
		final SourcePosition start = position();

		consume(Keyword.IF);
		consume(SyntaxToken.O_BRACK);
		final Node condition = parseExpression();
		consume(SyntaxToken.C_BRACK);

		final Node body = parseStatement();
		final Node elseBody = tryConsume(Keyword.ELSE) ? parseStatement() : NULL;

		return Node(
			"If",
			position(start),
			Child("condition", condition),
			Child("body", body),
			Child("elseBody", elseBody));
	}

	private Node parseBlock() throws ParsingException {
		final SourcePosition start = position();
		final List<Node> body = new ArrayList<Node>();

		consume(SyntaxToken.O_BLOCK);

		while (!mTokens.atEnd() && !is(SyntaxToken.C_BLOCK)) {
			body.add(parseStatement());
		}

		consume(SyntaxToken.C_BLOCK);

		return Node("Block", position(start), Child("body", body));
	}

	private Node tryParseDefinition(boolean aExpression) throws ParsingException {
		if (is(TokenType.KEYWORD)) {
			switch (mTokens.current().<Keyword> getValue()) {
			case FUNCTION:
				return parseFunction(aExpression);
			case CLASS:
				throw new ParsingException("Classes are not yet implemented");
			}
		}

		return null;
	}

	private Node parseFunction(boolean aIsExpression) throws ParsingException {
		final SourcePosition start = position();

		consume(Keyword.FUNCTION);

		final Node name;
		if (aIsExpression && !is(TokenType.IDENTIFIER)) {
			// expressions have optional name
			name = null;
		} else {
			name = parseIdentifier();
		}

		final Node parameters = parseFunctionParameters();
		final Node body = parseBlock();

		return Node(
			"FunctionDefinition",
			position(start),
			Attribute("isExpression", Boolean.valueOf(aIsExpression)),
			Attribute("isLambda", Boolean.FALSE),
			Child("name", name),
			Child("parameters", parameters),
			Child("body", body));
	}

	private Node parseLambda() throws ParsingException {
		final SourcePosition start = position();

		final Node parameters = parseFunctionParameters();
		consume(SyntaxToken.LAMBDA);
		final Node body = parseStatement();

		return Node(
			"FunctionDefinition",
			position(start),
			Attribute("isExpression", Boolean.TRUE),
			Attribute("isLambda", Boolean.TRUE),
			Child("parameters", parameters),
			Child("body", body));
	}

	private Node parseFunctionParameters() throws ParsingException {
		final SourcePosition start = position();

		consume(SyntaxToken.O_BRACK);

		if (tryConsume(SyntaxToken.C_BRACK)) {
			return Node("FunctionParameters", position(start), Child("body", Collections.emptyList()));
		}

		final List<Node> children = new ArrayList<Node>();
		while (true) {
			final Node name = parseIdentifier();

			children.add(name);

			if (tryConsume(SyntaxToken.C_BRACK)) {
				break;
			}

			consume(SyntaxToken.COMMA);
		}

		return Node("FunctionParameters", position(start), Child("body", children));
	}

	private Node parseExpression() throws ParsingException {
		// TODO Auto-generated method stub
		return parsePrimaryExpression();
	}

	private Node parsePrimaryExpression() throws ParsingException {
		switch (mTokens.current().getType()) {
		case CONSTANT:
			return asNode(mTokens.current());
		case IDENTIFIER:
			return asNode(mTokens.current());
		case SYNTAX_TOKEN:
			if (is(SyntaxToken.O_BRACK)) {
				return parseImmutableCollectionOrExpression();
			}

			if (is(SyntaxToken.O_INDEX)) {
				return parseMutableCollection();
			}

			//$FALL-THROUGH$
		default:
			throw throwUnexpected(mTokens.current());

		}
	}

	private Node parseImmutableCollectionOrExpression() throws ParsingException {
		final SourcePosition start = position();

		// lots of ambiguity here
		try {
			save();
			consume(SyntaxToken.O_BRACK);

			if (tryConsume(SyntaxToken.C_BRACK)) {
				reload();
				return parseLambda();
			}

			final Node first = parseExpression();
			if (tryConsume(SyntaxToken.C_BRACK)) {
				if (tryConsume(SyntaxToken.LAMBDA)) {
					reload();
					return parseLambda();
				}

				return Node("PriorityExpression", position(start), Child("Body", first));
			}

			if (tryConsume(SyntaxToken.COMMA)) {
				reload();
				final Node array = parseArray();

				if (tryConsume(SyntaxToken.LAMBDA)) {
					reload();
					return parseLambda();
				}

				return array;
			}

			if (tryConsume(SyntaxToken.ARROW)) {
				if (tryConsume(SyntaxToken.C_BRACK) || tryConsume(SyntaxToken.COMMA)) {
					reload();
					return parseSet();
				}

				reload();
				return parseMap();
			}

			if (tryConsume(SyntaxToken.COLON)) {
				reload();
				return parseConsList();
			}

			if (tryConsume(SyntaxToken.RANGE)) {
				reload();
				return parseRangeList();
			}

			throw throwUnexpected(mTokens.current());
		} finally {
			release();
		}
	}

	private Node parseMutableCollection() throws ParsingException {
		try {
			save();
			consume(SyntaxToken.O_INDEX);

			if (tryConsume(SyntaxToken.C_INDEX)) {
				reload();
				return parseArray();
			}

			@SuppressWarnings("unused")
			final Node first = parseExpression();

			if (tryConsume(SyntaxToken.C_INDEX) || tryConsume(SyntaxToken.COMMA)) {
				reload();
				return parseArray();
			}

			if (tryConsume(SyntaxToken.ARROW)) {
				if (tryConsume(SyntaxToken.C_BRACK) || tryConsume(SyntaxToken.COMMA)) {
					reload();
					return parseSet();
				}

				reload();
				return parseMap();
			}

			throw throwUnexpected(mTokens.current());
		} finally {
			release();
		}
	}

	private Node parseArray() throws ParsingException {
		final SourcePosition start = position();
		final List<Node> body = new ArrayList<Node>();
		final boolean immutable = is(SyntaxToken.O_BRACK);
		final SyntaxToken closingBrack = immutable ? SyntaxToken.C_BRACK : SyntaxToken.C_INDEX;

		consume(immutable ? SyntaxToken.O_BRACK : SyntaxToken.O_INDEX);

		while (!tryConsume(closingBrack)) {
			final Node expr = parseExpression();

			body.add(expr);

			if (!tryConsume(SyntaxToken.COMMA)) {
				consume(closingBrack);
				break;
			}
		}

		return Node(
			"ArrayLiteral",
			position(start),
			Attribute("immutable", Boolean.valueOf(immutable)),
			Child("body", body));
	}

	private Node parseMap() throws ParsingException {
		final SourcePosition start = position();
		final boolean immutable = is(SyntaxToken.O_BRACK);
		final List<Pair<Node, Node>> body = new ArrayList<Pair<Node, Node>>();
		final SyntaxToken closingBrack = immutable ? SyntaxToken.C_BRACK : SyntaxToken.C_INDEX;

		consume(immutable ? SyntaxToken.O_BRACK : SyntaxToken.O_INDEX);

		do {
			final Node key = parseExpression();
			consume(SyntaxToken.ARROW);
			final Node value = parseExpression();

			body.add(new Pair<Node, Node>(key, value));

			if (!tryConsume(SyntaxToken.COMMA)) {
				consume(closingBrack);
				break;
			}
		} while (!tryConsume(closingBrack));

		return Node(
			"MapLiteral",
			position(start),
			Attribute("immutable", Boolean.valueOf(immutable)),
			Child("body", body));
	}

	private Node parseSet() throws ParsingException {
		final SourcePosition start = position();
		final List<Node> body = new ArrayList<Node>();
		final boolean immutable = is(SyntaxToken.O_BRACK);
		final SyntaxToken closingBrack = immutable ? SyntaxToken.C_BRACK : SyntaxToken.C_INDEX;

		consume(immutable ? SyntaxToken.O_BRACK : SyntaxToken.O_INDEX);

		do {
			final Node value = parseExpression();
			consume(SyntaxToken.ARROW);

			body.add(value);

			if (!tryConsume(SyntaxToken.COMMA)) {
				consume(closingBrack);
				break;
			}
		} while (!tryConsume(closingBrack));

		return Node(
			"SetLiteral",
			position(start),
			Attribute("immutable", Boolean.valueOf(immutable)),
			Child("body", body));
	}

	private Node parseConsList() throws ParsingException {
		final SourcePosition start = position();
		final List<Node> body = new ArrayList<Node>();

		consume(SyntaxToken.O_BRACK);

		do {
			final Node expr = parseExpression();
			body.add(expr);
		} while (!tryConsume(SyntaxToken.COLON));

		consume(SyntaxToken.C_BRACK);

		return Node("ConsLiteral", position(start), Child("body", body));
	}

	private Node parseRangeList() throws ParsingException {
		final SourcePosition start = position();

		consume(SyntaxToken.O_BRACK);
		final Node first = parseExpression();

		consume(SyntaxToken.RANGE);

		final Node end;
		if (tryConsume(SyntaxToken.C_BRACK)) {
			end = null;
		} else {
			end = parseExpression();
			consume(SyntaxToken.C_BRACK);
		}

		return Node("RangeLiteral", position(start), Child("start", first), Child("end", end));
	}

	private void recoverStatement() {
		// TODO
	}

	private void recoverBlock() {
		// TODO
	}

	private Node parseIdentifier() throws ParsingException {
		final Token token = mTokens.current();

		consume(TokenType.KEYWORD);
		return Node("Identifier", token.getPosition(), Child("value", token.getValue()));
	}

	private String parseVisibility() throws ParsingException {
		final Token token = mTokens.current();

		consume(TokenType.KEYWORD);
		switch (token.<Keyword> getValue()) {
		case VAR:
			return "var";
		case LOCAL:
			return "local";
		default:
			throw throwUnexpected(token);
		}
	}

	private static Node asNode(final Token aToken) {
		String type = null;

		if (aToken.getType() == TokenType.CONSTANT) {
			type = "Literal";
		}

		if (aToken.getType() == TokenType.IDENTIFIER) {
			type = "Identifier";
		}

		AssertUtil.notNull(type, "Unimplemented: {}", aToken);

		return Node("Identifier", aToken.getPosition(), Child("value", aToken.getValue()));
	}

	private void save() {
		mSavePoints = new ConsList<TokenIterator>(mTokens.diverge(), mSavePoints);
	}

	private void reload() {
		mTokens = mSavePoints.first();
	}

	private void release() {
		mSavePoints = mSavePoints.rest();
	}

	private SourcePosition position() {
		if (mTokens.atEnd()) {
			try {
				return mTokens.last().getPosition();
			} catch (final NoSuchElementException e) {
				return SourcePosition.BEGINING;
			}
		}

		return mTokens.current().getPosition();
	}

	private SourcePosition position(final SourcePosition aStart) {
		return new SourcePosition(aStart.getStart(), position().getEnd(), aStart.getLine(), aStart.getChar());
	}

	private boolean is(final TokenType aExpected) {
		return mTokens.current().getType() == aExpected;
	}

	private boolean is(final OperatorType aExpected) {
		return mTokens.current().getValue() == aExpected;
	}

	private boolean is(final SyntaxToken aExpected) {
		return mTokens.current().getValue() == aExpected;
	}

	private boolean is(final Keyword aExpected) {
		return mTokens.current().getValue() == aExpected;
	}

	private boolean tryConsume(final TokenType aExpected) {
		if (!is(aExpected)) {
			return false;
		}

		mTokens.advance();
		return true;
	}

	private boolean tryConsume(final OperatorType aExpected) {
		if (!is(aExpected)) {
			return false;
		}

		mTokens.advance();
		return true;
	}

	private boolean tryConsume(final SyntaxToken aExpected) {
		if (!is(aExpected)) {
			return false;
		}

		mTokens.advance();
		return true;
	}

	private boolean tryConsume(final Keyword aExpected) {
		if (!is(aExpected)) {
			return false;
		}

		mTokens.advance();
		return true;
	}

	private void consume(final TokenType aExpected) throws ParsingException {
		if (!is(aExpected)) {
			throwExpected(aExpected);
		}
	}

	private void consume(final SyntaxToken aExpected) throws ParsingException {
		if (!is(aExpected)) {
			throwExpected(aExpected);
		}

		mTokens.advance();
	}

	private void consume(final Keyword aExpected) throws ParsingException {
		if (!is(aExpected)) {
			throwExpected(aExpected);
		}

		mTokens.advance();
	}

	private String msg(final String aFormat, final Object... aArgs) {
		return StringUtil.format(aFormat, aArgs);
	}

	private void throwExpected(final Object aExpected) throws ParsingException {
		final Token t = mTokens.current();
		final String msg = msg("{}: Expected token: {}, found {}", t.getPosition(), aExpected, t);

		throw new ParsingException(msg);
	}

	private ParsingException throwUnexpected(final Token aToken) throws ParsingException {
		throw new ParsingException(msg("{}: Unexpected token: {}", aToken.getPosition(), aToken));
	}
}
