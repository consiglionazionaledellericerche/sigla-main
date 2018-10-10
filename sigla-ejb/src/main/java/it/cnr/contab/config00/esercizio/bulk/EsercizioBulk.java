package it.cnr.contab.config00.esercizio.bulk;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.OrderedHashtable;

import java.util.Hashtable;
import java.util.Optional;

/**
 * Gestione dei dati relativi alla tabella Esercizio
 */
public class EsercizioBulk extends EsercizioBase {
    final public static String STATO_INIZIALE = "I";
    final public static String STATO_PDG_APERTO = "G";
    final public static String STATO_APERTO = "A";
    //		final public static String STATO_CHIUSO_PROV 	= "P";
    final public static String STATO_CHIUSO_DEF = "C";
    final public static Integer ESERCIZIO_INIZIO;
    final public static Integer ESERCIZIO_FINE;
    private static OrderedHashtable statoKeys;
    private static Hashtable prossimoStato;

    static {

        Integer ESERCIZIO_INIZIO_DA_CONFIG;
        try {
            ESERCIZIO_INIZIO_DA_CONFIG = Integer.valueOf(Config.getHandler().getProperty(EsercizioBulk.class, "Esercizio.inizio"));
        } catch (Throwable t) {
            ESERCIZIO_INIZIO_DA_CONFIG = new Integer(2003);
        }
        Integer ESERCIZIO_FINE_DA_CONFIG;
        try {
            ESERCIZIO_FINE_DA_CONFIG = Integer.valueOf(Config.getHandler().getProperty(EsercizioBulk.class, "Esercizio.fine"));
        } catch (Throwable t) {
            ESERCIZIO_FINE_DA_CONFIG = new Integer(2100);
        }
        ESERCIZIO_INIZIO = ESERCIZIO_INIZIO_DA_CONFIG;
        ESERCIZIO_FINE = ESERCIZIO_FINE_DA_CONFIG;
    }

    /**
     * Primo costruttore della classe <code>EsercizioBulk</code>.
     */
    public EsercizioBulk() {
        super();
    }

    /**
     * Secondo costruttore della classe <code>EsercizioBulk</code>.
     *
     * @param esercizio Esercizio contabile corrente.
     */
    public EsercizioBulk(String cd_cds, java.lang.Integer esercizio) {
        super(cd_cds, esercizio);
        setCds(new CdsBulk(cd_cds));
    }

    /**
     * Metodo statico che permette di recuperare il valore dei possibili stati
     * che può assumere un esercizio contabile.
     *
     * @return prossimoStato Variabile di tipo <code>Hashtable</code> che
     * contiene codici e relative descrizioni degli stati.
     */
    public static java.util.Hashtable getProssimoStato() {
        if (prossimoStato == null) {
            prossimoStato = new Hashtable();
            // prossimoStato.put( STATO_INIZIALE, STATO_APERTO );
            prossimoStato.put(STATO_INIZIALE, STATO_PDG_APERTO);
            prossimoStato.put(STATO_PDG_APERTO, STATO_APERTO);
            //	prossimoStato.put( STATO_APERTO, STATO_CHIUSO_PROV );
            //	prossimoStato.put( STATO_CHIUSO_PROV, STATO_CHIUSO_DEF );
            prossimoStato.put(STATO_APERTO, STATO_CHIUSO_DEF);
            prossimoStato.put(STATO_CHIUSO_DEF, STATO_APERTO);
        }
        return prossimoStato;
    }

    /**
     * Metodo utilizzato per fare il reset delle variabili statiche definite
     * nella classe <code>EsercizioBulk</code>.
     */
    public static void reset() {
        statoKeys = null;
    }

    /**
     * Metodo con cui si ottiene il valore dello stato attuale dell'esercizio in
     * questione.
     *
     * @return statoKeys Valore dello stato dell' esercizio contabile corrente,
     * di tipo <code>OrderedHashtable</code>.
     */
    public OrderedHashtable getStatoKeys() {
        if (statoKeys == null) {
            statoKeys = new OrderedHashtable();
            statoKeys.put("I", "Iniziale");
            statoKeys.put("G", "Piano di Gestione aperto");
            statoKeys.put("A", "Aperto");
//		statoKeys.put("P", "Chiuso provvisoriamente");
            statoKeys.put("C", "Chiuso");
        }
        return statoKeys;
    }

    /**
     * Metodo per inizializzare l'oggetto bulk in fase di inserimento.
     *
     * @param bp      Business Process <code>CRUDBP</code> in uso.
     * @param context <code>ActionContext</code> in uso.
     * @return OggettoBulk this Oggetto bulk <code>EsercizioBulk</code>.
     */
    public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
        super.initializeForInsert(bp, context);
        setIm_cassa_iniziale(new java.math.BigDecimal(0));
        setSt_apertura_chiusura(STATO_INIZIALE);
        setCd_cds(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getUnita_padre().getCd_unita_organizzativa());
        return this;
    }

    /**
     * Metodo per inizializzare l'oggetto bulk in fase di inserimento.
     *
     * @param bp      Business Process <code>CRUDBP</code> in uso.
     * @param context <code>ActionContext</code> in uso.
     * @return OggettoBulk this Oggetto bulk <code>EsercizioBulk</code>.
     */
    public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {
        super.initializeForSearch(bp, context);
        setCd_cds(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getUnita_padre().getCd_unita_organizzativa());
        return this;
    }

    /**
     * Metodo che stabilisce se l'esercizio contabile corrente è stato chiuso.
     *
     * @return boolean True se l'esercizio contabile in questione è stato chiuso
     * in modo provvisorio o definitivo.
     */
    public boolean isChiuso() {
//	return (getSt_apertura_chiusura().equals( STATO_CHIUSO_DEF) || getSt_apertura_chiusura().equals( STATO_CHIUSO_PROV ));
        return getSt_apertura_chiusura().equals(STATO_CHIUSO_DEF);

    }

    /**
     * Determina quando abilitare o meno nell'interfaccia utente il campo <code>st_apertura_chiusura</code>.
     *
     * @return boolean true quando il campo deve essere disabilitato.
     */
    public boolean isROSt_apertura_chiusura() {
        return true;
    }

    /**
     * Metodo con cui si verifica la validità di alcuni campi, mediante un
     * controllo sintattico o contestuale.
     */
    public void validate() throws ValidationException {
        if (getEsercizio() == null)
            throw new ValidationException("Il campo ESERCIZIO è obbligatorio.");
        if (getEsercizio().toString().length() != 4)
            throw new ValidationException("Il campo ESERCIZIO deve essere di quattro cifre.");
        if (getSt_apertura_chiusura() == null)
            throw new ValidationException("E' necessario indicare lo STATO dell'esercizio contabile.");
    }

    @Override
    public String getCd_cds() {
        return Optional.ofNullable(getCds()).map(CdsBulk::getCd_unita_organizzativa).orElse(super.getCd_cds());
    }
}
