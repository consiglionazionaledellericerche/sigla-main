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
import java.util.Optional;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class VRichiestaPerOrdiniBulk extends VRichiestaPerOrdiniBase {
    public final static String STATO_INSERITO= "INS";
    public final static String STATO_ANNULLATO= "ANN";
	/**
	 * [RICHIESTA_UOP Testata Richieste]
	 **/
	private java.math.BigDecimal quantitaAutorizzata;

	private java.lang.String notaUopDest;

	private String allegatiDocumentale;
	/**
	 * [BENE_SERVIZIO Rappresenta la classificazione di beni e servizi il cui dettaglio è esposto in sede di registrazione delle righe fattura passiva.

Da questa gestione sono ricavati gli elementi per la gestione di magazziono e di inventario dalla registrazione di fatture passive]
	 **/
	private Bene_servizioBulk beneServizio =  new Bene_servizioBulk();
	private Bene_servizioBulk beneServizioDef =  new Bene_servizioBulk();
	/**
	 * [CATEGORIA_GRUPPO_INVENT Definisce le categorie ed i relativi gruppi di beni associabili ad un record della tabella BENE_SERVIZIO. Tale gestione si applica

L'entità è definita su due livelli obbligatori:
1 livello = Categoria inventariale
2 livello = Gruppo di appartenenza del bene nell'ambito di una data categoria inventariale

Ogni categoria deve essere associata ad un capitolo del piano dei conti  economico-finanziario (qualsiasi livello anche il solo titolo) per l'individuazione del range delle possibili voci contabili associabili ad un documento che si riferisce a beni inventariabili. Tale associazione rileva solo in sede di collegamento delle righe di una fattura passiva ad una scadenza di obbligazione; le voci contabili presenti in OBBLIGAZIONE_SCAD_VOCE (collegata a OBBLIGAZIONE_SCADENZARIO) devono essere compatibili con quanto indicato sulla categoria inventariale. Si intende che le componenti il capitolo associabili sono solo quelle di spesa riferite ad un CdS non C.N.R.
]
	 **/
	private Categoria_gruppo_inventBulk categoriaGruppoInvent =  new Categoria_gruppo_inventBulk();
	/**
	 * [UNITA_MISURA Rappresenta l'anagrafica delle unità di misura.]
	 **/
	private UnitaMisuraBulk unitaMisura =  new UnitaMisuraBulk();
	/**
	 * [OBBLIGAZIONE Rappresenta la testata di una obbligazione; il concetto è gestito sia per il CNR (impegni) che per i CdS.

Sono sempre utilizzati tutti e tre i livelli in cui si articola l'obbligazione; una OBBLIGAZIONE,  più riferimenti a scadenze in OBBLIGAZIONE_SCADENZARIO e, per ciascuno di questi, più occorrenze in OBBLIGAZIONE_SCAD_VOCE.
In genere sono presenti più occorrenza in OBBLIGAZIONE_SCAD_VOCE per ogni scadenza.
Nel caso di partita di giro è sempre presente una relazione uno a uno tra i diversi concetti in cui è articolata l'obbligazione; allo stesso modo sono gestiti gli impegni CNR

Nella terna di tabelle relative alle obbligazioni sono gestiti anche i residui (solo CNR). Si prevede di estendere tale gestione (riporto a nuovo esercizio del delta non associato a mandati) anche per le obbligazioni del CdS se queste risultano non pagate al termine dell''esercizio contabile.
]
	 **/
	private ObbligazioneBulk obbligazione =  new ObbligazioneBulk();
	/**
	 * [LINEA_ATTIVITA Linea di attività definita per CDR]
	 **/
	private WorkpackageBulk lineaAttivita = new WorkpackageBulk();
	private CdrBulk centroResponsabilita = new CdrBulk();
	private ProgettoBulk progetto = new ProgettoBulk();
	/**
	 * [ELEMENTO_VOCE Contiene l'anagrafica dei capitoli.
Tale anagrafica viene utilizzata sia per il PDC Finanziario CNR che per quello CDS.
In parte questa tabella risulta precaricata con dati cablati.

Per parte spese CDS:
Parte I e Titolo cablati
Il Capitolo viene aggiunto dall"utente

Per parte entrate CDS:
Titolo cablato.
Il Capitolo viene aggiunto dall"utente

Parte spese CNR:
Parte I e Titolo cablati
Capitolo definito dall"utente per la categoria 2 (SAC)

Parte entrate CNR:
Titolo cablato
Categoria definita dall"utente collegata a Titolo
Capitolo definito dall"utente collegato a Categoria

]
	 **/
	private Elemento_voceBulk elementoVoce =  new Elemento_voceBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RICHIESTA_UOP_RIGA
	 **/
	public VRichiestaPerOrdiniBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RICHIESTA_UOP_RIGA
	 **/
	public VRichiestaPerOrdiniBulk(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero, java.lang.Integer riga) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero, riga);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta la classificazione di beni e servizi il cui dettaglio è esposto in sede di registrazione delle righe fattura passiva.

