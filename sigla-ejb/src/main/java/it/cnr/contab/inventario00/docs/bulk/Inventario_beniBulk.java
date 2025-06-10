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

package it.cnr.contab.inventario00.docs.bulk;

import java.util.*;

import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.*;

public class Inventario_beniBulk extends Inventario_beniBase {

	
//	private Buono_carico_scaricoBulk buono_cs;		
	private Condizione_beneBulk condizioneBene;
	private Tipo_ammortamentoBulk tipo_ammortamento;	
	private java.util.Collection ti_ammortamenti; 	
	private Categoria_gruppo_inventBulk categoria_Bene;
	private Transito_beni_ordiniBulk transito_beni_ordini;
	private Ubicazione_beneBulk ubicazione;
	private boolean pubblicazione;

	public Transito_beni_ordiniBulk getTransito_beni_ordini() {
		return transito_beni_ordini;
	}

	public void setTransito_beni_ordini(Transito_beni_ordiniBulk transito_beni_ordini) {
		this.transito_beni_ordini = transito_beni_ordini;
	}

	private Integer num_buono;
	private Boolean da_fattura;
	private Boolean ha_dettagli;
	// VALORE_UNITARIO
	private java.math.BigDecimal valore_unitario;

	// VARIAZIONE_MENO_INIZIALE: è il valore di variazione meno preso dalla vista V_INVENTARIO_BENI_APG
	private java.math.BigDecimal variazione_meno_iniziale;

	// VARIAZIONE_PIU_INIZIALE: è il valore di variazione piu preso dalla vista V_INVENTARIO_BENI_APG
	private java.math.BigDecimal variazione_piu_iniziale;

	/* FL_VISIBILE: è il flag che indica se il bene deve essere mostrato oppure no. 
	 *	Viene utilizzato anche per quei beni accessori scaricati in seguito allo scarico di 
	 *	un bene padre, e sui quali non è possibile fare operazioni.
	*/ 
	private String fl_visibile;

	// VALORE_ALIENAZIONE_APG: è il valore di valore alienazione preso dalla vista V_INVENTARIO_BENI_APG
	private java.math.BigDecimal valore_alienazione_apg;

	/*  Bene di riferimento nel caso di un BENE ACCESSORIO */
	private Inventario_beniBulk bene_principale;
	
	/* COLLECTION DI UTIIZZATORIVBULK  */	
	private SimpleBulkList v_utilizzatoriColl = new SimpleBulkList();
	
	private SimpleBulkList utilizzatori = new SimpleBulkList();
	
	private SimpleBulkList buoni_dettColl = new SimpleBulkList();
	
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdr_utilizzatore;
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk assegnatario;

	private Collection condizioni;

	private Boolean contab;
	//private java.lang.String ti_istituzionale_commerciale;
		
	public final static java.util.Dictionary ISTITUZIONALE_COMMERCIALE = TipoIVA.TipoIVAKeys;

	public final static String ISTOTALMENTESCARICATO = "Y";
	public final static String ISNOTTOTALMENTESCARICATO = "N";

	private Id_inventarioBulk inventario;
	protected final static java.text.Format FORMAT = new java.text.DecimalFormat("000");;
	private java.math.BigDecimal valoreBene;
	private it.cnr.jada.bulk.SimpleBulkList accessori = new SimpleBulkList();

	/*  Nuovo bene padre: utilizzato nel trasferimento intra UO, per indicare l'assegnazione di 
		 un bene accessorio ad un nuovo bene principale
	*/
	private Inventario_beniBulk nuovo_bene_padre;

    private Categoria_gruppo_inventBulk nuova_categoria;
	
	// Flag che indica, durante un trasferimento intra UO, che un bene accessorio diventa bene principale.
	private Boolean fl_trasf_come_principale;

	public final static Dictionary statoKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String STATO_FUNZIONANTE = "FUN";
	final public static String STATO_OBSOLETO = "OBS";
	final public static String STATO_SMARRITO = "SMR";

