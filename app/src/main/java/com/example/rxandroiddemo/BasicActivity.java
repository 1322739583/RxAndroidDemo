package com.example.rxandroiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 用来解释RxJava一些基本的概念
 */
public class BasicActivity extends AppCompatActivity {

    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
    }


    /**
     * subcribe方法是订阅的意思，或者理解为注册或者attach都可以，这是观察者设计模式的一个基本概念，也就是将观察者和被观察者绑定。
     * subscribe接受的参数是观察者，RxJava提供了两种观察者一个是Observer，从名字就看出来了，这个类就是观察者的意思，还有一个类是
     * Consumer，这个类可以简单理解为是Observer的精简版，Observer需要实现四个方法，Consumer最少只需要一个，使用起来更方便和清晰。
     * @param view
     */
    public void subcribeWithObserver(View view) {
        Observable
                .just(1, 2, 3)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //数据开关，直接关闭
                        //d.dispose();
                        Log.d("CreateActivity", "onSubscribe");
                    }

                    @Override
                    public void onNext(Integer i) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("BasicActivity", i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("BasicActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("BasicActivity", "onComplete");
                    }
                });
    }

    public void subcribeWithConsumer(View view) {
        //最后可以调用dispose解除关联，不然会警告没有引用
        Observable
                .just(1, 2, 3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                           Log.d("BasicActivity", "integer:" + integer);
                    }
                })
                .dispose();
    }

    public void disposable(View view) {
    }


}
