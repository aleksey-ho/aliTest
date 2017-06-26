package com.example.aleks.myapplication.utils;

import android.util.Log;

import com.google.common.io.Closeables;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import io.reactivex.Single;

public class URLUnshortener {
    private static final String TAG = "URLUnshortener";

    public static final int DEFAULT_CONNECT_TIMEOUT = 1000;
    public static final int DEFAULT_READ_TIMEOUT = 1000;
    public static final int DEFAULT_CACHE_SIZE = 10000;

    private final int connectTimeout;
    private final int readTimeout;
    private final LRUCache<URL, URL> cache;

    public URLUnshortener() {
        this(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_CACHE_SIZE);
    }

    /**
     * @param connectTimeout HTTP connection timeout, in ms
     * @param readTimeout HTTP read timeout, in ms
     * @param cacheSize Number of resolved URLs to maintain in cache
     */
    public URLUnshortener(int connectTimeout, int readTimeout, int cacheSize) {
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.cache = LRUCache.build(cacheSize);
    }

    /**
     * Expand the given short {@link URL}
     * @param address
     * @return
     */
    public Single<URL> expand(URL address) {
        return Single.create(emitter -> {
            //Check cache
            URL inCache = cache.get(address);
            if(inCache != null) {
                emitter.onSuccess(inCache);
                return;
            }

            //Connect & check for the location field
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) address.openConnection(Proxy.NO_PROXY);
                connection.setConnectTimeout(connectTimeout);
                connection.setInstanceFollowRedirects(false);
                connection.setReadTimeout(readTimeout);
                connection.connect();
                String expandedURL = connection.getHeaderField("Location");
                if(expandedURL != null) {
                    URL expanded = new URL(expandedURL);
                    cache.put(address, expanded);
                    emitter.onSuccess(expanded);
                    return;
                }
            } catch (Throwable e) {
                Log.d(TAG, "Problem while expanding " + address + ": " + e);
            } finally {
                try {
                    if(connection != null) {
                        Closeables.closeQuietly(connection.getInputStream());
                    }
                } catch (IOException e) {
                    Log.d(TAG, "Unable to close connection stream: " + e);
                }
            }
            emitter.onSuccess(address);
        });
    }
}
