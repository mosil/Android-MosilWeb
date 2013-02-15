/*
 * Copyright (C) 2013 Mosil(http://mosil.biz>)
 * 
 * The MID License (MIT);
 * 
 * 		http://opensource.org/licenses/MIT
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

import android.annotation.SuppressLint;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("UseSparseArrays") public class MosilWebConf {
	public static int HTTP_PORT = 80;
	public static int SSL_PORT = 443;
	
	public static int CONNECT_TIME = 10000;
	public static int SOCKET_TIME = 20000;
	
	public static String HEADER_ACCEPT = 
			"application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5,application/youtube-client";
	
	/**
	 * 列舉 (Enumeration): 傳遞內容的類型
	 * */
	public static enum ContentType{
		Json(0){
			@Override
			public String toString() {
				return "application/json";
			}
		},
		Encode(1){
			@Override
			public String toString() {
				return "application/x-www-form-urlencoded";
			}
		};
		
		private int mValue;
		ContentType(int _value){
			mValue = _value;
		}
		
		/**
		 * 取得此列舉項目之數值
		 * */
		public int getValue(){
			return mValue;
		}
		
		/**
		 * 取得此列舉項目之鍵值
		 * @param	_value	列舉項目
		 * */
		public static ContentType getKey(int _value){
			return lookup.get(_value);
		}
		
		private static final Map<Integer, ContentType> lookup
			= new HashMap<Integer, ContentType>();
		
		static{
			for(ContentType type : EnumSet.allOf(ContentType.class)){
				lookup.put(type.getValue(), type);
			}
		}
	}
	
}
