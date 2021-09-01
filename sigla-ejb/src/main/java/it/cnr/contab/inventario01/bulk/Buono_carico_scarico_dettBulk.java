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
* Created by Generator 1.0
* Date 19/01/2006
*/
package it.cnr.contab.inventario01.bulk;
import java.math.BigDecimal;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Condizione_beneBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.SimpleBulkList;

public class Buono_carico_scarico_dettBulk extends Buono_carico_scarico_dettBase {
	public final static  String STATO_COGE_X = "X";
	public final static String STATO_COGE_N = "N";
	public final static String STATO_COGE_C = "C";
	public final static String STATO_COGE_R = "R";
	private Inventario_beniBulk bene;
	private int gruppi;
	private Buono_carico_scaricoBulk buono_cs;
	private Boolean fl_accessorio_contestuale=new Boolean(false);
	private SimpleBulkList v_utilizzatoriColl = new BulkList();
	private BigDecimal totale = new BigDecimal(0);		
	protected Boolean fl_bene_accessorio;
	private Categoria_gruppo_voceBulk cat_voce;		
	public Buono_carico_scarico_dettBulk() {
		super();
	}
	public Buono_carico_scarico_dettBulk(java.lang.Long pg_inventario, java.lang.String ti_documento, java.lang.Integer esercizio, java.lang.Long pg_buono_c_s, java.lang.Long nr_inventario, java.lang.Integer progressivo) {
		super(pg_inventario, ti_documento, esercizio, pg_buono_c_s, nr_inventario, progressivo);
		setBuono_cs(new it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk(pg_inventario,  ti_documento, esercizio, pg_buono_c_s));
		setBene(new Inventario_beniBulk(nr_inventario,pg_inventario,new Long (progressivo.longValue())));
	}
	
	public Inventario_beniBulk getBene() {
		return bene;
	}
	public void setBene(Inventario_beniBulk bulk) {
		bene = bulk;
	}
	public Buono_carico_scaricoBulk getBuono_cs() {
		return buono_cs;
	}
	public void setBuono_cs(Buono_carico_scaricoBulk bulk) {
		buono_cs = bulk;
	}
	/**
	 * Restituisce il codice del bene, che risulta come concatenazione di NR_INVENTARIO
	 * 	e PROGRESSIVO.
	 * Dato che il PROGRESSIVO deve apparire come un codice di TRE cifre,
	 *	si utilizza un Format.
	 * @return String
	 */
	public String getCod_bene() {
	
		if (getNr_inventario() == null || getProgressivo() == null )
			return "";
		java.text.DecimalFormat formato = new java.text.DecimalFormat("000");	
		return getNr_inventario().toString() + "-" + formato.format(getProgressivo());
	
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/20/2001 3:32:48 PM)
	 * @return java.util.Collection
	 */
	public Condizione_beneBulk getCondizioneBene() {
		return bene.getCondizioneBene();
	}

