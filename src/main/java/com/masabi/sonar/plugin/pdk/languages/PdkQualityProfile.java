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
package com.masabi.sonar.plugin.pdk.languages;

import static com.masabi.sonar.plugin.pdk.rules.PdkLintRulesDefinition.REPO_KEY;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;

/**
 * Default Quality profile for the projects having files of language "pdk"
 */
public final class PdkQualityProfile extends ProfileDefinition {

  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
    RulesProfile profile = RulesProfile.create("PDK Rules", PdkLanguage.KEY);

    profile.activateRule(Rule.create(REPO_KEY, "pdk:generic"), null);

    profile.activateRule(Rule.create(REPO_KEY, "metadata-json-lint:error"), null);
    profile.activateRule(Rule.create(REPO_KEY, "metadata-json-lint:warning"), null);

    profile.activateRule(Rule.create(REPO_KEY, "metadata-syntax:error"), null);

    profile.activateRule(Rule.create(REPO_KEY, "puppet-lint:error"), null);
    profile.activateRule(Rule.create(REPO_KEY, "puppet-lint:warning"), null);

    profile.activateRule(Rule.create(REPO_KEY, "puppet-syntax:error"), null);

    profile.activateRule(Rule.create(REPO_KEY, "rubocop:convention"), null);
    profile.activateRule(Rule.create(REPO_KEY, "rubocop:error"), null);
    profile.activateRule(Rule.create(REPO_KEY, "rubocop:fatal"), null);
    profile.activateRule(Rule.create(REPO_KEY, "rubocop:refactor"), null);
    profile.activateRule(Rule.create(REPO_KEY, "rubocop:warning"), null);

    return profile;
  }
}

