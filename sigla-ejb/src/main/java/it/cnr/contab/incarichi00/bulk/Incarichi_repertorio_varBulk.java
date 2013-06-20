/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 24/09/2007
 */
package it.cnr.contab.incarichi00.bulk;

import it.cnr.cmisdl.model.Node;
import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.incarichi00.bulk.cmis.CMISFileAssegniRicerca;
import it.cnr.contab.incarichi00.bulk.cmis.CMISFileBorseStudio;
import it.cnr.contab.incarichi00.bulk.cmis.CMISFileIncarichi;
import it.cnr.contab.util.Utility;

import java.io.IOException;

public class Incarichi_repertorio_varBulk extends Incarichi_repertorio_varKey {
	public static final java.util.Dictionary tipo_variazioneForEnteKeys = new it.cnr.jada.util.OrderedHashtable();
	public static final java.util.Dictionary tipo_variazioneKeys = new it.cnr.jada.util.OrderedHashtable();
	public static final java.util.Dictionary tipo_variazioneForAssegniRicercaKeys = new it.cnr.jada.util.OrderedHashtable();
	public static final java.util.Dictionary tipo_variazioneMinimaKeys = new it.cnr.jada.util.OrderedHashtable();
	public static final java.util.Dictionary tipo_variazioneMinimaForAssegniRicercaKeys = new it.cnr.jada.util.OrderedHashtable();

	private Incarichi_repertorioBulk incarichi_repertorio;

//  TIPO_VARIAZIONE VARCHAR(2) NOT NULL
	private java.lang.String tipo_variazione;

//  DT_VARIAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_variazione;
 
//  DS_VARIAZIONE VARCHAR(2000) NOT NULL
	private java.lang.String ds_variazione;

//	IMPORTO_INIZIALE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_lordo;
 
//  IMPORTO_COMPLESSIVO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal importo_complessivo;

//  DT_FINE_VALIDITA TIMESTAMP(7)
	private java.sql.Timestamp dt_fine_validita;

	final public static String TIPO_INTEGRAZIONE_INCARICO_TRANS = "T";
	final public static String TIPO_INTEGRAZIONE_INCARICO = "I";
	final public static String TIPO_VARIAZIONE_CONTRIBUTI = "C";
	final public static String TIPO_VARIAZIONE_GENERICA = "G";
	final public static String TIPO_VARIAZIONE_MATERNITA = "M";

	static {
		tipo_variazioneMinimaKeys.put(TIPO_VARIAZIONE_CONTRIBUTI,"Adeguamento Incremento Aliquote");

		tipo_variazioneMinimaForAssegniRicercaKeys.put(TIPO_VARIAZIONE_CONTRIBUTI,"Adeguamento Incremento Aliquote");
		tipo_variazioneMinimaForAssegniRicercaKeys.put(TIPO_VARIAZIONE_MATERNITA,"Variazione per Maternità");

		tipo_variazioneKeys.put(TIPO_INTEGRAZIONE_INCARICO_TRANS,"Periodo transitorio - Adeguamento alla durata del progetto");
		tipo_variazioneKeys.put(TIPO_VARIAZIONE_CONTRIBUTI,"Adeguamento Incremento Aliquote");

		tipo_variazioneForAssegniRicercaKeys.put(TIPO_INTEGRAZIONE_INCARICO_TRANS,"Periodo transitorio - Adeguamento alla durata del progetto");
		tipo_variazioneForAssegniRicercaKeys.put(TIPO_VARIAZIONE_CONTRIBUTI,"Adeguamento Incremento Aliquote");
		tipo_variazioneForAssegniRicercaKeys.put(TIPO_VARIAZIONE_MATERNITA,"Variazione per Maternità");

		tipo_variazioneForEnteKeys.put(TIPO_INTEGRAZIONE_INCARICO_TRANS,"Periodo transitorio - Adeguamento alla durata del progetto");
		tipo_variazioneForEnteKeys.put(TIPO_INTEGRAZIONE_INCARICO,"Adeguamento Incarico");
		tipo_variazioneForEnteKeys.put(TIPO_VARIAZIONE_CONTRIBUTI,"Adeguamento Incremento Aliquote");
		tipo_variazioneForEnteKeys.put(TIPO_VARIAZIONE_MATERNITA,"Variazione per Maternità");
		tipo_variazioneForEnteKeys.put(TIPO_VARIAZIONE_GENERICA,"Variazione Generica");
	}

