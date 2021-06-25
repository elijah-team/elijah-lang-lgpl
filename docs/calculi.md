# Calculi

Just for fun, you can say that **Elijah** supports two calculi.

1. SET
2. BAL

## SET Calculus

This stands for _Statements_, _Expressions_ and _Types_. This means that everything you see in Elijah can be considered one of these three.

It could also be called _TEE_ for _Types_, _Expressions_ and _Elements_, as all the _Statements_ above are implemented as _OS_Elements_.

See also [Structure](structure.md).

## BAL Calculus

This one is not currently implemented, but I am considering a rewrite to support it because the compiler does not need to generate bytecode so early on.  It might be advantageous to support a different intermediate representation. 

**BAL** stands for _Bindings_, _Applications_, and _Lambdas_.  It is a way of representing the internals of functions to make it easier to reason about code.  Note this only applies to functions and has no meaning otherwise.

Think of Haskell's _Core_ language. This might also simplify translating to OCaml.