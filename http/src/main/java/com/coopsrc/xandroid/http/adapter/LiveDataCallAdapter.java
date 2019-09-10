package com.coopsrc.xandroid.http.adapter;

import androidx.lifecycle.LiveData;

import com.coopsrc.xandroid.http.response.ApiResponse;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<ApiResponse<R>>> {
    private final Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @NotNull
    @Override
    public Type responseType() {
        return responseType;
    }

    @NotNull
    @Override
    public LiveData<ApiResponse<R>> adapt(@NotNull final Call<R> call) {
        return new LiveData<ApiResponse<R>>() {
            @Override
            protected void onActive() {
                super.onActive();
                AtomicBoolean started = new AtomicBoolean(false);

                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(@NotNull Call<R> call, @NotNull Response<R> response) {
                            postValue(new ApiResponse<>(response));
                        }

                        @Override
                        public void onFailure(@NotNull Call<R> call, @NotNull Throwable throwable) {

                            postValue(new ApiResponse<R>(throwable));
                        }
                    });
                }
            }
        };
    }

}
