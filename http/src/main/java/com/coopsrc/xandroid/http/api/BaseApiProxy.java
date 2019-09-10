package com.coopsrc.xandroid.http.api;

import androidx.annotation.NonNull;

import com.coopsrc.xandroid.http.RetrofitManager;
import com.coopsrc.xandroid.http.config.BasicParamsConfig;
import com.coopsrc.xandroid.http.config.HttpClientConfig;
import com.coopsrc.xandroid.http.config.ServerHostConfig;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

import retrofit2.CallAdapter;
import retrofit2.Converter;
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
        mRetrofit = RetrofitManager.newRetrofit(serverHostConfig(), clientConfig(), basicParamsConfig());
        mApiService = initApiService();
    }

    @NonNull
    protected HttpClientConfig clientConfig() {
        return new ClientConfig();
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

    protected class ClientConfig extends HttpClientConfig {
        @NotNull
        @Override
        public Set<CallAdapter.Factory> callAdapterFactories() {
            return super.callAdapterFactories();
        }

        @NotNull
        @Override
        public Set<Converter.Factory> converterFactories() {
            return super.converterFactories();
        }
    }

    protected class ParamsConfig extends BasicParamsConfig {

    }

    protected abstract class ServerConfig extends ServerHostConfig {

    }

}
