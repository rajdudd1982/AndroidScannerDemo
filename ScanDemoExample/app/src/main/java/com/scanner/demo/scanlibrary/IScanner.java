package com.scanner.demo.scanlibrary;

import android.net.Uri;

import java.util.List;

/**
 * Created by jhansi on 04/04/15.
 */
public interface IScanner {

    void onBitmapSelect(List<Uri> uris);

    void onScanFinish(List<Uri> uris);
}
