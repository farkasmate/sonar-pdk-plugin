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
package com.masabi.sonar.plugin.pdk.settings;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PdkRules {

  private static final String PATH_TO_RULES_XML = "/rules.xml";
  public static final String GENERIC_ERROR_KEY = "pdk:generic";

  private static List<String> ruleKeys;

  private static void initialize() {
    ruleKeys = new ArrayList<String>();

    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      InputStream rulesXml = PdkRules.class.getResourceAsStream(PATH_TO_RULES_XML);
      Document rules = dBuilder.parse(rulesXml);
      rules.getDocumentElement().normalize();

      NodeList keys = rules.getElementsByTagName("key");
      for (int i = 0; i < keys.getLength(); i++) {
        Node key = keys.item(i);
        String keyName = key
          .getFirstChild()
          .getTextContent();
        ruleKeys.add(keyName);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean isValidKey(String ruleKey) {
    if (ruleKeys == null) {
      initialize();
    }

    return ruleKeys.contains(ruleKey);
  }
}

