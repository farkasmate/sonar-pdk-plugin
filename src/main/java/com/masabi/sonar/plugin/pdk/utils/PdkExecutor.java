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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class PdkExecutor {

  private static final Logger LOGGER = Loggers.get(PdkExecutor.class);

  public void execute() {
    LOGGER.info("Updating bundle");
    executeCommand("bundle update");

    File junitDir = new File("./junit/");
    if (!junitDir.exists()) {
      junitDir.mkdir();
    }

    LOGGER.info("Executing 'pdk validate'");
    executeCommand("pdk validate --format=junit:junit/validate.xml");

    //LOGGER.info("Executing 'pdk test unit'");
    //executeCommand("pdk test unit --format=junit:junit/test_unit.xml");
  }

  private void executeCommand(String command) {
    StringBuffer output = new StringBuffer();

    Process p;
    try {
      p = Runtime.getRuntime().exec(command);
      p.waitFor();
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line = "";
      while ((line = reader.readLine())!= null) {
        LOGGER.debug(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

