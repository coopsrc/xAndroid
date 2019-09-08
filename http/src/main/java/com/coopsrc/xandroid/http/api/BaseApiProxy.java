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
public abstract class BaseApiProxy<T> {

    protected Retrofit mRetrofit;
    protected T mApiService;

    protected BaseApiProxy() {
        mRetrofit = RetrofitManager.newRetrofit(serverHostConfig(), basicParamsConfig());
        mApiService = initApiService();
    }

    @NonNull
    protected abstract ServerHostConfig serverHostConfig();

    @NonNull
    protected BasicParamsConfig basicParamsConfig() {
        return new ParamsConfig();
    }

    protected abstract T initApiService();


    protected T createApiService(final Class<T> service) {
        return mRetrofit.create(service);
    }

    public <S> S create(final Class<S> service) {
        return mRetrofit.create(service);
    }

    protected T createApiService(String baseUrl, final Class<T> service) {
        return mRetrofit.newBuilder().baseUrl(baseUrl).build().create(service);
    }

    public <S> S create(String baseUrl, final Class<S> service) {
        return mRetrofit.newBuilder().baseUrl(baseUrl).build().create(service);
    }

    public void updateBaseUrl(String baseUrl) {
        mRetrofit = mRetrofit.newBuilder().baseUrl(baseUrl).build();
    }

    protected class ParamsConfig extends BasicParamsConfig {

    }

    protected abstract class ServerConfig extends ServerHostConfig {

    }

}
