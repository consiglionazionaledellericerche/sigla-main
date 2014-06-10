package it.cnr.contab.docamm00.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import it.cnr.brevetti.TrovatiWebServiceBeanServiceStub;
import it.cnr.contab.docamm00.docs.bulk.TrovatoBulk;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;

public class RicercaTrovato {

	private static String targetEndpoint; //="http://siglaas4.cedrc.cnr.it:8480/";
	
	public RicercaTrovato() throws FileNotFoundException, IOException {
		super();
		// carica le proprietà solo alla prima occorrenza della classe
		// if (getTargetEndpoint()==null)
			// loadProperties();
	}

	public static String getTargetEndpoint() {
		return targetEndpoint;
	}

	public static void setTargetEndpoint(String targetEndpoint) {
		RicercaTrovato.targetEndpoint = targetEndpoint;
	}

	private static ConfigurationContext axis2ConfContext(String sAxis2Conf, String sAxis2Repo) throws Exception {
		return ConfigurationContextFactory.createConfigurationContextFromFileSystem(
				sAxis2Repo/*.replace("\\", "/")*/,
        		sAxis2Conf/*.replace("\\", "/")*/);
	}

	/**
	 * purtroppo non funziona, non so perchè, vedi questo link in cui parla di un file da aggiungere al repository, modules.list
	 * http://www.archivum.info/axis-user@ws.apache.org/2008-01/00573/Re-%28axis2%29-custom-configuration-context.html
	 * 
	 * @param urlAxis2Conf
	 * @param urlAxis2Repo
	 * @return
	 * @throws Exception
	 */
	private static ConfigurationContext axis2ConfContext(URL urlAxis2Conf, URL urlAxis2Repo) throws Exception {
		return ConfigurationContextFactory.createConfigurationContextFromURIs(
				urlAxis2Conf,
				urlAxis2Repo);
	}

	public BulkList<TrovatoBulk> cerca(String sAxis2Conf, String sAxis2Repo, String uo) throws Exception {
		ConfigurationContext ctx = axis2ConfContext(sAxis2Conf, sAxis2Repo);
		return cerca(ctx, uo);
	}

	public BulkList<TrovatoBulk> cerca(URL urlAxis2Conf, URL urlAxis2Repo, String uo) throws Exception {
		ConfigurationContext ctx = axis2ConfContext(urlAxis2Conf, urlAxis2Repo);
		return cerca(ctx, uo);
	}

	public TrovatoBulk ricercaDatiTrovato(it.cnr.jada.UserContext userContext,Long trovato)throws ComponentException,java.rmi.RemoteException,PersistencyException {
		TrovatoBulk trovatoBulk = new TrovatoBulk();
		if (trovato == null){
			trovatoBulk.setPg_trovato(trovato);
			trovatoBulk.setInventore(null);
			trovatoBulk.setTitolo(null);
		} else {
			trovatoBulk.setPg_trovato(trovato);
//	TODO da inserire chiamata al ws.
//			trovatoBulk.setInventore("Gianfranco Gasparro");
//			trovatoBulk.setTitolo("Invenzione di grande prestigio");
		}
		return trovatoBulk; 
	}

	private BulkList<TrovatoBulk> cerca(ConfigurationContext ctx, String uo) throws Exception {
		
        TrovatiWebServiceBeanServiceStub stub = new TrovatiWebServiceBeanServiceStub(
        		ctx,
				targetEndpoint+"/brevetti/ws/TrovatiWebServiceBean");

        TrovatiWebServiceBeanServiceStub.FindTrovatiByUoE trovatiE = new TrovatiWebServiceBeanServiceStub.FindTrovatiByUoE();
        TrovatiWebServiceBeanServiceStub.FindTrovatiByUo trovati = new TrovatiWebServiceBeanServiceStub.FindTrovatiByUo();
		//trovati.setUo("503000");
        trovati.setUo(uo.replace(".", ""));
        trovatiE.setFindTrovatiByUo(trovati);
		TrovatiWebServiceBeanServiceStub.FindTrovatiByUoResponseE respF = stub.findTrovatiByUo(trovatiE);

		BulkList<TrovatoBulk> listaTrovati = new BulkList<TrovatoBulk>();
		if (respF.getFindTrovatiByUoResponse().getResult()!=null) {
			for (int i=0;i<respF.getFindTrovatiByUoResponse().getResult().length;i++) {
		    	TrovatiWebServiceBeanServiceStub.TrovatoBean trovatoBean = (TrovatiWebServiceBeanServiceStub.TrovatoBean) respF.getFindTrovatiByUoResponse().getResult()[i];
				TrovatoBulk trovato = new TrovatoBulk();
				trovato.setPg_trovato(new Long(trovatoBean.getNsrif()));
				trovato.setTitolo(trovatoBean.getTitolo());
				listaTrovati.add(trovato);
		    }
		}
	    return listaTrovati;
	}

	public synchronized void loadProperties() throws FileNotFoundException, IOException {
		Properties  prop = new Properties();
		URL urlAxis2Prop=getClass().getClassLoader().getResource("axis2/axis2-brevetti.properties");
		prop.load(new FileInputStream(urlAxis2Prop.getPath()));
		if (urlAxis2Prop!=null) {
			setTargetEndpoint(prop.getProperty("targetEndpoint"));
			PWCBHandler.setSiglaClientWSPassword(prop.getProperty("siglaWSClientPassword"));
		}
	}
}
