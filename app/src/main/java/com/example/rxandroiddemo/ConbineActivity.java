package com.example.rxandroiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.reactivestreams.Subscriber;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class ConbineActivity extends AppCompatActivity {

    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_observable);
    }

    /**
     * 错误例子
     *
     * @param view
     */
    public void merge1(View view) {
        Observable<Integer> observable1 = Observable.just(1, 2, 3);
        Observable<Integer> observable2 = Observable.just(4, 5, 6);
        Observable
                .merge(observable1, observable2)
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
                        Log.d("ConbineActivity", i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("ConbineActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("ConbineActivity", "onComplete");
                    }
                });

    }


    /**
     * 这个方法其实非常简单，就是把两个Observable按顺序执行
     * 官方例子
     *
     * @param view
     */
    public void merge2(View view) {
        Observable<Integer> odds = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d("ConbineActivity", "Source1: emitting from " + Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Observable<Integer> evens = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d("ConbineActivity", "Source2: emitting from " + Thread.currentThread().getName());
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onNext(6);
                emitter.onComplete();
            }
        });

        Observable.merge(odds, evens)
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
                        Log.d("ConbineActivity", i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("ConbineActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("ConbineActivity", "onComplete");
                    }
                });
    }




    /**
     * 出现错误后数据丢失
     * @param view
     */
    public void merge3(View view) {
//        Observable
//                //这个类的中文翻译是在订阅时可以被观察到
//                .create(new ObservableOnSubscribe<Integer>() {
//                    //翻译为可观察的发射源
//                    @Override
//                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                        //每调用一次onNext就相对于发送一个事件，Observer接口中的onNext会调用多次，onSubscribe调用一次，
//                        //onError或者onComplete都有机会调用一次
//                        emitter.onNext(1);
//                        emitter.onNext(2);
//                        emitter.onError(new Exception("No Network"));
//                        emitter.onNext(3);
//                        //onComplete或者onError后面的方法不会被调用
//                        emitter.onComplete();
//                        emitter.onNext(4);
//                    }
//                })
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.d("ConbineActivity", "onSubscribe");
//                        //onSubscribe是最先被调用的
//                        //Disposable相对于阀门，调用这个dispose()方法，后面的方法都不会被调用，可以用于测试，而不用大量修改代码
//                        //d.dispose();
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Log.d("ConbineActivity", "onNext " + integer + " " + Thread.currentThread().getName());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d("ConbineActivity", "onError");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d("ConbineActivity", "onComplete");
//                    }
//                });


        Observable<Integer> source1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d("ConbineActivity", "Source1: emitting from " + Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Exception("No Network"));
                emitter.onNext(3);
                emitter.onComplete();
            }
        })
                .observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread());
        Observable<Integer> source2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d("ConbineActivity", "Source2: emitting from " + Thread.currentThread().getName());
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onNext(6);
                emitter.onComplete();
            }
        });

        Observable.merge(source1, source2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //数据开关，直接关闭
                        //d.dispose();
                        Log.d("ConbineActivity", "onSubscribe");
                    }

                    @Override
                    public void onNext(Integer i) {

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Log.d("ConbineActivity", i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("ConbineActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("ConbineActivity", "onComplete");
                    }
                });
    }

    public void merge4(View view) {
        Observable<Integer> source1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d("ConbineActivity", "Source1: emitting from " + Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Exception("No Network"));
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());



        Observable<Integer> source2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d("ConbineActivity", "Source2: emitting from " + Thread.currentThread().getName());
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onNext(6);
                emitter.onComplete();
            }
        });

        Observable.mergeDelayError(source1, source2)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //数据开关，直接关闭
                        //d.dispose();
                        Log.d("ConbineActivity", "onSubscribe");
                    }

                    @Override
                    public void onNext(Integer i) {

//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }

                        Log.d("ConbineActivity", i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("ConbineActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("ConbineActivity", "onComplete");
                    }
                });
    }


    public void merge5(View view) {

        Observable<Integer> observable1 = Observable.just(1, 2, 3)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        int delay = new Random().nextInt(5);
                        Log.d("ConbineActivity", "delay:" + delay);
                        Thread.sleep(delay);
                        return integer;
                    }
                })
                .subscribeOn(Schedulers.io());
        Observable<Integer> observable2 = Observable.just(4, 5, 6).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                int delay = new Random().nextInt(5);
                Thread.sleep(delay);
                return integer;
            }
        });


        Disposable d = Observable
                .merge(observable1, observable2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        //  Thread.sleep(1000);
                        Log.d("MultiObservableActivity", "integer:" + integer);
                    }
                });

    }

    public void mergeWith(View view) {
        Observable<Integer> source1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d("ConbineActivity", "Source1: emitting from " + Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Exception("No Network"));
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable<Integer> source2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d("ConbineActivity", "Source2: emitting from " + Thread.currentThread().getName());
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onNext(6);
                emitter.onComplete();
            }
        });

        source1.mergeWith(source2)
                .subscribeOn(Schedulers.io())
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
                        Log.d("ConbineActivity", i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("ConbineActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("ConbineActivity", "onComplete");
                    }
                });
    }



}
