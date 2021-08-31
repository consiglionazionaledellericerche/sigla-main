/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.util.Dictionary;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;
import org.springframework.util.StringUtils;

public class TipoMovimentoMagBulk extends TipoMovimentoMagBase {

	public final static String AZIONE_DECREMENTA = "D";
	public final static String AZIONE_AZZERA = "0";
	public final static String AZIONE_SOSTITUISCE = "S";
	public final static String AZIONE_SOTTRAE = "-";
	public final static String AZIONE_SOMMA = "+";


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
		TIPO = new OrderedHashtable();
		TIPO.put(CARICO_MANUALE,"Car. Manuale");
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


	public boolean isEnableMovStorno(){
		if (StringUtils.isEmpty(getTipo()))
			return true;
		return (!(getTipo().equals(STORNI)));

	}
	public boolean isEnableMovRif(){
		if (StringUtils.isEmpty(getTipo()))
			return true;

		return getTipo().equals(CARICO_AUTOMATICO)||
				getTipo().equals(CARICO_TRASFERIMENTO)||
				getTipo().equals(SCARICO_AUTOMATICO)||
				getTipo().equals(SCARICO_TRASFERIMENTO);
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
		SEGNO = new OrderedHashtable();
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
		AGG_QUANTITA = new OrderedHashtable();
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
		SCELTA = new OrderedHashtable();
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


	/**
	 * [UNITA_ORGANIZZATIVA Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	private Unita_organizzativaBulk unitaOrganizzativa =  new Unita_organizzativaBulk();
	/**
	 * [TIPO_MOVIMENTO_MAG Anagrafica delle Tipologie dei Movimenti.]
	 **/
	private TipoMovimentoMagBulk tipoMovimentoMagStorno;
	private TipoMovimentoMagBulk tipoMovimentoMagAlt;
	private TipoMovimentoMagBulk tipoMovimentoMagRif;
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
	public TipoMovimentoMagBulk(String cdCds, String cdTipoMovimento) {
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

	public TipoMovimentoMagBulk getTipoMovimentoMagRif() {
		return tipoMovimentoMagRif;
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

	public void setTipoMovimentoMagRif(TipoMovimentoMagBulk tipoMovimentoMagRif)  {
		this.tipoMovimentoMagRif=tipoMovimentoMagRif;
	}



	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public String getCdCds() {
		Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaOrganizzativa();
		if (unitaOrganizzativa == null)
			return null;
		return getUnitaOrganizzativa().getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(String cdCds)  {
		this.getUnitaOrganizzativa().setCd_unita_organizzativa(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsStorno]
	 **/
	public String getCdCdsStorno() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagStorno();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagStorno().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsStorno]
	 **/
	public void setCdCdsStorno(String cdCdsStorno)  {
		if (this.getTipoMovimentoMagStorno() == null){
			setTipoMovimentoMagStorno(new TipoMovimentoMagBulk());
		}
		this.getTipoMovimentoMagStorno().setCdCds(cdCdsStorno);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoStorno]
	 **/
	public String getCdTipoMovimentoStorno() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagStorno();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagStorno().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoStorno]
	 **/
	public void setCdTipoMovimentoStorno(String cdTipoMovimentoStorno)  {
		if (this.getTipoMovimentoMagStorno() == null){
			setTipoMovimentoMagStorno(new TipoMovimentoMagBulk());
		}
		this.getTipoMovimentoMagStorno().setCdTipoMovimento(cdTipoMovimentoStorno);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsAlt]
	 **/
	public String getCdCdsAlt() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagAlt();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagAlt().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsAlt]
	 **/
	public void setCdCdsAlt(String cdCdsAlt)  {
		if (this.getTipoMovimentoMagAlt() == null){
			setTipoMovimentoMagAlt(new TipoMovimentoMagBulk());
		}
		this.getTipoMovimentoMagAlt().setCdCds(cdCdsAlt);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoAlt]
	 **/
	public String getCdTipoMovimentoAlt() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagAlt();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagAlt().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoAlt]
	 **/
	public void setCdTipoMovimentoAlt(String cdTipoMovimentoAlt)  {
		if (this.getTipoMovimentoMagAlt() == null){
			setTipoMovimentoMagAlt(new TipoMovimentoMagBulk());
		}
		this.getTipoMovimentoMagAlt().setCdTipoMovimento(cdTipoMovimentoAlt);
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsRif]
	 **/
	public String getCdCdsRif() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagRif();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagRif().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsRif]
	 **/
	public void setCdCdsRif(String cdCdsAlt)  {
		if (this.getTipoMovimentoMagRif() == null){
			setTipoMovimentoMagRif(new TipoMovimentoMagBulk());
		}
		this.getTipoMovimentoMagRif().setCdCds(cdCdsAlt);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoMovimentoRif]
	 **/
	public String getCdTipoMovimentoRif() {
		TipoMovimentoMagBulk tipoMovimentoMag = this.getTipoMovimentoMagRif();
		if (tipoMovimentoMag == null)
			return null;
		return getTipoMovimentoMagRif().getCdTipoMovimento();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoMovimentoRif]
	 **/
	public void setCdTipoMovimentoRif(String cdTipoMovimentoAlt)  {
		if (this.getTipoMovimentoMagRif() == null){
			setTipoMovimentoMagRif(new TipoMovimentoMagBulk());
		}
		this.getTipoMovimentoMagRif().setCdTipoMovimento(cdTipoMovimentoAlt);
	}

	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

		super.initializeForInsert(bp,context);
		
		setTipoMovimentoMagAlt(new TipoMovimentoMagBulk());
		setTipoMovimentoMagStorno(new TipoMovimentoMagBulk());
		setTipoMovimentoMagRif(new TipoMovimentoMagBulk());
		return this;
	}
	protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setCdCds(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_cds());
		return super.initialize(bp,context);
	}


	public Boolean isMovimentoDiCarico(){
		return getTipo() != null && (getTipo().equals(CARICO_MANUALE) || getTipo().equals(CARICO_AUTOMATICO) || getTipo().equals(CARICO_TRASFERIMENTO));
	}

	public Boolean isCaricoDaOrdine(){
		return getTipo() != null && (getTipo().equals(CARICO_AUTOMATICO));
	}

	public boolean isROTipoMovimentoMagRifSearchTool(){
		return ( !isEnableMovRif());
	}
	public boolean isROTipoMovimentoMagStornoSearchTool(){
		return (!isEnableMovStorno());
	}


}
