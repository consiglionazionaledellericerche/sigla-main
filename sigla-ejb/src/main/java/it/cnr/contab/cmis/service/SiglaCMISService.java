package it.cnr.contab.cmis.service;

import it.cnr.contab.cmis.CMISRelationship;
import it.cnr.contab.cmis.MimeTypes;
import it.cnr.contab.cmis.acl.ACLType;
import it.cnr.contab.cmis.acl.Permission;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Relationship;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.bindings.CmisBindingFactory;
import org.apache.chemistry.opencmis.client.bindings.impl.CmisBindingsHelper;
import org.apache.chemistry.opencmis.client.bindings.impl.SessionImpl;
import org.apache.chemistry.opencmis.client.bindings.spi.AbstractAuthenticationProvider;
import org.apache.chemistry.opencmis.client.bindings.spi.BindingSession;
import org.apache.chemistry.opencmis.client.bindings.spi.http.Output;
import org.apache.chemistry.opencmis.client.bindings.spi.http.Response;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.client.util.OperationContextUtils;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.Acl;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.RelationshipDirection;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisRuntimeException;
import org.apache.chemistry.opencmis.commons.impl.UrlBuilder;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class SiglaCMISService {
	private transient static final Log logger = LogFactory.getLog(SiglaCMISService.class);
	public static final String ASPECT_TITLED = "P:cm:titled", 
			PROPERTY_TITLE = "cm:title", 
			PROPERTY_DESCRIPTION = "cm:description", 
			PROPERTY_AUTHOR = "cm:author",
			ALFCMIS_NODEREF = "alfcmis:nodeRef";
	private CMISBulkInfo<?> cmisBulkInfo;
	private String baseURL;
	private Map<String, String> serverParameters;
	
	protected Session siglaSession;
	protected BindingSession siglaBindingSession;
	
	public void setCmisBulkInfo(CMISBulkInfo<?> cmisBulkInfo) {
		this.cmisBulkInfo = cmisBulkInfo;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public void setServerParameters(Map<String, String> serverParameters) {
		this.serverParameters = serverParameters;
	}

	public void init(){
		siglaSession = createSession();
		OperationContext operationContext = OperationContextUtils.createMaximumOperationContext();
		operationContext.setMaxItemsPerPage(Integer.MAX_VALUE);
		siglaSession.setDefaultContext(operationContext);
		siglaBindingSession = createBindingSession();
	}

    private Session createSession(){
    	return createSession(serverParameters.get("user.admin.username"), serverParameters.get("user.admin.password"));
    }

    private Session createSession(String userName, String password){
    	SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
        Map<String, String> sessionParameters = new HashMap<String, String>();
        sessionParameters.putAll(serverParameters);
        sessionParameters.put(SessionParameter.USER, userName);
        sessionParameters.put(SessionParameter.PASSWORD, password);

        sessionParameters.put(SessionParameter.REPOSITORY_ID, sessionFactory.getRepositories(sessionParameters).get(0).getId());
        sessionParameters.put(SessionParameter.LOCALE_ISO3166_COUNTRY, Locale.ITALY.getCountry());
        sessionParameters.put(SessionParameter.LOCALE_ISO639_LANGUAGE, Locale.ITALY.getLanguage());
        sessionParameters.put(SessionParameter.LOCALE_VARIANT, Locale.ITALY.getVariant());

    	return sessionFactory.createSession(sessionParameters);
    }

	private BindingSession createBindingSession(){
    	BindingSession session = new SessionImpl();
        if (serverParameters == null){
            return null;
        }
        Map<String, String> sessionParameters = new HashMap<String, String>();
        sessionParameters.putAll(serverParameters);
        sessionParameters.put(SessionParameter.USER, serverParameters.get("user.admin.username"));
        sessionParameters.put(SessionParameter.PASSWORD, serverParameters.get("user.admin.password"));
        if (!sessionParameters.containsKey(SessionParameter.AUTHENTICATION_PROVIDER_CLASS)) {
            sessionParameters.put(SessionParameter.AUTHENTICATION_PROVIDER_CLASS, CmisBindingFactory.STANDARD_AUTHENTICATION_PROVIDER);
        }
        sessionParameters.put(SessionParameter.AUTH_HTTP_BASIC, "true");
        sessionParameters.put(SessionParameter.AUTH_SOAP_USERNAMETOKEN, "false");
        for (Map.Entry<String, String> entry : sessionParameters.entrySet()) {
            session.put(entry.getKey(), entry.getValue());
        }
        // create authentication provider and add it session
        String authProvider = sessionParameters.get(SessionParameter.AUTHENTICATION_PROVIDER_CLASS);
        if (authProvider != null) {
            Object authProviderObj = null;

            try {
                authProviderObj = Class.forName(authProvider).newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not load authentication provider: " + e, e);
            }

            if (!(authProviderObj instanceof AbstractAuthenticationProvider)) {
                throw new IllegalArgumentException(
                        "Authentication provider does not extend AbstractAuthenticationProvider!");
            }

            session.put(CmisBindingsHelper.AUTHENTICATION_PROVIDER_OBJECT,
                    (AbstractAuthenticationProvider) authProviderObj);
            ((AbstractAuthenticationProvider) authProviderObj).setSession(session);
        }
    	return session;
    }

	public String sanitizeFilename(String name) {
		name = name.trim();
		Pattern pattern = Pattern.compile("([\\/:@()&<>?\"])");
		Matcher matcher = pattern.matcher(name);
		if(!matcher.matches()){
			String str1 = matcher.replaceAll("_"); 
			return str1;
		} else {
			return name;
		}		
	}
	
	public String sanitizeFolderName(String name) {
		name = name.trim();
		Pattern pattern = Pattern.compile("([\\/:@()&<>?\"])");
		Matcher matcher = pattern.matcher(name);
		if(!matcher.matches()){
			String str1 = matcher.replaceAll("'"); 
			return str1;
		} else {
			return name;
		}		
	}

	public String getRepositoyURL() {
		return baseURL;
	}
	
	public CmisObject getNodeByPath(CMISPath cmisPath){
		return getNodeByPath(cmisPath.getPath());
	}

	public CmisObject getNodeByPath(String path){
		return siglaSession.getObjectByPath(path);
	}
	
	public CmisObject getNodeByNodeRef(String nodeRef){
		return siglaSession.getObject(nodeRef);
	}

	public CmisObject getNodeByNodeRef(String nodeRef, UsernamePasswordCredentials usernamePasswordCredentials){
		return createSession(usernamePasswordCredentials.getUserName(), usernamePasswordCredentials.getPassword()).getObject(nodeRef);
	}
	
	public CMISPath createFolderIfNotPresent(CMISPath cmisPath, String folderName, String title, String description){
		return createFolderIfNotPresent(cmisPath, folderName, title, description, null);
	}

	public CMISPath createFolderIfNotPresent(CMISPath cmisPath, String folderName, String title, String description, OggettoBulk oggettoBulk){
		return createFolderIfNotPresent(cmisPath, folderName, title, description, oggettoBulk, null);
	}

	public CMISPath createFolderIfNotPresent(CMISPath cmisPath, String folderName, String title, String description, OggettoBulk oggettoBulk, String objectTypeName){
		CmisObject cmisObject = getNodeByPath(cmisPath);
		Map<String, Object> metadataProperties = new HashMap<String, Object>();
		List<String> aspectsToAdd = new ArrayList<String>();
		try{
			folderName = sanitizeFolderName(folderName);
			metadataProperties.put(PropertyIds.NAME, folderName);
			aspectsToAdd.add(ASPECT_TITLED);
			if (title != null)
				metadataProperties.put(PROPERTY_TITLE, title);
			if (description != null)
				metadataProperties.put(PROPERTY_DESCRIPTION, description);

			if (oggettoBulk != null) {
				if (cmisBulkInfo.getType(siglaSession, oggettoBulk)!=null || objectTypeName!=null)
					metadataProperties.put(PropertyIds.OBJECT_TYPE_ID, cmisBulkInfo.getType(siglaSession, oggettoBulk).getId());
				for (Property<?> property : cmisBulkInfo.getProperty(siglaSession, oggettoBulk)) {
					metadataProperties.put(property.getId(), property.getValue());
				}
				aspectsToAdd.addAll(cmisBulkInfo.getAspect(siglaSession, oggettoBulk));
				for (Property<?> property : cmisBulkInfo.getAspectProperty(siglaSession, oggettoBulk)) {
					metadataProperties.put(property.getId(), property.getValue());
				}
			} else {
				metadataProperties.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_FOLDER.value());				
			}
			metadataProperties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, aspectsToAdd);
			Folder folder = (Folder) getNodeByNodeRef(siglaSession.createFolder(metadataProperties, cmisObject).getId());
			return CMISPath.construct(folder.getPath());
		}catch(CmisContentAlreadyExistsException _ex){
			if (oggettoBulk!=null){
				Folder folder = (Folder) getNodeByPath(cmisPath.getPath()+(cmisPath.getPath().equals("/")?"":"/")+sanitizeFilename(folderName).toLowerCase());
		        List<String> aspects = folder.getPropertyValue(PropertyIds.SECONDARY_OBJECT_TYPE_IDS);
		        aspects.addAll(aspectsToAdd);
		        metadataProperties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, aspects);
				folder.updateProperties(metadataProperties, true);
				return CMISPath.construct(folder.getPath());
			}
			return cmisPath.appendToPath(folderName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	public void deleteNode(CmisObject cmisObject){
		if (cmisObject.getBaseTypeId().equals(BaseTypeId.CMIS_FOLDER))
			((Folder)cmisObject).deleteTree(true, UnfileObject.DELETE, false);
		else	
			siglaSession.delete(cmisObject);
	}
	
	public InputStream getResource(CmisObject cmisObject){
		return ((Document)cmisObject).getContentStream().getStream();
	}
	
	public Document storePrintDocument(OggettoBulk oggettoBulk, Report report, CMISPath cmisPath, Permission... permissions){
		try {
			return storeSimpleDocument(oggettoBulk, report.getInputStream(), report.getContentType(), report.getName(), cmisPath, permissions);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Document storeSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name, 
			CMISPath cmisPath, Permission... permissions){
		return storeSimpleDocument(oggettoBulk, inputStream, contentType, name, cmisPath, null, false, permissions);
	}
	
	public Document storeSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name, 
				CMISPath cmisPath, String objectTypeName, boolean makeVersionable, Permission... permissions){
		CmisObject parentNode = getNodeByPath(cmisPath);
		Map<String, Object> metadataProperties = new HashMap<String, Object>();		
		try {
			name = sanitizeFilename(name);
			metadataProperties.put(PropertyIds.NAME, name);
			ContentStream contentStream = new ContentStreamImpl(
					name,
					BigInteger.ZERO,
					contentType,
					inputStream);			
			if (cmisBulkInfo.getType(siglaSession, oggettoBulk)!=null || objectTypeName!=null)
				metadataProperties.put(PropertyIds.OBJECT_TYPE_ID, cmisBulkInfo.getType(siglaSession, oggettoBulk).getId());			
			for (Property<?> property : cmisBulkInfo.getProperty(siglaSession, oggettoBulk)) {
				metadataProperties.put(property.getId(), property.getValue());
			}
			for (Property<?> property : cmisBulkInfo.getAspectProperty(siglaSession, oggettoBulk)) {
				metadataProperties.put(property.getId(), property.getValue());
			}			
			metadataProperties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, cmisBulkInfo.getAspect(siglaSession, oggettoBulk));
			Document node = (Document) getNodeByNodeRef(
					siglaSession.createDocument(metadataProperties, parentNode, contentStream, VersioningState.MAJOR).getId());
			if (permissions.length > 0 ){
				setInheritedPermission(siglaBindingSession, node.getProperty(ALFCMIS_NODEREF).getValueAsString(), Boolean.FALSE);
				if (permissions != null && permissions.length > 0) {
					addAcl(siglaBindingSession, node.getProperty(ALFCMIS_NODEREF).getValueAsString(), Permission.convert(permissions));
				}
			}
			if (makeVersionable)
				addAutoVersion((Document) node, false);
			return node;
		}catch (CmisBaseException e) {
			throw e;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Document restoreSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name, 
			CMISPath cmisPath, Permission... permissions){
		return restoreSimpleDocument(oggettoBulk, inputStream, contentType, name, cmisPath, null, false, permissions);
	}

	public Document restoreSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name, 
			CMISPath cmisPath, String objectTypeName, boolean makeVersionable, Permission... permissions){
		Document node = null;
		try {
			node = (Document) getNodeByPath(cmisPath.getPath()+
											(cmisPath.getPath().equals("/")?"":"/")+
											sanitizeFilename(name).toLowerCase());
		} catch (CmisObjectNotFoundException e){
			return storeSimpleDocument(oggettoBulk, inputStream, contentType, name, cmisPath, objectTypeName, makeVersionable, permissions);
		}
		updateContent(node.getId(), inputStream, contentType);
		return node;
	}

	public void updateProperties(OggettoBulk oggettoBulk, CmisObject node){
		try {
			Map<String, Object> metadataProperties = new HashMap<String, Object>();
			for (Property<?> property : cmisBulkInfo.getProperty(siglaSession, oggettoBulk)) {
				metadataProperties.put(property.getId(), property.getValue());
			}
			for (Property<?> property : cmisBulkInfo.getAspectProperty(siglaSession, oggettoBulk)) {
				metadataProperties.put(property.getId(), property.getValue());
			}			
			metadataProperties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, cmisBulkInfo.getAspect(siglaSession, oggettoBulk));
			if (node.getBaseTypeId().equals(BaseTypeId.CMIS_DOCUMENT)) {
				node = ((Document)node).getObjectOfLatestVersion(false);
			}
			node = siglaSession.getObject(node);
			node.refresh();
			node.updateProperties(metadataProperties, true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void updateContent(String nodeRef, InputStream inputStream, String contentType){
		Document document = (Document) getNodeByNodeRef(nodeRef);
		ContentStream contentStream = new ContentStreamImpl(
				document.getName(),
				BigInteger.ZERO,
				contentType,
				inputStream);
		document.setContentStream(contentStream, true);
	}

	public void createRelationship(String sourceNodeRef, String sourceNodeTarget, CMISRelationship relationship){
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, relationship.value());
		properties.put(PropertyIds.SOURCE_ID, sourceNodeRef);
		properties.put(PropertyIds.TARGET_ID, sourceNodeTarget);
		siglaSession.createRelationship(properties);
	}
	
	public List<CmisObject> getRelationship(String sourceNodeRef, CMISRelationship relationship, boolean fromTarget){
		List<CmisObject> result = new ArrayList<CmisObject>();
		ItemIterable<Relationship> relationships = siglaSession.getRelationships(new ObjectIdImpl(sourceNodeRef), true, 
				fromTarget ? RelationshipDirection.TARGET : RelationshipDirection.SOURCE, null, siglaSession.getDefaultContext());
		if (relationships.getTotalNumItems() > 0){
			for (Relationship rel : relationships) {
				result.add(siglaSession.getObject(fromTarget?rel.getSource():rel.getTarget()));
			}
		}		
		return result;
	}
	
	public List<CmisObject> getRelationship(String sourceNodeRef, CMISRelationship relationship){
		return getRelationship(sourceNodeRef, relationship, false);
	}
	
	public List<CmisObject> getRelationshipFromTarget(String sourceNodeRef, CMISRelationship relationship){
		return getRelationship(sourceNodeRef, relationship, true);	
	}
	
	public void makeVersionable(CmisObject node){
		addAutoVersion((Document) node, false);
	}
	
	public ItemIterable<CmisObject> getChildren(Folder folder){
		return folder.getChildren();
	}
	
	public void addConsumerToEveryone(CmisObject cmisObject){
		addAcl(siglaBindingSession, cmisObject.getProperty(ALFCMIS_NODEREF).getValueAsString(), Collections.singletonMap("GROUP_EVERYONE", ACLType.Consumer));
	}

	public void removeConsumerToEveryone(CmisObject cmisObject){
		removeAcl(siglaBindingSession, cmisObject.getProperty(ALFCMIS_NODEREF).getValueAsString(), Collections.singletonMap("GROUP_EVERYONE", ACLType.Consumer));
	}

	public void copyNode(Document source, Folder target){
		source.addToFolder(target, true);
	}
	
	public ItemIterable<QueryResult> search(StringBuffer query){
		return search(query, siglaSession.getDefaultContext());
	}

	public ItemIterable<QueryResult> search(StringBuffer query, OperationContext operationContext){
		return siglaSession.query(query.toString(), false, operationContext);
	}
	
	public List<CmisObject> searchAndFetchNode(StringBuffer query){
		List<CmisObject> results = new ArrayList<CmisObject>();
		for (QueryResult queryResult : search(query)) {
			results.add(getNodeByNodeRef((String) queryResult.getPropertyValueById(PropertyIds.OBJECT_ID)));
		}
		return results;
	}

    public void addAspect(CmisObject cmisObject, String... aspectName){
        Map<String, Object> metadataProperties = new HashMap<String, Object>();
        List<String> aspects = cmisObject.getPropertyValue(PropertyIds.SECONDARY_OBJECT_TYPE_IDS);
        aspects.addAll(Arrays.asList(aspectName));
        metadataProperties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, aspects);
        cmisObject.updateProperties(metadataProperties);
    }
    
	public void removeAspect(CmisObject cmisObject, String... aspectName){
		List<Object> aspects = cmisObject.getProperty(PropertyIds.SECONDARY_OBJECT_TYPE_IDS).getValues();
		aspects.removeAll(Arrays.asList(aspectName));
		cmisObject.updateProperties(Collections.singletonMap(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, aspects));
	}

	public boolean hasAspect(CmisObject cmisObject, String aspectName) {
		return cmisObject.getProperty(PropertyIds.SECONDARY_OBJECT_TYPE_IDS).getValues().contains(aspectName);
	}
	
	public void setInheritedPermission(CMISPath cmisPath, Boolean inheritedPermission){
		setInheritedPermission(siglaBindingSession, getNodeByPath(cmisPath).getProperty(ALFCMIS_NODEREF).getValueAsString(), inheritedPermission);
	}
		
	public Response invokeGET(UrlBuilder url) {
		return CmisBindingsHelper.getHttpInvoker(
				siglaBindingSession).invokeGET(url, siglaBindingSession);		
	}

	public Response invokePOST(UrlBuilder url, MimeTypes mimeType, final byte[] content) {
		if (logger.isDebugEnabled())
			logger.debug("Invoke URL:" + url);
		return CmisBindingsHelper.getHttpInvoker(siglaBindingSession).invokePOST(url, mimeType.mimetype(),
				new Output() {
					public void write(OutputStream out) throws Exception {
            			out.write(content);
            		}
        		}, siglaBindingSession);
	}

	private void setInheritedPermission(BindingSession cmisSession,
			String objectId, final Boolean inheritedPermission) {
		String link = baseURL
				.concat("service/cnr/nodes/permissions/")
				.concat(objectId.replace(":/", ""));
		UrlBuilder url = new UrlBuilder(link);
		Response resp = CmisBindingsHelper.getHttpInvoker(cmisSession).invokePOST(url,
				MimeTypes.JSON.mimetype(), new Output() {
					public void write(OutputStream out) throws Exception {
						JSONObject jsonObject = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						jsonObject.put("permissions", jsonArray);
						jsonObject.put("isInherited", inheritedPermission);
						out.write(jsonObject.toString().getBytes());
					}
				}, cmisSession);
		int status = resp.getResponseCode();
		if (status == HttpStatus.SC_NOT_FOUND
				|| status == HttpStatus.SC_BAD_REQUEST
				|| status == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			throw new CmisRuntimeException("Inherited Permission error. Exception: "
					+ resp.getErrorContent());
	}
	public Ace getACL(CmisObject cmisObject, String authority, String permission) {
		Acl acl = cmisObject.getAcl();
		for (Ace ace : acl.getAces()) {
			if (ace.getPrincipalId().equals(authority) && ace.getPermissions().contains(permission))
				return ace;
		}
		return null;
	}
	
	private void removeAcl(BindingSession cmisSession, String nodeRef,
			Map<String, ACLType> permission) {
		managePermission(cmisSession, nodeRef, permission, true);
	}

	private void addAcl(BindingSession cmisSession, String nodeRef,
			Map<String, ACLType> permission) {
		managePermission(cmisSession, nodeRef, permission, false);
	}

	private void managePermission(BindingSession cmisSession, String objectId,
			final Map<String, ACLType> permission, final boolean remove) {
		String link = baseURL
				.concat("service/cnr/nodes/permissions/")
				.concat(objectId.replace(":/", ""));
		UrlBuilder url = new UrlBuilder(link);
		Response resp = CmisBindingsHelper.getHttpInvoker(cmisSession).invokePOST(url,
				MimeTypes.JSON.mimetype(), new Output() {
					public void write(OutputStream out) throws Exception {
						JSONObject jsonObject = new JSONObject();
						JSONArray jsonArray = new JSONArray();
						for (String authority : permission.keySet()) {
							JSONObject jsonAutority = new JSONObject();
							jsonAutority.put("authority", authority);
							jsonAutority.put("role", permission.get(authority));
							if (remove)
								jsonAutority.put("remove", remove);
							jsonArray.put(jsonAutority);
						}
						jsonObject.put("permissions", jsonArray);
						out.write(jsonObject.toString().getBytes());
					}
				}, cmisSession);
		int status = resp.getResponseCode();

		logger.info((remove ? "remove" : "add") + " permission " + permission + " on item "
				+ objectId + ", status = " + status);

		if (status == HttpStatus.SC_NOT_FOUND
				|| status == HttpStatus.SC_BAD_REQUEST
				|| status == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			throw new CmisRuntimeException("Manage permission error. Exception: "
					+ resp.getErrorContent());
	}

	public void addAutoVersion(Document doc,
			final boolean autoVersionOnUpdateProps) {
		String link = baseURL.concat(
				"service/api/metadata/node/");
		link = link.concat(doc.getProperty(ALFCMIS_NODEREF).getValueAsString().replace(":/", ""));
		UrlBuilder url = new UrlBuilder(link);
		Response resp = CmisBindingsHelper.getHttpInvoker(siglaBindingSession).invokePOST(url,
				MimeTypes.JSON.mimetype(), new Output() {
					public void write(OutputStream out) throws Exception {
						JSONObject jsonObject = new JSONObject();
						JSONObject jsonObjectProp = new JSONObject();
						jsonObjectProp.put("cm:autoVersion", true);
						jsonObjectProp.put("cm:autoVersionOnUpdateProps",
								autoVersionOnUpdateProps);
						jsonObject.put("properties", jsonObjectProp);
						out.write(jsonObject.toString().getBytes());
					}
				}, siglaBindingSession);
		int status = resp.getResponseCode();
		if (status == HttpStatus.SC_NOT_FOUND
				|| status == HttpStatus.SC_BAD_REQUEST
				|| status == HttpStatus.SC_INTERNAL_SERVER_ERROR)
			throw new CmisRuntimeException("Add Auto Version. Exception: "
					+ resp.getErrorContent());
	}
}
