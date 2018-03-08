package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.persistency.Keyed;

public class Tipo_documento_genericoBase extends Tipo_documento_genericoKey implements Keyed {
	private static final long serialVersionUID = 1L;

	// CODICE VARCHAR2(3 BYTE) NOT NULL
	private java.lang.String codice;

	// DESCRIZIONE VARCHAR2(1000 BYTE) NOT NULL
	private java.lang.String descrizione;

	// TIPO_DOCUMENTO VARCHAR2(1 BYTE) NOT NULL
	private java.lang.String tipoDocumento;

	// soggetto_bollo CHAR(1) NOT NULL
	private java.lang.Boolean soggetto_bollo;

	public Tipo_documento_genericoBase() {
		super();
	}

	public Tipo_documento_genericoBase(java.lang.Integer id) {
		super(id);
	}

	public java.lang.String getCodice() {
		return codice;
	}

	public void setCodice(java.lang.String codice) {
		this.codice = codice;
	}

	public java.lang.String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(java.lang.String descrizione) {
		this.descrizione = descrizione;
	}

	public java.lang.Boolean getSoggetto_bollo() {
		return soggetto_bollo;
	}

	public void setSoggetto_bollo(java.lang.Boolean soggetto_bollo) {
		this.soggetto_bollo = soggetto_bollo;
	}

	public java.lang.String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(java.lang.String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

}
