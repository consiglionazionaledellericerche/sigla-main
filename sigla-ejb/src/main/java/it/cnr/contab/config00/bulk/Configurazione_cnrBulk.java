package it.cnr.contab.config00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Configurazione_cnrBulk extends Configurazione_cnrBase {

	public final static String PK_TERZO_SPECIALE = "TERZO_SPECIALE";
	public final static String SK_CODICE_DIVERSI_PGIRO = "CODICE_DIVERSI_PGIRO";
	public final static String SK_CODICE_DIVERSI_IMPEGNI = "CODICE_DIVERSI_IMPEGNI";

	public final static String PK_LINEA_ATTIVITA_SPECIALE = "LINEA_ATTIVITA_SPECIALE";
	public final static String SK_LINEA_COMUNE_VERSAMENTO_IVA = "LINEA_COMUNE_VERSAMENTO_IVA";
	
	public final static String SK_LINEA_ATTIVITA_ENTRATA_ENTE = "LINEA_ATTIVITA_ENTRATA_ENTE";
	public final static String SK_LINEA_ATTIVITA_SPESA_ENTE = "LINEA_ATTIVITA_SPESA_ENTE";

	public final static String PK_CD_DIVISA = "CD_DIVISA";
	public final static String SK_EURO = "EURO";

	public final static String PK_ELEMENTO_VOCE_SPECIALE = "ELEMENTO_VOCE_SPECIALE";
	public final static String SK_VOCE_IVA_FATTURA_ESTERA = "VOCE_IVA_FATTURA_ESTERA";

	public final static String PK_CONTO_CORRENTE_SPECIALE = "CONTO_CORRENTE_SPECIALE";
	public final static String SK_ENTE = "ENTE";
	public final static String SK_BANCA_ITALIA = "BANCA_ITALIA";

	public final static String PK_UO_SPECIALE = "UO_SPECIALE";
	public final static String SK_UO_ACCREDITAMENTO_SAC = "UO_ACCREDITAMENTO_SAC";
	public final static String SK_UO_DISTINTA_SAC = "UO_DISTINTA_SAC";
	
	public final static String PK_PDG_VARIAZIONE = "PDG_VARIAZIONE";
	public final static String SK_TIPO_VAR_APPROVA_CDS = "TIPO_VAR_APPROVA_CDS";

	public final static String PK_VAR_STANZ_RES = "VAR_STANZ_RES";
	public final static String SK_TIPO_VAR_APPROVA_CNR = "TIPO_VAR_APPROVA_CNR";
	
	public final static String PK_CDR_SPECIALE = "CDR_SPECIALE";
	public final static String SK_CDR_PERSONALE = "CDR_PERSONALE";

	public final static String PK_CODICE_SIOPE_DEFAULT = "CODICE_SIOPE_DEFAULT";
	public final static String SK_MANDATO_ACCREDITAMENTO = "MANDATO_ACCREDITAMENTO";
	public final static String SK_REVERSALE_TRASFERIMENTO = "REVERSALE_TRASFERIMENTO";

	public final static String PK_LIMITE_COLL_MERAMENTE_OCCASIONALI = "LIMITE_COLL_MERAMENTE_OCCASIONALI";
	
	public final static String PK_COSTANTI = "COSTANTI";
	public final static String SK_TOTALE_GIORNI_LAVORATIVI_COSTI_PERSONALE = "TOTALE_GIORNI_LAVORATIVI_COSTI_PERSONALE";

	public final static String PK_ANNI_RESIDUI_VAR_ST_RES = "ANNI_RESIDUI_VAR_ST_RES";
	public final static String PK_ANNI_RESIDUI_IM_RES_IMP = "ANNI_RESIDUI_IM_RES_IMP";
	public final static String PK_ANNI_RESIDUI_IM_RES_PRO = "ANNI_RESIDUI_IM_RES_PRO";
	public final static String SK_MODELLO_INTRA_12="MODELLO_INTRA_12";
	public final static String SK_MODELLO_INTRASTAT="MODELLO_INTRASTAT";
	public final static String PK_INCARICHI_MODIFICA_ALLEGATI = "INCARICHI_MODIFICA_ALLEGATI";
	public final static String SK_INCARICHI_MOD_CONTRATTO = "INCARICHI_MOD_CONTRATTO";
	
	public final static String PK_LIMITE_UTILIZZO_CONTANTI = "LIMITE_UTILIZZO_CONTANTI";
	public final static String SK_LIMITE1 = "LIMITE1";

	public Configurazione_cnrBulk() {
	super();
}
public Configurazione_cnrBulk(java.lang.String cd_chiave_primaria,java.lang.String cd_chiave_secondaria,java.lang.String cd_unita_funzionale,java.lang.Integer esercizio) {
	super(cd_chiave_primaria,cd_chiave_secondaria,cd_unita_funzionale,esercizio);
}
}
