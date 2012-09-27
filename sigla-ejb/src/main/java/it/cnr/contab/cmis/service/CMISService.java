package it.cnr.contab.cmis.service;

import it.cnr.cmisdl.model.Node;
import it.cnr.cmisdl.model.paging.ListNodePage;
import it.cnr.cmisdl.model.property.AspectMetdata;
import it.cnr.cmisdl.service.AuthenticationService;
import it.cnr.cmisdl.service.ContentService;
import it.cnr.cmisdl.service.DictionaryService;
import it.cnr.cmisdl.service.NodeService;
import it.cnr.cmisdl.service.SearchService;
import it.cnr.contab.cmis.CMISRelationship;
import it.cnr.contab.cmis.acl.Permission;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.jada.bulk.OggettoBulk;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.management.RuntimeErrorException;

import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class CMISService {
	private transient static final Log logger = LogFactory.getLog(CMISService.class);
	private NodeService nodeService;
	private SearchService searchService;
	private DictionaryService dictionaryService;
	private AuthenticationService authenticationService;
	private ContentService contentService;
	private CMISBulkInfo<?> cmisBulkInfo;
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

	public CMISPath createFolderIfNotPresent(CMISPath cmisPath, String folderName, String title, String description){
		Node node = nodeService.getNodeByPath(systemCredentials, cmisPath.getPath());
		try{
			List<Property<?>> metadataProperties = new ArrayList<Property<?>>();
			List<String> aspectsToAdd = new ArrayList<String>();
			List<Property<?>> aspectProperties = new ArrayList<Property<?>>();
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
			Node folder = nodeService.createFolder(systemCredentials, node, metadataProperties, aspectsToAdd, aspectProperties);
			return CMISPath.construct(folder.getPath());
		}catch(CmisConstraintException _ex){
			return cmisPath.appendToPath(folderName);
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
	
	public String getContentType(InputStream inputStream,  String contentType){
		ContentHandler contenthandler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		AutoDetectParser parser = new AutoDetectParser();
		try {
			parser.parse(inputStream, contenthandler, metadata);
			return metadata.get(Metadata.CONTENT_TYPE);
		} catch(Throwable e){
		}
		return contentType;
	}
	
	public Node storeSimpleDocument(OggettoBulk oggettoBulk, InputStream inputStream, String contentType, String name, 
				CMISPath cmisPath, Permission... permissions){
		Node parentNode = nodeService.getNodeByPath(systemCredentials, cmisPath.getPath());
		try {
			name = sanitizeFilename(name);
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    IOUtils.copy(inputStream, baos);
			Node node = nodeService.createContent(systemCredentials, parentNode, new ByteArrayInputStream(baos.toByteArray()), name, 
					getContentType(new ByteArrayInputStream(baos.toByteArray()),contentType), cmisBulkInfo.getType(systemCredentials, oggettoBulk).getId(), 
					cmisBulkInfo.getProperty(systemCredentials, oggettoBulk), 
					cmisBulkInfo.getAspect(systemCredentials, oggettoBulk), 
					cmisBulkInfo.getAspectProperty(systemCredentials, oggettoBulk));
			if (permissions.length > 0 ){
				nodeService.setInheritedPermission(systemCredentials, node, Boolean.FALSE);
				for (Permission permission : permissions) {
					nodeService.addACL(systemCredentials, node, permission.getUserName(), permission.getRole().getRoleName());
				}
			}
			//TODO non dovrebbe essere necessario, ma non so perchè i metadati non li prende in creazione
			updateProperties(oggettoBulk, node);
			return node;
		} catch (CmisBaseException e) {
			e.printStackTrace();
			System.err.println(e.getErrorContent());
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
}
