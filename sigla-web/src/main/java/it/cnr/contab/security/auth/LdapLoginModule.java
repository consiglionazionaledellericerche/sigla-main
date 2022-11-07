/*
 * Copyright (C) 2022  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.security.auth;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.auth.spi.LdapExtLoginModule;

import javax.naming.*;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class LdapLoginModule extends LdapExtLoginModule {
    private static final String USER_ATTRIBUTES = "userAttributes";

    @Override
    protected String bindDNAuthentication(InitialLdapContext ctx, String user, Object credential, String baseDN, String filter) throws NamingException {
        SearchControls constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        constraints.setTimeLimit(searchTimeLimit);
        final String[] attributes = Optional.ofNullable(options.get(USER_ATTRIBUTES))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(s -> s.split(";"))
                .orElse(null);
        constraints.setReturningAttributes(attributes);

        NamingEnumeration results = null;

        Object[] filterArgs = {user};

        LdapContext ldapCtx = ctx;

        boolean referralsLeft = true;
        SearchResult sr = null;
        while (referralsLeft) {
            try {
                results = ldapCtx.search(baseDN, filter, filterArgs, constraints);
                while (results.hasMore()) {
                    sr = (SearchResult) results.next();
                    break;
                }
                referralsLeft = false;
            } catch (ReferralException e) {
                ldapCtx = (LdapContext) e.getReferralContext();
                if (results != null) {
                    results.close();
                }
            }
        }

        if (sr == null) {
            results.close();
            throw PicketBoxMessages.MESSAGES.failedToFindBaseContextDN(baseDN);
        }

        String name = sr.getName();
        String userDN = null;
        Attributes attrs = sr.getAttributes();
        if (attrs != null) {
            Attribute dn = attrs.get(distinguishedNameAttribute);
            if (dn != null) {
                userDN = (String) dn.get();
            }
        }
        Optional.ofNullable(getIdentity())
                .filter(SIGLALDAPPrincipal.class::isInstance)
                .map(SIGLALDAPPrincipal.class::cast)
                .ifPresent(SIGLALDAPPrincipal -> {
                    Arrays.asList(attributes).stream().forEach(attribute -> {
                        Optional<Serializable> value = Optional.ofNullable(attrs.get(attribute))
                                .map(attribute1 -> {
                                    try {
                                        return attribute1.get();
                                    } catch (NamingException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .filter(Serializable.class::isInstance)
                                .map(Serializable.class::cast);
                        if (value.isPresent()) {
                            SIGLALDAPPrincipal.addAttribute(attribute, value.get());
                        }
                    });
                });

        results.close();
        results = null;

        if (userDN == null) {
            if (sr.isRelative()) {
                userDN = new CompositeName(name).get(0) + ("".equals(baseDN) ? "" : "," + baseDN);
                // SECURITY-225: don't need to authenticate again
                if (isPasswordValidated) {
                    // Bind as the user dn to authenticate the user
                    InitialLdapContext userCtx = constructInitialLdapContext(userDN, credential);
                    userCtx.close();
                }
            } else {
                userDN = bindDNReferralAuthentication(sr.getName(), credential);
                if (userDN == null) {
                    throw PicketBoxMessages.MESSAGES.unableToFollowReferralForAuth(name);
                }
            }
        } else {
            if (isPasswordValidated) {
                // Bind as the user dn to authenticate the user
                InitialLdapContext userCtx = constructInitialLdapContext(userDN, credential);
                userCtx.close();
            }
        }

        return userDN;
    }

    private InitialLdapContext constructInitialLdapContext(String dn, Object credential) throws NamingException {
        String protocol = (String) options.get(Context.SECURITY_PROTOCOL);
        String providerURL = (String) options.get(Context.PROVIDER_URL);
        if (providerURL == null)
            providerURL = "ldap://localhost:" + ((protocol != null && protocol.equals("ssl")) ? "636" : "389");

        Properties env = constructLdapContextEnvironment(providerURL, dn, credential);
        return new InitialLdapContext(env, null);
    }

    private Properties constructLdapContextEnvironment(String namingProviderURL, String principalDN, Object credential) {
        Properties env = new Properties();
        Iterator iter = options.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            env.put(entry.getKey(), entry.getValue());
        }

        // Set defaults for key values if they are missing
        String factoryName = env.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        if (factoryName == null) {
            factoryName = "com.sun.jndi.ldap.LdapCtxFactory";
            env.setProperty(Context.INITIAL_CONTEXT_FACTORY, factoryName);
        }
        String authType = env.getProperty(Context.SECURITY_AUTHENTICATION);
        if (authType == null)
            env.setProperty(Context.SECURITY_AUTHENTICATION, "simple");

        if (namingProviderURL != null) {
            env.setProperty(Context.PROVIDER_URL, namingProviderURL);
        }

        // JBAS-3555, allow anonymous login with no bindDN and bindCredential
        if (principalDN != null)
            env.setProperty(Context.SECURITY_PRINCIPAL, principalDN);
        if (credential != null)
            env.put(Context.SECURITY_CREDENTIALS, credential);
        return env;
    }

    private String bindDNReferralAuthentication(String absoluteName, Object credential)
            throws NamingException {
        URI uri;
        try {
            uri = new URI(absoluteName);
        } catch (URISyntaxException e) {
            throw PicketBoxMessages.MESSAGES.unableToParseReferralAbsoluteName(e, absoluteName);
        }
        String name = uri.getPath().substring(1);
        String namingProviderURL = uri.getScheme() + "://" + uri.getAuthority();

        Properties refEnv = constructLdapContextEnvironment(namingProviderURL, name, credential);

        InitialLdapContext refCtx = new InitialLdapContext(refEnv, null);
        refCtx.close();
        return name;
    }
}
