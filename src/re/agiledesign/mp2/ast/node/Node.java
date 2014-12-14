package re.agiledesign.mp2.ast.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import re.agiledesign.mp2.lexer.SourcePosition;
import re.agiledesign.mp2.util.Pair;

public class Node {
	private final String mType;
	private final SourcePosition mPosition;

	private Map<Object, Object> mAttributes;
	private final List<Pair<String, ?>> mChildren;

	/*package*/Node(
		final String aType,
		final SourcePosition aPosition,
		final List<Pair<String, ?>> aChildren,
		final Map<Object, Object> aAttributes) {

		mType = aType;
		mPosition = aPosition;
		mChildren = aChildren;
		mAttributes = aAttributes;
	}

	public String getType() {
		return mType;
	}

	public SourcePosition getPosition() {
		return mPosition;
	}

	public <T> T getFirstChild() {
		return this.<T> getChild(0);
	}

	public <T> T getSecondChild() {
		return this.<T> getChild(1);
	}

	public <T> T getChild(final int aIndex) {
		if (mChildren == null) {
			return null;
		}

		if (!(aIndex < mChildren.size())) {
			return null;
		}

		return (T) mChildren.get(aIndex).getSecond();
	}

	public <T> T getChild(final String aName) {
		if (mChildren == null) {
			return null;
		}

		for (final Pair<String, ?> c : mChildren) {
			if (aName.equals(c.getFirst())) {
				return (T) c.getSecond();
			}
		}

		return null;
	}

	public <T> T getAttribute(final Object aKey) {
		if (mAttributes == null) {
			return null;
		}

		return (T) mAttributes.get(aKey);
	}

	public void setAttribute(final Object aKey, final Object aValue) {
		if (mAttributes == null) {
			mAttributes = new HashMap<Object, Object>();
		}

		mAttributes.put(aKey, aValue);
	}

	public static class DSL {
		public static class Type {
			public static class ChildOrAttribute<F, S> extends Pair<F, S> {
				public ChildOrAttribute(F aFirst, S aSecond) {
					super(aFirst, aSecond);
				}
			}

			public static class Attribute extends ChildOrAttribute<Object, Object> {
				public Attribute(final Object aFirst, final Object aSecond) {
					super(aFirst, aSecond);
				}
			}

			public static class Child extends ChildOrAttribute<String, Object> {
				public Child(final String aFirst, final Object aSecond) {
					super(aFirst, aSecond);
				}
			}
		}

		public static Type.Attribute Attribute(final Object aKey, final Object aValue) {
			return new Type.Attribute(aKey, aValue);
		}

		public static Type.Child Child(final Object aValue) {
			return Child(null, aValue);
		}

		public static Type.Child Child(final String aName, final Object aValue) {
			return new Type.Child(aName, aValue);
		}

		public static Node Node(final String aType,
				final SourcePosition aPosition,
				final Collection<? extends Type.ChildOrAttribute<?, ?>> aArgs) {
			List<Pair<String, ?>> children = null;
			Map<Object, Object> attributes = null;

			for (final Type.ChildOrAttribute<?, ?> a : aArgs) {
				if (a instanceof Type.Child) {
					if (children == null) {
						children = new ArrayList<Pair<String, ?>>();
					}

					children.add((Type.Child) a);
				}

				if (a instanceof Type.Attribute) {
					if (attributes == null) {
						attributes = new HashMap<Object, Object>();
					}

					attributes.put(a.getFirst(), a.getSecond());
				}
			}

			return new Node(aType, aPosition, children, attributes);
		}

		public static Node Node(final String aType,
				final SourcePosition aPosition,
				final Type.ChildOrAttribute<?, ?>... aArgs) {
			return Node(aType, aPosition, Arrays.asList(aArgs));
		}
	}
}