	public Incarichi_repertorio_varBulk() {
		super();
	}
	public Incarichi_repertorio_varBulk(java.lang.Integer esercizio, java.lang.Long pg_repertorio, java.lang.Long progressivo_riga) {
		super(esercizio, pg_repertorio, progressivo_riga);
		setIncarichi_repertorio(new Incarichi_repertorioBulk(esercizio,pg_repertorio));
	}
	public Incarichi_repertorioBulk getIncarichi_repertorio() {
		return incarichi_repertorio;
	}
	public void setIncarichi_repertorio(
			Incarichi_repertorioBulk incarichi_repertorio) {
		this.incarichi_repertorio = incarichi_repertorio;
	}
	public Integer getEsercizio() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getEsercizio();
	}
	public void setEsercizio(Integer esercizio) {
		if (this.getIncarichi_repertorio() != null)
			this.getIncarichi_repertorio().setEsercizio(esercizio);
	}	
	public Long getPg_repertorio() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getPg_repertorio();
	}
	public void setPg_repertorio(Long pg_archivio) {
		if (this.getIncarichi_repertorio() != null)
			this.getIncarichi_repertorio().setPg_repertorio(pg_archivio);
	}	
	public Integer getFaseProcesso() {
		if (getIncarichi_repertorio() == null ||
	        getIncarichi_repertorio().getIncarichi_procedura() == null ||
	        getIncarichi_repertorio().getIncarichi_procedura().getFaseProcesso() == null)
			return null;
		return getIncarichi_repertorio().getIncarichi_procedura().getFaseProcesso();
	}
	public java.lang.String getTipo_variazione() {
		return tipo_variazione;
	}
	public void setTipo_variazione(java.lang.String tipo_variazione) {
		this.tipo_variazione = tipo_variazione;
	}
	public java.sql.Timestamp getDt_variazione() {
		return dt_variazione;
	}
	public void setDt_variazione(java.sql.Timestamp dt_variazione) {
		this.dt_variazione = dt_variazione;
	}
	public java.lang.String getDs_variazione() {
		return ds_variazione;
	}
	public void setDs_variazione(java.lang.String ds_variazione) {
		this.ds_variazione = ds_variazione;
	}
	public java.sql.Timestamp getDt_fine_validita() {
		return dt_fine_validita;
	}
	public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
		this.dt_fine_validita = dt_fine_validita;
	}
	public java.math.BigDecimal getImporto_lordo() {
		return importo_lordo;
	}
	public void setImporto_lordo(java.math.BigDecimal importo_lordo)  {
		this.importo_lordo=importo_lordo;
	}
	public java.math.BigDecimal getImporto_complessivo() {
		return importo_complessivo;
	}
	public void setImporto_complessivo(java.math.BigDecimal importo_complessivo)  {
		this.importo_complessivo=importo_complessivo;
	}
    public boolean isIntegrazioneIncarico() {
		return isAllegatoValido() && getTipo_archivio() != null && getTipo_archivio().equals(TIPO_ALLEGATO_CONTRATTO);
    }
    public boolean isVariazioneIntegrazioneIncarico() {
		return isVariazioneIntegrazioneIncaricoTransitorio() || isVariazioneIntegrazioneIncaricoGenerico();
    }
    public boolean isVariazioneIntegrazioneIncaricoTransitorio() {
		return isAllegatoValido() && getTipo_variazione() != null && getTipo_variazione().equals(TIPO_INTEGRAZIONE_INCARICO_TRANS);
    }
    public boolean isVariazioneIntegrazioneIncaricoGenerico() {
		return isAllegatoValido() && getTipo_variazione() != null && getTipo_variazione().equals(TIPO_INTEGRAZIONE_INCARICO);
    }
    public boolean isVariazioneIntegrazioneContributi() {
		return isAllegatoValido() && getTipo_variazione() != null && getTipo_variazione().equals(TIPO_VARIAZIONE_CONTRIBUTI);
    }
    public boolean isVariazioneIntegrazioneMaternita() {
		return isAllegatoValido() && getTipo_variazione() != null && getTipo_variazione().equals(TIPO_VARIAZIONE_MATERNITA);
    }
    public boolean isVariazioneIntegrazioneGenerica() {
		return isAllegatoValido() && getTipo_variazione() != null && getTipo_variazione().equals(TIPO_VARIAZIONE_GENERICA);
    }
	public boolean isROImportoLordo() {
		return !isVariazioneIntegrazioneIncarico();
	}
	public boolean isROImportoComplessivo() {
		return isVariazioneIntegrazioneIncarico();
	}
	public boolean isRODataFineValidita() {
		return !isVariazioneIntegrazioneIncarico();
	}
	public boolean isFileRequired() {
		return isVariazioneIntegrazioneIncaricoTransitorio() && isDefinitivo();
	}
	public Integer getEsercizioProcedura() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getEsercizio_procedura();
	}
	public Long getPg_procedura() {
		if (this.getIncarichi_repertorio() == null)
			return null;
		return this.getIncarichi_repertorio().getPg_procedura();
	}
	public CMISFile getCMISFile() throws IOException{
		CMISFile cmisFile=null;
		if (this.getIncarichi_repertorio()!=null && 
			this.getIncarichi_repertorio().getIncarichi_procedura()!=null) {
			if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForBorseStudio()) {
				if (this.getFile()==null)
					cmisFile = new CMISFileBorseStudio(this);
				else
					cmisFile = new CMISFileBorseStudio(this.getFile(), this.getNome_file(), this);
			} else if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForAssegniRicerca()) {
				if (this.getFile()==null)
					cmisFile = new CMISFileAssegniRicerca(this);
				else
					cmisFile = new CMISFileAssegniRicerca(this.getFile(), this.getNome_file(), this);
			}
		}
		if (cmisFile==null) {
			if (this.getFile()==null)
				cmisFile = new CMISFileIncarichi(this);
			else
				cmisFile = new CMISFileIncarichi(this.getFile(), this.getNome_file(), this);
		}
		return cmisFile;
	}
	public CMISFile getCMISFile(Node node) {
		if (this.getIncarichi_repertorio()!=null && 
			this.getIncarichi_repertorio().getIncarichi_procedura()!=null) {
			if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForBorseStudio())
				return new CMISFileBorseStudio(node, this);
			else if (this.getIncarichi_repertorio().getIncarichi_procedura().isProceduraForAssegniRicerca())
				return new CMISFileAssegniRicerca(node, this);
		}
		return new CMISFileIncarichi(node, this);
	}
	public String constructCMISNomeFile() {
		StringBuffer nomeFile = new StringBuffer();
		nomeFile = nomeFile.append(super.constructCMISNomeFile());
		nomeFile = nomeFile.append("-"+this.getIncarichi_repertorio().getCd_unita_organizzativa());
		nomeFile = nomeFile.append("-"+this.getEsercizio().toString()+Utility.lpad(this.getPg_repertorio().toString(),9,'0'));
		nomeFile = nomeFile.append("."+(this.getNome_file()!=null?this.getNome_file():Incarichi_archivioBulk.getTipo_archivioKeys().get(this.getTipo_archivio()).toString()+".txt"));
		return nomeFile.toString();
	}
}