	static {
		statoKeys.put(STATO_FUNZIONANTE, "Bene rinvenuto e funzionante");
		statoKeys.put(STATO_OBSOLETO, "Bene non funzionante/obsoleto");
		statoKeys.put(STATO_SMARRITO, "Bene non rinvenuto/smarrito/oggetto di furto");
	}

	public Inventario_beniBulk() {
	super();
}
public Inventario_beniBulk(java.lang.Long nr_inventario,java.lang.Long pg_inventario,java.lang.Long progressivo) {
	super(nr_inventario,pg_inventario,progressivo);
	setInventario(new it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk(pg_inventario));
}
public int addToV_utilizzatoriColl (Utilizzatore_CdrVBulk nuovoVUtilizzatore)
{

	v_utilizzatoriColl.add(nuovoVUtilizzatore);
	nuovoVUtilizzatore.setBene(this);

	if (nuovoVUtilizzatore.getCdr()==null)
		nuovoVUtilizzatore.setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk());
		
	return v_utilizzatoriColl.size()-1;
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2002 16.42.31)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.SimpleBulkList getAccessori() {
	return accessori;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2001 5:00:26 PM)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getAssegnatario() {
	return assegnatario;
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 2:27:09 PM)
 * @return it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk
 */
public Inventario_beniBulk getBene_principale() {
	return bene_principale;
}
public BulkCollection[] getBulkLists() {
	
	 return new it.cnr.jada.bulk.BulkCollection[] { 
			/*v_utilizzatoriColl,*/
			utilizzatori
			};
}

public Integer getnum_buono() {
	return num_buono;
}
/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 3:32:48 PM)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Categoria_beneBulk
 */
public Categoria_gruppo_inventBulk getCategoria_Bene() {
	return categoria_Bene;
}
public java.lang.Integer getCd_assegnatario() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk assegnatario = this.getAssegnatario();
	if (assegnatario == null)
		return null;
	return assegnatario.getCd_terzo();
}
	public java.lang.Long getId_transito_beni_ordini() {
		Transito_beni_ordiniBulk transito = this.getTransito_beni_ordini();
		if (transito == null)
			return null;
		return transito.getId();
	}
public java.lang.String getCd_categoria_bene() {
	Categoria_gruppo_inventBulk categoria_Bene = this.getCategoria_Bene();
	if (categoria_Bene == null)
		return null;
	return categoria_Bene.getCd_categoria_gruppo();
}
public java.lang.String getCd_categoria_gruppo() {
	it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk categoria_Bene = this.getCategoria_Bene();
	if (categoria_Bene == null)
		return null;
	return categoria_Bene.getCd_categoria_gruppo();
}
public java.lang.String getCd_cds() {
	it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk ubicazione = this.getUbicazione();
	if (ubicazione == null)
		return null;
	return ubicazione.getCd_cds();
}
public java.lang.String getCd_condizione_bene() {
	it.cnr.contab.inventario00.tabrif.bulk.Condizione_beneBulk condizioneBene = this.getCondizioneBene();
	if (condizioneBene == null)
		return null;
	return condizioneBene.getCd_condizione_bene();
}
public java.lang.String getCd_ubicazione() {
	it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk ubicazione = this.getUbicazione();
	if (ubicazione == null)
		return null;
	return ubicazione.getCd_ubicazione();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk ubicazione = this.getUbicazione();
	if (ubicazione == null)
		return null;
	return ubicazione.getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 3:32:48 PM)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Condizione_beneBulk
 */
public String getCdCondizione_Bene() {
	return condizioneBene.getCd_condizione_bene();
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 3:31:48 PM)
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr_utilizzatore() {
	return cdr_utilizzatore;
}
/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 3:32:48 PM)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Condizione_beneBulk
 */
public Condizione_beneBulk getCondizioneBene() {
	return condizioneBene;
}
/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 3:32:48 PM)
 * @return java.util.Collection
 */
public Collection getCondizioni() {
	return condizioni;
}

/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 3:32:48 PM)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Condizione_beneBulk
 */
