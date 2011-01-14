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
