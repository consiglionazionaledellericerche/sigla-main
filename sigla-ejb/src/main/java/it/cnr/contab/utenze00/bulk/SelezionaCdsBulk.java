package it.cnr.contab.utenze00.bulk;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;

public class SelezionaCdsBulk extends it.cnr.jada.bulk.OggettoBulk {

	private CdsBulk cds;

	private Unita_organizzativaBulk uo;

	private CdrBulk cdr;

public SelezionaCdsBulk() {
	super();
}

public CdrBulk getCdr() {
	return cdr;
}

public void setCdr(CdrBulk cdr) {
	this.cdr = cdr;
}

public CdsBulk getCds() {
	return cds;
}

public void setCds(CdsBulk cds) {
	this.cds = cds;
}

public Unita_organizzativaBulk getUo() {
	return uo;
}

public void setUo(Unita_organizzativaBulk uo) {
	this.uo = uo;
}
}
