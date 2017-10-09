/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;

public class EvasioneOrdineBulk extends EvasioneOrdineBase {
	/**
	 * [NUMERAZIONE_MAG Definisce i contatori per la numerazione dei magazzini.]
	 **/
	private NumerazioneMagBulk numerazioneMag =  new NumerazioneMagBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EVASIONE_ORDINE
	 **/

	private UnitaOperativaOrdBulk unitaOperativaAbilitata = new UnitaOperativaOrdBulk();
	
	protected BulkList righeConsegnaDaEvadereColl= new BulkList();

	public EvasioneOrdineBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: EVASIONE_ORDINE
	 **/
	public EvasioneOrdineBulk(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.Integer esercizio, java.lang.String cdNumeratoreMag, java.lang.Long numero) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, numero);
		setNumerazioneMag( new NumerazioneMagBulk(cdCds,cdMagazzino,esercizio,cdNumeratoreMag) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Definisce i contatori per la numerazione dei magazzini.]
	 **/
	public NumerazioneMagBulk getNumerazioneMag() {
		return numerazioneMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Definisce i contatori per la numerazione dei magazzini.]
	 **/
	public void setNumerazioneMag(NumerazioneMagBulk numerazioneMag)  {
		this.numerazioneMag=numerazioneMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return Optional.ofNullable(this.getNumerazioneMag())
				.map(NumerazioneMagBulk::getCdCds)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getNumerazioneMag().setCdCds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		return Optional.ofNullable(this.getNumerazioneMag())
				.map(NumerazioneMagBulk::getCdMagazzino)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.getNumerazioneMag().setCdMagazzino(cdMagazzino);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return Optional.ofNullable(this.getNumerazioneMag())
				.map(NumerazioneMagBulk::getEsercizio)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getNumerazioneMag().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreMag]
	 **/
	public java.lang.String getCdNumeratoreMag() {
		return Optional.ofNullable(this.getNumerazioneMag())
				.map(NumerazioneMagBulk::getCdNumeratoreMag)
				.orElse(null);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreMag]
	 **/
	public void setCdNumeratoreMag(java.lang.String cdNumeratoreMag)  {
		this.getNumerazioneMag().setCdNumeratoreMag(cdNumeratoreMag);
	}
	public BulkList getRigheConsegnaDaEvadereColl() {
		return righeConsegnaDaEvadereColl;
	}
	public void setRigheConsegnaDaEvadereColl(BulkList righeConsegnaDaEvadereColl) {
		this.righeConsegnaDaEvadereColl = righeConsegnaDaEvadereColl;
	}
	public OrdineAcqConsegnaBulk removeFromConsegnaDaEvadereColl(int index) 
	{
		OrdineAcqConsegnaBulk element = (OrdineAcqConsegnaBulk)righeConsegnaDaEvadereColl.get(index);
		return (OrdineAcqConsegnaBulk)righeConsegnaDaEvadereColl.remove(index);
	}
	public int addToRigheOrdineColl( OrdineAcqConsegnaBulk nuovoRigo ) 
	{
		nuovoRigo.setStato(OrdineAcqRigaBulk.STATO_INSERITA);
		righeConsegnaDaEvadereColl.add(nuovoRigo);
		return righeConsegnaDaEvadereColl.size()-1;
	}
	public BulkCollection[] getBulkLists() {

		return new it.cnr.jada.bulk.BulkCollection[] { 
				righeConsegnaDaEvadereColl
		};
	}
	public List getChildren() {
		return getRigheConsegnaDaEvadereColl();
	}
	public UnitaOperativaOrdBulk getUnitaOperativaAbilitata() {
		return unitaOperativaAbilitata;
	}
	public void setUnitaOperativaAbilitata(UnitaOperativaOrdBulk unitaOperativaAbilitata) {
		this.unitaOperativaAbilitata = unitaOperativaAbilitata;
	}
}