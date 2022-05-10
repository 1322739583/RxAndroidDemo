package com.example.rxandroiddemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.time.Instant;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    String name=this.getClass().getName();

    Observer<String> stringObserver = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            //数据开关，直接关闭
            //d.dispose();
        }

        @Override
        public void onNext(String s) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("MainActivity", s + " " + Thread.currentThread().getName());
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Log.d("MainActivity", "onError");
        }

        @Override
        public void onComplete() {
            Log.d("MainActivity", "onComplete");
        }
    };

    Observer<Integer> intObserver = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("MainActivity", "integer:" + integer+" "+Thread.currentThread().getName());
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Log.d("MainActivity", "onError");
        }

        @Override
        public void onComplete() {
            Log.d("MainActivity", "onComplete");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //testObservableOnSubscribe();
        // testCompose();
        //  testBackPresure();
        testFlowable();
    }




    private void testFlowable() {
        Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; i < 129; i++) {
                            Thread.sleep(5);
                            Log.d(TAG, "上游" + "integer:" + i + " " + Thread.currentThread().getName());
                            emitter.onNext(i);
                        }
                    }
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //   s.request(2);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(50);
                            Log.d(TAG, "下游" + "integer:" + integer + " " + Thread.currentThread().getName());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        Log.d(TAG, "onError: " + " thread : " + Thread.currentThread().getName());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 测试叠加性
     */
    public void testFlowable2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {

            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                long requested = e.requested();
                for (int j = 0; j < requested; j++) {
                    Log.d(TAG, "上游根据emitter.requested()的数量发射事件 : " + j);
                    e.onNext(j);
                }

                Log.d(TAG, "subscribe: 完成发送");
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        //调用了三次Subscription.request(n) 那么上游通过FlowableEmitter.requested()方法
                        //获取的返回值就是request(2) + request(2) + request(2)这个三个值叠加的效果
                        //即在同一线程的订阅发布的情况下下游要求上游发送事件的数量是具有叠加性的
                        s.request(2);
                        s.request(2);
                        s.request(2);
                        Log.d(TAG, "onSubscribe: 开始subscribe连接 thread : " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer + " thread : " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: " + " thread : " + Thread.currentThread().getName());
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " + " thread : " + Thread.currentThread().getName());
                    }
                });
    }


    /**
     * 测试实时更新性
     */
    public void testFlowable3() {
        //Flowable和Subscriber在同一线程的情况下 FLowableEmitter.requested()获取下游需要多少个事件的个数
        //是具有实时更新性的,每次发射事件之后通过FLowableEmitter.requested()方法都会获取观察者最新能接收的事件个数
        //但是这个观察者接收事件个数,仅仅是针对next事件而言的
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                long requested = e.requested();
                for (int j = 0; j < requested; j++) {
                    //通过e.requested()方法获取Subscriber最新需要的事件的个数
                    Log.d(TAG, "subscribe: 发射事件 : " + j + " 下游需要 " + e.requested() + "个事件");
                    e.onNext(j);
                    Log.d(TAG, "subscribe: 发射事件 : " + j + " 完成之后下游需要 " + e.requested() + "个事件");
                }
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(5);
                        Log.d(TAG, "onSubscribe: 开始subscribe连接 thread : " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer + " thread : " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: " + " thread : " + Thread.currentThread().getName());
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " + " thread : " + Thread.currentThread().getName());
                    }
                });
    }

    private void testBackPresure() {
        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        for (int i = 0; i < 5; i++) {
                            Thread.sleep(500);
                            Log.d(TAG, "integer:" + i + " " + Thread.currentThread().getName());
                            emitter.onNext(i);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(1000);
                            Log.d(TAG, "integer:" + integer + " " + Thread.currentThread().getName());

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "onError: " + " thread : " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 这个类的中文翻译是在订阅时可以被观察到
     */
    private void testObservableOnSubscribe() {
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
                        Log.d(TAG, "onSubscribe");
                        //onSubscribe是最先被调用的
                        //Disposable相对于阀门，调用这个dispose()方法，后面的方法都不会被调用，可以用于测试，而不用大量修改代码
                        //d.dispose();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }


    private void testCompose() {


        Observable
                //这个类的中文翻译是在订阅时可以被观察到
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
