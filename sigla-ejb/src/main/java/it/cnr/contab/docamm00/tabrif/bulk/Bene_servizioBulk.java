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

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.contab.ordmag.anag00.GruppoMerceologicoBulk;
import it.cnr.contab.ordmag.anag00.TipoArticoloBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

import java.util.Dictionary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)	
public class Bene_servizioBulk extends Bene_servizioBase {

	protected final static java.lang.Boolean TRUE= java.lang.Boolean.TRUE;
	protected final static java.lang.Boolean FALSE= java.lang.Boolean.FALSE;

	public final static String TIPO_CONSEGNA_MAGAZZINO = "MAG";
	public final static String TIPO_CONSEGNA_TRANSITO = "TRA";
	public final static String TIPO_CONSEGNA_FUORI_MAGAZZINO = "FMA";

	public final static String BENE_SERVIZIO = "*";
	public final static String SERVIZIO = "S";
	public final static String BENE = "B";

	public final static String TIPO_SERVIZIO_MANUTENZIONE = "M";
	public final static String MANUTENZIONE = "S";
	public final static String MANUTENZIONE_INCREMENTATIVA = "I";
	public final static String MANUTENZIONE_NON_INCREMENTATIVA = "N";

	public final static String STATO_VALIDO = "Y";
	public final static String STATO_NON_ORDINABILE = "O";
	public final static String STATO_NON_VALIDO = "N";

	public final static Dictionary statoKeys;
	static {
		statoKeys = new it.cnr.jada.util.OrderedHashtable();
		statoKeys.put(STATO_VALIDO,"Valido");
		statoKeys.put(STATO_NON_ORDINABILE,"Non Ordinabile");
		statoKeys.put(STATO_NON_VALIDO,"Non Valido");	
	};

	public final static Dictionary BENI_SERVIZI;

	static {

		BENI_SERVIZI = new it.cnr.jada.util.OrderedHashtable();
		BENI_SERVIZI.put(BENE,"Bene");
		BENI_SERVIZI.put(SERVIZIO,"Servizio");
		//		BENI_SERVIZI.put(BENE_SERVIZIO,"Entrambi");
	}

	public final static Dictionary TIPO_CONSEGNA;
	static{
		TIPO_CONSEGNA = new it.cnr.jada.util.OrderedHashtable();
		TIPO_CONSEGNA.put(TIPO_CONSEGNA_TRANSITO,"Transito");
		TIPO_CONSEGNA.put(TIPO_CONSEGNA_MAGAZZINO,"Magazzino");
		TIPO_CONSEGNA.put(TIPO_CONSEGNA_FUORI_MAGAZZINO,"Fuori Magazzino");
	}

	public final static Dictionary TIPO_SERVIZIO;
	static{
		TIPO_SERVIZIO = new it.cnr.jada.util.OrderedHashtable();
		TIPO_SERVIZIO.put(TIPO_SERVIZIO_MANUTENZIONE,"Manutenzione");
		TIPO_SERVIZIO.put(SERVIZIO,"Servizio");
	}

	public final static Dictionary TIPO_MANUTENZIONE;
	static{
		TIPO_MANUTENZIONE = new it.cnr.jada.util.OrderedHashtable();
		TIPO_MANUTENZIONE.put(MANUTENZIONE_NON_INCREMENTATIVA,"Non Incrementativa");
		TIPO_MANUTENZIONE.put(MANUTENZIONE_INCREMENTATIVA,"Incrementativa");
	}

	protected GruppoMerceologicoBulk gruppoMerceologico;
	protected TipoArticoloBulk tipoArticolo;
	protected UnitaMisuraBulk unitaMisura;
	protected Voce_ivaBulk voce_iva;
	protected Categoria_gruppo_inventBulk categoria_gruppo;
	public Bene_servizioBulk() {
		super();
	}
	public Bene_servizioBulk(java.lang.String cd_bene_servizio) {
		super(cd_bene_servizio);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/09/2002 11.35.25)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk
	 */
	public Categoria_gruppo_inventBulk getCategoria_gruppo() {
		return categoria_gruppo;
	}
	public java.lang.String getCd_categoria_gruppo() {
		it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk categoria_gruppo = this.getCategoria_gruppo();
		if (categoria_gruppo == null)
			return null;
		return categoria_gruppo.getCd_categoria_gruppo();
	}
	public java.lang.String getCd_voce_iva() {
		it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voce_iva = this.getVoce_iva();
		if (voce_iva == null)
			return null;
		return voce_iva.getCd_voce_iva();
	}
	public java.lang.String getCdTipoArticolo() {
		TipoArticoloBulk tipoArticolo = this.getTipoArticolo();
		if (tipoArticolo == null)
			return null;
		return tipoArticolo.getCdTipoArticolo();
	}
	public java.lang.String getCdGruppoMerceologico() {
		GruppoMerceologicoBulk gruppoMerceologico = this.getGruppoMerceologico();
		if (gruppoMerceologico== null)
			return null;
		return gruppoMerceologico.getCdGruppoMerceologico();
	}
	public java.lang.String getCdUnitaMisura() {
		UnitaMisuraBulk unitaMisura = this.getUnitaMisura();
		if (unitaMisura== null)
			return null;
		return unitaMisura.getCdUnitaMisura();
	}
	public Dictionary getTi_bene_servizioKeys() {

		return BENI_SERVIZI;
	}
	public Dictionary getStatoKeys() {

		return statoKeys;
	}
	public Dictionary getTipoServizioKeys() {

		return TIPO_SERVIZIO;
	}
	public Dictionary getTipoManutenzioneKeys() {

		return TIPO_MANUTENZIONE;
	}
	public Dictionary getTipoGestioneKeys() {

		return TIPO_CONSEGNA;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/09/2002 11.35.35)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
	 */
	public Voce_ivaBulk getVoce_iva() {
		return voce_iva;
	}
	/**
	 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>.
	 * Questo metodo viene invocato da {@link #initializeForEdit}, {@link #initializeForInsert},
	 * {@link #initializeForSearch} e {@link #initializeForFreeSearch} e può contenere
	 * inizializzazioni comuni ai 4 stati del <code>FormController</code>
	 */
	protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		voce_iva = new Voce_ivaBulk();
		categoria_gruppo = new Categoria_gruppo_inventBulk();
		tipoArticolo = new TipoArticoloBulk();
		gruppoMerceologico = new GruppoMerceologicoBulk();
		unitaMisura = new UnitaMisuraBulk();
		//for (java.util.Iterator i = childrenController.values().iterator();i.hasNext();)	
		//this.get

