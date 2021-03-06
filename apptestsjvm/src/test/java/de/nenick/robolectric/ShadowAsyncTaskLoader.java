package de.nenick.quacc.robolectric;

import android.content.Context;
import android.content.AsyncTaskLoader;

import org.robolectric.shadows.ShadowApplication;
import org.robolectric.util.SimpleFuture;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.annotation.Implementation;
import java.util.concurrent.Callable;

/**
 * Fix an issue with endless running tests when using cursor loader.
 *
 * This class will be in next robolectric release (current is rc3) and can then be removed.
 */

@Implements(AsyncTaskLoader.class)
public class ShadowAsyncTaskLoader<D> {
    @RealObject private AsyncTaskLoader<D> realObject;
    private SimpleFuture<D> future;

    public void __constructor__(Context context) {
        BackgroundWorker worker = new BackgroundWorker();
        future = new SimpleFuture<D>(worker) {
            @Override
            protected void done() {
                try {
                    final D result = get();
                    ShadowApplication.getInstance().getForegroundThreadScheduler().post(new Runnable() {
                        @Override
                        public void run() {
                            realObject.deliverResult(result);
                        }
                    });
                } catch (InterruptedException e) {
                    // Ignore
                }
            }
        };
    }

    @Implementation
    public void onForceLoad() {
        ShadowApplication.getInstance().getBackgroundThreadScheduler().post(new Runnable() {
            @Override
            public void run() {
                future.run();
            }
        });
    }

    private final class BackgroundWorker implements Callable<D> {
        @Override
        public D call() throws Exception {
            return realObject.loadInBackground();
        }
    }
}
