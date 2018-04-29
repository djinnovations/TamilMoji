package dj.example.main.customviews;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.net.MalformedURLException;
import java.net.URL;

import dj.example.main.R;
import dj.example.main.redundant.BaseFragment;

public class DefaultWebView extends BaseFragment {
    private ProgressBar mProgressBar;

    public DefaultWebView() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_webview_fragment_layout, container, false);
        this.mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        WebView webView = (WebView)view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl((String)this.getFragmentData().getActionValue());
        webView.setWebViewClient(new WebViewClient());
        webView.setBackgroundColor(0);
        WebChromeClient webChromeClient = this.createWebChromeClient();
        if(webChromeClient != null) {
            this.configWebChromeClient(webChromeClient);
            webView.setWebChromeClient(webChromeClient);
        }

        WebViewClient webViewClient = this.createWebViewClient();
        if(webViewClient != null) {
            this.configWebViewClient(webViewClient);
            webView.setWebViewClient(webViewClient);
        }

        return view;
    }

    protected WebChromeClient createWebChromeClient() {
        return new DefaultWebView.DefaultWebChromeClient(this.mProgressBar);
    }

    protected void configWebChromeClient(WebChromeClient client) {
    }

    protected WebViewClient createWebViewClient() {
        return new DefaultWebView.DefaultWebViewClient(this.mProgressBar);
    }

    protected void configWebViewClient(WebViewClient client) {
    }

    protected void garbageCollectorCall() {
    }

    public static String getHost(String url) {
        try {
            return (new URL(url)).getHost();
        } catch (MalformedURLException var2) {
            var2.printStackTrace();
            return url;
        }
    }

    public class DefaultWebViewClient extends WebViewClient {
        private ProgressBar mProgressBar;

        public DefaultWebViewClient(ProgressBar mProgressBar) {
            this.mProgressBar = mProgressBar;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(this.mProgressBar != null) {
                this.mProgressBar.setIndeterminate(true);
                this.mProgressBar.setProgress(10);
                this.mProgressBar.setVisibility(View.VISIBLE);
            }

        }

        public void onPageFinished(WebView view, String url) {
            if(this.mProgressBar != null) {
                this.mProgressBar.setVisibility(View.GONE);
                this.mProgressBar.setIndeterminate(false);
                this.mProgressBar.setProgress(0);
            }

        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.endsWith(".mp4")) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(Uri.parse(url), "video/*");
                view.getContext().startActivity(intent);
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
            }
        }
    }

    public class DefaultWebChromeClient extends WebChromeClient {
        private ProgressBar mProgressBar;

        public DefaultWebChromeClient(ProgressBar mProgressBar) {
            this.mProgressBar = mProgressBar;
        }

        public void onProgressChanged(WebView view, int progress) {
            if(progress == 100) {
                progress = 0;
            }

            if(this.mProgressBar != null) {
                this.mProgressBar.setProgress(progress);
                this.mProgressBar.setIndeterminate(false);
                if(progress == View.VISIBLE) {
                    this.mProgressBar.setVisibility(View.GONE);
                } else {
                    this.mProgressBar.setVisibility(View.VISIBLE);
                }
            }

        }
    }
}

