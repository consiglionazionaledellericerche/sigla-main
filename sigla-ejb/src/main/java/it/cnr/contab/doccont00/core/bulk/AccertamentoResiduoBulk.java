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

package it.cnr.contab.doccont00.core.bulk;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Optional;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;

@SuppressWarnings("unchecked")
public class AccertamentoResiduoBulk extends AccertamentoBulk {
	private static final long serialVersionUID = 1L;
	public final static int LUNGHEZZA_NUMERO_ACCERTAMENTO = 6;
	private Accertamento_modificaBulk accertamento_modifica = new Accertamento_modificaBulk();	
	private boolean saldiDaAggiornare = false;
	private String stato;
	@SuppressWarnings("rawtypes")
	public final static Dictionary stato_AccertamentoResiduoKeys = new OrderedHashtable();;
	public enum Stato {
		INCASSATO("Incassato","INS", 1900, 3000),
		CERTO("Certo","CER", 1900, 3000),
		DILAZIONATO("Dilazionato","DIL", 1900, 3000),
		INCERTO("Incerto","INC", 1900, 2021),
		DUBBIO("Dubbio","DUB", 1900, 3000),
		INESIGIBILE("Inesigibile","INE", 1900, 3000),
		PARZIALMENTE_INESIGIBILE("Parzialmente Inesigibile","PIN", 1900, 3000),
		GIUDIZIALMENTE_CONTROVERSO("Giudizialmente Controverso","GIU", 2022, 3000);
		private final String label, value;
		private final Integer from, to;

		private Stato(String label, String value, Integer from, Integer to) {
			this.value = value;
			this.label = label;
			this.from = from;
			this.to = to;
		}
		public String value() {
			return value;
		}
		public String label() {
			return label;
		}
		public Integer from() {
			return from;
		}
		public Integer to() {
			return to;
		}
	}

	/**
	 * AccertamentoResiduoBulk constructor comment.
	 */
	public AccertamentoResiduoBulk() {
		super();
		initialize();	
	}
	/**
	 * AccertamentoResiduoBulk constructor comment.
	 * @param cd_cds java.lang.String
	 * @param esercizio java.lang.Integer
	 * @param esercizio_originale java.lang.Integer
	 * @param pg_accertamento java.lang.Long
	 */
	public AccertamentoResiduoBulk(String cd_cds, Integer esercizio, Integer esercizio_originale, Long pg_accertamento) {
		super(cd_cds, esercizio, esercizio_originale, pg_accertamento);
		initialize();	
	}
	// metodo per inizializzare l'oggetto bulk
	private void initialize () {
		setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_ACR_RES );
		setFl_pgiro( new Boolean( false ));
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.doccont00.core.bulk.AccertamentoBulk#initialize(it.cnr.jada.util.action.CRUDBP, it.cnr.jada.action.ActionContext)
	 */
	public OggettoBulk initialize(CRUDBP crudbp, ActionContext actioncontext) {
		super.initialize(crudbp, actioncontext);
		caricaAnniResidui(actioncontext);
		return this; 
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.doccont00.core.bulk.AccertamentoBulk#initializeForInsert(it.cnr.jada.util.action.CRUDBP, it.cnr.jada.action.ActionContext)
	 */
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		super.initializeForInsert(bp, context);
		setEsercizio_originale(null);
		return this; 
	}
	public Accertamento_modificaBulk getAccertamento_modifica() {
		return accertamento_modifica;
	}
	public void setAccertamento_modifica(
			Accertamento_modificaBulk accertamento_modifica) {
		this.accertamento_modifica = accertamento_modifica;
	}
	/**
	 * se sono da aggiornare i saldi in modifica perchè
	 * l'accertamento non proviene da modifiche in documenti
	 * amministrativi, dato che i saldi verrebbero aggiornati
	 * attraverso "deferredSaldi"
	 * 
	 * @return
	 */
	public boolean isSaldiDaAggiornare() {
		return saldiDaAggiornare;
	}
	/**
	 * imposta che sono da aggiornare i saldi in modifica perchè
	 * l'accertamento non proviene da modifiche in documenti
	 * amministrativi, dato che i saldi verrebbero aggiornati
	 * attraverso "deferredSaldi"
	 * 
	 */
	public void setSaldiDaAggiornare(boolean saldiDaAggiornare) {
		this.saldiDaAggiornare = saldiDaAggiornare;
	}