			setFl_gestione_magazzino(null);
			setFl_gestione_inventario(null);
			setFl_obb_intrastat_acq(TRUE);	
			setFl_obb_intrastat_ven(TRUE);	
			setFl_valido(null);
			setFl_autofattura(null);	
			setFl_bollo(null);	
		return super.initialize(bp,context);
	}
	/**
	 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>.
	 * Questo metodo viene invocato da {@link #initializeForEdit}, {@link #initializeForInsert},
	 * {@link #initializeForSearch} e {@link #initializeForFreeSearch} e può contenere
	 * inizializzazioni comuni ai 4 stati del <code>FormController</code>
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		Bene_servizioBulk bulk = (Bene_servizioBulk)super.initializeForInsert(bp,context);

		if (getFl_gestione_magazzino()==null)
			setFl_gestione_magazzino(FALSE);
		if (getFl_gestione_inventario()==null)
			setFl_gestione_inventario(FALSE);
		if (getFl_obb_intrastat_acq()==null)
			setFl_obb_intrastat_acq(TRUE);	
		if (getFl_obb_intrastat_ven()==null)
			setFl_obb_intrastat_ven(TRUE);	
		if (getFl_valido()==null)
			setFl_valido(STATO_VALIDO);
		if (getFl_autofattura()==null)
			setFl_autofattura(FALSE);	
		if (getFl_bollo()==null)
			setFl_bollo(FALSE);	

		setTi_bene_servizio(BENE);

		return bulk;
	}
	public boolean isROcategoria_gruppo() {

		return getCategoria_gruppo() == null ||
				getCategoria_gruppo().getCrudStatus() == OggettoBulk.NORMAL;
	}
	public boolean isROTi_bene_servizio() {

		return false;
	}
	public boolean isROvoce_iva() {

		return getVoce_iva() == null ||
				getVoce_iva().getCrudStatus() == OggettoBulk.NORMAL;
	}
	public void setCategoria_gruppo(Categoria_gruppo_inventBulk catben) {
		categoria_gruppo = catben;
		if (catben!=null)
			setCd_categoria_gruppo(categoria_gruppo.getCd_categoria_gruppo());
	}
	public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo) {
		this.getCategoria_gruppo().setCd_categoria_gruppo(cd_categoria_gruppo);
	}
	public void setCd_voce_iva(java.lang.String cd_voce_iva) {
		this.getVoce_iva().setCd_voce_iva(cd_voce_iva);
	}
	public void setCdTipoArticolo(java.lang.String cdTipoArticolo) {
		this.getTipoArticolo().setCdTipoArticolo(cdTipoArticolo);
	}
	public void setVoce_iva(Voce_ivaBulk vi) {
		voce_iva = vi;
		if (vi!=null)
			super.setCd_voce_iva(voce_iva.getCd_voce_iva());
	}
	public void validate() throws ValidationException {
		super.validate();

		if (getFl_gestione_magazzino()==null)
			setFl_gestione_magazzino(FALSE);
		if (getFl_gestione_inventario()==null)
			setFl_gestione_inventario(FALSE);
		if (getFl_gestione_inventario().booleanValue() && (getCategoria_gruppo()==null || getCategoria_gruppo().getDs_categoria_gruppo()==null))
			throw new ValidationException("Inserire un gruppo inventariale");
	}
	public GruppoMerceologicoBulk getGruppoMerceologico() {
		return gruppoMerceologico;
	}
	public void setGruppoMerceologico(GruppoMerceologicoBulk gruppoMerceologico) {
		this.gruppoMerceologico = gruppoMerceologico;
		if (gruppoMerceologico!=null)
			super.setCdGruppoMerceologico(gruppoMerceologico.getCdGruppoMerceologico());
	}
	public TipoArticoloBulk getTipoArticolo() {
		return tipoArticolo;
	}
	public void setTipoArticolo(TipoArticoloBulk tipoArticolo) {
		this.tipoArticolo = tipoArticolo;
		if (tipoArticolo!=null)
			super.setCdTipoArticolo(tipoArticolo.getCdTipoArticolo());
	}
	public UnitaMisuraBulk getUnitaMisura() {
		return unitaMisura;
	}
	public void setUnitaMisura(UnitaMisuraBulk unitaMisura) {
		this.unitaMisura = unitaMisura;
		if (unitaMisura!=null)
			super.setCdTipoArticolo(unitaMisura.getCdUnitaMisura());
	}
	public void setUnita_misura(java.lang.String cdUnitaMisura) {
		this.getUnitaMisura().setCdUnitaMisura(cdUnitaMisura);
	}
	public void setCdGruppoMerceologico(java.lang.String cdGruppoMerceologico) {
		this.getGruppoMerceologico().setCdGruppoMerceologico(cdGruppoMerceologico);
	}
}
