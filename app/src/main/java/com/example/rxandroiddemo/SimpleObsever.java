package com.example.rxandroiddemo;

import android.util.Log;

import java.util.Observable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SimpleObsever<T> implements Observer<T> {

    public final String TAG=this.getClass().getSimpleName();

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
         Log.d(TAG, "t:" + t+" "+Thread.currentThread().getName());
    }

    @Override
    public void onError(Throwable e) {
           e.printStackTrace();
           Log.d(TAG, "onError");
    }

    @Override
    public void onComplete() {
          Log.d(TAG, "onComplete");
    }
}
