# React wrapper in Scala.js

This is a wrapper for the [React JavaScript library](http://facebook.github.io/react/).

This wrapper is written with [Scala.js](http://www.scala-js.org/) and allows to write React components in Scala.

It uses [Scala.rx](https://github.com/lihaoyi/scala.rx) to synchronize the model and the view.


# Why is it interesting?

The rendering of React components can be optimized if we know that the model has not changed.

With persistent data structures provided in Scala, it is very easy to check if the model has changed: we just checked if the instances are equal.


# how to use it?
For the moment, this wrapper is not provided as a library. My goal is first to check if people are interested in it.

This project was initialized with the [Scala js example app](https://github.com/sjrd/scala-js-example-app).
Just follow the same instructions here to check the examples and to change them.

In `sbt`:
- `~packageJS` for development (and open `index-dev.html`)
- `optimizeJS` for production (and open `index.html`)


## TODOs
### better model update
ideas: [lenses](https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0#boilerplate-free-lenses-for-arbitrary-case-classes), [Extensible records](https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0#extensible-records)

### no more js.Dynamic for DOM
- Use a type for DOM -> render typesafe
- provide utilities to construct DOM easier than current DOM.a(js.Dynamic.literal(....))

### stateful components
wraps getInitialState, update shouldComponentUpdate...

### handle not only AnyRef but also Any?


