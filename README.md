# PDK Plugin for SonarQube

Compatible with SonarQube 5.6 LTS

### License

Copyright 2016-2017 SonarSource.

Licensed under the [GNU Lesser General Public License, Version 3.0](http://www.gnu.org/licenses/lgpl.txt)

## Troubleshooting

### Issue get marked as **Closed (Fixed)** without code change

It can happen if **pdk** is skipping a test suite. For example if you have a Puppet syntax error, pdk will fail in the *puppet-syntax* phase and skip *puppet-lint* and all the puppet-lint issues will be marked as Closed.

Fix the syntax error, analyze the code again and new puppet-lint issues will be created.

### WARN  - Rule 'x' not found, falling back to 'pdk:generic'

The rule **x** is not defined in the used version of the plugin.

Add the definition to *src/main/resources/rules.xml* and activate it in *src/main/java/com/masabi/sonar/plugin/pdk/languages/PdkQualityProfile.java*.