public String getDsCondizione_Bene() {
	return condizioneBene.getDs_condizione_bene();
}
/**
 * Insert the method's description here.
 * Creation date: (04/08/2004 10.00.35)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_trasf_come_principale() {
	return fl_trasf_come_principale;
}
/**
 * Insert the method's description here.
 * Creation date: (09/07/2002 15.26.09)
 * @return java.lang.String
 */
public java.lang.String getFl_visibile() {
	return fl_visibile;
}
/**
 * Insert the method's description here.
 * Creation date: (27/12/2001 15.41.33)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk
 */
public Id_inventarioBulk getInventario() {
	return inventario;
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2002 15.22.46)
 * @return java.lang.String
 */
public java.lang.String getNumeroBeneCompleto() {
	if (getNr_inventario()!=null && getProgressivo()!=null)
		return getNr_inventario().toString() + "-" + FORMAT.format(getProgressivo());

	return "";		
}
/**
 * Insert the method's description here.
 * Creation date: (04/08/2004 10.00.35)
 * @return it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk
 */
public Inventario_beniBulk getNuovo_bene_padre() {
	return nuovo_bene_padre;
}
public java.lang.Long getPg_inventario() {
	it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario = this.getInventario();
	if (inventario == null)
		return null;
	return inventario.getPg_inventario();
}
/**
 * Insert the method's description here.
 * Creation date: (21/05/2002 15.13.26)
 * @return java.util.Collection
 */
public java.util.Collection getTi_ammortamenti() {
	return ti_ammortamenti;
}
/**
* Restituisce il <code>Dictionary</code> per la gestione 
* dei tipi istituzionale/commerciale
*
* @return java.util.Dictionary
*/

public Dictionary getTi_istituzionale_commercialeKeys() {

	return ISTITUZIONALE_COMMERCIALE;
}
/**
 * Insert the method's description here.
 * Creation date: (21/05/2002 15.13.26)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk
 */
public it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk getTipo_ammortamento() {
	return tipo_ammortamento;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2001 3:50:25 PM)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 */
public it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk getUbicazione() {
	return ubicazione;
}
/**
 * Insert the method's description here.
 * Creation date: (06/03/2002 18.20.55)
 * @return it.cnr.jada.bulk.SimpleBulkList
 */
public it.cnr.jada.bulk.SimpleBulkList getUtilizzatori() {
	return utilizzatori;
}
/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 11.07.07)
 * @return it.cnr.jada.bulk.SimpleBulkList
 */
public it.cnr.jada.bulk.SimpleBulkList getV_utilizzatoriColl() {
	return v_utilizzatoriColl;
}
/**
 * Insert the method's description here.
 * Creation date: (17/04/2002 17.00.13)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getValore_alienazione_apg() {
	return valore_alienazione_apg;
}
/**
 * Insert the method's description here.
 * Creation date: (08/04/2002 15.13.42)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getValore_unitario() {
	return valore_unitario;
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2002 12.06.58)
 * @return java.lang.Float
 */
public java.math.BigDecimal getValoreBene() {
	java.math.BigDecimal val1 = new java.math.BigDecimal(0);
	java.math.BigDecimal val2 = new java.math.BigDecimal(0);

	if (getValore_iniziale()!=null){
		// Aggiunge le variaioni_piu iniziali
		if (getVariazione_piu_iniziale()!=null){
			val1 = getVariazione_piu_iniziale().abs();
		} else if (getVariazione_piu() != null){
			val1 = getVariazione_piu().abs();
		}

		// Aggiunge le variazioni_meno iniziali
		if (getVariazione_meno_iniziale()!=null){
			val2 = getVariazione_meno_iniziale().abs().negate();
		} else {
			if (getVariazione_meno()!=null)
				val2 = getVariazione_meno().abs().negate();
		}
			

		return getValore_iniziale().add(val1).add(val2);
	}
	else
		return (new java.math.BigDecimal("0"));
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2002 12.06.58)
 * @return java.lang.Float
 */
