

What to include in the docs for MSDscript:

* Overview of what MSDscript/interpreter is and what it can do,
  including running on a command line

* What kind of modes you can use: interp, print, step, ...
  including examples of running on a command line

* How build (e.g., how to use Makefile) and embed in another app,
  including which headers files to use for embedding

* MSDscript (the language spec)
    - assuming a programmer audience
    - assume to assume familiarity with command line
      e.g., describe how `_let` binds variables

  Describe all the built-in functions

  Consider including a Backus-Naur Form (BNF) grammar

* Potential errors?

* How to report bugs? GitHub (and expect Issues there)

* License
  GPL
  LGPL
  MIT
  Apache 2.0
  CCA...





Typo: "domumentation.md" -> "documentation.md"

Your archive contains built artifacts in "cmake-build-debug" and
"cmake-build-debug-coverage". Those shouldn't be included.

Your overview and build instructions look good.

The description of the MSDscript language is somewhat in the
description of the Expr classes, but there's not enough information
here for someone to use MSDscript or read MSDscript programs. The
grammar is a start, but more information is needed to say what a
`_let` expression or `_fun` expression means, including the way those
bind variables. Overall, the documentation needs to describe the
language well enough that someone who has never heard of MSDscript can
write simple programs and predict what an MSDscript program will
produce.

Your documentation includes a lot of information about the classes and
methods in the implementation.