	public void validate() throws ValidationException {
		if ( getIm_accertamento() == null )
			throw new ValidationException( "Il campo IMPORTO è obbligatorio." );

		if (this.isStatoInesigibile() || this.isStatoParzialmenteInesigibile()) {
			if (this.getIm_quota_inesigibile()==null) 
				throw new ValidationException( "Il campo QUOTA INESIGIBILE è obbligatorio." );
			if (this.getIm_quota_inesigibile().compareTo(BigDecimal.ZERO)<=0) 
				throw new ValidationException( "Il campo QUOTA INESIGIBILE deve essere positivo." );
			if (this.getIm_quota_inesigibile().compareTo(this.getImportoNonIncassato())>0)
				throw new ValidationException( "Il campo QUOTA INESIGIBILE non può essere superiore all'importo residuo da incassare ("
						+ new it.cnr.contab.util.EuroFormat().format(this.getImportoNonIncassato()) +")." );
		}
		
		super.validate();
	}
	
	public String getStato() {
		return stato;
	}
	public void setStato(String stato) {
		this.stato = stato;
	}
	@SuppressWarnings("rawtypes")
	public Dictionary getStato_AccertamentoResiduoKeys() {
		Arrays.stream(Stato.values())
				.filter(stato->Optional.ofNullable(this.getEsercizio()).map(ese->ese.compareTo(stato.from())>=0).orElse(Boolean.TRUE))
				.filter(stato->Optional.ofNullable(this.getEsercizio()).map(ese->ese.compareTo(stato.to())<=0).orElse(Boolean.TRUE))
				.forEach(stato->stato_AccertamentoResiduoKeys.put(stato.value, stato.label));
		return stato_AccertamentoResiduoKeys;
	}
	
	private java.math.BigDecimal im_quota_inesigibile;
	
	public java.math.BigDecimal getIm_quota_inesigibile() {
		return im_quota_inesigibile;
	}
	
	public void setIm_quota_inesigibile(java.math.BigDecimal im_quota_inesigibile) {
		this.im_quota_inesigibile = im_quota_inesigibile;
	}
	
	public java.math.BigDecimal getIm_quota_inesigibile_ripartita() {
		return getPdgVincoliColl().stream().map(x->x.getIm_vincolo()).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO)
				.add(getAccertamentoVincoliPerentiColl().stream().map(x->x.getIm_vincolo()).reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
	}

	public java.math.BigDecimal getIm_quota_inesigibile_da_ripartire() {
		if (getIm_quota_inesigibile_ripartita()!=null)  
			return getIm_quota_inesigibile().subtract(getIm_quota_inesigibile_ripartita());
		else 
			return getIm_quota_inesigibile();	
	}
	
	public boolean isStatoInesigibile() {
		return Stato.INESIGIBILE.value.equals(getStato());
	}

	public boolean isStatoParzialmenteInesigibile() {
		return Stato.PARZIALMENTE_INESIGIBILE.value.equals(getStato());
	}

	public boolean isStatoDubbio() {
		return Stato.DUBBIO.value.equals(getStato());
	}

	public boolean isStatoGiudizialmenteControverso() {
		return Stato.GIUDIZIALMENTE_CONTROVERSO.value.equals(getStato());
	}

	public boolean isStatoDilazionato() {
		return Stato.DILAZIONATO.value.equals(getStato());
	}

	public boolean isStatoIncerto() {
		return Stato.INCERTO.value.equals(getStato());
	}
}