/*
 * Copyright (C) 2013 Mosil(http://mosil.biz)
 * 
 * The MID License (MIT);
 * 
 *         http://opensource.org/licenses/MIT
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH 
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package biz.mosil.webtools;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import biz.mosil.webtools.MosilWebConf.ContentType;

import android.content.Context;
import android.util.Log;

public class MosilWeb {
    public static String TAG = "Mosil's Web Tool";
    public Context mContext;
    
    private String mHostName;
    private HttpParams mHttpParams;
    private HttpResponse mHttpResponse;
    private HttpContext mHttpContext;
    
    /**
     * Setting Cookie
     * @param _cookie
     * */
    public MosilWeb setCookie(BasicCookieStore _cookie){
        mHttpContext.setAttribute(ClientContext.COOKIE_STORE, _cookie);
        return this;
    }
    /**
     * Get Cookie
     * */
    public BasicCookieStore getCookie(){
        return (BasicCookieStore) mHttpContext.getAttribute(ClientContext.COOKIE_STORE);
    }
    /**
     * Clear Cookie
     * */
    public void clearCookie(){
        ((BasicCookieStore)mHttpContext.getAttribute(ClientContext.COOKIE_STORE)).clear();
    }
    
    /**
     * Constructor
     * */
    public MosilWeb(Context _context){
        mHostName = "";
        mContext = _context;
        
        mHttpContext = new BasicHttpContext();
        mHttpContext.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());
        
        mHttpParams = new BasicHttpParams();
        mHttpParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);
        HttpConnectionParams.setConnectionTimeout(mHttpParams, MosilWebConf.CONNECT_TIME);
        HttpConnectionParams.setSoTimeout(mHttpParams, MosilWebConf.SOCKET_TIME);
    }
    
    
    
    
    
    /**
     * 設定目標網址
     * @param _hostname    Host Name，請不要加上 "http://" 或是 "https://" 
     * @param _path        路徑或是呼叫函式
     * */
    public MosilWeb setUrl(String _hostname, String... _path){
        mHostName = _hostname;
        if(_path != null){
            for (String path : _path) {
                mHostName += path;
            }
        }
        return this;
    }
    
    /**
     * 將資料轉成 QueryString 格式
     * @return (String) <b>QueryString</b> Format: key1=value1&key2=value2&...
     * */
    public static final String parseToQueryString(Map<String, Object> _data){
        String queryString = "";
        if(_data != null){
            int n = 0;
            for(Map.Entry<String, Object> data : _data.entrySet()){
                n++;
                queryString += data.getKey() + "=" + data.getValue().toString();
                queryString += (n == _data.size()) ? "" : "&";
            }
        }
        return queryString;
    }
    
    /**
     * 將資料轉成 JsonString 格式
     * @return (String) <b>JsonString</b> Format: {"key1":"value1", "key2":"value2", ...}
     * */
    public static final String parseToJsonString(Map<String, Object> _data){
        String jsonString = "";
        if(_data != null){
            JSONObject json = new JSONObject();
            try{
                for(Map.Entry<String, Object> data : _data.entrySet()){
                    json.put(data.getKey(), data.getValue());
                }
            } catch (JSONException _ex) {
                Log.e(TAG, "Parse To JsonString Error: " + _ex.toString());
            }
            jsonString = json.toString();
        } 
        return jsonString;
    }
    /**
     * 取得回傳結果之字串
     * @return    (String) Response Data，若是null則回傳空字串
     * */
    public String getResponse(){
        String result = "";
        if(mHttpResponse != null){
            try {
                result = EntityUtils.toString(mHttpResponse.getEntity());
            } catch (ParseException _ex) {
                Log.e(TAG, "Parse Response To String Error: " + _ex.toString());
            } catch (IOException _ex) {
                Log.e(TAG, _ex.toString());
            }
        }
        return result;
    }
    
    /**
     * Get HTTP status after action GET/POST/Invoke
     * @return {@link org.apache.http.HttpStatus}. 
     *             Example: 200(SC_OK), 404(SC_NOT_FOUND), etc.
     * 
     * @see <a href="http://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/org/apache/http/HttpStatus.html">HTTP Status</a><br />
     *         <a href="http://support.google.com/webmasters/bin/answer.py?hl=en&answer=40132">HTTP status codes from Google(English)</a><br />
     *         <a href="http://support.google.com/webmasters/bin/answer.py?hl=zh-Hant&answer=40132">HTTP status codes from Google(正體中文)</a><br />
     * 
     * */
    public int getResponseStatus(){
        if(mHttpResponse == null){
            throw new ExceptionInInitializerError(mContext.getString(R.string.exce_after_connect));
        }
        return mHttpResponse.getStatusLine().getStatusCode();
    }
    
    
    
    
    
    /**
     * 確定 Target URL 已經被設定了
     * */
    private void chkHostName(){
        if(mHostName == null || mHostName.equals("")){
            throw new ExceptionInInitializerError(mContext.getString(R.string.exce_init_set_url));
        } 
    }
    /**
     * 使用 Get 方式執行網路連線
     * */
    public MosilWeb actGet(final String _queryString){
        chkHostName();
        
        String targetUrl = "http://" + mHostName;
        if(!_queryString.equals("")){
            targetUrl += "?" + _queryString;
        }
        
        HttpGet httpGet = new HttpGet(targetUrl);
        DefaultHttpClient httpClient = new DefaultHttpClient(mHttpParams);
        
        try {
            mHttpResponse = httpClient.execute(httpGet);
        } catch (ClientProtocolException _ex) {
            throw new RuntimeException("Client Protocol Exception: " + _ex.toString());
        } catch (IOException _ex) {
            throw new RuntimeException("IO Exception: " + _ex.toString());
        }
        
        return this;
    }
    
    /**
     * 使用 Post 方式執行網路連線，傳遞參數使用 JSON 格式
     * @param _jsonString    要傳遞的參數
     * */
    public MosilWeb actPost(final String _jsonString){
        return actPost(ContentType.Json, _jsonString);
    }
    /**
     * 使用 Post 方式執行網路連線
     * @param _type     傳遞參數的類型    {@link MosilWebConf.ContentType}
     * @param _postData    要傳遞的參數
     * */
    public MosilWeb actPost(ContentType _type, final String _postData){
        return actHttp(_type, _postData, "");
    }
    
    
    /**
     * 執行 HTTP 連線，預設傳遞格式為{@link MosilWebConf.ContentType.Encode}
     * @param _postData     用 POST 方式傳遞的參數
     * @param _queryString    用 GET 方式傳的參數(Query String)
     * */
    public MosilWeb actHttp(final String _postData, final String _queryString){
        return webInvoke(false, ContentType.Encode, _postData, _queryString);
    }
    /**
     * 執行 HTTP 連線
     * @param _type         {@link MosilWebConf.ContentType}
     * @param _postData     用 POST 方式傳遞的參數
     * @param _queryString    用 GET 方式傳的參數(Query String)
     * */
    public MosilWeb actHttp(ContentType _type, final String _postData, final String _queryString){
        return webInvoke(false, _type, _postData, _queryString);
    }
    /**
     * 執行 HTTPS 連線
     * @param _postData    用 POST 方式傳遞的參數
     * */
    public MosilWeb actHttps(final String _postData){
        return actHttps(_postData, "");
    }
    /**
     * 執行 HTTPS
     * @param _postData        用 POST 方式傳遞的參數
     * @param _queryString    用 GET 方式傳的參數(Query String)
     * */
    public MosilWeb actHttps(final String _postData, final String _queryString){
        return webInvoke(true, ContentType.Encode, _postData, _queryString);
    }
    
    
    
    
    
    /**
     * 執行HTTP
     * @param _isSSL        是否採用 HTTPS
     * @param _type         {@link biz.mosil.webtools.MosilWebConf.ContentType}
     * @param _postData     用 POST 方式傳遞的參數
     * @param _queryString    用 GET 方式傳的參數(Query String)
     * */
    private MosilWeb webInvoke(boolean _isSSL, ContentType _type, final String _postData, final String _queryString){
        chkHostName();
        try{
            String targetUrl = ((_isSSL) ? "https://" : "http://") + mHostName;
            if(!_queryString.equals("")){
                targetUrl += "?" + _queryString;
            }
            
            HttpPost httpPost = new HttpPost(targetUrl);
            httpPost.setHeader("Accept", MosilWebConf.HEADER_ACCEPT);
            httpPost.setHeader("Content-Type", _type.toString());
            StringEntity entity = null;
            try {
                entity = new StringEntity(_postData, "UTF-8");
            } catch (UnsupportedEncodingException _ex) {
                throw new RuntimeException("" + _ex.toString());
            } 
            httpPost.setEntity(entity);
            
            mHttpResponse = (_isSSL)
                    ? MosilSSLSocketFactory.getHttpClient(mHttpParams).execute(httpPost, mHttpContext)
                    : new DefaultHttpClient(mHttpParams).execute(httpPost, mHttpContext);
            
        } catch (ClientProtocolException _ex) {
            throw new RuntimeException("Client Portocol Exception: " + _ex.toString());
        } catch (IOException _ex) {
            throw new RuntimeException("IO Exception: " + _ex.toString());
        }
        
        return this;
    }
    
    
    
    
    
    /**
     * 取得 InputStream
     * */
    public static final InputStream getInputStream(final String _url){
        InputStream result = null;
        
        try {
            URL url = new URL(_url);
            URLConnection conn = url.openConnection();
            
            if(!(conn instanceof HttpURLConnection)){
                throw new IOException("This URL Can't Connect!");
            }
            
            HttpURLConnection httpConn = (HttpURLConnection)conn;
            //若是需要帳號密碼時，是否要跳出輸入訊息
            httpConn.setAllowUserInteraction(false);
            //處理轉址動作
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            
            final int response = httpConn.getResponseCode();
            
            if(response == HttpURLConnection.HTTP_OK){
                result = httpConn.getInputStream();
            }
            
        } catch (MalformedURLException _ex) {
            throw new RuntimeException("Malformed URL Exception: " + _ex.toString());
        } catch (IOException _ex) {
            throw new RuntimeException("IO Exception: " + _ex.toString());
        }
        return result;
    }
}
