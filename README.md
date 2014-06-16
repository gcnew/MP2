MP2
===

MP2 is a POC interpreter - the fruit of the third generation of my compiler/interpreter efforts.

The basic ideas behind MP2 were laid out during my school years at TUES. I started writing my first interpreter back then but I quickly abandoned it because of lack of time. However I continued thinking and dreaming about it thus I made a second try, this time I started with a virtual machine. The VM was a simple stack based VM doing mainly arithmetics, jumping and some runtime compilation/linking (you could feed a string to the `compile` and `exec` instructions, the former compiled and linked with target the current execution while the later created a new execution, evaluating the code inside it and putting the result back on top of parent's execution stack). The grand idea was to make the abandoned interpreter a frontend for the VM. This didn't happen however as I was already last year in school. At the time I was in a delicate artistic mood - my priorities had completely changed and I was questioning programming at all.

.... Two years had passed since graduating school, I was working in a software company where we were developing a templating solution for mobile apps. The templates were declarative only, something I didn't like and objected all the time. I've always been a great fan of dynamic languages and APIs thus I was constantly lobbying for a good backend API and a dynamic templating language. It didn't happen for quite some time but one day we had the urge to have basic logic in the templates. It wasn't possible to overcome the issue declaratively - not in a sane manner. That was the time I embraced the opportunity and proposed using a language for the purpose. I made a statement that even a simple reverse polish notation expression parser would do the trick. My colleagues were not very fascinated but non the less allowed me to give it a try (in a very time constrained manner, of course). I had just one day for the experiment. I was very excited as these were the to be scions of dynamism, and the first time I had to do something in the field I find most interesting (but never spare the time) - compilers. I had work at the university so I left work earlier. Later, when I came back home, I extracted some parts of the lexer and parser of my first interpreter and added Java reflection (at the time we were using Java). The end result was a very basic interpreter capable of creating and assigning variables, calling reflected methods chains and grouping several expressions into blocks. At 4 o'clock I was satified with what I had so I went to bed. The next day MP2 went live. Although mockingly basic it accomplished the job we had. The reflection part was actually very powerful, as it allowed us to feed MP2 with "services" which would provide all the functions it was lacking. I remember one colleague writing "or" and "and" functions :). In my spare time I made a few improvements - I added logical operators, arithmetic operators and some sugar. I upgraded the version we had at work with these improvements and the upgrade story of MP2 ended here. With these "extra" features it had everything the company needed.

I continued adding some improvements from time to time. Far less often that I would have liked to. While it's sufficiently more powerful now it's still very basic to my liking. The ultimate goals behind MP2 have always been:
 - great language support for extensions (imagine new language features just by dropping a library)
 - making developers happy by giving them the tools to write DSLs that make them productive and give them the joy of doing something "cool"
 - better language expressiveness by declaring intention/built in constraints
 - well integrated functional aspects for easier reasoning and more manageable code
 - imperative support to naturally express real life
 - a language that leaves to you the choice whats good and whats bad
 - access to the language's AST :) - endless tinkering for the hungry
