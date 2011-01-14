/*
 * Created on Jan 19, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.inventario01.bp;


import java.rmi.RemoteException;

import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;
/**
 * @author rpucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class CRUDCaricoScaricoInventarioBP extends SimpleCRUDBP{
	public static final String CARICO= "CARICO";
	public static final String SCARICO= "SCARICO";
	private String Tipo;
	
	protected boolean by_fattura = false;
	private boolean first = true;	
	private boolean isAmministratore = false;
	private boolean isVisualizzazione=false;
	protected boolean by_documento = false;
	public CRUDCaricoScaricoInventarioBP() {
	super();
}

public CRUDCaricoScaricoInventarioBP(String function) {
	super(function);
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	 Integer esercizio = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext());
		  if (this instanceof CRUDCaricoInventarioBP)
			setTipo(this.CARICO);									  
		  else
			setTipo(this.SCARICO);
		setSearchResultColumnSet(getTipo()); 
		setFreeSearchSet(getTipo()); 
		try {	
			BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession)createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",BuonoCaricoScaricoComponentSession.class);
			setVisualizzazione(session.isEsercizioCOEPChiuso(context.getUserContext()));
			setAmministratore(UtenteBulk.isAmministratoreInventario(context.getUserContext()));
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}			
		super.init(config,context);
		
	  	initVariabili(context, getTipo());   
	}	 		 
	public void initVariabili(it.cnr.jada.action.ActionContext context, String Tipo) {
			
		if (this instanceof CRUDCaricoInventarioBP)
			setTipo(this.CARICO);									  
		else
		 	setTipo(this.SCARICO);						
		  setSearchResultColumnSet(getTipo());
		  setFreeSearchSet(getTipo());
	 }
	public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
		try {
			BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession)createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",BuonoCaricoScaricoComponentSession.class);
			setAmministratore(UtenteBulk.isAmministratoreInventario(context.getUserContext()));
		} catch (ComponentException e1) {
			throw handleException(e1);
		} catch (RemoteException e1) {
			throw handleException(e1);
		}
			bulk = super.initializeModelForEdit(context, bulk);
			return bulk;
	}
	
	public String getTipo() {
		return Tipo;
	}
	public void setTipo(String string) {
		Tipo = string;
	}

	public boolean isBy_fattura() {
		return by_fattura;
	}

	public boolean isFirst() {
		return first;
	}
	public boolean isAmministratore() {
		return isAmministratore;
	}
	public void setBy_fattura(boolean b) {
			 by_fattura = b;
			 setFirst(true);
			 Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)getModel();
			 buonoCS.setByFattura(new Boolean(b));

	}
	public void setFirst(boolean b) {
		first = b;
	}

	public void setAmministratore(boolean b) {
		isAmministratore = b;
	}
	public boolean isVisualizzazione() {
		return isVisualizzazione;
	}

	public void setVisualizzazione(boolean b) {
		isVisualizzazione = b;
	}
	public boolean isEditable() {
			return !isVisualizzazione()&&super.isEditable();
	}

	public boolean isAssociata(UserContext uc, Buono_carico_scarico_dettBulk dett) throws BusinessProcessException, ComponentException, RemoteException {
			 if (dett==null||dett.getBene()==null ||dett.getBene().getProgressivo()==null)
				 return false;
			 else{
				BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession)createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",BuonoCaricoScaricoComponentSession.class);
				return session.verifica_associazioni(uc,dett);
			 }
			
	}
	public boolean isAssociataTestata(UserContext uc, Buono_carico_scaricoBulk buono) throws BusinessProcessException, ComponentException, RemoteException {
		 if (buono==null)
			 return false;
		 else{
			BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession)createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",BuonoCaricoScaricoComponentSession.class);
			return session.verifica_associazioni(uc,buono);
		 }
		
	}
	public boolean isNonUltimo(UserContext uc, Buono_carico_scarico_dettBulk dett) throws BusinessProcessException, ComponentException, RemoteException {
		
			 if (dett==null||dett.getBene()==null ||dett.getBene().getProgressivo()==null)
				 return false;
			 else{
				BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession)createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",BuonoCaricoScaricoComponentSession.class);
				return session.isNonUltimo(uc,dett);
			 }
	}
	public boolean isBy_documento() {
		return by_documento;
	}

	public void setBy_documento(boolean by_doc) {
		 by_documento = by_doc;
		 setFirst(true);
		 Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)getModel();
		 buonoCS.setByDocumento(new Boolean(by_doc));
	}
	public boolean isContabilizzato(UserContext uc, Buono_carico_scaricoBulk buono) throws BusinessProcessException, ComponentException, RemoteException {
		 if (buono==null)
			 return false;
		 else{
			BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession)createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",BuonoCaricoScaricoComponentSession.class);
			return session.isContabilizzato(uc,buono);
}
	}
}
