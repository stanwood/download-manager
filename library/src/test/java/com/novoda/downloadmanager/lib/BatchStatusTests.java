package com.novoda.downloadmanager.lib;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Ignore
public class BatchStatusTests {

    private static final DownloadDeleter UNUSED_DELETER = null;
    private static final SystemFacade UNUSED_SYSTEM = null;
    @Mock
    private ContentResolver mockContentResolver;
    @Mock
    private DownloadsUriProvider mockDownloadsUriProvider;
    @Mock
    private Uri mockUri;

    private BatchRepository batchRepository;
    @Before
    public void setUp() {
        initMocks(this);

        this.batchRepository = new BatchRepository(mockContentResolver, UNUSED_DELETER, mockDownloadsUriProvider, UNUSED_SYSTEM);
    }

    @Test
    public void givenThereSomeItemsInABatchQueuedAndSomeAreCompleteThenTheReportedStateIsDownloading() {
        Cursor mockCursor = givenACursorReturningTheStatuses(DownloadStatus.SUCCESS, DownloadStatus.SUCCESS, DownloadStatus.PENDING, DownloadStatus.PENDING);

        when(mockContentResolver.query(any(Uri.class), any(String[].class), anyString(), any(String[].class), anyString()))
                .thenReturn(mockCursor);
        int batchStatus = batchRepository.getBatchStatus(0);

        assertThat(batchStatus).isEqualTo(DownloadStatus.RUNNING);
    }

    @Test
    public void givenAllItemsInABatchAreQueuedThenItReportsAsQueued() {
        Cursor mockCursor = givenACursorReturningTheStatuses(DownloadStatus.PENDING, DownloadStatus.PENDING);

        when(mockContentResolver.query(any(Uri.class), any(String[].class), anyString(), any(String[].class), anyString()))
                .thenReturn(mockCursor);
        int batchStatus = batchRepository.getBatchStatus(0);

        assertThat(batchStatus).isEqualTo(DownloadStatus.PENDING);
    }

    @Test
    public void givenAllItemsInABatchAreCompleteThenItReportsAsComplete() {
        Cursor mockCursor = givenACursorReturningTheStatuses(DownloadStatus.SUCCESS, DownloadStatus.SUCCESS);

        when(mockContentResolver.query(any(Uri.class), any(String[].class), anyString(), any(String[].class), anyString()))
                .thenReturn(mockCursor);
        int batchStatus = batchRepository.getBatchStatus(0);

        assertThat(batchStatus).isEqualTo(DownloadStatus.SUCCESS);
    }

    @NonNull
    private Cursor givenACursorReturningTheStatuses(Integer... statuses) {
        List<Integer> statusList = Arrays.asList(statuses);
        return new MockCursorWithStatuses(statusList);
    }

    static class MockCursorWithStatuses implements Cursor {

        private final List<Integer> statuses;
        private int position = -1;

        public MockCursorWithStatuses(List<Integer> statuses) {
            this.statuses = statuses;
        }

        @Override
        public int getCount() {
            return statuses.size();
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public boolean move(int i) {
            return position + i >= 0 && position + i < statuses.size();
        }

        @Override
        public boolean moveToPosition(int i) {
            return i >= 0 && i < statuses.size();
        }

        @Override
        public boolean moveToFirst() {
            position = 0;
            return true;
        }

        @Override
        public boolean moveToLast() {
            position = statuses.size() - 1;
            return false;
        }

        @Override
        public boolean moveToNext() {
            if (position < statuses.size() - 1) {
                position++;
                return true;
            }
            return false;
        }

        @Override
        public boolean moveToPrevious() {
            if (position > 1) {
                position--;
                return true;
            }
            return false;
        }

        @Override
        public boolean isFirst() {
            return position == 0;
        }

        @Override
        public boolean isLast() {
            return position == statuses.size() - 1;
        }

        @Override
        public boolean isBeforeFirst() {
            return false;
        }

        @Override
        public boolean isAfterLast() {
            return false;
        }

        @Override
        public int getColumnIndex(String s) {
            return 0;
        }

        @Override
        public int getColumnIndexOrThrow(String s) throws IllegalArgumentException {
            return 0;
        }

        @Override
        public String getColumnName(int i) {
            return null;
        }

        @Override
        public String[] getColumnNames() {
            return new String[0];
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public byte[] getBlob(int i) {
            return new byte[0];
        }

        @Override
        public String getString(int i) {
            return null;
        }

        @Override
        public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {

        }

        @Override
        public short getShort(int i) {
            return 0;
        }

        @Override
        public int getInt(int i) {
            return statuses.get(position);
        }

        @Override
        public long getLong(int i) {
            return 0;
        }

        @Override
        public float getFloat(int i) {
            return 0;
        }

        @Override
        public double getDouble(int i) {
            return 0;
        }

        @Override
        public int getType(int i) {
            return 0;
        }

        @Override
        public boolean isNull(int i) {
            return statuses == null;
        }

        @Override
        public void deactivate() {

        }

        @Override
        public boolean requery() {
            return false;
        }

        @Override
        public void close() {

        }

        @Override
        public boolean isClosed() {
            return false;
        }

        @Override
        public void registerContentObserver(ContentObserver contentObserver) {

        }

        @Override
        public void unregisterContentObserver(ContentObserver contentObserver) {

        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void setNotificationUri(ContentResolver contentResolver, Uri uri) {

        }

        @Override
        public Uri getNotificationUri() {
            return null;
        }

        @Override
        public boolean getWantsAllOnMoveCalls() {
            return false;
        }

        @Override
        public Bundle getExtras() {
            return null;
        }

        @Override
        public Bundle respond(Bundle bundle) {
            return null;
        }
    }
}
