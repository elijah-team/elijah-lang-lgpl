# Type Names

There a four type of type names in **Elijjah**.

1. Generic

~~~~
(?|"generic") IDENT
~~~~

2. Typeof

~~~~
"typeof" QUALIDENT
~~~~

3. Normal

~~~~
IDENT[IDENT (, IDENT)*]
~~~~

4. Function

~~~~
"func" IDENT LPAREN args RPAREN -> returnType
"proc" IDENT LPAREN args RPAREN
~~~~

Obviously for a `proc`, one doesn't include the return type.
The `returnType` is optional for a `func`, but good code includes it.