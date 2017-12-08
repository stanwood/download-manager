package com.novoda.downloadmanager.demo.simple;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.novoda.downloadmanager.DownloadManagerBuilder;
import com.novoda.downloadmanager.demo.R;
import com.novoda.downloadmanager.lib.DownloadManager;
import com.novoda.downloadmanager.lib.Query;
import com.novoda.downloadmanager.lib.Request;
import com.novoda.downloadmanager.lib.RequestBatch;
import com.novoda.downloadmanager.notifications.NotificationVisibility;

import java.net.URI;
import java.util.List;

public class MainActivity extends AppCompatActivity implements QueryForDownloadsAsyncTask.Callback {

    private static final String BIG_FILE = "http://ipv4.download.thinkbroadband.com/50MB.zip";
    private static final String SMALL_FILE = "http://ipv4.download.thinkbroadband.com/10MB.zip";
    private static final String PENGUINS_IMAGE = "http://i.imgur.com/Y7pMO5Kb.jpg";

    private DownloadManager downloadManager;
    private RecyclerView recyclerView;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initializeWithDefaults(this);

        setContentView(R.layout.activity_main);
        emptyView = findViewById(R.id.main_no_downloads_view);
        recyclerView = (RecyclerView) findViewById(R.id.main_downloads_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        downloadManager = DownloadManagerBuilder.from(this)
                .build();

        setupDownloadingExample();
        setupQueryingExample();
    }

    private void setupDownloadingExample() {

        final RequestBatch firstRequestBatch = buildRequestBatch("The Chase", "thechase.dat", "penguins.dat");
        final RequestBatch secondRequestBatch = buildRequestBatch("Educating Yorkshire", "educatingyorkshire.dat", "penguins2.dat");

        findViewById(R.id.main_download_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(@NonNull View v) {
                        downloadManager.enqueue(firstRequestBatch);
                        downloadManager.enqueue(secondRequestBatch);
                    }
                });
    }

    @NonNull
    private RequestBatch buildRequestBatch(String title, String subPath, String subPath1) {
        final RequestBatch requestBatch = new RequestBatch.Builder()
                .withTitle(title)
                .withVisibility(NotificationVisibility.ACTIVE_OR_COMPLETE)
                .withDescription("Programme Description!")
                .withBigPictureUrl(PENGUINS_IMAGE)
                .build();

        URI smallFileURI = URI.create(SMALL_FILE);
        Request smallFileRequest = new Request(smallFileURI)
                .setDestinationInInternalFilesDir(Environment.DIRECTORY_PICTURES, subPath);

        URI bigFileURI = URI.create(BIG_FILE);
        Request bigFileRequest = new Request(bigFileURI)
                .setDestinationInInternalFilesDir(Environment.DIRECTORY_PICTURES, subPath1);

        requestBatch.addRequest(smallFileRequest);
        requestBatch.addRequest(bigFileRequest);
        return requestBatch;
    }

    private void setupQueryingExample() {
        queryForDownloads();
        findViewById(R.id.main_refresh_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(@NonNull View v) {
                        queryForDownloads();
                    }
                }
        );
    }

    private void queryForDownloads() {
        QueryForDownloadsAsyncTask.newInstance(downloadManager, MainActivity.this).execute(new Query());
    }

    @Override
    public void onQueryResult(List<BeardDownload> beardDownloads) {
        recyclerView.setAdapter(new BeardDownloadAdapter(beardDownloads));
        emptyView.setVisibility(beardDownloads.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
