package it.cnr.contab.cmis.service;

import it.cnr.cmisdl.model.ACL;
import it.cnr.cmisdl.model.Node;
import it.cnr.cmisdl.model.paging.ListNodePage;
import it.cnr.cmisdl.model.paging.PagingNode;
import it.cnr.cmisdl.model.property.AspectMetdata;
import it.cnr.cmisdl.service.AuthenticationService;
import it.cnr.cmisdl.service.ContentService;
import it.cnr.cmisdl.service.DictionaryService;
import it.cnr.cmisdl.service.NodeService;
import it.cnr.cmisdl.service.RepositoryService;
import it.cnr.cmisdl.service.SearchService;
import it.cnr.contab.cmis.CMISRelationship;
import it.cnr.contab.cmis.acl.Permission;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CMISService {
	private transient static final Log logger = LogFactory.getLog(CMISService.class);
	private NodeService nodeService;
	private SearchService searchService;
	private DictionaryService dictionaryService;
	private AuthenticationService authenticationService;
	private ContentService contentService;
	private CMISBulkInfo<?> cmisBulkInfo;
	private RepositoryService repositoryService;
	protected Credentials systemCredentials;
	
	public void init(){
		systemCredentials = authenticationService.getUserSession(
				(UsernamePasswordCredentials) authenticationService.getSystemCredentials());
	}
	
	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public void setDictionaryService(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	public void setAuthenticationService(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	public void setCmisBulkInfo(CMISBulkInfo cmisBulkInfo) {
		this.cmisBulkInfo = cmisBulkInfo;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public String getRepositoyURL(){
		String target = repositoryService.getRepositoryApiURL().toString();
		target = target.replace("/service/api/cmis", "/service").replace("/cmisatom", "/service");
		return target;
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

	public Node getNodeByPath(CMISPath cmisPath){
		return nodeService.getNodeByPath(systemCredentials, cmisPath.getPath());
	}
	
	public Node getNodeByNodeRef(String nodeRef){
		return nodeService.getNodeByNodeRef(systemCredentials, nodeRef);
	}

	public Node getNodeByNodeRef(String nodeRef, Credentials customCredentials){
		return nodeService.getNodeByNodeRef(customCredentials, nodeRef);
	}
	
	public CMISPath createFolderIfNotPresent(CMISPath cmisPath, String folderName, String title, String description){
		return createFolderIfNotPresent(cmisPath, folderName, title, description, null);
	}

	public CMISPath createFolderIfNotPresent(CMISPath cmisPath, String folderName, String title, String description, OggettoBulk oggettoBulk){
		return createFolderIfNotPresent(cmisPath, folderName, title, description, oggettoBulk, null);
	}

	public CMISPath createFolderIfNotPresent(CMISPath cmisPath, String folderName, String title, String description, OggettoBulk oggettoBulk, String objectTypeName){
		Node node = nodeService.getNodeByPath(systemCredentials, cmisPath.getPath());
		List<Property<?>> metadataProperties = new ArrayList<Property<?>>();
		List<String> aspectsToAdd = new ArrayList<String>();
		List<Property<?>> aspectProperties = new ArrayList<Property<?>>();
		try{
			folderName = sanitizeFolderName(folderName);
			metadataProperties.add(
					nodeService.getDictionaryService().createProperty(systemCredentials, 
							BaseTypeId.CMIS_FOLDER.value(), 
							PropertyIds.NAME, Arrays.asList(folderName)));
			aspectsToAdd.add(AspectMetdata.getCmisAspectName(AspectMetdata.ASPECT_TITLED));
			if (title != null)
				aspectProperties.add(
					nodeService.getDictionaryService().createProperty(systemCredentials, 
							AspectMetdata.getCmisAspectName(AspectMetdata.ASPECT_TITLED), 
							AspectMetdata.getPrefixLocalPart(AspectMetdata.PROPERTY_TITLE), 
							Arrays.asList(title)));
			if (description != null)
				aspectProperties.add(
					nodeService.getDictionaryService().createProperty(systemCredentials, 
							AspectMetdata.getCmisAspectName(AspectMetdata.ASPECT_TITLED), 
							AspectMetdata.getPrefixLocalPart(AspectMetdata.PROPERTY_DESCRIPTION), 
							Arrays.asList(description)));

			if (oggettoBulk != null) {
				if (cmisBulkInfo.getType(systemCredentials, oggettoBulk)!=null || objectTypeName!=null)
					metadataProperties.add(
							nodeService.getDictionaryService().createProperty(systemCredentials, 
									(objectTypeName==null?cmisBulkInfo.getType(systemCredentials, oggettoBulk).getId():objectTypeName),
									PropertyIds.OBJECT_TYPE_ID, Arrays.asList(cmisBulkInfo.getType(systemCredentials, oggettoBulk).getId())));
				metadataProperties.addAll(cmisBulkInfo.getProperty(systemCredentials, oggettoBulk));
				aspectsToAdd.addAll(cmisBulkInfo.getAspect(systemCredentials, oggettoBulk));
				aspectProperties.addAll(cmisBulkInfo.getAspectProperty(systemCredentials, oggettoBulk));
			}
			Node folder = nodeService.createFolder(systemCredentials, node, metadataProperties, aspectsToAdd, aspectProperties);
			return CMISPath.construct(folder.getPath());
		}catch(CmisConstraintException _ex){
			if (oggettoBulk!=null){
				Node folder = nodeService.getNodeByPath(systemCredentials, cmisPath.getPath()+
																    (cmisPath.getPath().equals("/")?"":"/")+
																    sanitizeFilename(folderName).toLowerCase());
				folder = nodeService.updateNode(systemCredentials, folder, metadataProperties, aspectsToAdd, null, aspectProperties);
				return CMISPath.construct(folder.getPath());
			}
			return cmisPath.appendToPath(folderName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	public void deleteNode(Node node){
		nodeService.deleteNode(systemCredentials, node);
	}
	
	public InputStream getResource(Node node){
		return contentService.retreiveContent(node, systemCredentials);
	}
	
	public Node storePrintDocument(OggettoBulk oggettoBulk, Report report, CMISPath cmisPath, Permission... permissions){
		try {
			return storeSimpleDocument(oggettoBulk, report.getInputStream(), report.getContentType(), report.getName(), cmisPath, permissions);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Node storeSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name, 
			CMISPath cmisPath, Permission... permissions){
		return storeSimpleDocument(oggettoBulk, inputStream, contentType, name, cmisPath, null, false, permissions);
	}
	
	public Node storeSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name, 
				CMISPath cmisPath, String objectTypeName, boolean makeVersionable, Permission... permissions){
		Node parentNode = nodeService.getNodeByPath(systemCredentials, cmisPath.getPath());
		try {
			name = sanitizeFilename(name);
			Node node = nodeService.createContent(systemCredentials, parentNode, inputStream, name, 
					contentType, (objectTypeName==null?cmisBulkInfo.getType(systemCredentials, oggettoBulk).getId():objectTypeName), 
					cmisBulkInfo.getProperty(systemCredentials, oggettoBulk), 
					cmisBulkInfo.getAspect(systemCredentials, oggettoBulk), 
					cmisBulkInfo.getAspectProperty(systemCredentials, oggettoBulk));
			if (permissions.length > 0 ){
				nodeService.setInheritedPermission(systemCredentials, node, Boolean.FALSE);
				for (Permission permission : permissions) {
					nodeService.addACL(systemCredentials, node, permission.getUserName(), permission.getRole().getRoleName());
				}
			}

			if (makeVersionable)
				nodeService.makeVersionable(systemCredentials, node);
			return node;
		}catch (CmisBaseException e) {
			throw e;
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Node restoreSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name, 
			CMISPath cmisPath, Permission... permissions){
		return restoreSimpleDocument(oggettoBulk, inputStream, contentType, name, cmisPath, null, false, permissions);
	}

	public Node restoreSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name, 
			CMISPath cmisPath, String objectTypeName, boolean makeVersionable, Permission... permissions){
		Node node = null;
		try {
			node = nodeService.getNodeByPath(systemCredentials, cmisPath.getPath()+
											(cmisPath.getPath().equals("/")?"":"/")+
											sanitizeFilename(name).toLowerCase());
		} catch (CmisObjectNotFoundException e){
			return storeSimpleDocument(oggettoBulk, inputStream, contentType, name, cmisPath, objectTypeName, makeVersionable, permissions);
		}
		updateContent(node.getId(), inputStream, contentType);
		return node;
	}

	public void updateProperties(OggettoBulk oggettoBulk, Node node){
		try {
			nodeService.updateNode(systemCredentials, node, 
				cmisBulkInfo.getProperty(systemCredentials, oggettoBulk), 
				cmisBulkInfo.getAspect(systemCredentials, oggettoBulk), null, 
					cmisBulkInfo.getAspectProperty(systemCredentials, oggettoBulk));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void updateContent(String nodeRef, InputStream inputStream, String contentType){
		contentService.uploadContent(nodeService.getNodeByNodeRef(systemCredentials, nodeRef), 
				inputStream, contentType, systemCredentials);
	}

	public void createRelationship(String sourceNodeRef, String sourceNodeTarget, CMISRelationship relationship){
		nodeService.createRelationship(systemCredentials, 
				nodeService.getNodeByNodeRef(systemCredentials, sourceNodeRef), 
				nodeService.getNodeByNodeRef(systemCredentials, sourceNodeTarget), relationship.value());
	}
	
	public ListNodePage<Node> getRelationship(String sourceNodeRef, CMISRelationship relationship){
		return nodeService.getRelationshipFromSource(systemCredentials, 
				nodeService.getNodeByNodeRef(systemCredentials, sourceNodeRef), relationship.value());	
	}

	public ListNodePage<Node> getRelationshipFromTarget(String sourceNodeRef, CMISRelationship relationship){
		return nodeService.getRelationshipFromTarget(systemCredentials, 
				nodeService.getNodeByNodeRef(systemCredentials, sourceNodeRef), relationship.value());	
	}
	
	public void makeVersionable(Node node){
		nodeService.makeVersionable(systemCredentials, node);
	}
	
	public ListNodePage<Node> getChildren(Node node, String orderBy, PagingNode pagingNode){
		if (pagingNode == null)
			pagingNode = new PagingNode() {
				public int getSkipCount() {
					return 0;
				}
				public int getPage() {
					return Integer.MAX_VALUE;
				}
				public int getMaxItems() {
					return Integer.MAX_VALUE;
				}
			};
		return nodeService.retreiveChildren(systemCredentials, node, orderBy, pagingNode);
	}
	
	public List<ACL> addConsumerToEveryone(Node node){
		return nodeService.addACL(systemCredentials, node, "GROUP_EVERYONE", "cmis:read");
	}

	public List<ACL> removeConsumerToEveryone(Node node){
		return nodeService.removeACL(systemCredentials, node, "GROUP_EVERYONE", "cmis:read");
	}

	public void copyNode(Node source, Node target){
		nodeService.copyNode(systemCredentials, source, target);
	}
	
	public ListNodePage<Node> search(StringBuffer query){
		return searchService.search(systemCredentials, query, null, null);
	}

	public ListNodePage<Node> search(StringBuffer query, Boolean fetchNode){
		return searchService.search(systemCredentials, query, null, null, fetchNode);
	}

	public void addAspect(Node node, String... aspectName){
		nodeService.updateNode(systemCredentials, node, null, Arrays.asList(aspectName), null, null);		
	}

	public void removeAspect(Node node, String... aspectName){
		nodeService.updateNode(systemCredentials, node, null, null, Arrays.asList(aspectName), null);		
	}

	public void setInheritedPermission(CMISPath cmisPath, Boolean inheritedPermission){
		nodeService.setInheritedPermission(systemCredentials, nodeService.getNodeByPath(systemCredentials, cmisPath.getPath()), inheritedPermission);
	}
	public ACL getACL(Node node, String userName, String permission) {
		List<ACL> listACL = nodeService.getACL(systemCredentials, node);
		for (ACL acl : listACL) {
			if (acl.getPrincipal().getName().equals(userName) && acl.getPermission().getKey().equals(permission))
				return acl;
		}
		return null;
	}
}
