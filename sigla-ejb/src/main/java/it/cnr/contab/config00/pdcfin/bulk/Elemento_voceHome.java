package it.cnr.contab.config00.pdcfin.bulk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import it.cnr.contab.anagraf00.tabrif.bulk.Tipologie_istatBulk;
import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Elemento_voceHome extends BulkHome implements ConsultazioniRestHome {
	private static java.util.Hashtable ti_appartenenzaKeys;
	private static java.util.Hashtable ti_gestioneKeys;
	private static java.util.Hashtable ti_elemento_voceKeys;

	public final static String APPARTENENZA_CNR = "C" ;
	public final static String APPARTENENZA_CDS = "D" ;

	public final static String GESTIONE_ENTRATE = "E" ;
	public final static String GESTIONE_SPESE   = "S" ;

	public final static String TIPO_TITOLO    	= "T" ;
	public final static String TIPO_PARTE     	= "P" ;
	public final static String TIPO_CAPITOLO  	= "C" ;
	public final static String TIPO_SEZIONE  	= "S" ;
	public final static String TIPO_CATEGORIA 	= "G" ;
	public final static String TIPO_ARTICOLO 	= "A" ;	
	
	public final static String CD_CNR_SPESE_CATEGORIA_2 = "2" ;

	public final static String PARTE_1 = "1" ;
	public final static String PARTE_2 = "2" ;		

	private static java.util.Hashtable lunghezzeChiavi;
	private static java.util.Hashtable tipiPadre;	

	protected Elemento_voceHome(Class clazz,java.sql.Connection connection) {
		super(clazz,connection);
	}
	protected Elemento_voceHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
		super(clazz,connection,persistentCache);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Costruisce un Elemento_voceHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 */
	public Elemento_voceHome(java.sql.Connection conn) {
		super(Elemento_voceBulk.class,conn);
	}
	/**
	 * <!-- @TODO: da completare -->
	 * Costruisce un Elemento_voceHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
	 */
	public Elemento_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Elemento_voceBulk.class,conn,persistentCache);
	}
	/**
	 * Genera un nuovo codice per Elemento_voceBulk calcolando il progressivo successivo al massimo esistente
	 * per l'Elemento_voceBulk
	 * @param evBulk elemento voce per cui e' necessario creare il codice
	 * @return String codice creato
	 */

	public String creaNuovoCodice( Elemento_voceBulk evBulk ) throws ApplicationException, PersistencyException
	{
		String codice;

		try
		{
			LoggableStatement ps = new LoggableStatement(getConnection(),
				"SELECT CD_PROPRIO_ELEMENTO FROM " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"ELEMENTO_VOCE " +
				"WHERE ESERCIZIO = ? AND " +
				"TI_APPARTENENZA = ? AND " +
				"TI_GESTIONE = ? AND " +
				"TI_ELEMENTO_VOCE = ? AND " +
				"CD_ELEMENTO_PADRE = ? AND " +
				"CD_PROPRIO_ELEMENTO = ( SELECT MAX(CD_PROPRIO_ELEMENTO) " +
				"FROM "+
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"ELEMENTO_VOCE WHERE " +
				"ESERCIZIO = ? AND " +
				"TI_APPARTENENZA = ? AND " +
				"TI_GESTIONE = ? AND " +
				"TI_ELEMENTO_VOCE = ? AND " +
				"CD_ELEMENTO_PADRE = ?) " +
				"FOR UPDATE NOWAIT",true ,this.getClass());
			ps.setObject( 1, evBulk.getEsercizio() );
			ps.setString( 2, evBulk.getTi_appartenenza() );
			ps.setString( 3, evBulk.getTi_gestione() );
			ps.setString( 4, evBulk.getTi_elemento_voce() );
			ps.setString( 5, evBulk.getElemento_padre().getCd_elemento_voce() );
			ps.setObject( 6, evBulk.getEsercizio() );
			ps.setString( 7, evBulk.getTi_appartenenza() );
			ps.setString( 8, evBulk.getTi_gestione() );
			ps.setString( 9, evBulk.getTi_elemento_voce() );
			ps.setString(10, evBulk.getElemento_padre().getCd_elemento_voce() );

			ResultSet rs = ps.executeQuery();
			if ( rs.next() )
			{
				codice = rs.getString( 1 );
				if ( codice != null )
				{
					long cdLong = Long.parseLong( codice ) + 1;
					codice = String.valueOf( cdLong );
				}
				else
					codice = String.valueOf( 1 );
			}
			else
				codice = String.valueOf( 1 );
			try{ps.close();}catch( java.sql.SQLException e ){};
	//		return getLunghezza_chiaviHome().create().formatCdrKey( codice, esercizio, livello );
			return codice;
		}
		catch (java.lang.NumberFormatException e)
		{
				throw new ApplicationException( "Esistono codice non numerici nel database. " );
		}
		catch ( SQLException e )
		{
				throw new PersistencyException( e );
		}

	}
	/**
	 * Formatta le chiavi (cd_elemento_voce) dell'Elemento_voceBulk in base all'appartenenza, alla gestione e al
	 * tipo di elemento voce a cui la chiave si riferisce
	 */

	public String formatKey( String key, String appartenenza, String gestione, String tipoVoce ) throws ApplicationException
	{
		if ( key == null || key.equals("") )
			return key;
		int len = getLunghezzaChiave( appartenenza, gestione, tipoVoce );
		if ( key.length() > len )
			throw new ApplicationException( "Il codice non può essere più lungo di " + len + " caratteri" );
		return leftPadding(key, len);

	}
	/**
	 * Restitusce la lunghezza della chiave di un Elemento_voceBulk in base all'appartenenza, alla gestione e al
	 * tipo di elemento voce
	 */


	public int getLunghezzaChiave( String appartenenza, String gestione, String tipo )
	{
		return ((Integer)((Hashtable)((Hashtable) getLunghezzeChiavi().get( appartenenza )).get( gestione)).get( tipo )).intValue();
	}
	/**
	 * Restitusce la hashtable contenente la lunghezza di tutte le chiavi raggruppate
	 * in base all'appartenenza, alla gestione e al tipo di elemento voce
	 */


	public java.util.Hashtable getLunghezzeChiavi()
	{
		if ( lunghezzeChiavi == null )
		{

			java.util.Hashtable lungCnrEntrate  = new java.util.Hashtable();
			lungCnrEntrate.put( TIPO_CATEGORIA, new Integer( 2 ));
			lungCnrEntrate.put( TIPO_CAPITOLO, new Integer( 12 ));

			java.util.Hashtable lungCnrSpese    = new java.util.Hashtable();
			lungCnrSpese.put( TIPO_CAPITOLO, new Integer( 3 ));

			java.util.Hashtable lungCdsEntrate  = new java.util.Hashtable();
			lungCdsEntrate.put( TIPO_CAPITOLO, new Integer( 3 ));

			java.util.Hashtable lungCdsSpese    = new java.util.Hashtable();
			lungCdsSpese.put( TIPO_CAPITOLO, new Integer( 15 ));


			lunghezzeChiavi = new java.util.Hashtable();
			java.util.Hashtable tmp = new java.util.Hashtable();
			tmp.put( GESTIONE_ENTRATE, lungCnrEntrate );
			tmp.put( GESTIONE_SPESE, lungCnrSpese);
			lunghezzeChiavi.put( APPARTENENZA_CNR, tmp );

			tmp = new java.util.Hashtable();
			tmp.put( GESTIONE_ENTRATE, lungCdsEntrate );
			tmp.put( GESTIONE_SPESE, lungCdsSpese);
			lunghezzeChiavi.put( APPARTENENZA_CDS, tmp );
		}
		return lunghezzeChiavi;
	}
	/**
	 * Restituisce il valore della proprietà 'tipiPadre'
	 *
	 * @return Il valore della proprietà 'tipiPadre'
	 */
	public static java.util.Hashtable getTipiPadre()
	{
		if ( tipiPadre == null )
		{
			tipiPadre = new java.util.Hashtable();

			java.util.Hashtable tipiCnrEntrate  = new java.util.Hashtable();
			tipiCnrEntrate.put( TIPO_CATEGORIA, TIPO_TITOLO);
			tipiCnrEntrate.put( TIPO_CAPITOLO, TIPO_CATEGORIA);

			java.util.Hashtable tipiCnrSpese    = new java.util.Hashtable();
			tipiCnrSpese.put( TIPO_CAPITOLO, TIPO_CATEGORIA);

			java.util.Hashtable tipiCdsEntrate  = new java.util.Hashtable();
			tipiCdsEntrate.put( TIPO_CAPITOLO, TIPO_TITOLO);

			java.util.Hashtable tipiCdsSpese    = new java.util.Hashtable();
			tipiCdsSpese.put( TIPO_CAPITOLO, TIPO_TITOLO);


			tipiPadre = new java.util.Hashtable();
			java.util.Hashtable tmp = new java.util.Hashtable();
			tmp.put( GESTIONE_ENTRATE, tipiCnrEntrate );
			tmp.put( GESTIONE_SPESE, tipiCnrSpese);
			tipiPadre.put( APPARTENENZA_CNR, tmp );

			tmp = new java.util.Hashtable();
			tmp.put( GESTIONE_ENTRATE, tipiCdsEntrate );
			tmp.put( GESTIONE_SPESE, tipiCdsSpese);
			tipiPadre.put( APPARTENENZA_CDS, tmp );

		}
		return tipiPadre;
	}
	/**
	 * Ritorna il tipo della voce padre dati appartenenza/gestione e tipo della voce figlio
	 *
	 * @param appartenenza Cnr/Cds
	 * @param gestione E/S
	 * @param tipo Tipo di voce
	 * @return Tipo della voce padre
	 */
	public static String getTipoPadre( String appartenenza, String gestione, String tipo )
	{
		return (String)((Hashtable)((Hashtable) getTipiPadre().get( appartenenza )).get( gestione)).get( tipo );
	}
	/**
	 * Aggiunge caratteri '0' all'inizio della stringa fino a raggiungerne la lunghezza richiesta
	 */

	public String leftPadding( String key, int keyLen )
	{
		int valueLen = key.length();

		String newKey = new String();

		for ( int i = 0; i < (keyLen - valueLen); i++ )
			newKey = newKey.concat("0");
		newKey = newKey.concat( key );
		return newKey;
	}
	/**
	 * Carica in una hashtable l'elenco di tipologie di appartenenza
	 * @return it.cnr.jada.util.OrderedHashtable
	 */

	public java.util.Hashtable loadTi_appartenenzaKeys( Elemento_voceBulk bulk )
	{
		if ( ti_appartenenzaKeys == null )
		{
			ti_appartenenzaKeys = new java.util.Hashtable();
			ti_appartenenzaKeys.put( APPARTENENZA_CNR, "C - CNR" );
			ti_appartenenzaKeys.put( APPARTENENZA_CDS, "D - CDS" );
		}
		return ti_appartenenzaKeys;
	}
	/**
	 * Carica in una hashtable l'elenco di tipologie di elemento voce
	 * @return it.cnr.jada.util.OrderedHashtable
	 */

	public java.util.Hashtable loadTi_elemento_voceKeys( Elemento_voceBulk bulk ) {
		if ( ti_elemento_voceKeys == null )
		{
			ti_elemento_voceKeys = new java.util.Hashtable();
	//		ti_elemento_voceKeys.put("T", "Titolo" );
	//		ti_elemento_voceKeys.put("P", "Parte" );
			ti_elemento_voceKeys.put( TIPO_CAPITOLO, "Capitolo" );
			ti_elemento_voceKeys.put( TIPO_CATEGORIA, "Categoria" );
		}
		return ti_elemento_voceKeys;
	}
	/**
	 * Carica in una hashtable l'elenco di tipologie di Gestione
	 * @return it.cnr.jada.util.OrderedHashtable
	 */

	public java.util.Hashtable loadTi_gestioneKeys( Elemento_voceBulk bulk ) {
		if ( ti_gestioneKeys == null )
		{
			ti_gestioneKeys = new java.util.Hashtable();
			ti_gestioneKeys.put(GESTIONE_SPESE, "S - Spese");
			ti_gestioneKeys.put(GESTIONE_ENTRATE, "E - Entrate");
		}
		return ti_gestioneKeys;
	}
	/**
	 * Reset chiavi
	 *
	 *
	 */
	public static void resetKeys()
	{
		ti_gestioneKeys = null;
		ti_appartenenzaKeys	= null;
		ti_elemento_voceKeys = null;
		tipiPadre = null;
		lunghezzeChiavi = null;
	}
	/**
	 * Restituisce il SQLBuilder per selezionare i Titoli di Spesa del Cds
	 * @param bulk bulk ricevente
	 * @param home home del bulk su cui si cerca
	 * @param bulkClause è l'istanza di bulk che ha indotto le clauses
	 * @param clause clause che arrivano dalle properties (form collegata al search tool)
	 * @return it.cnr.jada.persistency.sql.SQLBuilder
	 */



	public SQLBuilder selectCdsSpesaTitoliByClause( Elemento_voceBulk bulk,it.cnr.jada.bulk.BulkHome home,OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = home.selectByClause(clause);
		sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, APPARTENENZA_CDS );
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, GESTIONE_SPESE );
		sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, TIPO_TITOLO);
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );
		return sql;
	}
	/**
	 * Restituisce il SQLBuilder per selezionare i Titoli di Spesa del Cnr
	 * @param bulk bulk ricevente
	 * @param home home del bulk su cui si cerca
	 * @param bulkClause è l'istanza di bulk che ha indotto le clauses
	 * @param clause clause che arrivano dalle properties (form collegata al search tool)
	 * @return it.cnr.jada.persistency.sql.SQLBuilder
	 */

	public SQLBuilder selectCnrSpesaTitoliByClause( Elemento_voceBulk bulk,it.cnr.jada.bulk.BulkHome home,OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = home.selectByClause(clause);
		sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, APPARTENENZA_CNR );
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, GESTIONE_SPESE );
		sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, TIPO_TITOLO);
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );
		return sql;
	}

	/**
	 * Recupera tutti i dati nella tabella CODICI_SIOPE relativi alla voce in uso.
	 *
	 * @param elemento_voce L'elemento voce in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>Codici_siopeBulk</code>
	 */
	public java.util.Collection findCodiciCollegatiSIOPE(UserContext userContext, Elemento_voceBulk elemento_voce, Tipologie_istatBulk tipologia) throws PersistencyException {
		PersistentHome codici_siopeHome = getHomeCache().getHome(Codici_siopeBulk.class);
		SQLBuilder sql = codici_siopeHome.createSQLBuilder();
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, elemento_voce.getEsercizio());
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, elemento_voce.getTi_gestione());

		if (tipologia != null) {
			sql.addTableToHeader("ASS_TIPOLOGIA_ISTAT_SIOPE");
			sql.addSQLJoin("CODICI_SIOPE.ESERCIZIO", "ASS_TIPOLOGIA_ISTAT_SIOPE.ESERCIZIO_SIOPE");
			sql.addSQLJoin("CODICI_SIOPE.TI_GESTIONE", "ASS_TIPOLOGIA_ISTAT_SIOPE.TI_GESTIONE_SIOPE");
			sql.addSQLJoin("CODICI_SIOPE.CD_SIOPE", "ASS_TIPOLOGIA_ISTAT_SIOPE.CD_SIOPE");
			sql.addSQLClause("AND", "ASS_TIPOLOGIA_ISTAT_SIOPE.PG_TIPOLOGIA", SQLBuilder.EQUALS, tipologia.getPg_tipologia());
		}

		sql.addTableToHeader("ASS_EV_SIOPE");
		sql.addSQLJoin("CODICI_SIOPE.ESERCIZIO", "ASS_EV_SIOPE.ESERCIZIO_SIOPE");
		sql.addSQLJoin("CODICI_SIOPE.TI_GESTIONE", "ASS_EV_SIOPE.TI_GESTIONE_SIOPE");
		sql.addSQLJoin("CODICI_SIOPE.CD_SIOPE", "ASS_EV_SIOPE.CD_SIOPE");
		sql.addSQLClause("AND", "ASS_EV_SIOPE.TI_APPARTENENZA", SQLBuilder.EQUALS, elemento_voce.getTi_appartenenza());
		sql.addSQLClause("AND", "ASS_EV_SIOPE.CD_ELEMENTO_VOCE", SQLBuilder.EQUALS, elemento_voce.getCd_elemento_voce());

		return codici_siopeHome.fetchAll(sql);
	}

	@Override
	public SQLBuilder restSelect(UserContext userContext, SQLBuilder sql, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
//		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS, CNRUserContext.getEsercizio(userContext));
		return sql;
	}

    public java.util.List<Elemento_voceBulk> findElementoVociAssociate(Progetto_piano_economicoBulk progettoPiaeco) throws IntrospectionException, PersistencyException {
        PersistentHome home = getHomeCache().getHome(Elemento_voceBulk.class);
        SQLBuilder sql = home.createSQLBuilder();
    	sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, progettoPiaeco.getEsercizio_piano());
    	sql.addClause(FindClause.AND, "cd_unita_piano", SQLBuilder.EQUALS, progettoPiaeco.getCd_unita_organizzativa());
    	sql.addClause(FindClause.AND, "cd_voce_piano", SQLBuilder.EQUALS, progettoPiaeco.getCd_voce_piano());
    	sql.addOrderBy("cd_elemento_voce");
        return home.fetchAll(sql);
    }

	public SQLBuilder selectElementoVociAssociate(Integer esercizio, Integer nrLivello, Integer idClassificazione) throws it.cnr.jada.persistency.PersistencyException {
        SQLBuilder sql = this.createSQLBuilder();
        
        sql.addSQLClause(FindClause.AND, "ELEMENTO_VOCE.ESERCIZIO", SQLBuilder.EQUALS, esercizio );

        sql.addTableToHeader("PARAMETRI_LIVELLI");
        sql.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "PARAMETRI_LIVELLI.ESERCIZIO");

        sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
        sql.addSQLJoin("ELEMENTO_VOCE.ID_CLASSIFICAZIONE", "V_CLASSIFICAZIONE_VOCI_ALL.ID_CLASSIFICAZIONE");
        sql.addSQLJoin("V_CLASSIFICAZIONE_VOCI_ALL.NR_LIVELLO", "PARAMETRI_LIVELLI.LIVELLI_SPESA");

        sql.addSQLClause(FindClause.AND, "V_CLASSIFICAZIONE_VOCI_ALL.ID_LIV"+nrLivello, SQLBuilder.EQUALS, idClassificazione);	
        return sql;
	}

	public java.util.List<Elemento_voceBulk> findElementoVociAssociate(Classificazione_vociBulk classificazione) throws IntrospectionException, PersistencyException {
    	Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHomeCache().getHome(Parametri_cnrBulk.class);
    	Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(classificazione.getEsercizio()));

    	PersistentHome home = getHomeCache().getHome(Elemento_voceBulk.class);
        return home.fetchAll(this.selectElementoVociAssociate(classificazione.getEsercizio(), parCnrBulk.getLivello_pdg_decis_spe(), classificazione.getId_classificazione()));
    }
}
