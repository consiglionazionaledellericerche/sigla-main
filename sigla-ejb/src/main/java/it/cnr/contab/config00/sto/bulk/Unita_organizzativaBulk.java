package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class Unita_organizzativaBulk extends Unita_organizzativaBase {
	private CdsBulk unita_padre = new CdsBulk();
	private CdsBulk cds_area_ricerca = new CdsBulk();
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk responsabile = new it.cnr.contab.anagraf00.core.bulk.TerzoBulk();
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk responsabile_amm = new it.cnr.contab.anagraf00.core.bulk.TerzoBulk();
	private Unita_organizzativaBulk uoDiRiferimento;
	private BulkList associazioneUoArea = new BulkList();

	private Area_scientificaBulk area_scientifica;
	public Unita_organizzativaBulk() {
		super();
		inizializza();
	}
	public Unita_organizzativaBulk(java.lang.String cd_unita_organizzativa) {
		super(cd_unita_organizzativa);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'area_scientifica'
	 *
	 * @return Il valore della proprietà 'area_scientifica'
	 */
	public Area_scientificaBulk getArea_scientifica() {
		return area_scientifica;
	}
	public java.lang.String getCd_area_ricerca() {
		it.cnr.contab.config00.sto.bulk.CdsBulk cds_area_ricerca = this.getCds_area_ricerca();
		if (cds_area_ricerca == null)
			return null;
		return cds_area_ricerca.getCd_unita_organizzativa();
	}
	public java.lang.String getCd_area_scientifica() {
		it.cnr.contab.config00.sto.bulk.Area_scientificaBulk area_scientifica = this.getArea_scientifica();
		if (area_scientifica == null)
			return null;
		return area_scientifica.getCd_area_scientifica();
	}
	public java.lang.String getCd_cds() {
		it.cnr.contab.config00.sto.bulk.CdsBulk cds = this.getUnita_padre();
		if (cds == null||!cds.getFl_cds().booleanValue())
			return null;
		return cds.getCd_unita_organizzativa();
	}
	public java.lang.Integer getCd_responsabile() {
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk responsabile = this.getResponsabile();
		if (responsabile == null)
			return null;
		return responsabile.getCd_terzo();
	}
	public java.lang.Integer getCd_responsabile_amm() {
		it.cnr.contab.anagraf00.core.bulk.TerzoBulk responsabile_amm = this.getResponsabile_amm();
		if (responsabile_amm == null)
			return null;
		return responsabile_amm.getCd_terzo();
	}
	public java.lang.String getCd_unita_padre() {
		it.cnr.contab.config00.sto.bulk.CdsBulk unita_padre = this.getUnita_padre();
		if (unita_padre == null)
			return null;
		return unita_padre.getCd_unita_organizzativa();
	}
	/**
	 * @return it.cnr.contab.config00.sto.bulk.CdsBulk
	 */
	public CdsBulk getCds_area_ricerca() {
		return cds_area_ricerca;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'ds_cds_area_ricerca'
	 *
	 * @return Il valore della proprietà 'ds_cds_area_ricerca'
	 */
	public java.lang.String getDs_cds_area_ricerca() {
		if ( cds_area_ricerca != null )
			return cds_area_ricerca.getDs_unita_organizzativa();
		return null;	
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'ds_estesa'
	 *
	 * @return Il valore della proprietà 'ds_estesa'
	 */
	public java.lang.String getDs_estesa() {
		return getCd_unita_organizzativa() + " - " + getDs_unita_organizzativa();	
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'ds_responsabile'
	 *
	 * @return Il valore della proprietà 'ds_responsabile'
	 */
	public java.lang.String getDs_responsabile() {
		if ( responsabile != null && responsabile.getAnagrafico() != null ) {
			StringBuffer ds_responsabile = new StringBuffer();
			if (responsabile.getAnagrafico().getCognome() != null) {
				ds_responsabile.append(responsabile.getAnagrafico().getCognome());
				ds_responsabile.append(' ');
			}
			if (responsabile.getAnagrafico().getNome() != null)
				ds_responsabile.append(responsabile.getAnagrafico().getNome());
			return ds_responsabile.toString();
		}
		return null;	
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'ds_responsabile_amm'
	 *
	 * @return Il valore della proprietà 'ds_responsabile_amm'
	 */
	public java.lang.String getDs_responsabile_amm() {
		if ( responsabile_amm != null && responsabile_amm.getAnagrafico() != null ) {
			StringBuffer ds_responsabile_amm = new StringBuffer();
			if (responsabile_amm.getAnagrafico().getCognome() != null) {
				ds_responsabile_amm.append(responsabile_amm.getAnagrafico().getCognome());
				ds_responsabile_amm.append(' ');
			}
			if (responsabile_amm.getAnagrafico().getNome() != null)
				ds_responsabile_amm.append(responsabile_amm.getAnagrafico().getNome());
			return ds_responsabile_amm.toString();
		}
		return null;	
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'ds_unita_padre'
	 *
	 * @return Il valore della proprietà 'ds_unita_padre'
	 */
	public java.lang.String getDs_unita_padre() {
		if (unita_padre != null )
			return unita_padre.getDs_unita_organizzativa();
		return null;	
	}
	/**
	 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getResponsabile() {
		return responsabile;
	}
	/**
	 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getResponsabile_amm() {
		return responsabile_amm;
	}
	/**
	 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public CdsBulk getUnita_padre() {
		return unita_padre;
	}
	/**
	 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public Unita_organizzativaBulk getUoDiRiferimento() {
		return uoDiRiferimento;
	}
	public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setCds_area_ricerca(new CdsBulk());
		return super.initializeForFreeSearch(bp,context);
	}
	/**
	 * Metodo per l'inizializzazione di alcuni attributi (esercizio, flag_rubrica) 
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setEsercizio_inizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		setEsercizio_fine( it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.ESERCIZIO_FINE);	
		setFl_rubrica( new Boolean( true ) );
		uoDiRiferimento = new Unita_organizzativaBulk();
		responsabile = new it.cnr.contab.anagraf00.core.bulk.V_terzo_persona_fisicaBulk();
		responsabile_amm = new it.cnr.contab.anagraf00.core.bulk.V_terzo_persona_fisicaBulk();
		return this;
	}
	/**
	 * Metodo per la gestione del campo <code>esercizio</code>.
	 */
	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		// setEsercizio_inizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
		responsabile = new it.cnr.contab.anagraf00.core.bulk.V_terzo_persona_fisicaBulk();
		responsabile_amm = new it.cnr.contab.anagraf00.core.bulk.V_terzo_persona_fisicaBulk();
		return this;
	}
	/**
	 * Inizializza gli attributi specifici dell'Unita Organizzativa
	 */

	private void inizializza()
	{
		setFl_cds( new Boolean( false ) );
	}	
	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo cds area ricerca
	 * @return boolean true se il campo deve essere disabilitato
	 */

	public boolean isROCds_area_ricerca() {
		// Check se l'unità padre è di tipo area
		if(
				getUnita_padre().getCd_tipo_unita() == null ||
				unita_padre.getCrudStatus() == UNDEFINED ||
				// Se l' UO padre è un'area, non posso collegare a questa UO un'area
				((unita_padre.getCrudStatus() == NORMAL) && getUnita_padre().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA))
				) {
			this.setCds_area_ricerca(null);
			return true;
		} else
			return false;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'rOFindUoDiRiferimento'
	 *
	 * @return Il valore della proprietà 'rOFindUoDiRiferimento'
	 */
	public boolean isROFindUoDiRiferimento() 
	{
		// Check se l'unità padre è di tipo SAC
		if(
				getUnita_padre() == null ||
				getUnita_padre().getCd_tipo_unita() == null ||
				getUnita_padre().getCrudStatus() == UNDEFINED ||
				//se cds != SAC
				((getUnita_padre().getCrudStatus() == NORMAL) && 
						!getUnita_padre().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC)) ||
						//se cds = SAC  && rubrica = true
						((getUnita_padre().getCrudStatus() == NORMAL) && 
								getUnita_padre().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC) &&    
								(getFl_rubrica() == null || getFl_rubrica().booleanValue()))
				) {
			return true;
		} else
			return false;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'rOFl_rubrica'
	 *
	 * @return Il valore della proprietà 'rOFl_rubrica'
	 */
	public boolean isROFl_rubrica() 
	{
		// Check se l'unità padre è di tipo SAC
		if(
				getUnita_padre() == null ||
				getUnita_padre().getCd_tipo_unita() == null ||
				getUnita_padre().getCrudStatus() == UNDEFINED ||
				((getUnita_padre().getCrudStatus() == NORMAL) && !getUnita_padre().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC))
				) {
			return true;
		} else
			return false;
	}
	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo responsabile
	 * @return boolean true quando il campo deve essere disabilitato
	 */

	public boolean isROResponsabile() {
		return responsabile == null || responsabile.getCrudStatus() == NORMAL;
	}
	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo responsabile amministrativo
	 * @return boolean true quando il campo deve essere disabilitato
	 */

	public boolean isROResponsabile_amm() {
		return responsabile_amm == null || responsabile_amm.getCrudStatus() == NORMAL;
	}
	/**
	 * Determina quando abilitare o meno nell'interfaccia utente il campo cdr
	 * @return boolean true quando il campo deve essere disabilitato
	 */

	public boolean isROUnita_padre() {
		return unita_padre == null || unita_padre.getCrudStatus() == NORMAL;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Restituisce il valore della proprietà 'rOUoDiRiferimento'
	 *
	 * @return Il valore della proprietà 'rOUoDiRiferimento'
	 */
	public boolean isROUoDiRiferimento() 
	{
		// Check se l'unità padre è di tipo SAC
		if(
				uoDiRiferimento == null || 
				uoDiRiferimento.getCrudStatus() == NORMAL ||
				getUnita_padre() == null ||
				getUnita_padre().getCd_tipo_unita() == null ||
				getUnita_padre().getCrudStatus() == UNDEFINED ||
				//se cds != SAC
				((getUnita_padre().getCrudStatus() == NORMAL) && 
						!getUnita_padre().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC)) ||
						//se cds = SAC  && rubrica = true
						((getUnita_padre().getCrudStatus() == NORMAL) && 
								getUnita_padre().getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC) &&        
								((getFl_rubrica() != null) && getFl_rubrica().booleanValue()))
				) {
			return true;
		} else
			return false;
	}
	/**
	 * Ritorna true se il ricevente è una UO area.
	 * Creation date: (20/04/2001 16:29:07)
	 * @return true se UO area
	 */

	public boolean isUoArea() {
		return getCd_tipo_unita() == null ? false : (getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA));
	}
	/**
	 * Ritorna true se il ricevente è una UO-CDS.
	 * Creation date: (20/04/2001 16:29:07)
	 * @return true se UO-CDS
	 */

	public boolean isUoCds() 
	{
		if (getFl_uo_cds() != null)
			return getFl_uo_cds().booleanValue();
		else
			return false	;
	}

	public boolean isUoEnte() {
		return Tipo_unita_organizzativaHome.TIPO_UO_ENTE.equals(getCd_tipo_unita());
	}

	public boolean isUoSAC() {
		return Tipo_unita_organizzativaHome.TIPO_UO_SAC.equals(getCd_tipo_unita());
	}
	
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'area_scientifica'
	 *
	 * @param newArea_scientifica	Il valore da assegnare a 'area_scientifica'
	 */
	public void setArea_scientifica(Area_scientificaBulk newArea_scientifica) {
		area_scientifica = newArea_scientifica;
	}
	public void setCd_area_ricerca(java.lang.String cd_area_ricerca) {
		this.getCds_area_ricerca().setCd_unita_organizzativa(cd_area_ricerca);
	}
	public void setCd_area_scientifica(java.lang.String cd_area_scientifica) {
		this.getArea_scientifica().setCd_area_scientifica(cd_area_scientifica);
	}
	public void setCd_responsabile(java.lang.Integer cd_responsabile) {
		this.getResponsabile().setCd_terzo(cd_responsabile);
	}
	public void setCd_responsabile_amm(java.lang.Integer cd_responsabile_amm) {
		this.getResponsabile_amm().setCd_terzo(cd_responsabile_amm);
	}
	public void setCd_unita_padre(java.lang.String cd_unita_padre) {
		this.getUnita_padre().setCd_unita_organizzativa(cd_unita_padre);
	}
	/**
	 * @param newCds_area_ricerca it.cnr.contab.config00.sto.bulk.CdsBulk
	 */
	public void setCds_area_ricerca(CdsBulk newCds_area_ricerca) {
		cds_area_ricerca = newCds_area_ricerca;
	}
	/**
	 * @param newResponsabile it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public void setResponsabile(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newResponsabile) {
		responsabile = newResponsabile;
	}
	/**
	 * @param newResponsabile_amm it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public void setResponsabile_amm(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newResponsabile_amm) {
		responsabile_amm = newResponsabile_amm;
	}
	/**
	 * @param newUnita_padre it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public void setUnita_padre(CdsBulk newUnita_padre) {
		unita_padre = newUnita_padre;
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Imposta il valore della proprietà 'uoDiRiferimento'
	 *
	 * @param newUoDiRiferimento	Il valore da assegnare a 'uoDiRiferimento'
	 */
	public void setUoDiRiferimento(Unita_organizzativaBulk newUoDiRiferimento) {
		uoDiRiferimento = newUoDiRiferimento;
	}
	/**
	 * Esegue la validazione formale dei campi di input
	 */

	public void validate() throws ValidationException 
	{
		if ( getEsercizio_fine() == null )
		{
			setEsercizio_fine( it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.ESERCIZIO_FINE);
			throw new ValidationException( "Il campo Esercizio di Terminazione deve essere valorizzato. " );
		}
		if ( getEsercizio_fine().compareTo( getEsercizio_inizio()) < 0 )
			throw new ValidationException( "L' esercizio di terminazione non può essere minore dell'esercizio di creazione. " );

		if ( getEsercizio_fine().toString().length() != 4 )
			throw new ValidationException( "Il campo Esercizio di terminazione deve essere di quattro cifre. " );

		if ( getUnita_padre() != null && unita_padre.getCd_unita_organizzativa() == null || unita_padre.getCd_unita_organizzativa().equals(""))
			throw new ValidationException( "Il campo CODICE CDS è obbligatorio. " );
		if ( responsabile.getCd_terzo() == null )
			throw new ValidationException( "Il campo RESPONSABILE è obbligatorio. " );	
		//	if ( responsabile_amm.getCd_terzo() == null )
		//		throw new ValidationException( "Il campo RESPONSABILE AMM. è obbligatorio. " );				
		if ( !isNullOrEmpty( getCd_proprio_unita() ) )
		{
			long cdLong;
			try
			{
				cdLong = Long.parseLong( getCd_proprio_unita() );
			}
			catch (java.lang.NumberFormatException e)
			{
				throw new ValidationException( "Il campo CODICE deve essere numerico. " );
			}
			if ( cdLong < 0 )
				throw new ValidationException( "Il campo CODICE deve essere maggiore di 0. " );		
		}
		if ( !getFl_rubrica().booleanValue() && uoDiRiferimento.getCd_unita_organizzativa() == null )
			throw new ValidationException( "Il campo Unita' Organizzativa di Riferimento deve essere valorizzato. " );			

	}

	public BulkList getAssociazioneUoArea() {
		return associazioneUoArea;
	}

	public void setAssociazioneUoArea(BulkList list) {
		associazioneUoArea = list;
	}

	public int addToAssociazioneUoArea(Ass_uo_areaBulk dett) {
		dett.setUnita_organizzativa(this);
		getAssociazioneUoArea().add(dett);
		return getAssociazioneUoArea().size()-1;
	}	

	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {getAssociazioneUoArea()};
	}

	public Ass_uo_areaBulk removeFromAssociazioneUoArea(int index) {
		Ass_uo_areaBulk dett = (Ass_uo_areaBulk)getAssociazioneUoArea().remove(index);
		return dett;
	}
}
