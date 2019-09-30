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

package it.cnr.contab.client.docamm;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class FatturaAttivaScad {
 private java.math.BigDecimal im_voce;
 private String gae;
 private String cdr;
 
	public FatturaAttivaScad() {
		super();
	}

public java.math.BigDecimal getIm_voce() {
	return im_voce;
}
@XmlElement(required=true)
public void setIm_voce(java.math.BigDecimal im_voce) {
	this.im_voce = im_voce;
}
public String getGae() {
	return gae;
}
@XmlElement(required=true)
public void setGae(String gae) {
	this.gae = gae;
}
public String getCdr() {
	return cdr;
}
@XmlElement(required=true)
public void setCdr(String cdr) {
	this.cdr = cdr;
}


}
