package it.cnr.contab.spring.storage.config;

import it.cnr.contab.spring.storage.MimeTypes;
import it.cnr.contab.spring.storage.SiglaStorageService;
import it.cnr.contab.spring.storage.StorageException;
import it.cnr.contab.spring.storage.StorageObject;
import it.cnr.jada.persistency.ObjectNotFoundException;
import org.apache.chemistry.opencmis.client.api.*;
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
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.RelationshipDirection;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.*;
import org.apache.chemistry.opencmis.commons.impl.UrlBuilder;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.*;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created by mspasiano on 6/15/17.
 */
@Configuration
@Profile("CMIS")
public class CMISSiglaStorageConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CMISSiglaStorageConfiguration.class);

    @Value("${org.apache.chemistry.opencmis.binding.atompub.url}")
    private String ATOMPUB_URL;
    @Value("${org.apache.chemistry.opencmis.binding.spi.type}")
    private String BINDING_TYPE;
    @Value("${org.apache.chemistry.opencmis.binding.connecttimeout}")
    private String CONNECT_TIMEOUT;
    @Value("${org.apache.chemistry.opencmis.binding.readtimeout}")
    private String READ_TIMEOUT;
    @Value("${org.apache.chemistry.opencmis.binding.httpinvoker.classname}")
    private String HTTP_INVOKER_CLASS;

    @Value("${repository.base.url}")
    private String baseURL;

    @Value("${user.admin.username}")
    private String adminUserName;
    @Value("${user.admin.password}")
    private String adminPassword;

    @Bean
    public SiglaStorageService storageService() {
        SiglaStorageService siglaStorageService =  new SiglaStorageService() {
            private Session siglaSession;
            private BindingSession siglaBindingSession;
            private static final String ZIP_CONTENT = "service/zipper/zipContent";

            @Override
            public void init() {
                this.siglaSession = createSession();
                createBindingSession();
            }

            public Session createSession(){
                return createSession(adminUserName, adminPassword);
            }

            public Session createSession(String userName, String password){
                SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
                Map<String, String> sessionParameters = new HashMap<String, String>();
                sessionParameters.put(SessionParameter.ATOMPUB_URL, ATOMPUB_URL);
                sessionParameters.put(SessionParameter.BINDING_TYPE, BINDING_TYPE);
                sessionParameters.put(SessionParameter.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
                sessionParameters.put(SessionParameter.READ_TIMEOUT, READ_TIMEOUT);
                sessionParameters.put(SessionParameter.HTTP_INVOKER_CLASS, HTTP_INVOKER_CLASS);
                sessionParameters.put(SessionParameter.USER, userName);
                sessionParameters.put(SessionParameter.PASSWORD, password);

                sessionParameters.put(SessionParameter.CONNECT_TIMEOUT, "10000");
                sessionParameters.put(SessionParameter.REPOSITORY_ID, sessionFactory.getRepositories(sessionParameters).get(0).getId());
                sessionParameters.put(SessionParameter.LOCALE_ISO3166_COUNTRY, Locale.ITALY.getCountry());
                sessionParameters.put(SessionParameter.LOCALE_ISO639_LANGUAGE, Locale.ITALY.getLanguage());
                sessionParameters.put(SessionParameter.LOCALE_VARIANT, Locale.ITALY.getVariant());
                sessionParameters.put(SessionParameter.CACHE_PATH_OMIT,String.valueOf(Boolean.TRUE));
                Session session = sessionFactory.createSession(sessionParameters);
                OperationContext operationContext = OperationContextUtils.createMaximumOperationContext();
                operationContext.setMaxItemsPerPage(Integer.MAX_VALUE);
                session.setDefaultContext(operationContext);
                return session;
            }

            public void createBindingSession(){
                BindingSession session = new SessionImpl();
                Map<String, String> sessionParameters = new HashMap<String, String>();
                sessionParameters.put(SessionParameter.ATOMPUB_URL, ATOMPUB_URL);
                sessionParameters.put(SessionParameter.BINDING_TYPE, BINDING_TYPE);
                sessionParameters.put(SessionParameter.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
                sessionParameters.put(SessionParameter.READ_TIMEOUT, READ_TIMEOUT);
                sessionParameters.put(SessionParameter.HTTP_INVOKER_CLASS, HTTP_INVOKER_CLASS);
                sessionParameters.put(SessionParameter.USER, adminUserName);
                sessionParameters.put(SessionParameter.PASSWORD, adminPassword);
                if (!sessionParameters.containsKey(SessionParameter.AUTHENTICATION_PROVIDER_CLASS)) {
                    sessionParameters.put(SessionParameter.AUTHENTICATION_PROVIDER_CLASS, CmisBindingFactory.STANDARD_AUTHENTICATION_PROVIDER);
                }
                sessionParameters.put(SessionParameter.AUTH_HTTP_BASIC, "true");
                sessionParameters.put(SessionParameter.AUTH_SOAP_USERNAMETOKEN, "false");
                sessionParameters.put(SessionParameter.CACHE_PATH_OMIT,String.valueOf(Boolean.TRUE));
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
                this.siglaBindingSession = session;
            }

            public void addAutoVersion(Document doc,
                                       final boolean autoVersionOnUpdateProps) throws StorageException {
                String link = baseURL.concat(
                        "service/api/metadata/node/");
                link = link.concat(doc.getProperty(StoragePropertyNames.ALFCMIS_NODEREF.value()).getValueAsString().replace(":/", ""));
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
                    throw new StorageException(StorageException.Type.GENERIC, "Add Auto Version. Exception: "+ resp.getErrorContent());
            }

            private Map<String, Object> convertProperties(List<Property<?>> properties) {
                return properties.stream()
                        .collect(HashMap::new, (m,v)->m.put(v.getId(), v.getValue()), HashMap::putAll);
            }

            private Map<String, Object> convertPropertiesData(List<PropertyData<?>> properties) {
                return properties.stream()
                        .collect(HashMap::new, (m,v)->m.put(v.getId(), v.getValues()), HashMap::putAll);
            }

            private String getPath(CmisObject cmisObject) {
                if (Optional.ofNullable(cmisObject)
                        .filter(cmisObject1 -> cmisObject1.getBaseTypeId().equals(BaseTypeId.CMIS_FOLDER)).isPresent()) {
                    return ((Folder)cmisObject).getPath();
                } else {
                    return Optional.ofNullable(cmisObject)
                            .map(Document.class::cast)
                            .map(document1 -> document1.<String>getPropertyValue(PropertyIds.PARENT_ID))
                            .map(parentId -> siglaSession.getObject(parentId))
                            .map(Folder.class::cast)
                            .map(folder -> folder.getPath().concat(SiglaStorageService.SUFFIX).concat(cmisObject.getName()))
                            .orElse(null);
                }
            }

            @Override
            public StorageObject createFolder(String path, String name, Map<String, Object> metadata) {
                return Optional.ofNullable(siglaSession.getObjectByPath(path))
                    .map(Folder.class::cast)
                    .map(folder -> siglaSession.createFolder(metadata, folder))
                    .map(objectId -> siglaSession.getObject(objectId))
                    .map(Folder.class::cast)
                    .map(folder -> new StorageObject(folder.getId(), folder.getPath(), convertProperties(folder.getProperties())))
                    .orElseThrow(() -> new StorageException(StorageException.Type.INVALID_ARGUMENTS, "You must specify path for create folder"));
            }

            @Override
            public StorageObject createDocument(InputStream inputStream, String contentType, Map<String, Object> metadataProperties,
                                                StorageObject parentObject, String path, boolean makeVersionable, Permission... permissions) {
                return Optional.ofNullable(siglaSession.createObjectId(parentObject.getKey()))
                        .map(objectId -> {
                                try {
                                    return siglaSession.createDocument(
                                            metadataProperties,
                                            objectId,
                                            new ContentStreamImpl(
                                                    String.valueOf(metadataProperties.get(StoragePropertyNames.NAME.value())),
                                                    BigInteger.ZERO,
                                                    contentType,
                                                    inputStream),
                                            VersioningState.MAJOR);
                                } catch (CmisConstraintException |CmisNameConstraintViolationException _ex) {
                                    throw new StorageException(StorageException.Type.CONSTRAINT_VIOLATED, _ex.getMessage(), _ex);
                                } catch (CmisBaseException _ex) {
                                    throw new StorageException(StorageException.Type.GENERIC, _ex.getMessage(), _ex);
                                }
                        })
                        .map(objectId -> siglaSession.getObject(objectId))
                        .map(Document.class::cast)
                        .map(document -> {
                            if (makeVersionable) {
                                addAutoVersion(document, false);
                            }
                            return document;
                        })
                        .map(document -> new StorageObject(document.getId(), getPath(document), convertProperties(document.getProperties())))
                        .orElseThrow(() -> new StorageException(StorageException.Type.INVALID_ARGUMENTS, "You must specify key parent for create document"));
            }

            @Override
            public void updateProperties(StorageObject storageObject, Map<String, Object> metadataProperties) {
                Optional.ofNullable(siglaSession.getObject(storageObject.getKey()))
                        .map(cmisObject -> {
                            Optional.ofNullable(metadataProperties.get(StoragePropertyNames.NAME.value()))
                                    .ifPresent(name -> {
                                        cmisObject.updateProperties(Collections.singletonMap(StoragePropertyNames.NAME.value(), name), true);
                                    });
                            return cmisObject.updateProperties(metadataProperties);
                        });
            }

            @Override
            public StorageObject updateStream(String key, InputStream inputStream, String contentType) {
                return Optional.ofNullable(siglaSession.getObject(key))
                        .map(cmisObject -> (Document)cmisObject)
                        .map(document -> {
                                return Optional.ofNullable(document.setContentStream(
                                        new ContentStreamImpl(
                                                null,
                                                BigInteger.ZERO,
                                                contentType,
                                                inputStream), true))
                                        .orElse((Document)siglaSession.getObject(key));
                                })
                        .map(document -> new StorageObject(document.getId(), getPath(document), convertProperties(document.getProperties())))
                        .orElseThrow(() -> new StorageException(StorageException.Type.INVALID_ARGUMENTS, "You must specify key for update stream"));
            }

            @Override
            public InputStream getInputStream(String key) {
                return Optional.ofNullable(siglaSession.getObject(key))
                        .map(Document.class::cast)
                        .map(document -> document.getContentStream().getStream())
                        .orElseThrow(() -> new StorageException(StorageException.Type.INVALID_ARGUMENTS, "You must specify key for get input stream"));
            }

            public InputStream getInputStream(String key, Boolean majorVersion) {
                return Optional.ofNullable(siglaSession.getObject(key))
                        .map(Document.class::cast)
                        .map(document -> document.getObjectOfLatestVersion(majorVersion))
                        .map(document -> document.getContentStream().getStream())
                        .orElseThrow(() -> new StorageException(StorageException.Type.INVALID_ARGUMENTS, "You must specify key for get input stream"));
            }

            public InputStream getInputStream(String key, String versionId) {
                return Optional.ofNullable(siglaSession.getObject(key))
                        .map(Document.class::cast)
                        .map(document ->
                                document.getAllVersions().stream()
                                        .filter(document1 -> document1.getVersionLabel().equals(versionId))
                                        .findFirst()
                                        .get()
                        )
                        .map(document -> document.getContentStream().getStream())
                        .orElseThrow(() -> new StorageException(StorageException.Type.INVALID_ARGUMENTS, "You must specify key for get input stream"));
            }
            @Override
            public Boolean delete(String id) {
                try {
                    Optional<CmisObject> cmisObject = Optional.ofNullable(siglaSession.getObject(id));
                    boolean exists = cmisObject.isPresent();
                    if (exists) {
                        if (cmisObject.get().getBaseTypeId().equals(StoragePropertyNames.CMIS_FOLDER.value())){
                            ((Folder)cmisObject.get()).deleteTree(true, UnfileObject.DELETE, false);
                        } else {
                            ((Document)cmisObject.get()).delete();
                        }
                    }  else {
                        logger.warn("item {} does not exist", id);
                    }
                    return exists;
                } catch(CmisObjectNotFoundException _ex) {
                    logger.warn("item {} does not exist", id);
                    return false;
                }
            }
            @Override
            public StorageObject getObject(String id) {
                try {
                    return Optional.ofNullable(siglaSession.getObject(id))
                            .map(cmisObject -> new StorageObject(cmisObject.getId(), getPath(cmisObject), convertProperties(cmisObject.getProperties())))
                            .orElse(null);
                } catch (CmisObjectNotFoundException _ex) {
                    return null;
                }
            }

            @Override
            public StorageObject getObject(String id, UsernamePasswordCredentials customCredentials) {
                try {
                    return Optional.ofNullable(createSession(customCredentials.getUserName(), customCredentials.getPassword()).getObject(id))
                            .map(cmisObject -> new StorageObject(cmisObject.getId(), getPath(cmisObject), convertProperties(cmisObject.getProperties())))
                            .orElse(null);
                } catch (CmisObjectNotFoundException _ex) {
                    return null;
                }
            }

            @Override
            public StorageObject getObjectByPath(String path, boolean isFolder) {
                try {
                    return Optional.ofNullable(siglaSession.getObjectByPath(path))
                            .map(cmisObject -> new StorageObject(cmisObject.getId(), getPath(cmisObject), convertProperties(cmisObject.getProperties())))
                            .orElse(null);
                } catch (CmisObjectNotFoundException _ex) {
                    return null;
                }
            }

            @Override
            public List<StorageObject> getChildren(String key) {
                return Optional.ofNullable(siglaSession.getObject(key))
                    .map(Folder.class::cast)
                    .map(folder -> folder.getChildren())
                    .map(cmisObjects -> {
                        List<StorageObject> list = new ArrayList<StorageObject>();
                        cmisObjects.forEach(cmisObject ->
                                list.add(new StorageObject(cmisObject.getId(),
                                        getPath(cmisObject),
                                        convertProperties(cmisObject.getProperties()))));
                        return list;
                    })
                    .orElse(Collections.EMPTY_LIST);
            }

            @Override
            public List<StorageObject> search(String query) {
                return Optional.ofNullable(query)
                        .map(statement -> siglaSession.query(statement, false))
                        .map(queryResults -> {
                            List<StorageObject> list = new ArrayList<StorageObject>();
                            if (queryResults.getTotalNumItems() > 0) {
                                queryResults.forEach(queryResult -> list.add(new StorageObject(
                                        Optional.ofNullable(
                                                queryResult.getPropertyValueById(StoragePropertyNames.ID.value())
                                        ).map(String.class::cast).orElse(null),
                                        Optional.ofNullable(
                                                queryResult.getPropertyValueById(StoragePropertyNames.PATH.value())
                                        ).map(String.class::cast).orElse(null),
                                        convertPropertiesData(queryResult.getProperties()))));
                            }
                            return list;
                        })
                        .orElse(Collections.EMPTY_LIST);
            }

            @Override
            public InputStream zipContent(List<String> keys) {
                UrlBuilder url = new UrlBuilder(baseURL.concat(ZIP_CONTENT));
                url.addParameter("destination", getObjectByPath("/User Homes/sigla", true).getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()));
                url.addParameter("filename", "Documenti contabili.zip");
                url.addParameter("noaccent", true);
                url.addParameter("getParent", true);
                url.addParameter("download", false);
                final JSONObject json = new JSONObject();
                json.put("nodes", keys);
                siglaBindingSession.put(SessionParameter.READ_TIMEOUT, -1);
                Response resZipContent = CmisBindingsHelper.getHttpInvoker(siglaBindingSession).invokePOST(url, MimeTypes.JSON.mimetype(),
                        new Output() {
                            public void write(OutputStream out) throws Exception {
                                out.write(json.toString().getBytes());
                            }
                        }, siglaBindingSession);
                if (resZipContent.getResponseCode() != HttpStatus.SC_OK) {
                    throw new StorageException(StorageException.Type.GENERIC, resZipContent.getErrorContent());
                }
                try {
                    JSONObject  jsonObject = new JSONObject(IOUtils.toString(resZipContent.getStream()));
                    return getInputStream(jsonObject.getString("nodeRef"));
                } catch (IOException e) {
                    throw new StorageException(StorageException.Type.GENERIC, e);
                }
            }

            @Override
            public String signDocuments(String json, String url) {
                try {
                    String webScriptURL = baseURL.concat(url);
                    UrlBuilder urlBuilder = new UrlBuilder(new URIBuilder(webScriptURL).build().toString());
                    Response response = CmisBindingsHelper.getHttpInvoker(siglaBindingSession).invokePOST(urlBuilder, MimeTypes.JSON.mimetype(),
                            new Output() {
                                public void write(OutputStream out) throws Exception {
                                    out.write(json.getBytes());
                                }
                            }, siglaBindingSession);
                    int status = response.getResponseCode();
                    if (status == HttpStatus.SC_NOT_FOUND
                            || status == HttpStatus.SC_INTERNAL_SERVER_ERROR
                            || status == HttpStatus.SC_UNAUTHORIZED
                            || status == HttpStatus.SC_BAD_REQUEST) {
                        JSONTokener tokenizer = new JSONTokener(new StringReader(response.getErrorContent()));
                        JSONObject jsonObject = new JSONObject(tokenizer);
                        String jsonMessage = jsonObject.getString("message");
                        throw new StorageException(StorageException.Type.GENERIC, jsonMessage);
                    }
                    JSONTokener tokenizer = new JSONTokener(new InputStreamReader(response.getStream()));
                    JSONObject jsonObject = new JSONObject(tokenizer);
                    return jsonObject.optString("nodeRef", null);
                } catch (URISyntaxException e) {
                    throw new StorageException(StorageException.Type.GENERIC, e);
                }
            }

            @Override
            public void copyNode(StorageObject source, StorageObject target) {
                Optional.ofNullable(source)
                        .map(storageObject -> siglaSession.getObject(storageObject.getKey()))
                        .map(Document.class::cast)
                        .ifPresent(document -> {
                            try {
                                document.addToFolder((Folder) siglaSession.getObject(target.getKey()), true);
                            } catch (CmisRuntimeException _ex) {
                                logger.warn(_ex.getMessage(), _ex);
                            }
                        });
            }

            @Override
            public void managePermission(StorageObject storageObject, Map<String, ACLType> permission, boolean remove) {
                String link = baseURL
                        .concat("service/cnr/nodes/permissions/")
                        .concat(storageObject.<String>getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()).replace(":/", ""));
                UrlBuilder url = new UrlBuilder(link);
                Response resp = CmisBindingsHelper.getHttpInvoker(siglaBindingSession).invokePOST(url,
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
                        }, siglaBindingSession);
                int status = resp.getResponseCode();

                logger.info((remove ? "remove" : "add") + " permission " + permission + " on item "
                        + storageObject.getKey() + ", status = " + status);

                if (status == HttpStatus.SC_NOT_FOUND
                        || status == HttpStatus.SC_BAD_REQUEST
                        || status == HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    throw new StorageException(StorageException.Type.GENERIC, "Manage permission error. Exception: " + resp.getErrorContent());
            }

            @Override
            public void setInheritedPermission(StorageObject storageObject, Boolean inherited) {
                String link = baseURL
                        .concat("service/cnr/nodes/permissions/")
                        .concat(storageObject.<String>getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()).replace(":/", ""));
                UrlBuilder url = new UrlBuilder(link);
                Response resp = CmisBindingsHelper.getHttpInvoker(siglaBindingSession).invokePOST(url,
                        MimeTypes.JSON.mimetype(), new Output() {
                            public void write(OutputStream out) throws Exception {
                                JSONObject jsonObject = new JSONObject();
                                JSONArray jsonArray = new JSONArray();
                                jsonObject.put("permissions", jsonArray);
                                jsonObject.put("isInherited", inherited);
                                out.write(jsonObject.toString().getBytes());
                            }
                        }, siglaBindingSession);
                int status = resp.getResponseCode();
                if (status == HttpStatus.SC_NOT_FOUND
                        || status == HttpStatus.SC_BAD_REQUEST
                        || status == HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    throw new StorageException(StorageException.Type.GENERIC, "Inherited Permission error. Exception: "
                            + resp.getErrorContent());
            }

            @Override
            public List<StorageObject> getRelationship(String key, String relationshipName, boolean fromTarget) {
                List<StorageObject> result = new ArrayList<StorageObject>();
                siglaSession.getRelationships(new ObjectIdImpl(key), true,
                        fromTarget ? RelationshipDirection.TARGET : RelationshipDirection.SOURCE, null, siglaSession.getDefaultContext())
                        .iterator()
                        .forEachRemaining(relationship -> {
                            CmisObject cmisObject = fromTarget ? relationship.getSource() : relationship.getTarget();
                            result.add(new StorageObject(cmisObject.getId(),
                                    getPath(cmisObject),
                                    convertProperties(cmisObject.getProperties())));
                        });
                return result;
            }

            @Override
            public void createRelationship(String source, String target, String relationshipName) {
                Map<String, String> properties = new HashMap<String, String>();
                properties.put(PropertyIds.OBJECT_TYPE_ID, relationshipName);
                properties.put(PropertyIds.SOURCE_ID, source);
                properties.put(PropertyIds.TARGET_ID, target);
                siglaSession.createRelationship(properties);
            }

            @Override
            public StoreType getStoreType() {
                return StoreType.CMIS;
            }
        };
        siglaStorageService.init();
        return siglaStorageService;
    }
}
