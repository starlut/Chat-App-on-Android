package star.lut.com.chatdemo.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import star.lut.com.chatdemo.Base.BaseApplication;
import star.lut.com.chatdemo.constants.ApiConstants;
import timber.log.Timber;

/**
 * Created by kamrulhasan on 28/10/18.
 */
public class WebServiceFactory {
    /**
     * Creates a retrofit service from an arbitrary class (clazz)
     *
     * @return retrofit service with defined endpoint/base-url
     */

    private static final String CACHE_CONTROL = "Cache-Control";

    public static <T> T createRetrofitService(Class<T> service) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();

        return retrofit.create(service);
    }

    private static OkHttpClient getOkHttpClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .method(original.method(), original.body())
                    .addHeader(ApiConstants.HEADER_NAME_CONTENT,ApiConstants.CONTENT_TYPE_JSON)
//                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            return chain.proceed(request);

        });

        httpClient.readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(mLoggingInterceptor)
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache());


        return httpClient.build();
    }

    private static final Interceptor mLoggingInterceptor = chain -> {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Timber.i(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        Timber.i(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        return response;
    };

    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(BaseApplication.getInstance().getCacheDir(), "http-cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
            Timber.e("Could not create Cache!");
        }
        return cache;
    }

    public static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder()
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    public static Interceptor provideOfflineCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (!BaseApplication.hasNetwork()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }
}
