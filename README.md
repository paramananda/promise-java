# promise-java
A JAVA implementation of Promise `.then` control flow library for Java and Android.

The Promise object represents the eventual completion (or failure)
of asynchronous method calls, and its resulting value.

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

This Android Promise Library is slightly different than the native Javascript Promise.
This promise object has two imprtant method i.e. `resolve()` and `reject()`,
whenever you are done, your method just call resolve or reject
function based on resultant value.
The resultant value will be automatically passed as argument to the
followng `then()` or `error()` function.
It supports above JAVA 1.8

Fork me on Github:
@see <a href="https://github.com/paramananda/promise-java">Open Github Project</a>


## HOW TO USE ?

* Simply copy `Promise.java` into your source code folder, now you are ready to play with async flow chain like below.

```java
promiseObject
      .then()
      .then()
      .then()    // Do task one after another 'n' number of chain
      .error();
```


### How it works ?

Following code snippet demonstrate a complex async call flow into simple code flow.

```java
doSomeTask(int someValue, String extra)
    .then(res -> doSecondTask((MyObject) res))       // res is result form doSomeTask()
    .then(res -> doThirdTask((OtherObject) res)))    // res is result form doThirdTask()
    .then(res -> doFourthTask((int) res)))           // res is result form doThirdTask()
    .then(res -> doFivthTask())
    .then(res -> {
         // Consume result of the previous function
         return true;    // done
    })
    .error(err -> handleError());                    // Incase of any p.reject() call from above function error will be available here 
```

```java
public Promise doSomeTask(int someValue, String extra){
    Promise p = new Promise();
    new Thread(()->{
        // do some background operation.
        // When your work done, resolve the promise like below;
        // After resolve the library will pass the value to doSecondTack()
        // Suppose your resultant value is instance of MyObject, then
        // When it is error call reject() with some error message or an Exception
        // p.reject("Some Error happened")
        
        MyObject myObject = new MyObject();
        p.resolve(myObject);
    });
    return p;
}

public OtherObject doSecondTask(MyObject myInput) {
    // Do some syncronous or asyncronous task and return the result,
    // Still it work with your promise chain.
    // Folloing snipet is just a sample work flow
    
    OtherObject obj = new OtherObject();
    
    return obj;
}


public Promise doThirdTask(OtherObject otherObject){
    // Do some task, return value using promise
    Promise p = new Promise();
    
    // Your task
    
    p.resolve();
    
    return p;
}

public Promise doFourthTask(){
    return doSomePromisableTask();    // I am not writing defination of this fuction, let this function is very similar to 
                                      // `doSomeTask()` function
}

public Promise doFivthTask(){
    return doSomePromisableTask()
          .then(res -> {
              // Do some task here
              return 1;      // this one will be available in the next the or parent then which called this task
          });
}
```

Other Static API of promise :

1. `Promise.all(Promise...).then();`
1. `Promise.parallel(Object..., <HandlerFunction>).then();`
1. `Promise.series(Object..., <HandlerFunction>).then();`


### How to use in android?

*__Download the source file__ add into your project src.*

__Promise.java__  Simply Copy this file into your project

__*It need JAVA 1.8 to compile__


## LICENCE

      The MIT License

      Copyright (c) 2017-2018 Paramananda Pradhan

      Permission is hereby granted, free of charge, to any person obtaining a copy
      of this software and associated documentation files (the "Software"), to deal
      in the Software without restriction, including without limitation the rights
      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
      copies of the Software, and to permit persons to whom the Software is
      furnished to do so, subject to the following conditions:

      The above copyright notice and this permission notice shall be included in
      all copies or substantial portions of the Software.

      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
      THE SOFTWARE.

