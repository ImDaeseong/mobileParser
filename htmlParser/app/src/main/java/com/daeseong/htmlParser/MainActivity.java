package com.daeseong.htmlParser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private WebView webview;
    private TextView tv;
    private Button button1;

    private String s1;
    private String s2;
    private String s3;
    private String s4;
    private String s5;

    private String num1, num2, num3, num4, num5, num6, num7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = (WebView)findViewById(R.id.webview);
        tv = (TextView)findViewById(R.id.tv);
        tv.setMovementMethod(new ScrollingMovementMethod());

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);// 자바 스크립트 사용
        webview.addJavascriptInterface(new JavaScriptInterface(), "Android");

        webSettings.setSupportZoom(false);//확대 축소 기능
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//캐시모드를 사용하지 않고 네트워크를 통해서만 호출
        webSettings.setAppCacheEnabled(false);//앱 내부 캐시 사용 여부 설정

        webSettings.setUseWideViewPort(true);//웹뷰에 맞게 출력하기
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(false); // 안드로이드 내장 줌 컨트롤 사용 X

        webview.setWebViewClient(new CustomWebViewClient());

        //네트워크 연결 여부
        if(IsConnect()){
            webview.loadUrl("http://www.lotto.co.kr/article/list/AC01");
        }else {
            webview.loadUrl("about:blank");
        }

        button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean IsConnect(){

        boolean bConnected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected())
                bConnected = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return bConnected;
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    public class JavaScriptInterface {

        @JavascriptInterface
        public void getHtml(String html) {

            try {

                getData(html);

                s3 = String.format("%s %s %s %s %s %s %s", num1, num2, num3, num4, num5, num6, num7);
                String sValue1 = String.format("회차: %s \n", s1);
                String sValue2 = String.format("추첨일: %s \n", s2);
                String sValue3 = String.format("당첨번호: %s \n", s3);
                String sValue4 = String.format("1등 당첨자수 : %s \n", s4);
                String sValue5 = String.format("1등 당첨금액 : %s \n", s5);
                tv.setText(sValue1 + sValue2 + sValue3 + sValue4 + sValue5);

            }catch (Exception ex){
                ex.getMessage().toString();
            }

        }

        private void getData(String html){

            Document document = Jsoup.parse(html);
            if (document != null) {

                Elements rowlist = document.select("ul").select("[class=wnr_cur_list]");
                if(rowlist != null){

                    Elements liList = rowlist.select("li");
                    if(liList != null){

                        Elements plist = liList.get(0).select("p");
                        if(plist != null){

                            Elements alist = plist.select("a");
                            if(alist != null){

                                int i=0;
                                Elements imglist = alist.select("img");
                                for(Element row : imglist){

                                    String src = row.attr("src");
                                    if( isNumeric(getOnlyFileName(src)) ){

                                        if(i == 0){
                                            num1 = getOnlyFileName(src);
                                        }else if(i == 1){
                                            num2 = getOnlyFileName(src);
                                        }else if(i == 2){
                                            num3 = getOnlyFileName(src);
                                        }else if(i == 3){
                                            num4 = getOnlyFileName(src);
                                        }else if(i == 4){
                                            num5 = getOnlyFileName(src);
                                        }else if(i == 5){
                                            num6 = getOnlyFileName(src);
                                        }else if(i == 6){
                                            num7 = getOnlyFileName(src);
                                        }
                                        i++;
                                    }
                                }
                            }

                            int i=0;
                            Elements spanlist = plist.select("span");
                            for(Element row : spanlist){

                                if(i == 0){
                                    s1 = row.text();
                                }else if(i == 1){
                                    s2 = row.text();
                                }else if(i == 2){
                                    s3 = row.text();
                                }else if(i == 3){
                                    s4 = row.text();
                                }else if(i == 4){
                                    s5 = row.text();
                                }
                                i++;
                            }

                        }
                    }

                }
            }
        }

        private String getOnlyFileName(String url){
            String sResult = "";
            int nIndex = url.lastIndexOf("/");
            if(nIndex >= 0){
                String sTemp = url.substring(nIndex +1 );
                nIndex = sTemp.lastIndexOf(".");
                if(nIndex >= 0){
                    sResult = sTemp.substring(0, nIndex);
                }
            }else {
                nIndex = url.lastIndexOf(".");
                if(nIndex >= 0){
                    sResult = url.substring(0, nIndex);
                }
            }
            return sResult;
        }

        //숫자만 정규식
        private boolean isNumeric(String sInput) {
            String regex = "[\\d]*$";
            return sInput.matches(regex);
        }
    }

}
