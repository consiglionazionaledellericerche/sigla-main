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

import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Condizione_beneBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk;
import it.cnr.contab.inventario01.bp.CRUDCaricoInventarioBP;
import it.cnr.contab.ordmag.magazzino.bulk.LottoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDBP;
import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;

public class Transito_beni_ordiniBulk extends Transito_beni_ordiniBase {

	public final static String STATO_ANNULLATO = "ANN";
	public final static String STATO_INSERITO = "INS";
	public final static String STATO_COMPLETO = "COM";
	public final static String STATO_TRASFERITO = "TRA";
	private Collection condizioni;

	public MovimentiMagBulk getMovimentiMag() {
		return movimentiMag;
	}

	public void setMovimentiMag(MovimentiMagBulk movimentiMag) {
		this.movimentiMag = movimentiMag;
	}

	//	private Buono_carico_scaricoBulk buono_cs;
	private MovimentiMagBulk movimentiMag;
	private Condizione_beneBulk condizioneBene;
	private Tipo_ammortamentoBulk tipo_ammortamento;
	private Collection ti_ammortamenti;
	private Ubicazione_beneBulk ubicazione;
	// VALORE_UNITARIO
	private java.math.BigDecimal valore_unitario;

	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk assegnatario;

	public final static Dictionary ISTITUZIONALE_COMMERCIALE;
	public final static String ISTITUZIONALE      = "I";
	public final static String COMMERCIALE      = "C";



	public Integer getNumeroOrdine() {
		if(getMovimentiMag() != null){
			MovimentiMagBulk movimento = getMovimentiMag();
			if(movimento.getLottoMag()!=null){
				LottoMagBulk lotto = movimento.getLottoMag();
				if(lotto.getOrdineAcqConsegna()!=null){
					OrdineAcqConsegnaBulk ordineAcq = lotto.getOrdineAcqConsegna();
					if(ordineAcq!=null)
						return ordineAcq.getNumero();
				}
			}
		}
		return null;
	}


	public Timestamp getDtRiferimento() {
		if(getMovimentiMag() != null){

			return getMovimentiMag().getDtRiferimento();
		}
		return null;
	}

	public String getNumeroBolla() {
		if(getMovimentiMag() != null){

			return getMovimentiMag().getNumeroBolla();
		}
		return null;
	}
	public Timestamp getDataBolla(){
		if(getMovimentiMag() != null){
			return getMovimentiMag().getDataBolla();
		}
		return null;
	}



