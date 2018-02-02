package it.cnr.contab.bollo00.bulk;

import it.cnr.jada.persistency.Keyed;

public class Atto_bolloBase extends Atto_bolloKey implements Keyed {
	private static final long serialVersionUID = 1L;

	// ESERCIZIO DECIMAL(4,0) NOT NULL 
	private java.lang.Integer esercizio;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cdUnitaOrganizzativa;
	
	// DESCRIZIONE_ATTO VARCHAR2(300 BYTE) NOT NULL
	private java.lang.String descrizioneAtto;

	// ID_TIPO_ATTO_BOLLO NUMBER NOT NULL
	private java.lang.Integer idTipoAttoBollo;

	// NUM_DETTAGLI NUMBER(6) NOT NULL
	private java.lang.Integer numDettagli;

	// NUM_RIGHE NUMBER(6)
	private java.lang.Integer numRighe;

	// NUM_REGISTRO NUMBER(6)
	private java.lang.Integer numRegistro;

	public Atto_bolloBase() {
		super();
	}

	public Atto_bolloBase(java.lang.Integer id) {
		super(id);
	}

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}

	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}

	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa) {
		this.cdUnitaOrganizzativa = cdUnitaOrganizzativa;
	}

	public java.lang.String getDescrizioneAtto() {
		return descrizioneAtto;
	}

	public void setDescrizioneAtto(java.lang.String descrizioneAtto) {
		this.descrizioneAtto = descrizioneAtto;
	}

	public java.lang.Integer getIdTipoAttoBollo() {
		return idTipoAttoBollo;
	}

	public void setIdTipoAttoBollo(java.lang.Integer idTipoAttoBollo) {
		this.idTipoAttoBollo = idTipoAttoBollo;
	}

	public java.lang.Integer getNumDettagli() {
		return numDettagli;
	}

	public void setNumDettagli(java.lang.Integer numDettagli) {
		this.numDettagli = numDettagli;
	}

	public java.lang.Integer getNumRighe() {
		return numRighe;
	}
	
	public void setNumRighe(java.lang.Integer numRighe) {
		this.numRighe = numRighe;
	}
	
	public java.lang.Integer getNumRegistro() {
		return numRegistro;
	}

	public void setNumRegistro(java.lang.Integer numRegistro) {
		this.numRegistro = numRegistro;
	}
}
