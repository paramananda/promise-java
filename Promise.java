/*
 * PARAMANANDA PRADHAN CONFIDENTIAL
 * Copyright (c) 2017-2018 All rights reserved.
 *
 * The source code contained or described herein and all documents
 * related to the source code ("Material") are owned by CRAWLINK
 * Networks Pvt Ltd. No part of the Material may be used, copied,
 * reproduced, modified, published, uploaded,posted, transmitted,
 * distributed, or disclosed in any way without CRAWLINK Networks
 * Pvt Ltd. prior written permission.
 *
 * No license under any patent, copyright, trade secret or other
 * intellectual property right is granted to or conferred upon you
 * by disclosure or delivery of the Materials, either expressly, by
 * implication, inducement, estoppel or otherwise. Any license
 * under such intellectual property rights must be express and
 * approved by PARAMANANDA PRADHAN in writing.
 *
 */

package com.ppn;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * The Promise object represents the eventual completion (or failure)
 * of an asynchronous method calls, and its resulting value.
 * <p>
 * A Promise is a proxy for a value not necessarily known when
 * the promise is created. It allows you to associate handlers
 * with an asynchronous action's eventual success value or failure reason.
 * This lets asynchronous methods return values like synchronous methods:
 * instead of immediately returning the final value,
 * the asynchronous method returns a promise to supply the value
 * at some point in the future.
 * <p>
 * For more information on Javascript Promise
 * please visit the official Mozilla Promise documentation
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise">
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Promise</a>
 * <p>
 * This Android Promise Library slightly different than the native Javascript Promise.
 * This promise object has two imprtant method i.e. `resolve()` and `reject()`,
 * whenevey you done, your process just call resolve or reject
 * function based on resultant value.
 * The resultant value will be automatically passed as argument to the
 * followng `then()` or `error()` function.
 * It supports above JAVA 1.8
 *
 * Fork me on Github:
 * @see <a href="https://github.com/paramananda/promise-java">Open Github Project</a>
 */

public class Promise {

    private static final String TAG = "Promise";
    private OnSuccessListener onSuccessListener;
    private OnErrorListener onErrorListener;
    private Promise child;
    private boolean isResolved;
    private Object resolvedObject;
    private Object tag;


    public Promise() {
    }

    public static Promise all(Promise... list) {
        Promise p = new Promise();
        if (list == null || list.length <= 0) {
            Log.w(TAG, "Promise list should not be empty!");
            p.resolve(new ArrayList<>());
            return p;
        }

        if (list != null && list.length > 0) {
            new Runnable() {
                int completedCount = 0;
                Object result[] = new Object[list.length];

                @Override
                public void run() {
                    for (int i = 0; i < list.length; i++) {
                        Promise promise = list[i];
                        promise.setTag(i);
                        promise.then(res -> {
                            result[(int) promise.getTag()] = res;
                            completed(null);
                            return res;
                        }).error(err -> completed(err));
                    }
                }

                private void completed(Object err) {
                    completedCount++;
                    if (err != null) {
                        p.reject(err);
                    } else if (completedCount == list.length) {
                        p.resolve(result);
                    }
                }

            }.run();
        } else {
            Log.w(TAG, "Promises should not be empty!");
            p.resolve(new ArrayList<>());
        }


        return p;
    }

    public static Promise series(List<?> list, OnSuccessListener listener) {
        Promise p = new Promise();
        if (list == null || listener == null || list.size() <= 0) {
            Log.e(TAG, "Arguments should not be NULL!");
            return null;
        }

        new Runnable() {
            int index = -1;
            int completedCount = 0;
            ArrayList<Object> result = new ArrayList<>(list.size());

            @Override
            public void run() {
                index++;
                if (index < list.size()) {
                    // for (int i = 0; i < list.size(); i++) {
                    handleSuccess(index, list.get(index));
                    //}
                } else {
                    p.resolve(result);
                }
            }

            private void handleSuccess(int index, Object object) {
                Object res = listener.onSuccess(object);
                result.add(index, res);
                if (res instanceof Promise) {
                    Promise pro = (Promise) res;
                    pro.setTag(index);
                    pro.then(r -> {
                        result.set((int) pro.getTag(), r);
                        if (!completed(null)) {
                            run();
                        }
                        return r;
                    }).error(err -> completed(err));
                } else {
                    completed(null);
                }

            }

            private boolean completed(Object err) {
                completedCount++;
                if (err != null) {
                    p.reject(err);
                } else if (completedCount == list.size()) {
                    p.resolve(result);
                    return true;
                }
                return false;
            }

        }.run();

        return p;
    }

