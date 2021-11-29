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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.util.Dictionary;

import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.util.enumeration.TipoIVA;
import it.cnr.jada.bulk.OggettoBulk;
public class NumerazioneOrdBulk extends NumerazioneOrdBase {
	/**
	 * [UNITA_OPERATIVA_ORD Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
	/**
	 * [TIPO_OPERAZIONE_ORD Rappresenta l'anagrafica dei tipi operazione degli ordini.]
	 **/
	private TipoOperazioneOrdBulk tipoOperazioneOrd =  new TipoOperazioneOrdBulk();
	public final static String COMMERCIALE = "C";
	public final static String ISTITUZIONALE = "I";
	public final static String PROMISCUA = "P";

	public final static Dictionary TIPO;
	static{
		TIPO = new it.cnr.jada.util.OrderedHashtable();
		for (TipoIVA tipoIVA : TipoIVA.values()) {
			TIPO.put(tipoIVA.value(), tipoIVA.label());
		}
	}

	/**
	 * [TIPO_SEZIONALE Definisce l'elenco delle tipologie di sezionali in uso. Rappresenta, idealmente, il suffisso di numerazione di ogni fattura (attiva/passiva) ai fini della protocollazione IVA.
E' stata aggiunta la possibilità di una differenziazione in base all'attività commerciale svolta rilevante per le sole tipologie commerciali.
Si prevede una gestione centralizzata di tale elenco.

Di base, come richiesta utente, abbiamo:

Gestione COMMERCIALE

IVA Fatture vendita
IVA Fatture acquisti
IVA Fatture vendita intra UE
IVA Fatture acquisti intra UE
IVA per Autofatture
IVA Fatture vendita S.Marino
IVA Fatture acquisti S. Marino

Gestione ISTITUZIONALE

IVA Fatture acquisti intra UE
IVA Fatture acquisti S. Marino

E' richiesto che la selezione del tipo sezionale, in sede di inserimento fatture (attive o passive) non  sia a carico dell'utente ma proposta in automatico dal sistema.
In ogni caso è stata richiesta la possibilità di poter ridefinire, nel tempo, la configurazione dei sezionali in uso.
A queste gestioni provvedono i flag definiti (fl_ordinario, fl_san_marino_con_iva, fl_san_marino_senza_iva, fl_intraue, fl_extra_ue, fl_autofattura) che mappano gli equivalenti presenti in testata delle fatture attive e passive. In questo modo è possibile trovare l'associazione tra documento amministrativo (fattura) e sezionale corrispondente. In sede di configurazione da parte dell'utente è possibile definire un sezionale per ogni flag, un solo sezionale che presenta tutti questi flag a TRUE o varie combinazioni di questi estremi.

L'unico caso in cui si può verificare l'ipotesi che diversi sezionali (per attività commerciale) abbiano gli stessi  flag a TRUE è il caso in cui siano gestite diverse attività commerciali altrimenti l'attivazione a TRUE o FALSE di questi flag è di tipo esclusivo su record differenti ad eccezione dei seguenti flag fl_ordinario e fl_autofattura che possono, per loro natura, risultare duplicati
]
	 **/
	private Tipo_sezionaleBulk tipoSezionale =  new Tipo_sezionaleBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: NUMERAZIONE_ORD
	 **/
	public NumerazioneOrdBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: NUMERAZIONE_ORD
	 **/
	public NumerazioneOrdBulk(java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore) {
		super(cdUnitaOperativa, esercizio, cdNumeratore);
		setUnitaOperativaOrd( new UnitaOperativaOrdBulk(cdUnitaOperativa) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public UnitaOperativaOrdBulk getUnitaOperativaOrd() {
		return unitaOperativaOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	public void setUnitaOperativaOrd(UnitaOperativaOrdBulk unitaOperativaOrd)  {
		this.unitaOperativaOrd=unitaOperativaOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta l'anagrafica dei tipi operazione degli ordini.]
	 **/
	public TipoOperazioneOrdBulk getTipoOperazioneOrd() {
		return tipoOperazioneOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta l'anagrafica dei tipi operazione degli ordini.]
	 **/
	public void setTipoOperazioneOrd(TipoOperazioneOrdBulk tipoOperazioneOrd)  {
		this.tipoOperazioneOrd=tipoOperazioneOrd;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Definisce l'elenco delle tipologie di sezionali in uso. Rappresenta, idealmente, il suffisso di numerazione di ogni fattura (attiva/passiva) ai fini della protocollazione IVA.
E' stata aggiunta la possibilità di una differenziazione in base all'attività commerciale svolta rilevante per le sole tipologie commerciali.
Si prevede una gestione centralizzata di tale elenco.

Di base, come richiesta utente, abbiamo:

Gestione COMMERCIALE

IVA Fatture vendita
IVA Fatture acquisti
IVA Fatture vendita intra UE
IVA Fatture acquisti intra UE
IVA per Autofatture
IVA Fatture vendita S.Marino
IVA Fatture acquisti S. Marino

Gestione ISTITUZIONALE

IVA Fatture acquisti intra UE
IVA Fatture acquisti S. Marino

E' richiesto che la selezione del tipo sezionale, in sede di inserimento fatture (attive o passive) non  sia a carico dell'utente ma proposta in automatico dal sistema.
In ogni caso è stata richiesta la possibilità di poter ridefinire, nel tempo, la configurazione dei sezionali in uso.
A queste gestioni provvedono i flag definiti (fl_ordinario, fl_san_marino_con_iva, fl_san_marino_senza_iva, fl_intraue, fl_extra_ue, fl_autofattura) che mappano gli equivalenti presenti in testata delle fatture attive e passive. In questo modo è possibile trovare l'associazione tra documento amministrativo (fattura) e sezionale corrispondente. In sede di configurazione da parte dell'utente è possibile definire un sezionale per ogni flag, un solo sezionale che presenta tutti questi flag a TRUE o varie combinazioni di questi estremi.

L'unico caso in cui si può verificare l'ipotesi che diversi sezionali (per attività commerciale) abbiano gli stessi  flag a TRUE è il caso in cui siano gestite diverse attività commerciali altrimenti l'attivazione a TRUE o FALSE di questi flag è di tipo esclusivo su record differenti ad eccezione dei seguenti flag fl_ordinario e fl_autofattura che possono, per loro natura, risultare duplicati
]
	 **/
	public Tipo_sezionaleBulk getTipoSezionale() {
		return tipoSezionale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Definisce l'elenco delle tipologie di sezionali in uso. Rappresenta, idealmente, il suffisso di numerazione di ogni fattura (attiva/passiva) ai fini della protocollazione IVA.
E' stata aggiunta la possibilità di una differenziazione in base all'attività commerciale svolta rilevante per le sole tipologie commerciali.
Si prevede una gestione centralizzata di tale elenco.

Di base, come richiesta utente, abbiamo:

Gestione COMMERCIALE

IVA Fatture vendita
IVA Fatture acquisti
IVA Fatture vendita intra UE
IVA Fatture acquisti intra UE
IVA per Autofatture
IVA Fatture vendita S.Marino
IVA Fatture acquisti S. Marino

Gestione ISTITUZIONALE

IVA Fatture acquisti intra UE
IVA Fatture acquisti S. Marino

E' richiesto che la selezione del tipo sezionale, in sede di inserimento fatture (attive o passive) non  sia a carico dell'utente ma proposta in automatico dal sistema.
In ogni caso è stata richiesta la possibilità di poter ridefinire, nel tempo, la configurazione dei sezionali in uso.
A queste gestioni provvedono i flag definiti (fl_ordinario, fl_san_marino_con_iva, fl_san_marino_senza_iva, fl_intraue, fl_extra_ue, fl_autofattura) che mappano gli equivalenti presenti in testata delle fatture attive e passive. In questo modo è possibile trovare l'associazione tra documento amministrativo (fattura) e sezionale corrispondente. In sede di configurazione da parte dell'utente è possibile definire un sezionale per ogni flag, un solo sezionale che presenta tutti questi flag a TRUE o varie combinazioni di questi estremi.

L'unico caso in cui si può verificare l'ipotesi che diversi sezionali (per attività commerciale) abbiano gli stessi  flag a TRUE è il caso in cui siano gestite diverse attività commerciali altrimenti l'attivazione a TRUE o FALSE di questi flag è di tipo esclusivo su record differenti ad eccezione dei seguenti flag fl_ordinario e fl_autofattura che possono, per loro natura, risultare duplicati
]
	 **/
	public void setTipoSezionale(Tipo_sezionaleBulk tipoSezionale)  {
		this.tipoSezionale=tipoSezionale;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		UnitaOperativaOrdBulk unitaOperativaOrd = this.getUnitaOperativaOrd();
		if (unitaOperativaOrd == null)
			return null;
		return getUnitaOperativaOrd().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getUnitaOperativaOrd().setCdUnitaOperativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOperazione]
	 **/
	public java.lang.String getCdTipoOperazione() {
		TipoOperazioneOrdBulk tipoOperazioneOrd = this.getTipoOperazioneOrd();
		if (tipoOperazioneOrd == null)
			return null;
		return getTipoOperazioneOrd().getCdTipoOperazione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOperazione]
	 **/
	public void setCdTipoOperazione(java.lang.String cdTipoOperazione)  {
		this.getTipoOperazioneOrd().setCdTipoOperazione(cdTipoOperazione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoSezionale]
	 **/
	public java.lang.String getCdTipoSezionale() {
		Tipo_sezionaleBulk tipoSezionale = this.getTipoSezionale();
		if (tipoSezionale == null)
			return null;
		return getTipoSezionale().getCd_tipo_sezionale();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoSezionale]
	 **/
	public void setCdTipoSezionale(java.lang.String cdTipoSezionale)  {
		this.getTipoSezionale().setCd_tipo_sezionale(cdTipoSezionale);
	}
	public Dictionary getTi_istituz_commercKeys() {
		return NumerazioneOrdBulk.TIPO;
	}
}