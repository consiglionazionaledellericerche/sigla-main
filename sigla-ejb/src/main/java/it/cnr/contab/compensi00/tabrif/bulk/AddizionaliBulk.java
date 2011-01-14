/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/03/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.SimpleBulkList;
public class AddizionaliBulk extends AddizionaliBase {
	public AddizionaliBulk() {
		super();
	}
	public AddizionaliBulk(java.lang.String cd_catastale) {
		super(cd_catastale);
	}
	private SimpleBulkList dettagli= new SimpleBulkList();
	public SimpleBulkList getDettagli() {
		return dettagli;
	}
	public void setDettagli(SimpleBulkList list) {
		dettagli = list;
	}
	public AddizionaliBulk removeFromDettagli( int indiceDiLinea ) {

		AddizionaliBulk element = (AddizionaliBulk)dettagli.get(indiceDiLinea);

		return (AddizionaliBulk)dettagli.remove(indiceDiLinea);
	}

	public int addToDettagli (AddizionaliBulk nuova)
	{
			getDettagli().add(nuova);
			return getDettagli().size()-1;
	}
	
}