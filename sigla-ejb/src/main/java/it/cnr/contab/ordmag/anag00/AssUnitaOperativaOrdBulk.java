/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;

public class AssUnitaOperativaOrdBulk extends AssUnitaOperativaOrdBase {
	/**
	 * [UNITA_OPERATIVA_ORD Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
	private UnitaOperativaOrdBulk unitaOperativaOrdRif =  new UnitaOperativaOrdBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ASS_UNITA_OPERATIVA_ORD
	 **/
	public AssUnitaOperativaOrdBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ASS_UNITA_OPERATIVA_ORD
	 **/
	public AssUnitaOperativaOrdBulk(java.lang.String cdUnitaOperativa, java.lang.String cdUnitaOperativaRif) {
		super(cdUnitaOperativa, cdUnitaOperativaRif);
		setUnitaOperativaOrd( new UnitaOperativaOrdBulk(cdUnitaOperativa) );
		setUnitaOperativaOrdRif( new UnitaOperativaOrdBulk(cdUnitaOperativaRif) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public UnitaOperativaOrdBulk getUnitaOperativaOrd() {
		return unitaOperativaOrd;
	}
	public UnitaOperativaOrdBulk getUnitaOperativaOrdRif() {
		return unitaOperativaOrdRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public void setUnitaOperativaOrd(UnitaOperativaOrdBulk unitaOperativaOrd)  {
		this.unitaOperativaOrd=unitaOperativaOrd;
	}
	public void setUnitaOperativaOrdRif(UnitaOperativaOrdBulk unitaOperativaOrdRif)  {
		this.unitaOperativaOrdRif=unitaOperativaOrdRif;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		UnitaOperativaOrdBulk unitaOperativaOrd = this.getUnitaOperativaOrd();
		if (unitaOperativaOrd == null)
			return null;
		return getUnitaOperativaOrd().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getUnitaOperativaOrd().setCdUnitaOperativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativaRif]
	 **/
	public java.lang.String getCdUnitaOperativaRif() {
		UnitaOperativaOrdBulk unitaOperativaOrd = this.getUnitaOperativaOrdRif();
		if (unitaOperativaOrd == null)
			return null;
		return getUnitaOperativaOrdRif().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativaRif]
	 **/
	public void setCdUnitaOperativaRif(java.lang.String cdUnitaOperativaRif)  {
		this.getUnitaOperativaOrdRif().setCdUnitaOperativa(cdUnitaOperativaRif);
	}
}