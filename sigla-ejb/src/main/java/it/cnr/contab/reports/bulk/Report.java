/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