public java.math.BigDecimal getValoreBene(boolean include_accessori) {
	
	java.math.BigDecimal val1 = new java.math.BigDecimal(0);
	java.math.BigDecimal val2 = new java.math.BigDecimal(0);
	java.math.BigDecimal valore_bene = new java.math.BigDecimal(0);
	java.math.BigDecimal valore_accessori = new java.math.BigDecimal(0);
	
	if (getValore_iniziale()!=null){
		if (getVariazione_piu()!=null)
			val1 = getVariazione_piu().abs();
		if (getVariazione_meno()!=null)
			val2 = getVariazione_meno().abs().negate();
				
		valore_bene = getValore_iniziale().add(val1).add(val2);
		
		if (include_accessori){
			SimpleBulkList accessori = getAccessori();
			for(Iterator i = accessori.iterator(); i.hasNext();){
				Inventario_beniBulk accessorio = (Inventario_beniBulk)i.next();
				valore_accessori = valore_accessori.add(accessorio.getValoreBene());
			}

			valore_bene = valore_bene.add(valore_accessori);
		}
	}

	return valore_bene;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2002 16.03.23)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getVariazione_meno() {
	if (super.getVariazione_meno()==null)
		return new java.math.BigDecimal(0);
	return super.getVariazione_meno();
}
/**
 * Insert the method's description here.
 * Creation date: (12/04/2002 12.10.54)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getVariazione_meno_iniziale() {
	return variazione_meno_iniziale;
}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2002 16.05.23)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getVariazione_piu() {
	if (super.getVariazione_piu()==null)
		return new java.math.BigDecimal(0);
	return super.getVariazione_piu();
}
/**
 * Insert the method's description here.
 * Creation date: (28/06/2004 16.48.45)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getVariazione_piu_iniziale() {
	return variazione_piu_iniziale;
}
/**
 * Insert the method's description here.
 * Creation date: (11/22/2001 3:56:54 PM)
 * @return java.lang.String
 */
public boolean hasUtilizzatori() {
	return v_utilizzatoriColl.size()>0;
}
public OggettoBulk initializeForFreeSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	return initializeForSearch(bp,context);
}
/**
 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
 * in stato <code>INSERT</code>.
 * Questo metodo viene invocato automaticamente da un 
 * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
 * per l'inserimento di un OggettoBulk.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	condizioneBene = new Condizione_beneBulk();
	setTi_commerciale_istituzionale(TipoIVA.ISTITUZIONALE.value());
	return this;
}
/**
* Restituisce <code>true</code> se il bene è un Bene Accessorio
*
* @return boolean
*/

public boolean isBeneAccessorio() {
	
	if (getProgressivo() != null && getProgressivo().compareTo(new Long (0))!=0)
		return true;
		
	return false;
}
/**
* Restituisce <code>true</code> se il bene ha le caratteristiche per essere cancellato
*
* @return boolean
*/

public boolean isCancellabile() {

	java.math.BigDecimal zero = new java.math.BigDecimal(0);
	java.math.BigDecimal val_alienazione = (getValore_alienazione()!=null?getValore_alienazione():zero);
	java.math.BigDecimal val_ammortizzato = (getValore_ammortizzato()!=null?getValore_ammortizzato():zero);

	
	return (//(!isBeneAccessorio()) &&
			//(!getFl_totalmente_scaricato().booleanValue()) &&
			//(!getFl_migrato().booleanValue()) &&
			(val_alienazione.compareTo(zero)==0) //&&
			//(val_ammortizzato.compareTo(zero)==0)
			);
	
}
/**
* Restituisce <code>true</code> se il bene è Istituzionale
*
* @return boolean
*/

public boolean isIstituzionale() {
	return TipoIVA.ISTITUZIONALE.value().equals(getTi_commerciale_istituzionale());
}
/**
* Restituisce <code>true</code> se il bene deriva da una operazione di migrazione,
*	<code>(FL_MIGRATO=='Y')</code>
*
* @return boolean
*/

public boolean isMigrato() {

	if (getFl_migrato() != null)
		return getFl_migrato().booleanValue();
	
	return false;	
}
/**
 * Insert the method's description here.
 * Creation date: (04/09/2002 8.31.30)
 * @return boolean
 */
