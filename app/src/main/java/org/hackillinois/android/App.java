package org.hackillinois.android;

import android.support.multidex.MultiDexApplication;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.danlew.android.joda.JodaTimeAndroid;

import org.hackillinois.android.api.HackIllinoisAPI;
import org.hackillinois.android.helper.Utils;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;
import timber.log.Timber.DebugTree;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static org.hackillinois.android.api.HackIllinoisAPI.SERVER_ADDRESS;

public class App extends MultiDexApplication {
	private final Gson gson = Converters.registerDateTime(new GsonBuilder()).create();
	private HackIllinoisAPI api;
	private Retrofit retrofit;
	private OkHttpClient okHttpClient;
	private final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
		Response originalResponse = chain.proceed(chain.request());
		if (Utils.isNetworkAvailable(getApplicationContext())) {
			int maxAge = 60 * 5; // read from cache for 5 minute
			return originalResponse.newBuilder()
					.header("Cache-Control", "public, max-age=" + maxAge)
					.build();
		} else {
			int maxStale = 60 * 60 * 24 * 7; // tolerate 1-week stale
			return originalResponse.newBuilder()
					.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
					.build();
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
		JodaTimeAndroid.init(this);
		Timber.plant(new DebugTree());
	}

	public Gson getGson() {
		return gson;
	}

	public OkHttpClient getOkHttp() {
		if (okHttpClient == null) {
			int cacheSize = 10 * 1024 * 1024; // 10 MB
			Cache cache = new Cache(getCacheDir(), cacheSize);

			okHttpClient = new OkHttpClient.Builder()
					.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
					.cache(cache)
					.build();
		}

		return okHttpClient;
	}

	public Retrofit getRetrofit() {
		if (retrofit == null) {
			retrofit = new Retrofit.Builder()
					.baseUrl(SERVER_ADDRESS)
					.addConverterFactory(GsonConverterFactory.create(getGson()))
					.client(getOkHttp())
					.build();
		}

		return retrofit;
	}

	public HackIllinoisAPI getApi() {
		if (api == null) {
			api = getRetrofit().create(HackIllinoisAPI.class);
		}

		return api;
	}
}
