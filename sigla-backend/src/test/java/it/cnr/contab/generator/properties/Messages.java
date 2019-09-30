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

package it.cnr.contab.generator.properties;

/**
 * Gestione costanti testo
 *
 * @author Marco Spasiano
 * @version 1.0
 * [18-Aug-2006] creazione
 * [24-Aug-2006] classe rinominata (ex BulkGeneratorMessages)
 */

public final class Messages {

    private static final String BUNDLE_NAME = "it.cnr.contab.generator.properties.Messages";//$NON-NLS-1$
    public static String GenerationWizardPage_title;
    public static String GenerationWizardPage_description;
    public static String GenerationWizardPage_artifact_exist;
    public static String GenerationWizardPage_artifact_overwrite;
    public static String GenerationWizardPage_prefix;
    public static String GenerationWizardPage_folder;
    public static String GenerationWizardPage_folder_err_empty;
    public static String GenerationWizardPage_folder_err_qualified;
    public static String GenerationWizardPage_prefix_err_empty;
    public static String GenerationWizardPage_prefix_err_qualified;
    public static String GenerationWizardPage_artifacts_caption;
    public static String GenerationWizardPage_root_err;
    public static String ArtifactGenerator_err_source_null;
    public static String ArtifactGenerator_err_ProjectClosed;
    public static String ArtifactGenerator_op_description;
    public static String BulkGeneratorPreferencePage_description;
    public static String BulkGeneratorPreferencePage_version;
    public static String BulkGeneratorPreferencePage_author;
    public static String BulkGeneratorPreferencePage_project;
    public static String DatabasePreferencePage_title;
    public static String DatabasePreferencePage_driver;
    public static String DatabasePreferencePage_url;
    public static String DatabasePreferencePage_user;
    public static String DatabasePreferencePage_password;
    public static String DatabasePreferencePage_schema;
    public static String DatabasePreferencePage_listTables;
    public static String SQLJavaPreferencePage_description;
    public static String SQLTypePreferencePage_description;
    public static String BulkGeneratorPreferencePage_0;
    public static String XMLPreferencePage_header;
    public static String XMLPreferencePage_description;
    public static String BulkGeneratorWizard_title;
    public static String BulkGeneratorWizard_error;
    public static String BulkGeneratorWizard_message;
    public static String DatabaseConnectionWizardPage_title;
    public static String DatabaseConnectionWizardPage_description;
    public static String DatabaseConnectionWizardPage_driver;
    public static String DatabaseConnectionWizardPage_url;
    public static String DatabaseConnectionWizardPage_user;
    public static String DatabaseConnectionWizardPage_password;
    public static String DatabaseConnectionWizardPage_open;
    public static String DatabaseConnectionWizardPage_connection_ok;
    public static String DatabaseConnectionWizardPage_connection_err;
    public static String DatabaseConnectionWizardPage_driver_mandatory;
    public static String DatabaseConnectionWizardPage_url_mandatory;
    public static String DatabaseConnectionWizardPage_user_mandatory;
    public static String DatabaseConnectionWizardPage_password_mandatory;
    public static String DatabaseMetadataWizardPage_titolo;
    public static String DatabaseMetadataWizardPage_description;
    public static String DatabaseMetadataWizardPage_schema;
    public static String DatabaseMetadataWizardPage_tipo;
    public static String DatabaseMetadataWizardPage_filtro;
    private Messages() {
        // Do not instantiate
    }

}
