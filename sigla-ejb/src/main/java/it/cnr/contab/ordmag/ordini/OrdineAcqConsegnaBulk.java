/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/06/2017
 */
package it.cnr.contab.ordmag.ordini;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
public class OrdineAcqConsegnaBulk extends OrdineAcqConsegnaBase {
	/**
	 * [ORDINE_ACQ_RIGA Riga Ordine d'Acquisto]
	 **/
	private OrdineAcqRigaBulk ordineAcqRiga =  new OrdineAcqRigaBulk();
	/**
	 * [MAGAZZINO Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	private MagazzinoBulk magazzino =  new MagazzinoBulk();
	/**
	 * [UNITA_OPERATIVA_ORD Rappresenta le unità operative utilizzate in gestione ordine e magazzino.]
	 **/
	private UnitaOperativaOrdBulk unitaOperativaOrd =  new UnitaOperativaOrdBulk();
	/**
	 * [OBBLIGAZIONE_SCADENZARIO Rappresenta l'articolazione in scadenze di una obbligazione. Per ogni obbligazione esiste almeno una scadenza.

La sommatoria degli importi delle scadenze deve corrispodere all'importo in testata dell'obbligazione.

E' il livello dell'obbligazione di riferimento per l'associazione sia con i documenti amministrativi che con i documenti autorizzatori (mandati).
Regole di funzionamento.
1) La relazione con i documenti amministrativi si esprime solo a livello di OBBLIGAZIONE_SCADENZARIO. Ad una scadenza possono essere associate più righe di dettaglio a condizione che appartengano tutte ad uno stesso documento amministrativo, unica eccezione il caso di note di accredito o di addebito. Quando si realizza questo collegamento l'importo della scadenza deve coincidere con la somma algebrica delle righe di documenti amministrativi associati.
Eccezione è data anche dalla gestione 1210 per le fatture passive dove, al ricevimento del sospeso dalla banca, la scadenza dell'obbligazione deve coincidere con il controvalore euro della contabile della banca mantenendo inalterato il valore della fattura stessa.
2) La relazione con i documenti contabili di tipo autorizzatorio (mandati) si esprime solo a livello di OBBLIGAZIONE_SCADENZARIO. Un mandato deve estinguere completamente l'importo di una scadenza obbligazione; non è prevista l''associazione parziale.

Gestione speciale è data per gli impegni CNR che operano a consumo sulla disponibilità della scadenza dell'obbligazione che pertanto può essere associata a diversi mandati nel tempo.]
	 **/
	private Obbligazione_scadenzarioBulk obbligazioneScadenzario =  new Obbligazione_scadenzarioBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ORDINE_ACQ_CONSEGNA
	 **/
	public OrdineAcqConsegnaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ORDINE_ACQ_CONSEGNA
	 **/
	public OrdineAcqConsegnaBulk(java.lang.String cdCds, java.lang.String cdUnitaOperativa, java.lang.Integer esercizio, java.lang.String cdNumeratore, java.lang.Integer numero, java.lang.Integer riga, java.lang.Integer consegna) {
		super(cdCds, cdUnitaOperativa, esercizio, cdNumeratore, numero, riga, consegna);
		setOrdineAcqRiga( new OrdineAcqRigaBulk(cdCds,cdUnitaOperativa,esercizio,cdNumeratore,numero,riga) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Riga Ordine d'Acquisto]
	 **/
	public OrdineAcqRigaBulk getOrdineAcqRiga() {
		return ordineAcqRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Riga Ordine d'Acquisto]
	 **/
	public void setOrdineAcqRiga(OrdineAcqRigaBulk ordineAcqRiga)  {
		this.ordineAcqRiga=ordineAcqRiga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	public MagazzinoBulk getMagazzino() {
		return magazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta i magazzini utilizzati in gestione ordine e magazzino.]
	 **/
	public void setMagazzino(MagazzinoBulk magazzino)  {
		this.magazzino=magazzino;
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
	 * Restituisce il valore di: [Rappresenta l'articolazione in scadenze di una obbligazione. Per ogni obbligazione esiste almeno una scadenza.

La sommatoria degli importi delle scadenze deve corrispodere all'importo in testata dell'obbligazione.

E' il livello dell'obbligazione di riferimento per l'associazione sia con i documenti amministrativi che con i documenti autorizzatori (mandati).
Regole di funzionamento.
1) La relazione con i documenti amministrativi si esprime solo a livello di OBBLIGAZIONE_SCADENZARIO. Ad una scadenza possono essere associate più righe di dettaglio a condizione che appartengano tutte ad uno stesso documento amministrativo, unica eccezione il caso di note di accredito o di addebito. Quando si realizza questo collegamento l'importo della scadenza deve coincidere con la somma algebrica delle righe di documenti amministrativi associati.
Eccezione è data anche dalla gestione 1210 per le fatture passive dove, al ricevimento del sospeso dalla banca, la scadenza dell'obbligazione deve coincidere con il controvalore euro della contabile della banca mantenendo inalterato il valore della fattura stessa.
2) La relazione con i documenti contabili di tipo autorizzatorio (mandati) si esprime solo a livello di OBBLIGAZIONE_SCADENZARIO. Un mandato deve estinguere completamente l'importo di una scadenza obbligazione; non è prevista l''associazione parziale.

Gestione speciale è data per gli impegni CNR che operano a consumo sulla disponibilità della scadenza dell'obbligazione che pertanto può essere associata a diversi mandati nel tempo.]
	 **/
	public Obbligazione_scadenzarioBulk getObbligazioneScadenzario() {
		return obbligazioneScadenzario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresenta l'articolazione in scadenze di una obbligazione. Per ogni obbligazione esiste almeno una scadenza.

La sommatoria degli importi delle scadenze deve corrispodere all'importo in testata dell'obbligazione.

E' il livello dell'obbligazione di riferimento per l'associazione sia con i documenti amministrativi che con i documenti autorizzatori (mandati).
Regole di funzionamento.
1) La relazione con i documenti amministrativi si esprime solo a livello di OBBLIGAZIONE_SCADENZARIO. Ad una scadenza possono essere associate più righe di dettaglio a condizione che appartengano tutte ad uno stesso documento amministrativo, unica eccezione il caso di note di accredito o di addebito. Quando si realizza questo collegamento l'importo della scadenza deve coincidere con la somma algebrica delle righe di documenti amministrativi associati.
Eccezione è data anche dalla gestione 1210 per le fatture passive dove, al ricevimento del sospeso dalla banca, la scadenza dell'obbligazione deve coincidere con il controvalore euro della contabile della banca mantenendo inalterato il valore della fattura stessa.
2) La relazione con i documenti contabili di tipo autorizzatorio (mandati) si esprime solo a livello di OBBLIGAZIONE_SCADENZARIO. Un mandato deve estinguere completamente l'importo di una scadenza obbligazione; non è prevista l''associazione parziale.

Gestione speciale è data per gli impegni CNR che operano a consumo sulla disponibilità della scadenza dell'obbligazione che pertanto può essere associata a diversi mandati nel tempo.]
	 **/
	public void setObbligazioneScadenzario(Obbligazione_scadenzarioBulk obbligazioneScadenzario)  {
		this.obbligazioneScadenzario=obbligazioneScadenzario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.getOrdineAcqRiga().setCdCds(cdCds);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.getOrdineAcqRiga().setCdUnitaOperativa(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.getOrdineAcqRiga().setEsercizio(esercizio);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratore]
	 **/
	public java.lang.String getCdNumeratore() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getCdNumeratore();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratore]
	 **/
	public void setCdNumeratore(java.lang.String cdNumeratore)  {
		this.getOrdineAcqRiga().setCdNumeratore(cdNumeratore);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [numero]
	 **/
	public java.lang.Integer getNumero() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getNumero();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [numero]
	 **/
	public void setNumero(java.lang.Integer numero)  {
		this.getOrdineAcqRiga().setNumero(numero);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [riga]
	 **/
	public java.lang.Integer getRiga() {
		OrdineAcqRigaBulk ordineAcqRiga = this.getOrdineAcqRiga();
		if (ordineAcqRiga == null)
			return null;
		return getOrdineAcqRiga().getRiga();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [riga]
	 **/
	public void setRiga(java.lang.Integer riga)  {
		this.getOrdineAcqRiga().setRiga(riga);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsMag]
	 **/
	public java.lang.String getCdCdsMag() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdCds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsMag]
	 **/
	public void setCdCdsMag(java.lang.String cdCdsMag)  {
		this.getMagazzino().setCdCds(cdCdsMag);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public java.lang.String getCdMagazzino() {
		MagazzinoBulk magazzino = this.getMagazzino();
		if (magazzino == null)
			return null;
		return getMagazzino().getCdMagazzino();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(java.lang.String cdMagazzino)  {
		this.getMagazzino().setCdMagazzino(cdMagazzino);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUopDest]
	 **/
	public java.lang.String getCdUopDest() {
		UnitaOperativaOrdBulk unitaOperativaOrd = this.getUnitaOperativaOrd();
		if (unitaOperativaOrd == null)
			return null;
		return getUnitaOperativaOrd().getCdUnitaOperativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUopDest]
	 **/
	public void setCdUopDest(java.lang.String cdUopDest)  {
		this.getUnitaOperativaOrd().setCdUnitaOperativa(cdUopDest);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdsObbl]
	 **/
	public java.lang.String getCdCdsObbl() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getCd_cds();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsObbl]
	 **/
	public void setCdCdsObbl(java.lang.String cdCdsObbl)  {
		this.getObbligazioneScadenzario().setCd_cds(cdCdsObbl);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioObbl]
	 **/
	public java.lang.Integer getEsercizioObbl() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getEsercizio();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioObbl]
	 **/
	public void setEsercizioObbl(java.lang.Integer esercizioObbl)  {
		this.getObbligazioneScadenzario().setEsercizio(esercizioObbl);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOrigObbl]
	 **/
	public java.lang.Integer getEsercizioOrigObbl() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getEsercizio_originale();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOrigObbl]
	 **/
	public void setEsercizioOrigObbl(java.lang.Integer esercizioOrigObbl)  {
		this.getObbligazioneScadenzario().setEsercizio_originale(esercizioOrigObbl);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazione]
	 **/
	public java.lang.Long getPgObbligazione() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getPg_obbligazione();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazione]
	 **/
	public void setPgObbligazione(java.lang.Long pgObbligazione)  {
		this.getObbligazioneScadenzario().setPg_obbligazione(pgObbligazione);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazioneScad]
	 **/
	public java.lang.Long getPgObbligazioneScad() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getPg_obbligazione_scadenzario();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazioneScad]
	 **/
	public void setPgObbligazioneScad(java.lang.Long pgObbligazioneScad)  {
		this.getObbligazioneScadenzario().setPg_obbligazione_scadenzario(pgObbligazioneScad);
	}
}