public boolean isPubblicazione() {
	return pubblicazione;
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isROassegnatarioBene() {
	
	return getAssegnatario() != null &&
			getAssegnatario().getCrudStatus() !=  OggettoBulk.UNDEFINED;
		
}
public boolean isROtarga(){
	if ((getCategoria_Bene() != null)&&  getCategoria_Bene().getFl_gestione_targa()!=null )
		return !getCategoria_Bene().getFl_gestione_targa().booleanValue();
	else if (getCrudStatus()== OggettoBulk.UNDEFINED)
		return false;
	return true;
}
public boolean isROseriale(){
	if ((getCategoria_Bene() != null)&&  getCategoria_Bene().getFl_gestione_seriale()!=null )
		if(getCategoria_Bene().getFl_gestione_seriale() && getSeriale()!=null)
			return true;
		else
			return !getCategoria_Bene().getFl_gestione_seriale().booleanValue();
	else if (getCrudStatus()== OggettoBulk.UNDEFINED)
		return false;
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isROcategoriaBene() {

	java.math.BigDecimal zero = new java.math.BigDecimal(0);
	java.math.BigDecimal val_alienazione = (getValore_alienazione()!=null?getValore_alienazione():zero);
	java.math.BigDecimal val_ammortizzato = (getValore_ammortizzato()!=null?getValore_ammortizzato():zero);

	if((this.getDa_fattura()!=null && this.getDa_fattura().booleanValue())||(this.isContab()!=null && this.isContab().booleanValue())||
			(this.isMigrato())||(this.isBeneAccessorio())){
			return true;
	}else
	return (!(
			(val_alienazione.compareTo(zero)==0) &&
			(val_ammortizzato.compareTo(zero)==0)
			) && 
			getCategoria_Bene()!=null && 
			getCategoria_Bene().getCrudStatus() != OggettoBulk.UNDEFINED);
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isROnuovoBenePadre() {

	return getNuovo_bene_padre() != null &&
		getNuovo_bene_padre().getCrudStatus() != OggettoBulk.UNDEFINED;
				
}
/**
 * Insert the method's description here.
 * Creation date: (10/02/2002 10:33:00 AM)
 * @return java.lang.String
 */
public boolean isROubicazioneBene() {
	
	return getUbicazione() != null &&
			getUbicazione().getCrudStatus() != OggettoBulk.UNDEFINED;
				
}
/**
* Restituisce <code>true</code> se il bene ha il FL_TOTALMENTE_SCARICATO settato a TRUE
*
* @return boolean
*/

public boolean isTotalmenteScaricato() {
	if (getFl_totalmente_scaricato() != null)
		return getFl_totalmente_scaricato().booleanValue();

	return false;
}
public Utilizzatore_CdrVBulk removeFromV_utilizzatoriColl( int indiceDiLinea ) {

	Utilizzatore_CdrVBulk element = (Utilizzatore_CdrVBulk)v_utilizzatoriColl.get(indiceDiLinea);

	return (Utilizzatore_CdrVBulk)v_utilizzatoriColl.remove(indiceDiLinea);


//	return null;


}
/**
 * Insert the method's description here.
 * Creation date: (15/01/2002 13.16.57)
 * @param val java.math.BigDecimal
 */
public void scaricaImporto(java.math.BigDecimal val) {
	setVariazione_meno(getVariazione_meno().add(val));
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2002 16.42.31)
 * @param newAccessori it.cnr.jada.bulk.BulkList
 */
public void setAccessori(it.cnr.jada.bulk.SimpleBulkList newAccessori) {

	accessori = newAccessori;
	for (int i = 0;i<accessori.size();i++){
		Inventario_beniBulk acc = (Inventario_beniBulk)accessori.get(i);
		acc.setBene_principale(this);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2001 5:00:26 PM)
 * @param newAssegnatario it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setAssegnatario(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newAssegnatario) {
	assegnatario = newAssegnatario;
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 2:27:09 PM)
 * @param newBene_principale it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk
 */
public void setBene_principale(Inventario_beniBulk newBene_principale) {
	bene_principale = newBene_principale;
}

public void setnum_buono(Integer numbuono) {
	num_buono = numbuono;
}

/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 3:32:48 PM)
 * @param newCategoria_Bene it.cnr.contab.docamm00.tabrif.bulk.Categoria_beneBulk
 */
public void setCategoria_Bene(Categoria_gruppo_inventBulk newCategoria_Bene) {
	categoria_Bene = newCategoria_Bene;
}
public void setCd_assegnatario(java.lang.Integer cd_assegnatario) {
	this.getAssegnatario().setCd_terzo(cd_assegnatario);
}
public void setCd_categoria_bene(java.lang.String cd_categoria_bene) {
	this.getCategoria_Bene().setCd_categoria_gruppo(cd_categoria_bene);
}
	public void setId_transito_beni_ordini(java.lang.Long id_transito_beni_ordini) {
		this.getTransito_beni_ordini().setId(id_transito_beni_ordini);
	}
public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo) {
	this.getCategoria_Bene().setCd_categoria_gruppo(cd_categoria_gruppo);
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getUbicazione().setCd_cds(cd_cds);
}
public void setCd_condizione_bene(java.lang.String cd_condizione_bene) {
	this.getCondizioneBene().setCd_condizione_bene(cd_condizione_bene);
}
public void setCd_ubicazione(java.lang.String cd_ubicazione) {
	this.getUbicazione().setCd_ubicazione(cd_ubicazione);
}
public void setCd_unita_organizzativa(java.lang.String cd_uo) {
	this.getUbicazione().setCd_unita_organizzativa(cd_uo);
}
/**
 * Insert the method's description here.
 * Creation date: (11/28/2001 3:31:48 PM)
 * @param newCdr_utilizzatore it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public void setCdr_utilizzatore(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr_utilizzatore) {
	cdr_utilizzatore = newCdr_utilizzatore;
}
/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 3:32:48 PM)
 * @param newCondizione_Bene it.cnr.contab.docamm00.tabrif.bulk.Condizione_beneBulk
 */
public void setCondizioneBene(Condizione_beneBulk newCondizioneBene) {
	condizioneBene = newCondizioneBene;
}
/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 3:32:48 PM)
 * @param newCondizioni java.util.Collection
 */
public void setCondizioni(Collection newCondizioni) {
	condizioni = newCondizioni;
}

/**
 * Insert the method's description here.
 * Creation date: (04/08/2004 10.00.35)
 * @param newFl_trasf_come_principale java.lang.Boolean
 */
public void setFl_trasf_come_principale(java.lang.Boolean newFl_trasf_come_principale) {
	fl_trasf_come_principale = newFl_trasf_come_principale;
}
/**
 * Insert the method's description here.
 * Creation date: (09/07/2002 15.26.09)
 * @param newFl_visibile java.lang.String
 */
public void setFl_visibile(java.lang.String newFl_visibile) {
	fl_visibile = newFl_visibile;
}
/**
 * Insert the method's description here.
 * Creation date: (27/12/2001 15.41.33)
 * @param newInventario it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk
 */
public void setInventario(Id_inventarioBulk newInventario) {
	inventario = newInventario;
}
/**
 * Insert the method's description here.
 * Creation date: (04/08/2004 10.00.35)
 * @param newNuovo_bene_padre it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk
 */
public void setNuovo_bene_padre(Inventario_beniBulk newNuovo_bene_padre) {
	nuovo_bene_padre = newNuovo_bene_padre;
}
public void setPg_inventario(java.lang.Long pg_inventario) {
	this.getInventario().setPg_inventario(pg_inventario);
}
/**
 * Insert the method's description here.
 * Creation date: (04/09/2002 8.31.30)
 * @param newPubblicazione boolean
 */
public void setPubblicazione(boolean newPubblicazione) {
	pubblicazione = newPubblicazione;
}
/**
 * Insert the method's description here.
 * Creation date: (21/05/2002 15.13.26)
 * @param newTi_ammortamenti java.util.Collection
 */
public void setTi_ammortamenti(java.util.Collection newTi_ammortamenti) {
	ti_ammortamenti = newTi_ammortamenti;
}
/**
 * Insert the method's description here.
 * Creation date: (21/05/2002 15.13.26)
 * @param newTipo_ammortamento it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk
 */
public void setTipo_ammortamento(it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk newTipo_ammortamento) {
	tipo_ammortamento = newTipo_ammortamento;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2001 3:50:25 PM)
 * @param newUbicazione it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 */
public void setUbicazione(it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk newUbicazione) {
	ubicazione = newUbicazione;
}
/**
 * Insert the method's description here.
 * Creation date: (06/03/2002 18.20.55)
 * @param newUtilizzatori it.cnr.jada.bulk.SimpleBulkList
 */
public void setUtilizzatori(it.cnr.jada.bulk.SimpleBulkList newUtilizzatori) {
	utilizzatori = newUtilizzatori;
}
/**
 * Insert the method's description here.
 * Creation date: (20/11/2001 14.41.50)
 * @param newFattura_attiva_dettColl it.cnr.jada.bulk.SimpleBulkList
 */
public void setV_utilizzatoriColl(SimpleBulkList newVUtilizzatoriColl) {
	v_utilizzatoriColl = newVUtilizzatoriColl;
}
/**
 * Insert the method's description here.
 * Creation date: (17/04/2002 17.00.13)
 * @param newValore_alienazione_apg java.math.BigDecimal
 */
public void setValore_alienazione_apg(java.math.BigDecimal newValore_alienazione_apg) {
	valore_alienazione_apg = newValore_alienazione_apg;
}
/**
 * Insert the method's description here.
 * Creation date: (08/04/2002 15.13.42)
 * @param newValore_unitario java.math.BigDecimal
 */
public void setValore_unitario(java.math.BigDecimal newValore_unitario) {
	valore_unitario = newValore_unitario;
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2002 12.06.58)
 * @param newValoreBene java.lang.Float
 */
public void setValoreBene(java.math.BigDecimal newValoreBene) {
	valoreBene = newValoreBene;
}
/**
 * Insert the method's description here.
 * Creation date: (12/04/2002 12.10.54)
 * @param newVariazione_meno_iniziale java.math.BigDecimal
 */
public void setVariazione_meno_iniziale(java.math.BigDecimal newVariazione_meno_iniziale) {
	variazione_meno_iniziale = newVariazione_meno_iniziale;
}
/**
 * Insert the method's description here.
 * Creation date: (28/06/2004 16.48.45)
 * @param newVariazione_piu_iniziale java.math.BigDecimal
 */
public void setVariazione_piu_iniziale(java.math.BigDecimal newVariazione_piu_iniziale) {
	variazione_piu_iniziale = newVariazione_piu_iniziale;
}
/**
 * @param b
 */
public void setDa_fattura(Boolean b) {
	da_fattura=b;
	
}
public Boolean getDa_fattura() {
	return da_fattura;
	
}
public Boolean getHa_dettagli() {
	return ha_dettagli;
}
public void setHa_dettagli(Boolean boolean1) {
	ha_dettagli = boolean1;
}
	
	public Boolean isContab() {
		return contab;
	}
	public void setContab(Boolean b) {
		contab = b;
	}
	public SimpleBulkList getBuoni_dettColl() {
		return buoni_dettColl;
	}
	public void setBuoni_dettColl(SimpleBulkList buoni_dettColl) {
		this.buoni_dettColl = buoni_dettColl;
	}
	public Categoria_gruppo_inventBulk getNuova_categoria() {
		return nuova_categoria;
	}
	public void setNuova_categoria(Categoria_gruppo_inventBulk nuova_categoria) {
		this.nuova_categoria = nuova_categoria;
	}
	public Boolean isDaOrdini(){
		return Optional.ofNullable(getTransito_beni_ordini()).isPresent();
	}

	public final java.util.Dictionary getStatoKeys() {
		return statoKeys;
	}
}
