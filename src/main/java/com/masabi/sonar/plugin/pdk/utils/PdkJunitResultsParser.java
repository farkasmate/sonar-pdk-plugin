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
package com.masabi.sonar.plugin.pdk.utils;

import com.masabi.sonar.plugin.pdk.settings.PdkRules;
import com.masabi.sonar.plugin.pdk.utils.PdkError;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamException;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PdkJunitResultsParser {

  private static final Logger LOGGER = Loggers.get(PdkJunitResultsParser.class);

  public List<PdkError> parse(final File file) throws XMLStreamException {
    LOGGER.info("Parsing file {}", file.getAbsolutePath());

    List<PdkError> errors = new ArrayList<PdkError>();

    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document results = dBuilder.parse(file);
      results.getDocumentElement().normalize();

      NodeList failures = results.getElementsByTagName("failure");
      for (int i = 0; i < failures.getLength(); i++) {
        Node failure = failures.item(i);

        String name = failure
          .getParentNode()
          .getParentNode()
          .getAttributes()
          .getNamedItem("name")
          .getNodeValue();

        String severity = failure
          .getAttributes()
          .getNamedItem("type")
          .getNodeValue();

        String errorKey = name + ":" + severity;

        String message = failure
          .getAttributes()
          .getNamedItem("message")
          .getNodeValue();

        String testCaseName = failure
          .getParentNode()
          .getAttributes()
          .getNamedItem("name")
          .getNodeValue();

        String relativeFilePath = testCaseName
          .split(":")[0];

        int fileLine = Integer.parseInt(
          testCaseName
          .split(":")[1]);

        if (!PdkRules.isValidKey(errorKey)) {
          LOGGER.debug("Rule '{}' not found, falling back to '{}'", errorKey, PdkRules.GENERIC_ERROR_KEY);
          errorKey = PdkRules.GENERIC_ERROR_KEY;
        }

        PdkError error = new PdkError(errorKey, message, relativeFilePath, fileLine);
        errors.add(error);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return errors;
  }
}

