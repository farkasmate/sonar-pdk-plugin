/*
 * PDK Plugin for SonarQube - based on
 * Example Plugin for SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.masabi.sonar.plugin.pdk.rules;

import com.masabi.sonar.plugin.pdk.languages.PdkLanguage;
import com.masabi.sonar.plugin.pdk.utils.PdkError;
import com.masabi.sonar.plugin.pdk.utils.PdkExecutor;
import com.masabi.sonar.plugin.pdk.utils.PdkJunitResultsParser;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.config.Settings;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class PdkLintIssuesLoaderSensor implements Sensor {

  private static final Logger LOGGER = Loggers.get(PdkLintIssuesLoaderSensor.class);

  protected static final String REPORT_PATH_KEY = "sonar.pdklint.reportPath";

  protected final Settings settings;
  protected final FileSystem fileSystem;
  protected SensorContext context;

  private PdkExecutor pdkExecutor;

  /**
   * Use of IoC to get Settings, FileSystem, RuleFinder and ResourcePerspectives
   */
  public PdkLintIssuesLoaderSensor(final Settings settings, final FileSystem fileSystem) {
    this.settings = settings;
    this.fileSystem = fileSystem;
    this.pdkExecutor = new PdkExecutor();
  }

  @Override
  public void describe(final SensorDescriptor descriptor) {
    descriptor.name("PDK Issues Loader Sensor");
    descriptor.onlyOnLanguage(PdkLanguage.KEY);
  }

  protected String reportPathKey() {
    return REPORT_PATH_KEY;
  }

  protected String getReportPath() {
    String reportPath = settings.getString(reportPathKey());
    if (!StringUtils.isEmpty(reportPath)) {
      return reportPath;
    } else {
      return null;
    }
  }

  @Override
  public void execute(final SensorContext context) {
    pdkExecutor.execute();
    if (!StringUtils.isEmpty(getReportPath())) {
      this.context = context;
      String reportPath = getReportPath();
      File analysisResultsFile = new File(reportPath);
      try {
        parseAndSaveResults(analysisResultsFile);
      } catch (XMLStreamException e) {
        throw new IllegalStateException("Unable to parse the provided file", e);
      }
    }
  }

  protected void parseAndSaveResults(final File file) throws XMLStreamException {
    LOGGER.info("(mock) Parsing Analysis Results");
    PdkJunitResultsParser parser = new PdkJunitResultsParser();
    List<PdkError> errors = parser.parse(file);
    for (PdkError error : errors) {
      getResourceAndSaveIssue(error);
    }
  }

  private void getResourceAndSaveIssue(final PdkError error) {
    LOGGER.debug(error.toString());

    InputFile inputFile = fileSystem.inputFile(
      fileSystem.predicates().and(
        fileSystem.predicates().hasRelativePath(error.getFilePath()),
        fileSystem.predicates().hasType(InputFile.Type.MAIN)));

    LOGGER.debug("inputFile null ? " + (inputFile == null));

    if (inputFile != null) {
      saveIssue(inputFile, error.getLine(), error.getType(), error.getDescription());
    } else {
      LOGGER.error("Not able to find a InputFile with " + error.getFilePath());
    }
  }

  private void saveIssue(final InputFile inputFile, int line, final String externalRuleKey, final String message) {
    RuleKey ruleKey = RuleKey.of(getRepositoryKeyForLanguage(inputFile.language()), externalRuleKey);

    NewIssue newIssue = context.newIssue()
      .forRule(ruleKey);

    NewIssueLocation primaryLocation = newIssue.newLocation()
      .on(inputFile)
      .message(message);
    if (line > 0) {
      primaryLocation.at(inputFile.selectLine(line));
    }
    newIssue.at(primaryLocation);

    newIssue.save();
  }

  private static String getRepositoryKeyForLanguage(String languageKey) {
    return languageKey.toLowerCase() + "-" + PdkLintRulesDefinition.KEY;
  }

  @Override
  public String toString() {
    return "PdkLintIssuesLoaderSensor";
  }
}

