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
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class MosilSSLSocketFactory extends SSLSocketFactory {
    private SSLContext mSSLContext = SSLContext.getInstance("TLS");
    
    public MosilSSLSocketFactory(KeyStore _truststore) throws 
            NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(_truststore);
        
        TrustManager trustManager = new X509TrustManager() {
            
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            
            @Override
            public void checkServerTrusted(X509Certificate[] _chain, String _authType)
                    throws CertificateException {
                
            }
            
            @Override
            public void checkClientTrusted(X509Certificate[] _chain, String _authType)
                    throws CertificateException {
                
            }
        };
        
        mSSLContext.init(null, new TrustManager[]{ trustManager }, null);
    }

    @Override
    public Socket createSocket() throws IOException {
        return mSSLContext.getSocketFactory().createSocket();
    }

    @Override
    public Socket createSocket(Socket _socket, String _host, int _port, boolean _autoClose) throws 
            IOException, UnknownHostException {
        return mSSLContext.getSocketFactory().createSocket(_socket, _host, _port, _autoClose);
    }
    
    public static HttpClient getHttpClient(HttpParams _params){
        try{
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            
            SSLSocketFactory factory = new MosilSSLSocketFactory(trustStore);
            factory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            
            HttpProtocolParams.setVersion(_params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(_params, HTTP.UTF_8);
            
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), MosilWebConf.HTTP_PORT));
            registry.register(new Scheme("https", factory, MosilWebConf.SSL_PORT));
            
            ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(_params, registry);
            
            return new DefaultHttpClient(clientConnectionManager, _params);
            
        } catch (Exception _ex) {
            return new DefaultHttpClient();
        }
    }
    
    public static HttpClient getHttpClient(){
        return getHttpClient(new BasicHttpParams());
    }
}
