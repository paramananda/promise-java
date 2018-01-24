# promise-java
A JAVA implementation of Promise `.then` control flow library for Java and Android.

The Promise object represents the eventual completion (or failure)
of an asynchronous method calls, and its resulting value.

A Promise is a proxy for a value not necessarily known when
the promise is created. It allows you to associate handlers
with an asynchronous action's eventual success value or failure reason.
This lets asynchronous methods return values like synchronous methods:
instead of immediately returning the final value,
the asynchronous method returns a promise to supply the value
at some point in the future.

For more information on Javascript Promise
please visit the official Mozilla Promise documentation

@see <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise">
https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise</a>

This Android Promise Library slightly different than the native Javascript Promise.
This promise object has two imprtant method i.e. `resolve()` and `reject()`,
whenevey you done, your process just call resolve or reject
function based on resultant value.
The resultant value will be automatically passed as argument to the
followng `then()` or `error()` function.
It supports above JAVA 1.8

Fork me on Github:
@see <a href="https://github.com/paramananda/promise-java">Open Github Project</a>
