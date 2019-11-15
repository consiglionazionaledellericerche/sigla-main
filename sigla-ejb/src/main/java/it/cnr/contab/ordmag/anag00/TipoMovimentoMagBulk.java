/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;
public class TipoMovimentoMagBulk extends TipoMovimentoMagBase {
	public static final String CARICO_MANUALE = "CM";
	public static final String CARICO_AUTOMATICO = "CA"; 
	public static final String CARICO_TRASFERIMENTO = "CT"; 
	public static final String SCARICO_MANUALE = "SM"; 
	public static final String SCARICO_AUTOMATICO = "SA";
	public static final String SCARICO_TRASFERIMENTO = "ST";
	public static final String RETTIFICA_PREZZO = "CV";
	public static final String RETTIFICA_INVENTARIO = "RI";
	public static final String CHIUSURE = "CH";
	public static final String RESI = "RS";
	public static final String DOTAZIONI = "DT";
	public static final String STORNI = "SR";
		
	public final static Dictionary TIPO;
	static{
		TIPO = new it.cnr.jada.util.OrderedHashtable();
		TIPO.put(CARICO_MANUALE," Car. Manuale");
		TIPO.put(CARICO_AUTOMATICO,"Car. Automatico");
		TIPO.put(CARICO_TRASFERIMENTO,"Car. Trasferimento");
		TIPO.put(SCARICO_MANUALE,"Scar. Manuale");
		TIPO.put(SCARICO_AUTOMATICO,"Scar. Automatico");
		TIPO.put(SCARICO_TRASFERIMENTO,"Scar. Trasf");
		TIPO.put(RETTIFICA_PREZZO,"Rettifica prezzo");
		TIPO.put(RETTIFICA_INVENTARIO,"Rettifica inventario");
		
		TIPO.put(SCARICO_TRASFERIMENTO," Scar. Trasf");
		TIPO.put(RETTIFICA_PREZZO,"Rettifica prezzo");
		TIPO.put(RETTIFICA_INVENTARIO,"Rettifica inventario");
		TIPO.put(CHIUSURE,"Chiusure");
		TIPO.put(RESI,"Resi");
		TIPO.put(DOTAZIONI,"Dotazioni");
		TIPO.put(STORNI,"Storni");
	}

	public Dictionary getTipoKeys() {
		return TIPO;
	}

	public Dictionary getTipoKeysForSearch() {

		OrderedHashtable d = (OrderedHashtable)getTipoKeys();
		if (d == null) return null;
		OrderedHashtable clone = (OrderedHashtable)d.clone();
		return clone;
	}
	public final static String SI= "S";
	public final static String NO= "N";
	
	
	public final static String SEGNO_NEGATIVO = "-";
	public final static String SEGNO_POSITIVO = "+";	
	
	public final static String AZZERA = "0";
	public final static String DECREMENTA = "-";
	public final static String INCREMENTA = "+";	
	public final static String SOSTITUISCI = "S";
	public final static String NO_VARIAZIONE = "N";
	
	public final static String DESCR_AZZERA = "Azzera";
	public final static String DESCR_DECREMENTA = "Decrementa";
	public final static String DESCR_INCREMENTA = "Incrementa";
	public final static String DESCR_SOSTITUISCI = "Sostituisci";	
	public final static String DESCR_NO_VARIAZIONE = "Nessuna Variazione";
	
	
	public final static Dictionary SEGNO;
	static{
		SEGNO = new it.cnr.jada.util.OrderedHashtable();
		SEGNO.put(SEGNO_NEGATIVO,SEGNO_NEGATIVO);
		SEGNO.put(SEGNO_POSITIVO,SEGNO_POSITIVO);
	}
	public Dictionary getSegnoKeys() {
		return SEGNO;
	}
	
	public Dictionary getSegnoKeysForSearch() {

		OrderedHashtable d = (OrderedHashtable)getSegnoKeys();
		if (d == null) return null;
		OrderedHashtable clone = (OrderedHashtable)d.clone();
		return clone;
	}
	
	public final static Dictionary AGG_QUANTITA;
	static{
		AGG_QUANTITA = new it.cnr.jada.util.OrderedHashtable();
		AGG_QUANTITA.put(AZZERA,DESCR_AZZERA);
		AGG_QUANTITA.put(DECREMENTA,DESCR_DECREMENTA);
		AGG_QUANTITA.put(INCREMENTA, DESCR_INCREMENTA);
		AGG_QUANTITA.put(SOSTITUISCI, DESCR_SOSTITUISCI);
		AGG_QUANTITA.put(NO_VARIAZIONE, DESCR_NO_VARIAZIONE);
	}
	public Dictionary getAggQuantitaKeys() {
		return AGG_QUANTITA;
	}
	public Dictionary getAggQuantitaKeysForSearch() {

		OrderedHashtable d = (OrderedHashtable)getSceltaKeys();
		if (d == null) return null;
		OrderedHashtable clone = (OrderedHashtable)d.clone();
		return clone;
	}
	
	public final static Dictionary SCELTA;
	static{
		SCELTA = new it.cnr.jada.util.OrderedHashtable();
		SCELTA.put(SI,SI);
		SCELTA.put(NO,NO);
	}
	public Dictionary getSceltaKeys() {
		return SCELTA;
	}
	public Dictionary getSceltaKeysForSearch() {

		OrderedHashtable d = (OrderedHashtable)getSceltaKeys();
		if (d == null) return null;
		OrderedHashtable clone = (OrderedHashtable)d.clone();
		return clone;
	}
	
	protected BulkList<TipoMovimentoMagAzBulk> righeTipoMovimentoAzioni= new BulkList<TipoMovimentoMagAzBulk>();
	
