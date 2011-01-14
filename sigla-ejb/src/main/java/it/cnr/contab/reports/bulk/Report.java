package it.cnr.contab.reports.bulk;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Report {
	private final String name;
	private final byte[] bytes;
	private final String contentType;
	private final Long contentLength;
	
	public Report(String name, byte[] bytes, String contentType, Long contentLength) {
		super();
		this.name = name;
		this.bytes = bytes;
		this.contentType = contentType;
		this.contentLength = contentLength;
	}

	public String getName() {
		return name;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public String getContentType() {
		return contentType;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public InputStream getInputStream(){
		return new ByteArrayInputStream(getBytes());
	}
}
