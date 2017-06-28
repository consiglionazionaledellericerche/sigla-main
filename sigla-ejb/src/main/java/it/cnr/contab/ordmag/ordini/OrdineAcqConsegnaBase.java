/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini;
import it.cnr.jada.persistency.Keyed;
public class OrdineAcqConsegnaBase extends OrdineAcqConsegnaKey implements Keyed {
//    STATO VARCHAR(3) NOT NULL
	private java.lang.String stato;
 
//    TIPO_CONSEGNA VARCHAR(3) NOT NULL
	private java.lang.String tipoConsegna;
 
//    DT_PREV_CONSEGNA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dtPrevConsegna;
 
//    QUANTITA DECIMAL(17,5) NOT NULL
	private java.math.BigDecimal quantita;
 
//    IM_IMPONIBILE_DIVISA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imImponibileDivisa;
 
//    IM_IVA_DIVISA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imIvaDivisa;
 
//    IM_IMPONIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imImponibile;
 
//    IM_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imIva;
 
//    IM_TOTALE_CONSEGNA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal imTotaleConsegna;
 
//    CD_CDS_MAG VARCHAR(10)
	private java.lang.String cdCdsMag;
 
//    CD_MAGAZZINO VARCHAR(10)
	private java.lang.String cdMagazzino;
 
//    CD_UOP_DEST VARCHAR(30)
	private java.lang.String cdUopDest;
 
//    CD_CDS_OBBL VARCHAR(30)
	private java.lang.String cdCdsObbl;
 
//    ESERCIZIO_OBBL DECIMAL(4,0)
	private java.lang.Integer esercizioObbl;
 
//    ESERCIZIO_ORIG_OBBL DECIMAL(4,0)
	private java.lang.Integer esercizioOrigObbl;
 
//    PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pgObbligazione;
 
//    PG_OBBLIGAZIONE_SCAD DECIMAL(10,0)
	private java.lang.Long pgObbligazioneScad;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ORDINE_ACQ_CONSEGNA
	 **/
	public OrdineAcqConsegnaBase() {
		super();
	}
	public OrdineAcqConsegnaBase(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero, java.lang.Integer riga, java.lang.Integer consegna) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero, riga, consegna);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stato]
	 **/
	public java.lang.String getStato() {
		return stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato]
	 **/
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tipoConsegna]
	 **/
	public java.lang.String getTipoConsegna() {
		return tipoConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tipoConsegna]
	 **/
	public void setTipoConsegna(java.lang.String tipoConsegna)  {
		this.tipoConsegna=tipoConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtPrevConsegna]
	 **/
	public java.sql.Timestamp getDtPrevConsegna() {
		return dtPrevConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtPrevConsegna]
	 **/
	public void setDtPrevConsegna(java.sql.Timestamp dtPrevConsegna)  {
		this.dtPrevConsegna=dtPrevConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [quantita]
	 **/
	public java.math.BigDecimal getQuantita() {
		return quantita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [quantita]
	 **/
	public void setQuantita(java.math.BigDecimal quantita)  {
		this.quantita=quantita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imImponibileDivisa]
	 **/
	public java.math.BigDecimal getImImponibileDivisa() {
		return imImponibileDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imImponibileDivisa]
	 **/
	public void setImImponibileDivisa(java.math.BigDecimal imImponibileDivisa)  {
		this.imImponibileDivisa=imImponibileDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imIvaDivisa]
	 **/
	public java.math.BigDecimal getImIvaDivisa() {
		return imIvaDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imIvaDivisa]
	 **/
	public void setImIvaDivisa(java.math.BigDecimal imIvaDivisa)  {
		this.imIvaDivisa=imIvaDivisa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imImponibile]
	 **/
	public java.math.BigDecimal getImImponibile() {
		return imImponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imImponibile]
	 **/
	public void setImImponibile(java.math.BigDecimal imImponibile)  {
		this.imImponibile=imImponibile;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imIva]
	 **/
	public java.math.BigDecimal getImIva() {
		return imIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imIva]
	 **/
	public void setImIva(java.math.BigDecimal imIva)  {
		this.imIva=imIva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imTotaleConsegna]
	 **/
	public java.math.BigDecimal getImTotaleConsegna() {
		return imTotaleConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imTotaleConsegna]
	 **/
	public void setImTotaleConsegna(java.math.BigDecimal imTotaleConsegna)  {
		this.imTotaleConsegna=imTotaleConsegna;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsMag]
	 **/
	public java.lang.String getCdCdsMag() {
		return cdCdsMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsMag]
	 **/
	public void setCdCdsMag(java.lang.String cdCdsMag)  {
		this.cdCdsMag=cdCdsMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		return cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.cdMagazzino=cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUopDest]
	 **/
	public java.lang.String getCdUopDest() {
		return cdUopDest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUopDest]
	 **/
	public void setCdUopDest(java.lang.String cdUopDest)  {
		this.cdUopDest=cdUopDest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsObbl]
	 **/
	public java.lang.String getCdCdsObbl() {
		return cdCdsObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsObbl]
	 **/
	public void setCdCdsObbl(java.lang.String cdCdsObbl)  {
		this.cdCdsObbl=cdCdsObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioObbl]
	 **/
	public java.lang.Integer getEsercizioObbl() {
		return esercizioObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioObbl]
	 **/
	public void setEsercizioObbl(java.lang.Integer esercizioObbl)  {
		this.esercizioObbl=esercizioObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOrigObbl]
	 **/
	public java.lang.Integer getEsercizioOrigObbl() {
		return esercizioOrigObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrigObbl]
	 **/
	public void setEsercizioOrigObbl(java.lang.Integer esercizioOrigObbl)  {
		this.esercizioOrigObbl=esercizioOrigObbl;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazione]
	 **/
	public java.lang.Long getPgObbligazione() {
		return pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazione]
	 **/
	public void setPgObbligazione(java.lang.Long pgObbligazione)  {
		this.pgObbligazione=pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazioneScad]
	 **/
	public java.lang.Long getPgObbligazioneScad() {
		return pgObbligazioneScad;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazioneScad]
	 **/
	public void setPgObbligazioneScad(java.lang.Long pgObbligazioneScad)  {
		this.pgObbligazioneScad=pgObbligazioneScad;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
}