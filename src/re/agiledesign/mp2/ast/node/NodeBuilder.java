package re.agiledesign.mp2.ast.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import re.agiledesign.mp2.lexer.SourcePosition;
import re.agiledesign.mp2.util.AssertUtil;
import re.agiledesign.mp2.util.Pair;

public class NodeBuilder {
	private String mType;
	private SourcePosition mStartPosition;
	private SourcePosition mEndPosition;

	private Map<Object, Object> mAttributes;
	private List<Pair<String, ?>> mChildren;

	public NodeBuilder() {
		// default constructor
	}

	public NodeBuilder(final SourcePosition aStartPosition) {
		setStartPosition(aStartPosition);
	}

	public NodeBuilder setType(final String aType) {
		mType = aType;

		return this;
	}

	public NodeBuilder setStartPosition(final SourcePosition aStartPosition) {
		mStartPosition = aStartPosition;

		return this;
	}

	public NodeBuilder setEndPosition(final SourcePosition aEndPosition) {
		mEndPosition = aEndPosition;

		return this;
	}

	public NodeBuilder addChild(final Object aChild) {
		addChild(null, aChild);

		return this;
	}

	public NodeBuilder addChild(final String aName, final Object aChild) {
		if (mChildren == null) {
			mChildren = new ArrayList<Pair<String, ?>>(3);
		}

		mChildren.add(new Pair<String, Object>(aName, aChild));

		return this;
	}

	public NodeBuilder setAttribute(final Object aKey, final Object aValue) {
		if (mAttributes == null) {
			mAttributes = new HashMap<Object, Object>();
		}

		mAttributes.put(aKey, aValue);

		return this;
	}

	private SourcePosition computePosition() {
		return new SourcePosition(
			mStartPosition.getStart(),
			mEndPosition.getEnd(),
			mStartPosition.getLine(),
			mStartPosition.getChar()
		);
	}

	public Node build() {
		AssertUtil.notNull(mType);

		return new Node(mType, computePosition(), mChildren, mAttributes);
	}
}
