/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
public class Blt_regole_diariaBulk extends Blt_regole_diariaBase {
	/**
	 * [BLT_ACCORDI Contiene la descrizione dettagliata e le caratteristiche principali dell'Accordo Bilaterale tra CNR e Ente omologo straniero da gestire]
	 **/
	private Blt_accordiBulk bltAccordi =  new Blt_accordiBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_REGOLE_DIARIA
	 **/
	public Blt_regole_diariaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_REGOLE_DIARIA
	 **/
	public Blt_regole_diariaBulk(java.lang.String cdAccordo, java.lang.Integer pgRegola) {
		super(cdAccordo, pgRegola);
		setBltAccordi( new Blt_accordiBulk(cdAccordo) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Contiene la descrizione dettagliata e le caratteristiche principali dell'Accordo Bilaterale tra CNR e Ente omologo straniero da gestire]
	 **/
	public Blt_accordiBulk getBltAccordi() {
		return bltAccordi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Contiene la descrizione dettagliata e le caratteristiche principali dell'Accordo Bilaterale tra CNR e Ente omologo straniero da gestire]
	 **/
	public void setBltAccordi(Blt_accordiBulk bltAccordi)  {
		this.bltAccordi=bltAccordi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdAccordo]
	 **/
	public java.lang.String getCdAccordo() {
		Blt_accordiBulk bltAccordi = this.getBltAccordi();
		if (bltAccordi == null)
			return null;
		return getBltAccordi().getCd_accordo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAccordo]
	 **/
	public void setCdAccordo(java.lang.String cdAccordo)  {
		this.getBltAccordi().setCd_accordo(cdAccordo);
	}
}