    public static Promise parallel(List<?> list, OnSuccessListener listener) {
        Promise p = new Promise();
        if (list == null || listener == null || list.size() <= 0) {
            Log.e(TAG, "Arguments should not be NULL!");
            return null;
        }

        new Runnable() {
            int completedCount = 0;
            ArrayList<Object> result = new ArrayList<>(list.size());

            @Override
            public void run() {
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        handleSuccess(i, list.get(i));
                    }
                } else {
                    p.resolve(result);
                }
            }

            private void handleSuccess(int index, Object object) {
                Object res = listener.onSuccess(object);
                result.add(index, res);
                if (res instanceof Promise) {
                    Promise pro = (Promise) res;
                    pro.setTag(index);
                    pro.then(r -> {
                        result.set((int) pro.getTag(), r);
                        completed(null);
                        return r;
                    }).error(err -> completed(err));
                } else {
                    completed(null);
                }

            }

            private void completed(Object err) {
                completedCount++;
                if (err != null) {
                    p.reject(err);
                } else if (completedCount == list.size()) {
                    p.resolve(result);
                }
            }

        }.run();

        return p;
    }

    /**
     * Call this function with your resultant value, it will be available
     * in following `then()` function call.
     *
     * @param object your resultant value (any type of data you can pass as argument
     *               e.g. int, String, List, Map, any Java object)
     * @return This will return the resultant value you passed in the function call
     */
    public Object resolve(Object object) {
        isResolved = true;
        resolvedObject = object;
        handleSuccess(child, object);
        return object;
    }

    /**
     * Call this function with your error value, it will be available
     * in following `error()` function call.
     *
     * @param object your error value (any type of data you can pass as argument
     *               e.g. int, String, List, Map, any Java object)
     * @return This will return the error value you passed in the function call
     */
    public Object reject(Object object) {
        handleError(object);

        return object;
    }

    /**
     * After executing asynchronous function the result will be available in the success listener
     * as argument.
     *
     * @param listener OnSuccessListener
     * @return It returns a promise for satisfying next chain call.
     */
    public Promise then(OnSuccessListener listener) {
        onSuccessListener = listener;
        child = new Promise();
        return child;
    }

    /**
     * This function must call at the end of the `then()` cain, any `reject()` occurs in
     * previous async execution this function will be called.
     *
     * @param listener
     */
    public void error(OnErrorListener listener) {
        onErrorListener = listener;
    }

    private void handleSuccess(Promise child, Object object) {
        if (onSuccessListener != null) {
            Object res = onSuccessListener.onSuccess(object);
            if (res != null) {
                if (res instanceof Promise) {
                    if (child != null) {
                        Promise p = (Promise) res;
                        p.onSuccessListener = child.onSuccessListener;
                        p.onErrorListener = child.onErrorListener;
                        p.child = child.child;
                        child = p;
                    }
                } else if (child != null) {
                    child.resolve(res);
                }
            } else {
                if (child != null) {
                    child.resolve(res);
                }
            }
        }
    }

    private void handleError(Object object) {
        if (onErrorListener != null) {
            onErrorListener.onError(object);
        } else if (child != null) {
            child.reject(object);
        }
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }


    public interface OnSuccessListener {
        Object onSuccess(Object object);
    }

    public interface OnErrorListener {
        void onError(Object object);
    }

}