Da questa gestione sono ricavati gli elementi per la gestione di magazziono e di inventario dalla registrazione di fatture passive]
	 **/
	public Bene_servizioBulk getBeneServizio() {
		return beneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta la classificazione di beni e servizi il cui dettaglio è esposto in sede di registrazione delle righe fattura passiva.

Da questa gestione sono ricavati gli elementi per la gestione di magazziono e di inventario dalla registrazione di fatture passive]
	 **/
	public void setBeneServizio(Bene_servizioBulk beneServizio)  {
		this.beneServizio=beneServizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Definisce le categorie ed i relativi gruppi di beni associabili ad un record della tabella BENE_SERVIZIO. Tale gestione si applica

L'entità è definita su due livelli obbligatori:
1 livello = Categoria inventariale
2 livello = Gruppo di appartenenza del bene nell'ambito di una data categoria inventariale

Ogni categoria deve essere associata ad un capitolo del piano dei conti  economico-finanziario (qualsiasi livello anche il solo titolo) per l'individuazione del range delle possibili voci contabili associabili ad un documento che si riferisce a beni inventariabili. Tale associazione rileva solo in sede di collegamento delle righe di una fattura passiva ad una scadenza di obbligazione; le voci contabili presenti in OBBLIGAZIONE_SCAD_VOCE (collegata a OBBLIGAZIONE_SCADENZARIO) devono essere compatibili con quanto indicato sulla categoria inventariale. Si intende che le componenti il capitolo associabili sono solo quelle di spesa riferite ad un CdS non C.N.R.
]
	 **/
	public Categoria_gruppo_inventBulk getCategoriaGruppoInvent() {
		return categoriaGruppoInvent;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Definisce le categorie ed i relativi gruppi di beni associabili ad un record della tabella BENE_SERVIZIO. Tale gestione si applica

L'entità è definita su due livelli obbligatori:
1 livello = Categoria inventariale
2 livello = Gruppo di appartenenza del bene nell'ambito di una data categoria inventariale

Ogni categoria deve essere associata ad un capitolo del piano dei conti  economico-finanziario (qualsiasi livello anche il solo titolo) per l'individuazione del range delle possibili voci contabili associabili ad un documento che si riferisce a beni inventariabili. Tale associazione rileva solo in sede di collegamento delle righe di una fattura passiva ad una scadenza di obbligazione; le voci contabili presenti in OBBLIGAZIONE_SCAD_VOCE (collegata a OBBLIGAZIONE_SCADENZARIO) devono essere compatibili con quanto indicato sulla categoria inventariale. Si intende che le componenti il capitolo associabili sono solo quelle di spesa riferite ad un CdS non C.N.R.
]
	 **/
	public void setCategoriaGruppoInvent(Categoria_gruppo_inventBulk categoriaGruppoInvent)  {
		this.categoriaGruppoInvent=categoriaGruppoInvent;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta l'anagrafica delle unità di misura.]
	 **/
	public UnitaMisuraBulk getUnitaMisura() {
		return unitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta l'anagrafica delle unità di misura.]
	 **/
	public void setUnitaMisura(UnitaMisuraBulk unitaMisura)  {
		this.unitaMisura=unitaMisura;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta la testata di una obbligazione; il concetto è gestito sia per il CNR (impegni) che per i CdS.

Sono sempre utilizzati tutti e tre i livelli in cui si articola l'obbligazione; una OBBLIGAZIONE,  più riferimenti a scadenze in OBBLIGAZIONE_SCADENZARIO e, per ciascuno di questi, più occorrenze in OBBLIGAZIONE_SCAD_VOCE.
In genere sono presenti più occorrenza in OBBLIGAZIONE_SCAD_VOCE per ogni scadenza.
Nel caso di partita di giro è sempre presente una relazione uno a uno tra i diversi concetti in cui è articolata l'obbligazione; allo stesso modo sono gestiti gli impegni CNR

Nella terna di tabelle relative alle obbligazioni sono gestiti anche i residui (solo CNR). Si prevede di estendere tale gestione (riporto a nuovo esercizio del delta non associato a mandati) anche per le obbligazioni del CdS se queste risultano non pagate al termine dell''esercizio contabile.
]
	 **/
	public ObbligazioneBulk getObbligazione() {
		return obbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta la testata di una obbligazione; il concetto è gestito sia per il CNR (impegni) che per i CdS.

Sono sempre utilizzati tutti e tre i livelli in cui si articola l'obbligazione; una OBBLIGAZIONE,  più riferimenti a scadenze in OBBLIGAZIONE_SCADENZARIO e, per ciascuno di questi, più occorrenze in OBBLIGAZIONE_SCAD_VOCE.
In genere sono presenti più occorrenza in OBBLIGAZIONE_SCAD_VOCE per ogni scadenza.
Nel caso di partita di giro è sempre presente una relazione uno a uno tra i diversi concetti in cui è articolata l'obbligazione; allo stesso modo sono gestiti gli impegni CNR

Nella terna di tabelle relative alle obbligazioni sono gestiti anche i residui (solo CNR). Si prevede di estendere tale gestione (riporto a nuovo esercizio del delta non associato a mandati) anche per le obbligazioni del CdS se queste risultano non pagate al termine dell''esercizio contabile.
]
	 **/
	public void setObbligazione(ObbligazioneBulk obbligazione)  {
		this.obbligazione=obbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Linea di attività definita per CDR]
	 **/
	public WorkpackageBulk getLineaAttivita() {
		return lineaAttivita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Linea di attività definita per CDR]
	 **/
	public void setLineaAttivita(WorkpackageBulk lineaAttivita)  {
		this.lineaAttivita=lineaAttivita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Contiene l'anagrafica dei capitoli.
Tale anagrafica viene utilizzata sia per il PDC Finanziario CNR che per quello CDS.
In parte questa tabella risulta precaricata con dati cablati.

Per parte spese CDS:
Parte I e Titolo cablati
Il Capitolo viene aggiunto dall"utente

Per parte entrate CDS:
Titolo cablato.
Il Capitolo viene aggiunto dall"utente

Parte spese CNR:
Parte I e Titolo cablati
Capitolo definito dall"utente per la categoria 2 (SAC)

Parte entrate CNR:
Titolo cablato
Categoria definita dall"utente collegata a Titolo
Capitolo definito dall"utente collegato a Categoria

]
	 **/
	public Elemento_voceBulk getElementoVoce() {
		return elementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Contiene l'anagrafica dei capitoli.
Tale anagrafica viene utilizzata sia per il PDC Finanziario CNR che per quello CDS.
In parte questa tabella risulta precaricata con dati cablati.

Per parte spese CDS:
Parte I e Titolo cablati
Il Capitolo viene aggiunto dall"utente

Per parte entrate CDS:
Titolo cablato.
Il Capitolo viene aggiunto dall"utente

Parte spese CNR:
Parte I e Titolo cablati
Capitolo definito dall"utente per la categoria 2 (SAC)

Parte entrate CNR:
Titolo cablato
Categoria definita dall"utente collegata a Titolo
Capitolo definito dall"utente collegato a Categoria

]
	 **/
	public void setElementoVoce(Elemento_voceBulk elementoVoce)  {
		this.elementoVoce=elementoVoce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdBeneServizio]
	 **/
	public java.lang.String getCdBeneServizio() {
		Bene_servizioBulk beneServizio = this.getBeneServizio();
		if (beneServizio == null)
			return null;
		return getBeneServizio().getCd_bene_servizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdBeneServizio]
	 **/
	public void setCdBeneServizio(java.lang.String cdBeneServizio)  {
		this.getBeneServizio().setCd_bene_servizio(cdBeneServizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdBeneServizioDef]
	 **/
	public java.lang.String getCdBeneServizioDef() {
		Bene_servizioBulk beneServizio = this.getBeneServizio();
		if (beneServizio == null)
			return null;
		return getBeneServizio().getCd_bene_servizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdBeneServizioDef]
	 **/
	public void setCdBeneServizioDef(java.lang.String cdBeneServizioDef)  {
		this.getBeneServizio().setCd_bene_servizio(cdBeneServizioDef);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCategoriaGruppo]
	 **/
	public java.lang.String getCdCategoriaGruppo() {
		Categoria_gruppo_inventBulk categoriaGruppoInvent = this.getCategoriaGruppoInvent();
		if (categoriaGruppoInvent == null)
			return null;
		return getCategoriaGruppoInvent().getCd_categoria_gruppo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCategoriaGruppo]
	 **/
	public void setCdCategoriaGruppo(java.lang.String cdCategoriaGruppo)  {
		this.getCategoriaGruppoInvent().setCd_categoria_gruppo(cdCategoriaGruppo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaMisura]
	 **/
	public java.lang.String getCdUnitaMisura() {
		UnitaMisuraBulk unitaMisura = this.getUnitaMisura();
		if (unitaMisura == null)
			return null;
		return getUnitaMisura().getCdUnitaMisura();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaMisura]
	 **/
	public void setCdUnitaMisura(java.lang.String cdUnitaMisura)  {
		this.getUnitaMisura().setCdUnitaMisura(cdUnitaMisura);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsObbl]
	 **/
	public java.lang.String getCdCdsObbl() {
		ObbligazioneBulk obbligazione = this.getObbligazione();
		if (obbligazione == null)
			return null;
		return getObbligazione().getCd_cds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsObbl]
	 **/
	public void setCdCdsObbl(java.lang.String cdCdsObbl)  {
		this.getObbligazione().setCd_cds(cdCdsObbl);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioObbl]
	 **/
	public java.lang.Integer getEsercizioObbl() {
		ObbligazioneBulk obbligazione = this.getObbligazione();
		if (obbligazione == null)
			return null;
		return getObbligazione().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioObbl]
	 **/
	public void setEsercizioObbl(java.lang.Integer esercizioObbl)  {
		this.getObbligazione().setEsercizio(esercizioObbl);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOrigObbl]
	 **/
	public java.lang.Integer getEsercizioOrigObbl() {
		ObbligazioneBulk obbligazione = this.getObbligazione();
		if (obbligazione == null)
			return null;
		return getObbligazione().getEsercizio_originale();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrigObbl]
	 **/
	public void setEsercizioOrigObbl(java.lang.Integer esercizioOrigObbl)  {
		this.getObbligazione().setEsercizio_originale(esercizioOrigObbl);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazione]
	 **/
	public java.lang.Long getPgObbligazione() {
		ObbligazioneBulk obbligazione = this.getObbligazione();
		if (obbligazione == null)
			return null;
		return getObbligazione().getPg_obbligazione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazione]
	 **/
	public void setPgObbligazione(java.lang.Long pgObbligazione)  {
		this.getObbligazione().setPg_obbligazione(pgObbligazione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCentroResponsabilita]
	 **/
	public java.lang.String getCdCentroResponsabilita() {
		it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLineaAttivita();
		if (linea_attivita == null)
			return null;
		it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = linea_attivita.getCentro_responsabilita();
		if (centro_responsabilita == null)
			return null;
		return centro_responsabilita.getCd_centro_responsabilita();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCentroResponsabilita]
	 **/
	public void setCdCentroResponsabilita(java.lang.String cdCentroResponsabilita)  {
		it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = this.getLineaAttivita().getCentro_responsabilita();
		if (centro_responsabilita == null){
			this.getLineaAttivita().setCentro_responsabilita(new CdrBulk());
		}
		this.getLineaAttivita().setCd_centro_responsabilita(cdCentroResponsabilita);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdLineaAttivita]
	 **/
	public java.lang.String getCdLineaAttivita() {
		WorkpackageBulk lineaAttivita = this.getLineaAttivita();
		if (lineaAttivita == null)
			return null;
		return getLineaAttivita().getCd_linea_attivita();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdLineaAttivita]
	 **/
	public void setCdLineaAttivita(java.lang.String cdLineaAttivita)  {
		this.getLineaAttivita().setCd_linea_attivita(cdLineaAttivita);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioVoce]
	 **/
	public java.lang.Integer getEsercizioVoce() {
		Elemento_voceBulk elementoVoce = this.getElementoVoce();
		if (elementoVoce == null)
			return null;
		return getElementoVoce().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioVoce]
	 **/
	public void setEsercizioVoce(java.lang.Integer esercizioVoce)  {
		Optional.ofNullable(this.getElementoVoce()).ifPresent(x -> x.setEsercizio(esercizioVoce)); 
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiAppartenenza]
	 **/
	public java.lang.String getTiAppartenenza() {
		Elemento_voceBulk elementoVoce = this.getElementoVoce();
		if (elementoVoce == null)
			return null;
		return getElementoVoce().getTi_appartenenza();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiAppartenenza]
	 **/
	public void setTiAppartenenza(java.lang.String tiAppartenenza)  {
		this.getElementoVoce().setTi_appartenenza(tiAppartenenza);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiGestione]
	 **/
	public java.lang.String getTiGestione() {
		Elemento_voceBulk elementoVoce = this.getElementoVoce();
		if (elementoVoce == null)
			return null;
		return getElementoVoce().getTi_gestione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiGestione]
	 **/
	public void setTiGestione(java.lang.String tiGestione)  {
		this.getElementoVoce().setTi_gestione(tiGestione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdElementoVoce]
	 **/
	public java.lang.String getCdElementoVoce() {
		Elemento_voceBulk elementoVoce = this.getElementoVoce();
		if (elementoVoce == null)
			return null;
		return getElementoVoce().getCd_elemento_voce();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdElementoVoce]
	 **/
	public void setCdElementoVoce(java.lang.String cdElementoVoce)  {
		this.getElementoVoce().setCd_elemento_voce(cdElementoVoce);
	}
	public Bene_servizioBulk getBeneServizioDef() {
		return beneServizioDef;
	}
	public void setBeneServizioDef(Bene_servizioBulk beneServizioDef) {
		this.beneServizioDef = beneServizioDef;
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
	public boolean isROBeneServizioSearchTool() {
		return 	false;
	}
	public CdrBulk getCentroResponsabilita() {
		return centroResponsabilita;
	}
	public void setCentroResponsabilita(CdrBulk centroResponsabilita) {
		this.centroResponsabilita = centroResponsabilita;
	}
	public ProgettoBulk getProgetto() {
		return progetto;
	}
	public void setProgetto(ProgettoBulk progetto) {
		this.progetto = progetto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [quantitaAutorizzata]
	 **/
	public java.math.BigDecimal getQuantitaAutorizzata() {
		return quantitaAutorizzata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [quantitaAutorizzata]
	 **/
	public void setQuantitaAutorizzata(java.math.BigDecimal quantitaAutorizzata)  {
		this.quantitaAutorizzata=quantitaAutorizzata;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [notaUopDest]
	 **/
	public java.lang.String getNotaUopDest() {
		return notaUopDest;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [notaUopDest]
	 **/
	public void setNotaUopDest(java.lang.String notaUopDest)  {
		this.notaUopDest=notaUopDest;
	}
	public String getCategoriaGruppo() {
		if (getCdCategoriaGruppo() == null){
			return null;
		}
		return getCdCategoriaGruppo()+"-"+getDsCategoriaGruppo();
	}
	public String getAllegatiDocumentale() {
		return allegatiDocumentale;
	}
	public void setAllegatiDocumentale(String allegatiDocumentale) {
		this.allegatiDocumentale = allegatiDocumentale;
	}
}