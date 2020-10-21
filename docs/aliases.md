# Aliases

**Aliases** add a name for an existing element.  The syntax is simple:

```
alias new_name = old.name
```

An alias can point any declaration, for example a `class` or `namespace`, or even another `alias`.

NOTE: I am thinking that I should limit aliases not to be able to point to packages. It doesn't seem like it would make sense. Use an `import` statement instead.

----

A **Type Alias** is the same, except the thing it points to must be a type of some kind, but the type must be concrete.


