package com.example.rxandroiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class TimeActivity extends AppCompatActivity {

    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
    }


    /**
     * 可以单独使用，不可以和just等数据方法一起使用，不然会有警告。
     * 类型固定是Long,不能改为其它类型
     * 可以配合repeat一起使用，相对于有定时功能的for循环
     *
     * @param view
     */
    public void timer(View view) {
        Observable
                .timer(2, TimeUnit.SECONDS)
                .repeat(10)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("TimeActivity", "onSubscribe");
                    }

                    @Override
                    public void onNext(Long i) {
                        Log.d("TimeActivity", "i:" + i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("TimeActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TimeActivity", "onComplete");
                    }
                });
    }

    /**
     * 会无限发射，加了repeat限制次数也是不行的
     * 不可以指定开始值，从0开始递增
     *
     * @param view
     */
    public void interval(View view) {
        Observable
                .interval(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("TimeActivity", "onSubscribe");
                    }

                    @Override
                    public void onNext(Long i) {
                        Log.d("TimeActivity", "i:" + i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("TimeActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TimeActivity", "onComplete");
                    }
                });


    }

    /**
     * 相当于多了延时功能，延时一定时间后开始.
     *
     * @param view
     */
    public void interval2(View view) {
//        Observable
//                .interval(5,2,TimeUnit.SECONDS)
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.d("TimeActivity", "onSubscribe");
//                    }
//
//                    @Override
//                    public void onNext(Long i) {
//                        Log.d("TimeActivity", "i:" + i+" "+Thread.currentThread().getName());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        Log.d("TimeActivity", "onError");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d("TimeActivity", "onComplete");
//                    }
//                });

        Observable
                .interval(1, 5, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d("TimeActivity", "i:" + aLong+" "+Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