	public String getChiaveHash (){

		if (getNr_inventario()==null || getProgressivo()==null)
			return null;	
		return getNr_inventario().toString() + "." + getProgressivo().toString();
	}
	/**
		 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
		 * in stato <code>INSERT</code>.
		 * Questo metodo viene invocato automaticamente da un 
		 * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
		 * per l'inserimento di un OggettoBulk.
		 */
		public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
			bene = new Inventario_beniBulk();
			bene.setTi_commerciale_istituzionale(TipoIVA.ISTITUZIONALE.value());
			return this;
		}
		/**
		 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
		 * in stato <code>INSERT</code>.
		 * Questo metodo viene invocato automaticamente da un 
		 * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
		 * per l'inserimento di un OggettoBulk.
		 */
		public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
			bene = new Inventario_beniBulk();
			bene.setTi_commerciale_istituzionale(TipoIVA.ISTITUZIONALE.value());
			return this;
		}
	/**
	 * Restituisce <code>true</code> se il bene è un Bene accessorio di un Bene non
	 * ancora registrato su DB, ma inserito nello stesso buono di carico
	 *
	 * @return boolean
	 */ 
	public boolean isAccessorioContestuale() {
	
		if (fl_accessorio_contestuale!=null){
			return fl_accessorio_contestuale.booleanValue();
		}
		else
			return false;
	}
	public boolean isAssociatoConAccessorioContestuale() {
		Buono_carico_scaricoBulk buono_carico = (Buono_carico_scaricoBulk)getBuono_cs();
		if (getChiaveHash() == null || buono_carico.getAccessoriContestualiHash()==null)
			return false;
		return (buono_carico.getAccessoriContestualiHash().containsKey(getChiaveHash()));
	}
	public boolean isROValore_unitario()
	{
		if (this.getStato_coge()==null)
		   return false;
		else   
		 if(this.getStato_coge().compareTo("C")==0)
			return true;
		else
			return false;
	}
	public boolean isROsearchTool() {
		return isBeneAccessorio();
	}
	public boolean isROcategoriaBene() {
		if (getBuono_cs().isByFattura()||isAccessorioContestuale())
			return true;	
		return false;
	}
	public boolean isROcollocazione() {
		// Questo metodo servirà per gestire se rendere editabile la collocazione e dovrà 
		//	tenere conto della categoria Gruppo del bene	
		if (getBene().getCategoria_Bene()!=null)
			return true;
		return false;
	}	
	public boolean isROfl_accessorio() {	
		return (getBuono_cs().isByFattura() && isAccessorioContestuale()||getBuono_cs().isByDocumento() && isAccessorioContestuale());				
	}

	public Boolean getFl_accessorio_contestuale() {
		return fl_accessorio_contestuale;
	}
	public void setFl_accessorio_contestuale(Boolean boolean1) {
		fl_accessorio_contestuale = boolean1;
	}
	public it.cnr.jada.bulk.SimpleBulkList getV_utilizzatoriColl() {
	return v_utilizzatoriColl;
	}
	public int addToV_utilizzatoriColl (Utilizzatore_CdrVBulk nuovoVUtilizzatore)
	{
		v_utilizzatoriColl.add(nuovoVUtilizzatore);
		nuovoVUtilizzatore.setDettaglio(this);
		nuovoVUtilizzatore.setBene(getBene());
		nuovoVUtilizzatore.setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk());
		return v_utilizzatoriColl.size()-1;
	}
	public boolean hasUtilizzatori() {
		return v_utilizzatoriColl.size()>0;
	}
	public Utilizzatore_CdrVBulk removeFromV_utilizzatoriColl( int indiceDiLinea ) {
		Utilizzatore_CdrVBulk element = (Utilizzatore_CdrVBulk)v_utilizzatoriColl.get(indiceDiLinea);
		element = (Utilizzatore_CdrVBulk)v_utilizzatoriColl.remove(indiceDiLinea);
		return element;
	}
	public void setV_utilizzatoriColl(SimpleBulkList newV_utilizzatoriColl) {
		v_utilizzatoriColl = newV_utilizzatoriColl;
	}
	/**
	 * Restituisce <code>true</code> se il bene è un Bene Accessorio
	 *
	 * @return boolean
	 */
	public boolean isBeneAccessorio() {		
		if (fl_bene_accessorio!=null)
			return fl_bene_accessorio.booleanValue();
		return false;
	}
	public Boolean getFl_bene_accessorio() {
		return fl_bene_accessorio;
	}	
	public void setFl_bene_accessorio(Boolean boolean1) {
		fl_bene_accessorio = boolean1;
	}
	/**
	* Restituisce <code>true</code> se il bene ha il FL_TOTALMENTE_SCARICATO settato a TRUE
	*
	* @return boolean
	*/
	public boolean isTotalmenteScaricato() {
		if (getBene() != null && getBene().getFl_totalmente_scaricato() != null)
			return getBene().getFl_totalmente_scaricato().booleanValue();
		return false;
	}
	 
	public void setPg_inventario(java.lang.Long pg_inventario)  {
		this.getBuono_cs().setPg_inventario(pg_inventario);
	}
	
	public java.lang.Long getPg_inventario () {
		return this.getBuono_cs().getPg_inventario();
	}
	public java.lang.Long getNr_inventario () {
		return this.getBene().getNr_inventario();
	}
	public void setNr_inventario (java.lang.Long nr_inventario) {
		this.getBene().setNr_inventario(nr_inventario);
	}
	public Integer getProgressivo () {
		return new Integer(this.getBene().getProgressivo().intValue());
	}
	public void setProgressivo (Integer progressivo) {
		this.getBene().setProgressivo(new Long(progressivo.longValue()));
	}
	
	public void setTi_documento(java.lang.String ti_documento)  {
		this.getBuono_cs().setTi_documento(ti_documento);
	}
	public java.lang.String getTi_documento () {
		return this.getBuono_cs().getTi_documento();
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getBuono_cs().setEsercizio(esercizio);
	}
	public java.lang.Integer getEsercizio () {
		return this.getBuono_cs().getEsercizio();
	}
	public void setPg_buono_c_s(java.lang.Long pg_buono_c_s)  {
		this.getBuono_cs().setPg_buono_c_s(pg_buono_c_s);
	}
	public java.lang.Long getPg_buono_c_s () {
		return this.getBuono_cs().getPg_buono_c_s();
	}
	public void CalcolaTotaleBene() { 

		if (getQuantita() == null) setQuantita(new Long(1));
		if (getValore_unitario() == null) setValore_unitario(new java.math.BigDecimal(0));
	    	setTotale(new BigDecimal(getQuantita().longValue()).multiply(getValore_unitario()).setScale(2,java.math.BigDecimal.ROUND_HALF_UP));
	    	
	}
	public BigDecimal getTotale() {
		return totale;
	}
	public void setTotale(BigDecimal decimal) {
		totale = decimal;
	}
	public int getGruppi() {
		return gruppi;
	}
	public void setGruppi(int gruppi) {
		this.gruppi = gruppi;
	}
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getAssegnatario() {
		return bene.getAssegnatario();
	}
	public String getEtichetta() {
		return bene.getEtichetta();
	}
	public String getCollocazione() {
		return  bene.getCollocazione();
	}
	public Categoria_gruppo_inventBulk getCategoria_Bene() {
		return bene.getCategoria_Bene();
	}
	public String getDs_bene() {
		return bene.getDs_bene();
	}
	public Ubicazione_beneBulk getUbicazione() {
		return bene.getUbicazione();
	}
	public BigDecimal getValoreBene() {
		return getValore_unitario();
	}
	public Categoria_gruppo_voceBulk getCat_voce() {
		return cat_voce;
	}
	public void setCat_voce(Categoria_gruppo_voceBulk cat_voce) {
		this.cat_voce = cat_voce;
	}
}