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

public class PdkError {

  private final String type;
  private final String description;
  private final String filePath;
  private final int line;

  public PdkError(final String type, final String description, final String filePath, final int line) {
    this.type = type;
    this.description = description;
    this.filePath = filePath;
    this.line = line;
  }

  public String getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }

  public String getFilePath() {
    return filePath;
  }

  public int getLine() {
    return line;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(type);
    s.append("|");
    s.append(description);
    s.append("|");
    s.append(filePath);
    s.append("(");
    s.append(line);
    s.append(")");
    return s.toString();
  }
}

