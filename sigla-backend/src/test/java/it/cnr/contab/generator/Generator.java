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

package it.cnr.contab.generator;

import it.cnr.contab.generator.artifacts.ArtifactGenerator;
import it.cnr.contab.generator.model.GeneratorBean;
import it.cnr.contab.generator.util.DatabaseUtil;
import org.apache.commons.cli.*;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Generator {

    public static final String PACKAGE_NAME = "packageName";
    public static final String PREFIX = "prefix";
    public static final String TABLE = "table";
    public static final String DRIVER = "driver";
    public static final String URL = "url";
    public static final String SCHEMA = "schema";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static void main(String[] args) {
        GeneratorBean bean = new GeneratorBean();
        final String userHome = System.getProperty("user.home");
        bean.setSourceFolder(userHome.concat(File.separator));
        bean.setTargetXMLFolder(userHome.concat(File.separator));
        Options options = new Options();

        Option packageName = new Option("n", PACKAGE_NAME, true, "You must set package name like \"it.cnr.contab.util.bulk\"");
        packageName.setRequired(true);
        options.addOption(packageName);

        Option prefix = new Option("r", PREFIX, true, "You must set prefix of the class generated like \"Help\"");
        prefix.setRequired(true);
        options.addOption(prefix);

        Option table = new Option("t", TABLE, true, "You must set table name like \"HELP_LKT\"");
        table.setRequired(true);
        options.addOption(table);

        Option driver = new Option("d", DRIVER, true, "You must set driver name like \"oracle.jdbc.driver.OracleDriver\"");
        driver.setRequired(true);
        options.addOption(driver);

        Option url = new Option("l", URL, true, "You must set url like \"jdbc:oracle:thin:@dbtest.cedrc.cnr.it:1521:SIGLAF\"");
        url.setRequired(true);
        options.addOption(url);

        Option schema = new Option("s", SCHEMA, true, "You must set schema like \"PCIR009\"");
        schema.setRequired(true);
        options.addOption(schema);

        Option username = new Option("u", USERNAME, true, "You must set db user like \"PCIR009\"");
        username.setRequired(true);
        options.addOption(username);

        Option password = new Option("p", PASSWORD, true, "You must set db password");
        password.setRequired(true);
        options.addOption(password);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        bean.setPackageName(cmd.getOptionValue(PACKAGE_NAME));
        bean.setPrefix(cmd.getOptionValue(PREFIX));
        bean.setTable(cmd.getOptionValue(TABLE));
        bean.setDriver(cmd.getOptionValue(DRIVER));
        bean.setUrl(cmd.getOptionValue(URL));
        bean.setSchema(cmd.getOptionValue(SCHEMA));
        bean.setUser(cmd.getOptionValue(USERNAME));
        bean.setPassword(cmd.getOptionValue(PASSWORD));
        ArtifactGenerator artifactGenerator = new ArtifactGenerator(bean);
        try {
            DatabaseUtil.getInstance().openConnection(bean);
            artifactGenerator.generate();
            System.out.println("All class and xml was generated into folder: " + userHome.concat(File.separator));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
