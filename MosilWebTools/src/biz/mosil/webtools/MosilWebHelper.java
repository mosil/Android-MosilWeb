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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MosilWebHelper {
	@SuppressLint("DefaultLocale") 
	public static boolean isFindHttpAtPrefixs(String _url){
		Pattern pattern = Pattern.compile("^http://", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(_url.toLowerCase());
		return matcher.find();
	}
	
	@SuppressLint("DefaultLocale") 
	public static boolean isFindHttpsAtPrefixs(String _url){
		Pattern pattern = Pattern.compile("^https://", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(_url.toLowerCase());
		return matcher.find();
	}
	
	public static String chkHttpsAtPrefixs(String _url){
		if(isFindHttpAtPrefixs(_url)){
			return _url.replace("(?i)" + "http://", "");
		} else if(isFindHttpsAtPrefixs(_url)){
			return _url.replace("(?i)" + "https://", "");
		}
		return (isFindHttpAtPrefixs(_url))
 				? _url.replace("http://", "")
 				: _url;
		
	}
}
