/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
public class LottoMagBulk extends LottoMagBase {
	public final static String STATO_INSERITO = "INS";
	public final static String STATO_SCADUTO = "SCA";
	public final static String STATO_VERIFICA = "VER";
	
	/**
	 * [NUMERAZIONE_MAG Definisce i contatori per la numerazione dei magazzini.]
	 **/
	private NumerazioneMagBulk numerazioneMag =  new NumerazioneMagBulk();
	private Bene_servizioBulk beneServizio =  new Bene_servizioBulk();
	private MagazzinoBulk magazzino =  new MagazzinoBulk();
	private OrdineAcqConsegnaBulk ordineAcqConsegna =  new OrdineAcqConsegnaBulk();
	private TerzoBulk terzo =  new TerzoBulk();
	private DivisaBulk divisa =  new DivisaBulk();
	
	public LottoMagBulk() {
		super();
	}
	public LottoMagBulk(String cdCds, String cdMagazzino, Integer esercizio, String cdNumeratoreMag, Integer pgLotto) {
		super(cdCds, cdMagazzino, esercizio, cdNumeratoreMag, pgLotto);
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
	public Bene_servizioBulk getBeneServizio() {
		return beneServizio;
	}
	public void setBeneServizio(Bene_servizioBulk beneServizio)  {
		this.beneServizio=beneServizio;
	}
	public MagazzinoBulk getMagazzino() {
		return magazzino;
	}
	public void setMagazzino(MagazzinoBulk magazzino)  {
		this.magazzino=magazzino;
	}
	public OrdineAcqConsegnaBulk getOrdineAcqConsegna() {
		return ordineAcqConsegna;
	}
	public void setOrdineAcqConsegna(OrdineAcqConsegnaBulk ordineAcqConsegna)  {
		this.ordineAcqConsegna=ordineAcqConsegna;
	}
	public TerzoBulk getTerzo() {
		return terzo;
	}
	public void setTerzo(TerzoBulk terzo)  {
		this.terzo=terzo;
	}
	public DivisaBulk getDivisa() {
		return divisa;
	}	
	public void setDivisa(DivisaBulk divisa)  {
		this.divisa=divisa;
	}
	public String getCdCds() {
		if (this.getNumerazioneMag() == null)
			return null;
		return this.getNumerazioneMag().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(String cdCds)  {
		this.getNumerazioneMag().setCdCds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public String getCdMagazzino() {
		if (this.getNumerazioneMag() == null)
			return null;
		return this.getNumerazioneMag().getCdMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(String cdMagazzino)  {
		this.getNumerazioneMag().setCdMagazzino(cdMagazzino);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public Integer getEsercizio() {
		if (this.getNumerazioneMag() == null)
			return null;
		return this.getNumerazioneMag().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(Integer esercizio)  {
		this.getNumerazioneMag().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreMag]
	 **/
	public String getCdNumeratoreMag() {
		if (this.getNumerazioneMag() == null)
			return null;
		return this.getNumerazioneMag().getCdNumeratoreMag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreMag]
	 **/
	public void setCdNumeratoreMag(String cdNumeratoreMag)  {
		this.getNumerazioneMag().setCdNumeratoreMag(cdNumeratoreMag);
	}
	public String getCdBeneServizio() {
		if (this.getBeneServizio() == null)
			return null;
		return this.getBeneServizio().getCd_bene_servizio();
	}
	public void setCdBeneServizio(String cdBeneServizio)  {
		this.getBeneServizio().setCd_bene_servizio(cdBeneServizio);
	}
	public String getCdCdsMag() {
		if (this.getMagazzino() == null)
			return null;
		return this.getMagazzino().getCdCds();
	}
	public void setCdCdsMag(String cdCdsMag)  {
		this.getMagazzino().setCdCds(cdCdsMag);
	}
	public String getCdMagazzinoMag() {
		if (this.getMagazzino() == null)
			return null;
		return this.getMagazzino().getCdMagazzino();
	}
	public void setCdMagazzinoMag(String cdMagazzinoMag)  {
		this.getMagazzino().setCdMagazzino(cdMagazzinoMag);
	}
	public String getCdCdsOrdine() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getCdCds();
	}
	public void setCdCdsOrdine(String cdCdsOrdine)  {
		this.getOrdineAcqConsegna().setCdCds(cdCdsOrdine);
	}
	public String getCdUnitaOperativa() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getCdUnitaOperativa();
	}
	public void setCdUnitaOperativa(String cdUnitaOperativa)  {
		this.getOrdineAcqConsegna().setCdUnitaOperativa(cdUnitaOperativa);
	}
	public Integer getEsercizioOrdine() {
		
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getEsercizio();
	}
	public void setEsercizioOrdine(Integer esercizioOrdine)  {
		this.getOrdineAcqConsegna().setEsercizio(esercizioOrdine);
	}
	public String getCdNumeratoreOrdine() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getCdNumeratore();
	}
	public void setCdNumeratoreOrdine(String cdNumeratoreOrdine)  {
		this.getOrdineAcqConsegna().setCdNumeratore(cdNumeratoreOrdine);
	}
	public Integer getNumeroOrdine() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getNumero();
	}
	public void setNumeroOrdine(Integer numeroOrdine)  {
		this.getOrdineAcqConsegna().setNumero(numeroOrdine);
	}
	public Integer getRigaOrdine() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getRiga();
	}
	public void setRigaOrdine(Integer rigaOrdine)  {
		this.getOrdineAcqConsegna().setRiga(rigaOrdine);
	}
	public Integer getConsegna() {
		if (this.getOrdineAcqConsegna() == null)
			return null;
		return this.getOrdineAcqConsegna().getConsegna();
	}
	public void setConsegna(Integer consegna)  {
		this.getOrdineAcqConsegna().setConsegna(consegna);
	}
	public Integer getCdTerzo() {
		if (this.getTerzo() == null)
			return null;
		return this.getTerzo().getCd_terzo();
	}
	public void setCdTerzo(Integer cdTerzo)  {
		this.getTerzo().setCd_terzo(cdTerzo);
	}
	public String getCdDivisa() {
		if (this.getDivisa() == null)
			return null;
		return this.getDivisa().getCd_divisa();
	}
	public void setCdDivisa(String cdDivisa)  {
		this.getDivisa().setCd_divisa(cdDivisa);
	}
}