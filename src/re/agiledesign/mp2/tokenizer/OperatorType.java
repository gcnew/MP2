package com.kleverbeast.dpf.common.operationparser.tokenizer;

public enum OperatorType {
	/*   = */ASSIGN,
	/*   + */ADD,
	/*  += */ADD_EQ(ADD),
	/*   - */SUBSTRACT,
	/*  -= */SUBSTRACT_EQ(SUBSTRACT),
	/*   * */MULTIPLY,
	/*  *= */MULTIPLY_EQ(MULTIPLY),
	/*   / */DIVIDE,
	/*  /= */DIVIDE_EQ(DIVIDE),
	/*   % */MODULO,
	/*  %= */MODULO_EQ(MODULO),
	/*   ! */NOT,
	/*  && */AND,
	/*  || */OR,
	/*   ~ */BIT_NOT,
	/*   & */BIT_AND,
	/*  &= */BIT_AND_EQ(BIT_AND),
	/*   | */BIT_OR,
	/*  |= */BIT_OR_EQ(BIT_OR),
	/*   ^ */BIT_XOR,
	/*  ^= */BIT_XOR_EQ(BIT_XOR),
	/*   > */IS_GREATER,
	/*  >= */IS_GREATER_OR_EQ,
	/*   < */IS_LESS,
	/*  <= */IS_LESS_OR_EQ,
	/*  == */IS_EQUAL,
	/*  != */IS_NOT_EQUAL,
	/* === */IS_REF_EQUAL,
	/* !== */IS_REF_NOT_EQUAL,
	/*  << */SHIFT_LEFT,
	/* <<= */SHIFT_LEFT_EQ(SHIFT_LEFT),
	/*  >> */SHIFT_RIGHT,
	/* >>= */SHIFT_RIGHT_EQ(SHIFT_RIGHT);

	private OperatorType mBasicOperator;

	private OperatorType(final OperatorType aBasicOperator) {
		mBasicOperator = aBasicOperator;
	}

	private OperatorType() {
		this(null);
	}

	public boolean hasAssignment() {
		return (this == ASSIGN) || (mBasicOperator != null);
	}

	public OperatorType getBasicOperator() {
		return mBasicOperator;
	}
}
