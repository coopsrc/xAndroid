package com.coopsrc.xandroid.http.api;

import androidx.annotation.NonNull;

import com.coopsrc.xandroid.http.BasicParamsConfig;
import com.coopsrc.xandroid.http.RetrofitManager;
import com.coopsrc.xandroid.http.ServerHostConfig;

import retrofit2.Retrofit;

/**
 * @author tingkuo
 * <p>
 * Date: 2019-09-02 14:08
 */
public abstract class BaseApiProxy<T extends BaseApiService> {

    protected Retrofit mRetrofit;
    protected T mApiService;

    protected BaseApiProxy() {
        mRetrofit = RetrofitManager.newRetrofit(serverHostConfig(), basicParamsConfig());
        mApiService = initApiService();
    }

    @NonNull
    protected abstract ServerHostConfig serverHostConfig();

    protected BasicParamsConfig basicParamsConfig() {
        return new BasicParamsConfig() {
        };
    }

    protected abstract T initApiService();


    public T createApiService(final Class<T> service) {
        return mRetrofit.create(service);
    }

    public T createApiService(String baseUrl, final Class<T> service) {
        return mRetrofit.newBuilder().baseUrl(baseUrl).build().create(service);
    }

    public void updateBaseUrl(String baseUrl) {
        mRetrofit = mRetrofit.newBuilder().baseUrl(baseUrl).build();
    }

}
