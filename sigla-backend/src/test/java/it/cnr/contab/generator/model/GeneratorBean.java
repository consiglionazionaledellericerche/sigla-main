/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.generator.model;

import it.cnr.contab.generator.properties.Preferences;
import it.cnr.contab.generator.util.DatabaseInfo;

import java.io.IOException;
import java.util.Properties;


/**
 * Incapsula i dati necessari alla generazione dei vari artefatti
 *
 * @author Marco Spasiano
 * @version 1.0 [7-Aug-2006] creazione
 */
public class GeneratorBean implements DatabaseInfo {
    private String driver;
    private String url;
    private String user;
    private String password;
    private String catalog;
    private String schema;
    private String filter;
    private String table;
    private String tableType;
    private String prefix;
    private String packageName;
    private String sourceFolder;
    private String targetXMLFolder;
    private boolean generationAllowed;

    public String getSourceFolder() {
        return sourceFolder;
    }

    public void setSourceFolder(String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String className) {
        this.prefix = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * ritorna la preferenza associata alla chiave indicata
     */
    public boolean isGenerationAllowed() {
        return generationAllowed;
    }

    public void setGenerationAllowed(boolean isGenerationAllowed) {
        this.generationAllowed = isGenerationAllowed;
    }

    public String getTargetXMLFolder() {
        return targetXMLFolder;
    }

    public void setTargetXMLFolder(String targetXMLFolder) {
        this.targetXMLFolder = targetXMLFolder;
    }

    public String getString(String toLowerCase) {
        Properties properties = new Properties();
        try {
            properties.load(Preferences.class.getResourceAsStream("Preferences.properties"));
            return properties.getProperty(toLowerCase);
        } catch (IOException e) {
            return null;
        }
    }
}
