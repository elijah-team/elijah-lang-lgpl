# Structure

**Elijjah** consists of

* Declarations
    * `class`, `namespace`, `alias`, `var`, `package`, `access`
* Statements (Control Flow)
    * `if`, `match`, `case`, `break`, `continue`, assignment
* Expressions
    * binary
    * unary
    * composite

        * Array (not implemented yet, but coming)
        * List
        * Dict  (not implemented yet, but coming)
        * Tuple (not implemented yet, but coming)

    * constatnt
    
* Special
    * XML
    * SQL
    * JSON (`%*{`) (borroed from ...)

NOTE: The things in special above are not implemented

----

The basic unit of execution is the function. All executable code is contained here, with the exception of initializers in `namespaces` and `classes`.

The function can appear in the following places:
   
* namespace or class
* inside another function (nested)
* as the right hand side of a variable assignment
* anywhere else an expression can go
    
----

Variables (`var`, `val`, and `const`) can only appear in namespaces, classes or functions, (ie not at the top level of a module).

