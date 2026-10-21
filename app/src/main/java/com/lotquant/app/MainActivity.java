package com.lotquant.app;
import android.app.Activity; import android.content.*; import android.graphics.Color; import android.media.AudioManager;
import android.net.Uri; import android.os.*; import android.webkit.*;
public class MainActivity extends Activity {
  WebView w;
  class Bridge { Context c; Bridge(Context c){this.c=c;}
    @JavascriptInterface public void vibrate(String p){ try{ String[] s=p.split(","); long[] t=new long[s.length+1];
      for(int i=0;i<s.length;i++) t[i+1]=Long.parseLong(s[i].trim());
      Vibrator v=(Vibrator)c.getSystemService(Context.VIBRATOR_SERVICE);
      if(Build.VERSION.SDK_INT>=26) v.vibrate(VibrationEffect.createWaveform(t,-1)); else v.vibrate(t,-1);}catch(Exception e){} } }
  @Override protected void onCreate(Bundle b){ super.onCreate(b); setVolumeControlStream(AudioManager.STREAM_MUSIC);
    w=new WebView(this); w.setBackgroundColor(Color.parseColor("#06080a")); setContentView(w);
    WebSettings s=w.getSettings(); s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setMediaPlaybackRequiresUserGesture(false);
    w.addJavascriptInterface(new Bridge(this),"AndroidBridge");
    w.setWebViewClient(new WebViewClient(){ @Override public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r){
      Uri u=r.getUrl(); String sc=u.getScheme(); if("mailto".equals(sc)||"http".equals(sc)||"https".equals(sc)){ startActivity(new Intent(Intent.ACTION_VIEW,u)); return true;} return false; }});
    w.loadUrl("file:///android_asset/index.html"); }
  @Override public void onBackPressed(){ if(w.canGoBack()) w.goBack(); else super.onBackPressed(); }
}
