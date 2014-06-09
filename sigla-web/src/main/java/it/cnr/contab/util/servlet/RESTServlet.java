package it.cnr.contab.util.servlet;


import it.cnr.contab.config00.ejb.Unita_organizzativaComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.servlet.JSONRequest.Clause;
import it.cnr.contab.util.servlet.JSONRequest.OrderBy;
import it.cnr.jada.action.ActionMapping;
import it.cnr.jada.action.ActionMappings;
import it.cnr.jada.action.ActionMappingsConfigurationException;
import it.cnr.jada.action.ActionPerformingError;
import it.cnr.jada.action.ActionUtil;
import it.cnr.jada.action.AdminUserContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.UserInfo;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.OrderConstants;
import it.cnr.jada.util.action.ConsultazioniBP;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class RESTServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private List<String> restExtension;
    private File actionDirFile;    
    private ActionMappings mappings;
    private String COMMAND_POST = "doRestResponse", COMMAND_GET = "doRestInfo";
	private static final Logger logger = Logger.getLogger(RESTServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req, resp, COMMAND_POST);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req, resp, COMMAND_GET);
	}

	protected void execute(HttpServletRequest req, HttpServletResponse resp, String command)
			throws ServletException, IOException {
        String s = req.getServletPath();
        String extension = s.substring(s.lastIndexOf("."));
        if(!restExtension.contains(extension))
            throw new ServletException("Le actions devono terminare con \""+ restExtension +"\"");
        
        s = s.substring(0, s.length() - extension.length());
        ActionMapping actionmapping = mappings.findActionMapping(s);
        if(actionmapping == null)
            throw new ServletException("Action not found ["+s+"]");
        actionmapping.setNeedExistingSession(Boolean.FALSE.toString());
		try {
			UtenteBulk utente = authenticate(req, resp);
			if (utente != null) {
				JSONRequest jsonRequest = null;
	            HttpActionContext httpactioncontext = new HttpActionContext(this, req, resp);
	            httpactioncontext.setActionMapping(actionmapping);
	            if (command.equals(COMMAND_POST)) {
	            	jsonRequest = new Gson().fromJson(new JsonParser().parse(req.getReader()), JSONRequest.class);
		            httpactioncontext.setUserContext(getContextFromRequest(jsonRequest, utente.getCd_utente(),httpactioncontext.getSessionId(), req));
		            httpactioncontext.setUserInfo(getUserInfo(utente, (CNRUserContext) httpactioncontext.getUserContext()));	            	
	            }
	            try{
	            	BusinessProcess businessProcess;
	            	if (req.getParameter("bpName") != null)
	            		businessProcess = mappings.createBusinessProcess(req.getParameter("bpName"), httpactioncontext);
	            	else
	            		businessProcess = mappings.createBusinessProcess(actionmapping, httpactioncontext);
	            	if (command.equals(COMMAND_POST)) {
		            	String mode = loginComponentSession().validaBPPerUtente(httpactioncontext.getUserContext(), utente, 
		            			CNRUserContext.getCd_unita_organizzativa(httpactioncontext.getUserContext()), businessProcess.getName());
		            	if (mode == null || !(businessProcess instanceof ConsultazioniBP)) {
		            		resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        			resp.getWriter().append("Utente non abilitato ad eseguire la richiesta!");
		        			return;
		            	}
		            	ConsultazioniBP consBP = ((ConsultazioniBP)businessProcess);
		        		if (jsonRequest.getClauses() != null) {
		        			CompoundFindClause compoundFindClause = new CompoundFindClause();
		        			for (Clause clause : jsonRequest.getClauses()) {
		        				compoundFindClause.addClause(clause.getCondition(), clause.getFieldName(), clause.getSQLOperator(), clause.getFieldValue());
		        			}
		        			consBP.setFindclause(compoundFindClause);
		        		}
		            	consBP.setIterator(httpactioncontext, 
		            			consBP.search(httpactioncontext,
		            			consBP.getFindclause(),
		        				(OggettoBulk) consBP.getBulkInfo().getBulkClass().newInstance()));
		            	parseRequestParameter(httpactioncontext, jsonRequest, consBP);
		            	
	            	}
	                req.setAttribute(it.cnr.jada.action.BusinessProcess.class.getName(), businessProcess);
	            	httpactioncontext.perform(null, actionmapping, command);
	            }catch(ActionPerformingError actionperformingerror)        {
	            	throw new ComponentException(actionperformingerror.getDetail());
	            }catch(RuntimeException runtimeexception){
	            	logger.error("RuntimeException", runtimeexception);
	            	throw new ComponentException(runtimeexception);
	            } catch (BusinessProcessException e) {
	            	logger.error("BusinessProcessException", e);
	                throw new ComponentException(e);
	    		} catch (InstantiationException e) {
	            	logger.error("InstantiationException", e);
	                throw new ComponentException(e);
				} catch (IllegalAccessException e) {
	            	logger.error("IllegalAccessException", e);
	                throw new ComponentException(e);
				}
			}
		} catch (ComponentException e) {
			logger.error("ComponentException", e);
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.getWriter().append(e.getClass().getName() + " - " + e.getMessage());
		}
	}

	private void parseRequestParameter(HttpActionContext actioncontext,
			JSONRequest jsonRequest, ConsultazioniBP consBP) throws RemoteException, BusinessProcessException {
		if (jsonRequest.getMaxItemsPerPage() != null){
    		consBP.setPageSize(jsonRequest.getMaxItemsPerPage());
    		consBP.refresh(actioncontext);
		}
		if (jsonRequest.getActivePage() != null)
    		consBP.goToPage(actioncontext, jsonRequest.getActivePage());
		if (jsonRequest.getOrderBy() != null) {
			for (OrderBy orderBy : jsonRequest.getOrderBy()) {
				consBP.setOrderBy(actioncontext, orderBy.name, 
						orderBy.getType() == null || orderBy.getType().equalsIgnoreCase("ASC")?OrderConstants.ORDER_ASC: OrderConstants.ORDER_DESC);
			}
		}
	}

	private UserInfo getUserInfo(UtenteBulk utente, CNRUserContext userContext) throws ComponentException {
		CNRUserInfo cnrUserInfo  = new CNRUserInfo();
		cnrUserInfo.setUtente(utente);
		try {
			cnrUserInfo.setUnita_organizzativa((Unita_organizzativaBulk) uoComponentSession().findUOByCodice(userContext, userContext.getCd_unita_organizzativa()));
			cnrUserInfo.setCdr(loginComponentSession().cdrDaUo(userContext, cnrUserInfo.getUnita_organizzativa()));
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
		return cnrUserInfo;
	}
	private CNRUserContext getContextFromRequest(JSONRequest jsonRequest, String user, String sessionId, HttpServletRequest req) throws IOException, ApplicationException {
		if (jsonRequest.getContext() == null)
			throw new ApplicationException("Il contesto utente non puo essere vuoto");
		if (jsonRequest.getContext().getEsercizio() == null)
			throw new ApplicationException("Esercizio non puo essere vuoto");
		if (jsonRequest.getContext().getCd_cds() == null)
			throw new ApplicationException("Il codice del CdS non puo essere vuoto");
		if (jsonRequest.getContext().getCd_unita_organizzativa() == null)
			throw new ApplicationException("Il codice della UO non puo essere vuoto");
		if (jsonRequest.getContext().getCd_cdr() == null)
			throw new ApplicationException("Il codice del CdR non puo essere vuoto");
		return new CNRUserContext(user, sessionId, jsonRequest.getContext().getEsercizio(), 
				jsonRequest.getContext().getCd_unita_organizzativa(), 
				jsonRequest.getContext().getCd_cds(), 
				jsonRequest.getContext().getCd_cdr());
	}
    
	@Override
	public void init() throws ServletException {
		super.init();
		restExtension = new ArrayList<String>();
		String extension = getServletConfig().getInitParameter("extension");
		if (extension == null)
			restExtension.add(".json");
		else {
			StringTokenizer tokens = new StringTokenizer(extension, ",");
			while (tokens.hasMoreTokens()) {
				restExtension.add(tokens.nextToken());
			}			
		}
		actionDirFile = new File(getServletContext().getRealPath("/actions/"));
        try{
            mappings = ActionUtil.reloadActions(actionDirFile);
        }catch(ActionMappingsConfigurationException actionmappingsconfigurationexception){
            throw new ServletException("Action mappings configuration exception", actionmappingsconfigurationexception);
        }		
	}

	public UtenteBulk authenticate(HttpServletRequest req, HttpServletResponse res) throws ComponentException{
        boolean authorized = false;
		UtenteBulk utente = new UtenteBulk();
        String authorization = req.getHeader("Authorization");
        // authenticate as specified by HTTP Basic Authentication
        if (authorization != null && authorization.length() != 0)
        {
            String[] authorizationParts = authorization.split(" ");
            if (!authorizationParts[0].equalsIgnoreCase("basic"))
            {
                throw new ApplicationException("Authorization '" + authorizationParts[0] + "' not supported.");
            }
            String decodedAuthorisation = new String(DatatypeConverter.parseBase64Binary(authorizationParts[1]));
            String[] parts = decodedAuthorisation.split(":");
            
            if (parts.length == 2)
            {
                // assume username and password passed as the parts
                String username = parts[0];
                String password = parts[1];
                if (logger.isDebugEnabled())
                    logger.debug("Authenticating (BASIC HTTP) user " + parts[0]);
				utente = new UtenteBulk();
				utente.setCd_utente(username.toUpperCase());
				utente.setPasswordInChiaro(password.toUpperCase());
				try {
					utente = loginComponentSession().validaUtente(AdminUserContext.getInstance(), utente);
					if (utente != null)
						authorized = true;
				} catch (RemoteException e) {
					throw new ApplicationException(e.getMessage());
				} catch (EJBException e) {
					throw new ApplicationException(e.getMessage());				
				}
            }
        }
        
        // request credentials if not authorized
        if (!authorized)
        {
            if (logger.isDebugEnabled())
            	logger.debug("Requesting authorization credentials");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setHeader("WWW-Authenticate", "Basic realm=\"SIGLA\"");               	
        }
        return utente;
	}

	public static GestioneLoginComponentSession loginComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession");
	}
	public static Unita_organizzativaComponentSession uoComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (Unita_organizzativaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Unita_organizzativaComponentSession");
	}
}
