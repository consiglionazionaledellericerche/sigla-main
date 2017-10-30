package it.cnr.contab.config00.bulk;

import java.util.Dictionary;

import it.cnr.jada.bulk.*;

/**
 * @author aimprota
 *
 */
@SuppressWarnings("unchecked")
public class Parametri_enteBulk extends Parametri_enteBase
{
	public static final String DB_PRODUZIONE  ="P";
	public static final String DB_FORMAZIONE  ="F";
	public static final String DB_TEST  ="T";
	public static final String DB_SVILUPPO  ="S";
	public static final String DB_ALTRO  ="A";

	public final static Dictionary tipoDBKeys;
	static {
		tipoDBKeys = new it.cnr.jada.util.OrderedHashtable();
		tipoDBKeys.put(DB_PRODUZIONE,"Produzione");
		tipoDBKeys.put(DB_FORMAZIONE,"Formazione");
		tipoDBKeys.put(DB_TEST,"Test");
		tipoDBKeys.put(DB_SVILUPPO,"Sviluppo");
		tipoDBKeys.put(DB_ALTRO,"Altro");
	};

	public final static Dictionary<String,String> logoDBKeys;
	static {
		logoDBKeys = new it.cnr.jada.util.OrderedHashtable();
		logoDBKeys.put(DB_PRODUZIONE,"logo_mini_prod.gif");
		logoDBKeys.put(DB_FORMAZIONE,"logo_mini_form.gif");
		logoDBKeys.put(DB_TEST,"logo_mini_test.gif");
		logoDBKeys.put(DB_SVILUPPO,"logo_mini_svil.gif");
		logoDBKeys.put(DB_ALTRO,"logo_mini_altro.gif");
	};
	
    /**
     * Costruttore di default
     */
    public Parametri_enteBulk()
    {
        super();
    }

    /**
     * @param id
     */
    public Parametri_enteBulk(Integer id)
    {
        super(id);
    }
    
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context){
		super.initializeForInsert(bp,context);
		setAttivo(Boolean.FALSE);
    	setFl_autenticazione_ldap(Boolean.FALSE);
    	setFl_informix(Boolean.FALSE);    	
    	setFl_gae_es(Boolean.FALSE);
    	setFl_prg_pianoeco(Boolean.FALSE);
	  return this;
	}

	public boolean isAutenticazioneLdap() {
		return getFl_autenticazione_ldap() != null && getFl_autenticazione_ldap().booleanValue();
	}
	
	public String checkLogo(){
		if (getTipo_db() != null){		
			return logoDBKeys.get(getTipo_db());
		}
		return "logo_mini_altro.gif";
	}
	
	public boolean isEnteCNR() {
		return "CNR".equals(getDescrizione());
	}
}
