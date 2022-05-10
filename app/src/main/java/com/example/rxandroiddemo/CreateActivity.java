package com.example.rxandroiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 测试创建数据源方法
 */
public class CreateActivity extends AppCompatActivity {

    private   final String TAG =this.getClass().getName();
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }


    /**
     * just非常简单，直接输入数据源就能用了，有10个数量的重载
     *
     * @param view
     */
    public void just(View view) {
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
                        Log.d("CreateActivity", i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("CreateActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("CreateActivity", "onComplete");
                    }
                });

    }

    public void range(View view) {
        Observable
                .range(5, 3)
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
                        Log.d("CreateActivity", i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("CreateActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("CreateActivity", "onComplete");
                    }
                });

    }

    public void fromArray(View view) {
        Observable
                .fromArray(1, 2, 3)
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
                        Log.d("CreateActivity", i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("CreateActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("CreateActivity", "onComplete");
                    }
                });
    }

    public void create(View view) {
        Observable
                //这个类的中文翻译是在订阅时可以被观察到
                .create(new ObservableOnSubscribe<Integer>() {
                    //翻译为可观察的发射源
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        //每调用一次onNext就相对于发送一个事件，Observer接口中的onNext会调用多次，onSubscribe调用一次，
                        //onError或者onComplete都有机会调用一次
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onNext(3);
                        //onComplete或者onError后面的方法不会被调用
                        emitter.onComplete();
                        emitter.onNext(4);
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("CreateActivity", "onSubscribe");
                        //onSubscribe是最先被调用的
                        //Disposable相对于阀门，调用这个dispose()方法，后面的方法都不会被调用，可以用于测试，而不用大量修改代码
                        //d.dispose();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d("CreateActivity", "onNext " + integer + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("CreateActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("CreateActivity", "onComplete");
                    }
                });

    }

    public void empty(View view) {
        Observable.empty().subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Object o) {
                Log.i(TAG, "onNext");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    public void never(View view) {
        Observable
                .never()
                .subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Object o) {
                Log.i(TAG, "onNext");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    public void error(View view) {
        Observable
                .error(new Exception())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.i(TAG, "onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }
}
