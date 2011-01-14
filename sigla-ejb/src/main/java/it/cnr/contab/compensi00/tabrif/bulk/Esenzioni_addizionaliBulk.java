/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 13/06/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.SimpleBulkList;
public class Esenzioni_addizionaliBulk extends Esenzioni_addizionaliBase {
	public Esenzioni_addizionaliBulk() {
		super();
	}
	public Esenzioni_addizionaliBulk(java.lang.String cd_catastale) {
		super(cd_catastale);
	}
	private SimpleBulkList dettagli= new SimpleBulkList();
	public SimpleBulkList getDettagli() {
		return dettagli;
	}
	public void setDettagli(SimpleBulkList list) {
		dettagli = list;
	}
	public Esenzioni_addizionaliBulk removeFromDettagli( int indiceDiLinea ) {

		Esenzioni_addizionaliBulk element = (Esenzioni_addizionaliBulk)dettagli.get(indiceDiLinea);

		return (Esenzioni_addizionaliBulk)dettagli.remove(indiceDiLinea);
	}

	public int addToDettagli (Esenzioni_addizionaliBulk nuova)
	{
			getDettagli().add(nuova);
			return getDettagli().size()-1;
	}
}