package com.coopsrc.xandroid.http.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coopsrc.xandroid.utils.LogUtils;

import java.io.IOException;

import retrofit2.Response;

public class ApiResponse<T> {
    public final int code;
    @Nullable
    public final T body;
    @Nullable
    public final String message;

    public ApiResponse(@NonNull Throwable throwable) {
        code = 500;
        body = null;
        message = throwable.getMessage();
    }

    public ApiResponse(@NonNull Response<T> response) {
        code = response.code();

        if (response.isSuccessful()) {
            body = response.body();
            message = response.message();
        } else {
            body = null;

            String errorMessage = null;

            if (response.errorBody() != null) {
                try {
                    errorMessage = response.errorBody().string();
                } catch (IOException e) {
                    LogUtils.e(e, "error while parsing response");
                }
            }
            if (errorMessage == null || errorMessage.trim().length() == 0) {
                errorMessage = response.message();
            }
            message = errorMessage;
        }
    }

    public boolean isSuccessful(){
        return code >= 200 && code < 300;
    }

}