	public TipoMovimentoMagAzBulk removeFromRigheTipoMovimentoAzioni(int index) 
	{
		// Gestisce la selezione del bottone cancella repertorio
		TipoMovimentoMagAzBulk element = (TipoMovimentoMagAzBulk)righeTipoMovimentoAzioni.get(index);
		return (TipoMovimentoMagAzBulk)righeTipoMovimentoAzioni.remove(index);
	}
	
	public BulkList<TipoMovimentoMagAzBulk> getRigheTipoMovimentoAzioni() {
		return righeTipoMovimentoAzioni;
	}
	public void setRigheTipoMovimentoAzioni(BulkList<TipoMovimentoMagAzBulk> righeTipoMovimentoAzioni) {
		this.righeTipoMovimentoAzioni = righeTipoMovimentoAzioni;
	}
	
	public int addToRigheTipoMovimentoAzioni( TipoMovimentoMagAzBulk azione ) 
	{


		azione.setTipoMovimentoMag(this);

		righeTipoMovimentoAzioni.add(azione);
		return righeTipoMovimentoAzioni.size()-1;
	}
			
	public BulkCollection[] getBulkLists() {

		// Metti solo le liste di oggetti che devono essere resi persistenti

		return new it.cnr.jada.bulk.BulkCollection[] { 
				righeTipoMovimentoAzioni
		};
	}
	
	public List getChildren() {
		return getRigheTipoMovimentoAzioni();
	}

	/**
	 * [UNITA_ORGANIZZATIVA Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	private Unita_organizzativaBulk unitaOrganizzativa =  new Unita_organizzativaBulk();
	/**
	 * [TIPO_MOVIMENTO_MAG Anagrafica delle Tipologie dei Movimenti.]
	 **/
	private TipoMovimentoMagBulk tipoMovimentoMagStorno;
	private TipoMovimentoMagBulk tipoMovimentoMagAlt;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_MOVIMENTO_MAG
	 **/
	public TipoMovimentoMagBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: TIPO_MOVIMENTO_MAG
	 **/
	public TipoMovimentoMagBulk(java.lang.String cdCds, java.lang.String cdTipoMovimento) {
		super(cdCds, cdTipoMovimento);
		setUnitaOrganizzativa( new Unita_organizzativaBulk(cdCds) );
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
	 * Restituisce il valore di: [Anagrafica delle Tipologie dei Movimenti.]
	 **/
	public TipoMovimentoMagBulk getTipoMovimentoMagStorno() {
		return tipoMovimentoMagStorno;
	}
	public TipoMovimentoMagBulk getTipoMovimentoMagAlt() {
		return tipoMovimentoMagAlt;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Anagrafica delle Tipologie dei Movimenti.]
	 **/
	public void setTipoMovimentoMagStorno(TipoMovimentoMagBulk tipoMovimentoMagStorno)  {
		this.tipoMovimentoMagStorno=tipoMovimentoMagStorno;
	}
	public void setTipoMovimentoMagAlt(TipoMovimentoMagBulk tipoMovimentoMagAlt)  {
		this.tipoMovimentoMagAlt=tipoMovimentoMagAlt;
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
	 * Restituisce il valore di: [cdCdsStorno]
	 **/
	public java.lang.String getCdCdsStorno() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagStorno();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagStorno().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsStorno]
	 **/
	public void setCdCdsStorno(java.lang.String cdCdsStorno)  {
		if (this.getTipoMovimentoMagStorno() == null){
			setTipoMovimentoMagStorno(new TipoMovimentoMagBulk());
		}
		this.getTipoMovimentoMagStorno().setCdCds(cdCdsStorno);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoStorno]
	 **/
	public java.lang.String getCdTipoMovimentoStorno() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagStorno();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagStorno().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoStorno]
	 **/
	public void setCdTipoMovimentoStorno(java.lang.String cdTipoMovimentoStorno)  {
		if (this.getTipoMovimentoMagStorno() == null){
			setTipoMovimentoMagStorno(new TipoMovimentoMagBulk());
		}
		this.getTipoMovimentoMagStorno().setCdTipoMovimento(cdTipoMovimentoStorno);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsAlt]
	 **/
	public java.lang.String getCdCdsAlt() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagAlt();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagAlt().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsAlt]
	 **/
	public void setCdCdsAlt(java.lang.String cdCdsAlt)  {
		if (this.getTipoMovimentoMagAlt() == null){
			setTipoMovimentoMagAlt(new TipoMovimentoMagBulk());
		}
		this.getTipoMovimentoMagAlt().setCdCds(cdCdsAlt);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoAlt]
	 **/
	public java.lang.String getCdTipoMovimentoAlt() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagAlt();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagAlt().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoAlt]
	 **/
	public void setCdTipoMovimentoAlt(java.lang.String cdTipoMovimentoAlt)  {
		if (this.getTipoMovimentoMagAlt() == null){
			setTipoMovimentoMagAlt(new TipoMovimentoMagBulk());
		}
		this.getTipoMovimentoMagAlt().setCdTipoMovimento(cdTipoMovimentoAlt);
	}

	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		super.initializeForInsert(bp,context);
		
		setTipoMovimentoMagAlt(new TipoMovimentoMagBulk());
		setTipoMovimentoMagStorno(new TipoMovimentoMagBulk());
		return this;
	}
	protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setCdCds(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_cds());
		return super.initialize(bp,context);
	}
	public Boolean isMovimentoDiCarico(){
		return getTipo() != null && (getTipo().equals(CARICO_MANUALE) || getTipo().equals(CARICO_AUTOMATICO) || getTipo().equals(CARICO_TRASFERIMENTO));
	}
}
