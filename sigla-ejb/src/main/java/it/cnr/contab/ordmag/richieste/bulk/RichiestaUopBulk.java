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

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/05/2017
 */
package it.cnr.contab.ordmag.richieste.bulk;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.StrServ;
import it.cnr.jada.util.action.CRUDBP;
public class RichiestaUopBulk extends RichiestaUopBase implements AllegatoParentBulk{
	protected BulkList righeRichiestaColl= new BulkList();
	/**
	 * [UNITA_ORGANIZZATIVA Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	private Unita_organizzativaBulk unitaOrganizzativa =  new Unita_organizzativaBulk();
	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
	/**
	 * [NUMERAZIONE_ORD Numeratori Ordini]
	 **/
	private NumerazioneOrdBulk numerazioneOrd =  new NumerazioneOrdBulk();
	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
	/**
	 * [UNITA_OPERATIVA_ORD Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	private UnitaOperativaOrdBulk unitaOperativaOrdDest =  new UnitaOperativaOrdBulk();
	public final static String STATO_ANNULLATA = "ANN";
	public final static String STATO_INSERITA = "INS";
	public final static String STATO_DEFINITIVA = "DEF";
	public final static String STATO_INVIATA_ORDINE = "INV";
	
	private Boolean isUtenteAbilitatoInserimentoRichiesta = true;
	private Boolean isForApprovazione = false;
		
	public final static Dictionary STATO;
	static{
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(STATO_INSERITA,"Inserita");
		STATO.put(STATO_ANNULLATA,"Annullata");
		STATO.put(STATO_DEFINITIVA,"Definitiva");
		STATO.put(STATO_INVIATA_ORDINE,"Inviata in Ordine");
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RICHIESTA_UOP
	 **/
	public RichiestaUopBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RICHIESTA_UOP
	 **/
	public RichiestaUopBulk(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero);
		setUnitaOrganizzativa( new Unita_organizzativaBulk(cdCds) );
		setNumerazioneOrd( new NumerazioneOrdBulk(cdUnitaOperativa,esercizio,cdNumeratore) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa)  {
		this.unitaOrganizzativa=unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Numeratori Ordini]
	 **/
	public NumerazioneOrdBulk getNumerazioneOrd() {
		return numerazioneOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Numeratori Ordini]
	 **/
	public void setNumerazioneOrd(NumerazioneOrdBulk numerazioneOrd)  {
		this.numerazioneOrd=numerazioneOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public UnitaOperativaOrdBulk getUnitaOperativaOrdDest() {
		return unitaOperativaOrdDest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public void setUnitaOperativaOrdDest(UnitaOperativaOrdBulk unitaOperativaOrdDest)  {
		this.unitaOperativaOrdDest=unitaOperativaOrdDest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaOrganizzativa();
		if (unitaOrganizzativa == null)
			return null;
		return getUnitaOrganizzativa().getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getUnitaOrganizzativa().setCd_unita_organizzativa(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		NumerazioneOrdBulk numerazioneOrd = this.getNumerazioneOrd();
		if (numerazioneOrd == null){
			if (this.getUnitaOperativaOrd() == null)
				return null;
			return this.getUnitaOperativaOrd().getCdUnitaOperativa();
		}
		return getNumerazioneOrd().getCdUnitaOperativa();
	}
	public java.lang.String getCdUopRichiesta() {
		UnitaOperativaOrdBulk uop = this.getUnitaOperativaOrd();
		if (uop == null){
			return null;
		}
		return this.getUnitaOperativaOrd().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getNumerazioneOrd().setCdUnitaOperativa(cdUnitaOperativa);
	}
	public void setCdUopRichiesta(java.lang.String cdUnitaOperativa)  {
		this.getUnitaOperativaOrd().setCdUnitaOperativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		NumerazioneOrdBulk numerazioneOrd = this.getNumerazioneOrd();
		if (numerazioneOrd == null)
			return null;
		return getNumerazioneOrd().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getNumerazioneOrd().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratore]
	 **/
	public java.lang.String getCdNumeratore() {
		NumerazioneOrdBulk numerazioneOrd = this.getNumerazioneOrd();
		if (numerazioneOrd == null)
			return null;
		return getNumerazioneOrd().getCdNumeratore();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratore]
	 **/
	public void setCdNumeratore(java.lang.String cdNumeratore)  {
		this.getNumerazioneOrd().setCdNumeratore(cdNumeratore);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativaDest() {
		UnitaOperativaOrdBulk unitaOperativaOrd = this.getUnitaOperativaOrdDest();
		if (unitaOperativaOrd == null)
			return null;
		return getUnitaOperativaOrdDest().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativaDest(java.lang.String cdUnitaOperativa)  {
		this.getUnitaOperativaOrdDest().setCdUnitaOperativa(cdUnitaOperativa);
	}
	protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		impostaCds(context);
		return super.initialize(bp,context);
	}
	public OggettoBulk initializeForSearch(CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		super.initializeForSearch(bp,context);
		impostaCds(context);
		return this;
	}
	private void impostaCds(it.cnr.jada.action.ActionContext context) {
		setCdCds(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_cds());
	}
	public RichiestaUopRigaBulk removeFromRigheRichiestaColl(int index) 
	{
		// Gestisce la selezione del bottone cancella repertorio
		return (RichiestaUopRigaBulk)righeRichiestaColl.remove(index);
	}
	public int addToRigheRichiestaColl( RichiestaUopRigaBulk nuovoRigo ) 
	{


//		nuovoRigo.setTi_associato_manrev(nuovoRigo.NON_ASSOCIATO_A_MANDATO);
//		nuovoRigo.setTerzo(new TerzoBulk());
//		if (getTi_entrate_spese()==ENTRATE){
//			nuovoRigo.setTerzo_uo_cds(getTerzo_uo_cds());		
//		}
		nuovoRigo.setRichiestaUop(this);

//		try {
//			java.sql.Timestamp ts = it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp();
//			nuovoRigo.setDt_da_competenza_coge((getDt_da_competenza_coge() == null)?ts : getDt_da_competenza_coge());
//			nuovoRigo.setDt_a_competenza_coge((getDt_a_competenza_coge() == null)?ts : getDt_a_competenza_coge());
//		} catch (javax.ejb.EJBException e) {
//			throw new it.cnr.jada.DetailedRuntimeException(e);
//		}	
		nuovoRigo.setStato(RichiestaUopRigaBulk.STATO_INSERITO);
		int max = 0;
		for (Iterator i = righeRichiestaColl.iterator(); i.hasNext();) {
			int prog = ((RichiestaUopRigaBulk)i.next()).getRiga();
			if (prog > max) max = prog;
		}
		nuovoRigo.setRiga(new Integer(max+1));
		righeRichiestaColl.add(nuovoRigo);
		return righeRichiestaColl.size()-1;
	}
	public BulkCollection[] getBulkLists() {

		// Metti solo le liste di oggetti che devono essere resi persistenti

		return new it.cnr.jada.bulk.BulkCollection[] { 
				righeRichiestaColl
		};
	}
	public List getChildren() {
		return getRigheRichiestaColl();
	}
	public BulkList getRigheRichiestaColl() {
		return righeRichiestaColl;
	}
	public void setRigheRichiestaColl(BulkList righeRichiestaColl) {
		this.righeRichiestaColl = righeRichiestaColl;
	}
	public UnitaOperativaOrdBulk getUnitaOperativaOrd() {
		return unitaOperativaOrd;
	}
	public void setUnitaOperativaOrd(UnitaOperativaOrdBulk unitaOperativaOrd) {
		this.unitaOperativaOrd = unitaOperativaOrd;
	}
	public Dictionary getStatoKeys() {
		return STATO;
	}
	public Dictionary getStatoKeysForSearch() {

		OrderedHashtable d = (OrderedHashtable)getStatoKeys();
		if (d == null) return null;
		OrderedHashtable clone = (OrderedHashtable)d.clone();
		return clone;
	}
	/**
	 * Il metodo inzializza la missione da modificare
	 */
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) 
	{
		setStato(STATO_INSERITA);
		java.sql.Timestamp dataReg = null;
		try {
			dataReg = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		} catch (javax.ejb.EJBException e) {
			throw new it.cnr.jada.DetailedRuntimeException(e);
		}
		setCdCds(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_padre());
		setDataRichiesta(dataReg);
		
		//	La data di registrazione la inizializzo sulla Component

		return this;
	}
	public AllegatoGenericoBulk removeFromArchivioAllegati(int index) {
		return getArchivioAllegati().remove(index);
	}
	public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
		archivioAllegati.add(allegato);
		return archivioAllegati.size()-1;		
	}
	public BulkList<AllegatoGenericoBulk> getArchivioAllegati() {
		return archivioAllegati;
	}
	public void setArchivioAllegati(
			BulkList<AllegatoGenericoBulk> archivioAllegati) {
		this.archivioAllegati = archivioAllegati;
	}
	public String constructCMISNomeFile() {
		StringBuffer nomeFile = new StringBuffer();
		nomeFile = nomeFile.append(StrServ.lpad(this.getNumero().toString(),9,"0"));
		return nomeFile.toString();
	}
	public String recuperoIdRichiestaAsString(){
		return StrServ.replace(getCdCds(), ".", "")+getEsercizio()+StrServ.replace(getCdUnitaOperativa(), ".", "")+StrServ.replace(getCdNumeratore(), ".", "")+StrServ.lpad(getNumero().toString(), 5);
	}
	public Boolean isInserita(){
		return getStato() != null && getStato().equals(STATO_INSERITA);
	}
	public Boolean isAnnullata(){
		return getStato() != null && getStato().equals(STATO_ANNULLATA);
	}
	public Boolean isDefinitiva(){
		return getStato() != null && getStato().equals(STATO_DEFINITIVA);
	}
	public Boolean isDefinitivaOInviata(){
		return isDefinitiva() || isInviataOrdine();
	}
	public Boolean isInviataOrdine(){
		return getStato() != null && getStato().equals(STATO_INVIATA_ORDINE);
	}
	public Boolean getIsUtenteAbilitatoInserimentoRichiesta() {
		return isUtenteAbilitatoInserimentoRichiesta;
	}
	public void setIsUtenteAbilitatoInserimentoRichiesta(Boolean isUtenteAbilitatoInserimentoRichiesta) {
		this.isUtenteAbilitatoInserimentoRichiesta = isUtenteAbilitatoInserimentoRichiesta;
	}
	public Boolean getIsForApprovazione() {
		return isForApprovazione;
	}
	public void setIsForApprovazione(Boolean isForApprovazione) {
		this.isForApprovazione = isForApprovazione;
	}
	@Override
	public void validate() throws ValidationException {
		super.validate();
		if(isDefinitiva() || isInviataOrdine()){
			if (getUnitaOperativaOrdDest() == null || getUnitaOperativaOrdDest().getCdUnitaOperativa() == null)
			throw new ValidationException("Non è possibile rendere definitiva una richiesta senza aver prima indicato l'unità operativa di destinazione.");
		}
	}

	
}