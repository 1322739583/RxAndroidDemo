package com.example.rxandroiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rxandroiddemo.R;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.internal.observers.BlockingObserver;

/**
 * 利用RxJava重构项目
 */
public class RefactorActivity extends AppCompatActivity {

    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refactor);

        /**
         * 如果你需要自己封装一个RxJava库，这个可以做为参考
         */
        Observable
                .just(listPerson())
                .subscribe(new Observer<List<Person>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Person> people) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }

    List<Person> listPerson() {
        return query("select * from person");
    }

    private List<Person> query(String s) {
        //从数据库查询数据
        return null;
    }


}
