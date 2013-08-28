package re.agiledesign.mp2.tokenizer;

public enum Keyword {
	IF, // if
	ELSE, // else
	FOR, // for
	BREAK, // break
	CONTINUE, //continue
	RETURN, // return
	DO, // do
	WHILE, // while
	SWITCH, // switch
	CASE, // case
	TRY, // try
	CATCH, // catch 
	FINALLY, // finally
	VAR, // var - a closable assignable variable
	LOCAL, // local - local var_name
	GLOBAL, // shortcut to the global scope
	THIS, // this
	FUNCTION, // function
	CLASS; // class
}
