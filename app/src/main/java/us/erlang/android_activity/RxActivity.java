package us.erlang.android_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);

        button = findViewById(R.id.start_counter);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Observable.interval(1, TimeUnit.SECONDS)
                        .take(12)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long number) throws Exception {
                                return String.format("The number is %d", number);
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        button.setEnabled(false);
                    }

                    @Override
                    public void onNext(String s) {
                        button.setText(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("RxActivity", e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        button.setEnabled(true);
                        button.setText("start counter");
                    }
                });
            }
        });
    }
}