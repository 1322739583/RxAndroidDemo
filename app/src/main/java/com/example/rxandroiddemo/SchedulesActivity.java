package com.example.rxandroiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 测试调度器Schedules相关
 */
public class SchedulesActivity extends AppCompatActivity {

    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);
        testWorker();
    }

    public void testWorker() {
        Scheduler scheduler = Schedulers.io();
        Scheduler.Worker worker = scheduler.createWorker();
        //返回当前时间
        long now = worker.now(TimeUnit.SECONDS);
        Log.d("SchedulesActivity", "now:" + now);
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                Log.d("SchedulesActivity", Thread.currentThread().getName());
            }
        });
    }


    public void newIO(View view) {
        Observable
                .range(5, 3)
                .subscribeOn(Schedulers.single())
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
                        Log.d("SchedulesActivity", i + " " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("SchedulesActivity", "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("SchedulesActivity", "onComplete");
                    }
                });


    }
}
