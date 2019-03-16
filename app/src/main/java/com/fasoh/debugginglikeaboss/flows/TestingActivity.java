package com.fasoh.debugginglikeaboss.flows;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fasoh.debugginglikeaboss.BuildConfig;
import com.fasoh.debugginglikeaboss.R;
import com.fasoh.debugginglikeaboss.model.User;
import com.fasoh.debugginglikeaboss.network.BackendOneProvider;
import com.fasoh.debugginglikeaboss.network.BackendOneService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class TestingActivity extends AppCompatActivity {

    private BackendOneService backendOneService;
    private static View btnLeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        backendOneService = BackendOneProvider.createService(this);

        View progressbar = findViewById(R.id.progressBar);
        TextView textView = findViewById(R.id.textView);
        Timber.d("This is a debug message");
        Timber.e("This is an error message");

        findViewById(R.id.button).setOnClickListener(v -> {
            progressbar.setVisibility(View.VISIBLE);
            Call<List<User>> accountCall = backendOneService.getPeople();
            accountCall.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful()) {
                        for (User user : response.body()) {
                            textView.append(String.format("\n Id: %d\nName: %s\n-------------------", user.getId(), user.getName()));
                        }
                    }
                    progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Timber.e(t);
                    progressbar.setVisibility(View.GONE);
                }
            });
        });

        findViewById(R.id.button2).setOnClickListener(v -> {
            progressbar.setVisibility(View.VISIBLE);
            Call<User> accountCall = backendOneService.postUser(new User(4L, "John Doe"));
            accountCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();
                        textView.setText(String.format("\n Id: %d\nName: %s\n-------------------", user.getId(), user.getName()));
                    }
                    progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Timber.e(t);
                    progressbar.setVisibility(View.GONE);
                }
            });
        });


        findViewById(R.id.button3).setOnClickListener(v -> {
            btnLeak = findViewById(R.id.button3);
            createInnerClass();
            startAsyncTask();
            LeakTesting.startFrom(this);
        });

        if (BuildConfig.BUILD_TYPE.equals("release")) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                throw new RuntimeException("This is a crash");
            }, 500);
        }
    }

    void createInnerClass() {
        class InnerClass {
        }
        InnerClass  inner = new InnerClass();
    }

    void startAsyncTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override protected Void doInBackground(Void... params) {
                while(true);
            }
        }.execute();
    }
}