	static {

		ISTITUZIONALE_COMMERCIALE = new it.cnr.jada.util.OrderedHashtable();
		ISTITUZIONALE_COMMERCIALE.put(ISTITUZIONALE,"Istituzionale");
		ISTITUZIONALE_COMMERCIALE.put(COMMERCIALE,"Commerciale");
	};
	private Id_inventarioBulk inventario;
	protected final static java.text.Format FORMAT = new java.text.DecimalFormat("000");;
	private java.math.BigDecimal valoreBene;

public Transito_beni_ordiniBulk() {
	super();
}
public Transito_beni_ordiniBulk(Long id) {
	super(id);
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
public Integer getCd_assegnatario() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk assegnatario = this.getAssegnatario();
	if (assegnatario == null)
		return null;
	return assegnatario.getCd_terzo();
}
public String getCd_cds() {
	Ubicazione_beneBulk ubicazione = this.getUbicazione();
	if (ubicazione == null)
		return null;
	return ubicazione.getCd_cds();
}
public String getCd_condizione_bene() {
	Condizione_beneBulk condizioneBene = this.getCondizioneBene();
	if (condizioneBene == null)
		return null;
	return condizioneBene.getCd_condizione_bene();
}
public String getCd_ubicazione() {
	Ubicazione_beneBulk ubicazione = this.getUbicazione();
	if (ubicazione == null)
		return null;
	return ubicazione.getCd_ubicazione();
}
public String getCd_unita_organizzativa() {
	Ubicazione_beneBulk ubicazione = this.getUbicazione();
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
public Condizione_beneBulk getCondizioneBene() {
	return condizioneBene;
}
/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 3:32:48 PM)
 * @return java.util.Collection
 */

/**
 * Insert the method's description here.
 * Creation date: (11/20/2001 3:32:48 PM)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Condizione_beneBulk
 */
public Id_inventarioBulk getInventario() {
	return inventario;
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2002 15.22.46)
 * @return java.lang.String
 */
/**
 * Insert the method's description here.
 * Creation date: (04/08/2004 10.00.35)
 * @return it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk
 */
/**
 * Insert the method's description here.
 * Creation date: (21/05/2002 15.13.26)
 * @return java.util.Collection
 */
public Collection getTi_ammortamenti() {
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
public Tipo_ammortamentoBulk getTipo_ammortamento() {
	return tipo_ammortamento;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2001 3:50:25 PM)
 * @return it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 */
public Ubicazione_beneBulk getUbicazione() {
	return ubicazione;
}
/**
 * Insert the method's description here.
 * Creation date: (06/03/2002 18.20.55)
 * @return it.cnr.jada.bulk.SimpleBulkList
 */
public java.math.BigDecimal getValore_unitario() {
	return valore_unitario;
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2002 12.06.58)
 * @return java.lang.Float
 */
/**
 * Insert the method's description here.
 * Creation date: (15/01/2002 16.03.23)
 * @return java.math.BigDecimal
 */
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
/**
* Restituisce <code>true</code> se il bene ha le caratteristiche per essere cancellato
*
* @return boolean
*/


public boolean isIstituzionale() {
	return ISTITUZIONALE.equals(getTi_commerciale_istituzionale());
}
/**
* Restituisce <code>true</code> se il bene deriva da una operazione di migrazione,
*	<code>(FL_MIGRATO=='Y')</code>
*
* @return boolean
*/

/**
 * Insert the method's description here.
 * Creation date: (15/01/2002 13.16.57)
 * @param val java.math.BigDecimal
 */
/**
 * Insert the method's description here.
 * Creation date: (10/01/2002 16.42.31)
 * @param newAccessori it.cnr.jada.bulk.BulkList
 */
/**
 * Insert the method's description here.
 * Creation date: (11/26/2001 5:00:26 PM)
 * @param newAssegnatario it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setAssegnatario(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newAssegnatario) {
	assegnatario = newAssegnatario;
}
public void setCd_assegnatario(Integer cd_assegnatario) {
	this.getAssegnatario().setCd_terzo(cd_assegnatario);
}
public void setCd_cds(String cd_cds) {
	this.getUbicazione().setCd_cds(cd_cds);
}
public void setCd_condizione_bene(String cd_condizione_bene) {
	this.getCondizioneBene().setCd_condizione_bene(cd_condizione_bene);
}
public void setCd_ubicazione(String cd_ubicazione) {
	this.getUbicazione().setCd_ubicazione(cd_ubicazione);
}
public void setCd_unita_organizzativa(String cd_uo) {
	this.getUbicazione().setCd_unita_organizzativa(cd_uo);
}
public void setCondizioneBene(Condizione_beneBulk newCondizioneBene) {
	condizioneBene = newCondizioneBene;
}

/**
 * Insert the method's description here.
 * Creation date: (04/08/2004 10.00.35)
 * @param newFl_trasf_come_principale java.lang.Boolean
 */
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
/**
 * Insert the method's description here.
 * Creation date: (21/05/2002 15.13.26)
 * @param newTi_ammortamenti java.util.Collection
 */
public void setTi_ammortamenti(Collection newTi_ammortamenti) {
	ti_ammortamenti = newTi_ammortamenti;
}
/**
 * Insert the method's description here.
 * Creation date: (21/05/2002 15.13.26)
 * @param newTipo_ammortamento it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk
 */
public void setTipo_ammortamento(Tipo_ammortamentoBulk newTipo_ammortamento) {
	tipo_ammortamento = newTipo_ammortamento;
}
/**
 * Insert the method's description here.
 * Creation date: (11/26/2001 3:50:25 PM)
 * @param newUbicazione it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk
 */
public void setUbicazione(Ubicazione_beneBulk newUbicazione) {
	ubicazione = newUbicazione;
}
public void setValore_unitario(java.math.BigDecimal newValore_unitario) {
	valore_unitario = newValore_unitario;
}

	public Collection getCondizioni() {
		return condizioni;
	}

	public void setCondizioni(Collection condizioni) {
		this.condizioni = condizioni;
	}
	public String getCd_categoria_gruppo() {
		if (getMovimentiMag() != null ){
			return getMovimentiMag().getLottoMag().getBeneServizio().getCategoria_gruppo().getCd_categoria_gruppo();
		}
		return null;
	}
	public final static Dictionary STATO;
	static{
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(STATO_INSERITO,"Inserito");
		STATO.put(STATO_COMPLETO,"Completo");
		STATO.put(STATO_ANNULLATO,"Annullato");
		STATO.put(STATO_TRASFERITO,"Trasferito");
	}

	public Dictionary getStatoKeys() {
		return STATO;
	}

	public Boolean isStatoInserito(){
		return STATO_INSERITO.equals(getStato());
	}
	public Boolean isStatoCompleto(){
		return STATO_COMPLETO.equals(getStato());
	}
	public Boolean isStatoTrasferito(){
		return STATO_TRASFERITO.equals(getStato());
	}
	public Boolean isStatoAnnullato(){
		return STATO_ANNULLATO.equals(getStato());
	}

	@Override
	public void validate() throws ValidationException {
		if (getTi_ammortamento() == null && getTipo_ammortamento() != null){
			setTi_ammortamento(getTipo_ammortamento().getTi_ammortamento());
		}
		if (getFl_ammortamento() != null && getFl_ammortamento() && getTi_ammortamento() == null){
			throw new ValidationException("Valorizzare il tipo ammortamento.");
		}
	}

	public Boolean isTuttiCampiValorizzatiPerInventariazione(){
		if (getUbicazione() != null && getUbicazione().getCd_ubicazione() != null && getCondizioneBene() != null && getFl_ammortamento() != null && (!getFl_ammortamento() ||(getFl_ammortamento() && getTi_ammortamento() != null))){
			return true;
		}
		return false;
	}
}
