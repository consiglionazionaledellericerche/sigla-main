package it.cnr.contab.util.servlet;

import java.util.LinkedList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class NameValuePairs extends LinkedList<NameValuePair>{

	private static final long serialVersionUID = 1L;

	public void add(String name, String value){
		add(new BasicNameValuePair(name, value));
	}
	
	@Override
	public NameValuePair[] toArray() {
		return super.toArray(new NameValuePair[size()]);
	}
}
