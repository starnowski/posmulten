
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
  export TIMESTAMP=`date +%s`
  export CONFIGURATION_JAR_TARGET_DIR="$BATS_TEST_DIRNAME/../../configuration-parent/configuration-jar/target"
  export CONFIGURATION_JAR_DIR="$BATS_TEST_DIRNAME/../../configuration-parent/configuration-jar"
  export CONFIGURATION_JAR_NAME=`find "$CONFIGURATION_JAR_TARGET_DIR" -name '*-jar-with-dependencies.jar'`
  #TODO directory with tests configuration
  export CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH="$BATS_TEST_DIRNAME/../../configuration-parent/configuration-yaml-interpreter-parent/configuration-yaml-interpreter/src/test/resources/com/github/starnowski/posmulten/configuration/yaml"
  mkdir -p "$BATS_TMPDIR/$TIMESTAMP"
}

@test "Run executable jar file with passed java properties for valid configuration file" {
  #given
  CONFIGURATION_FILE_PATH="$CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH/all-fields.yaml"
  [ -f "$CONFIGURATION_FILE_PATH" ]
  # Results files
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]

  #when
  run java -Dposmulten.configuration.config.file.path="$CONFIGURATION_FILE_PATH" -Dposmulten.configuration.create.script.path="$BATS_TMPDIR/$TIMESTAMP/create_script.sql" -Dposmulten.configuration.drop.script.path="$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]

  #Smoke tests for scripts content
  grep 'CREATE POLICY' "$BATS_TMPDIR/$TIMESTAMP/create_script.sql"
  grep 'DROP POLICY IF EXISTS' "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql"
}

@test "Run executable jar file with passed java properties for valid configuration file to generate creation script and sql file with validation statements" {
  #given
  CONFIGURATION_FILE_PATH="$CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH/all-fields.yaml"
  [ -f "$CONFIGURATION_FILE_PATH" ]
  # Results files
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql" ]

  #when
  run java -Dposmulten.configuration.config.file.path="$CONFIGURATION_FILE_PATH" -Dposmulten.configuration.create.script.path="$BATS_TMPDIR/$TIMESTAMP/create_script.sql" -Dposmulten.configuration.validation.statements.path="$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ -f "$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql" ]

  #Smoke tests for scripts content
  grep 'CREATE POLICY' "$BATS_TMPDIR/$TIMESTAMP/create_script.sql"
  grep 'SELECT COUNT(1) ' "$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql"
}

@test "Run executable jar file with passed java properties for valid configuration file to generate dropping script and sql file with validation statements" {
  #given
  CONFIGURATION_FILE_PATH="$CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH/all-fields.yaml"
  [ -f "$CONFIGURATION_FILE_PATH" ]
  # Results files
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql" ]

  #when
  run java -Dposmulten.configuration.config.file.path="$CONFIGURATION_FILE_PATH" -Dposmulten.configuration.drop.script.path="$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" -Dposmulten.configuration.validation.statements.path="$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]
  [ -f "$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql" ]

  #Smoke tests for scripts content
  grep 'DROP POLICY IF EXISTS' "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql"
  grep 'SELECT COUNT(1) ' "$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql"
}

@test "Run executable jar file with passed java properties for valid configuration file to generate creation script and sql file with validation statements for template parameters" {
  #given
  CONFIGURATION_FILE_PATH="$CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH/configuration-with-template-values.yaml"
  [ -f "$CONFIGURATION_FILE_PATH" ]
  # Results files
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql" ]

  #when
  run java -Dposmulten.configuration.config.file.path="$CONFIGURATION_FILE_PATH" -Dposmulten.configuration.create.script.path="$BATS_TMPDIR/$TIMESTAMP/create_script.sql" -Dposmulten.configuration.validation.statements.path="$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql" -Dposmulten.configuration.config.context.decorator.replaceCharactersMap='{{db_rls_grantee}}=my_user,{{db_schema}}=some_schema' -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ -f "$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql" ]

  #Smoke tests for scripts content
  grep 'some_schema' "$BATS_TMPDIR/$TIMESTAMP/create_script.sql"
  grep 'my_user' "$BATS_TMPDIR/$TIMESTAMP/create_script.sql"
  grep 'some_schema' "$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql"
  grep 'my_user' "$BATS_TMPDIR/$TIMESTAMP/valid_ss.sql"
}

@test "The executable jar file should log basic info messages for valid configuration file" {
  #given
  CONFIGURATION_FILE_PATH="$CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH/all-fields.yaml"
  [ -f "$CONFIGURATION_FILE_PATH" ]

  #when
  run java -Dposmulten.configuration.config.file.path="$CONFIGURATION_FILE_PATH" -Dposmulten.configuration.create.script.path="$BATS_TMPDIR/$TIMESTAMP/create_script.sql" -Dposmulten.configuration.drop.script.path="$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  echo "$output" > "$BATS_TMPDIR/$TIMESTAMP/output"
  grep 'INFO: Generate DDL statements based on file:' "$BATS_TMPDIR/$TIMESTAMP/output"
  grep 'INFO: Saving DDL statements that creates the shared schema strategy to ' "$BATS_TMPDIR/$TIMESTAMP/output"
  grep 'INFO: Saving DDL statements that drop the shared schema strategy to ' "$BATS_TMPDIR/$TIMESTAMP/output"
}

@test "Run executable jar file with passed java properties for invalid configuration file" {
  #given
  CONFIGURATION_FILE_PATH="$CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH/invalid-list-nodes-blank-fields.yaml"
  [ -f "$CONFIGURATION_FILE_PATH" ]
  # Results files
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]

  #when
  run java -Dposmulten.configuration.config.file.path="$CONFIGURATION_FILE_PATH" -Dposmulten.configuration.create.script.path="$BATS_TMPDIR/$TIMESTAMP/create_script.sql" -Dposmulten.configuration.drop.script.path="$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 1 ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]

  #Smoke tests for validation messages
  echo "$output" > "$BATS_TMPDIR/$TIMESTAMP/output"
  grep 'SEVERE: Posmulten invalid configuration' "$BATS_TMPDIR/$TIMESTAMP/output"
  grep 'SEVERE: Configuration error: tables\[3\].rls_policy.primary_key_definition.name_for_function_that_checks_if_record_exists_in_table must not be blank' "$BATS_TMPDIR/$TIMESTAMP/output"
}

@test "Run executable jar file with passed java properties for invalid configuration file where there are invalid reference for foreign keys" {
  #given
  CONFIGURATION_FILE_PATH="$BATS_TEST_DIRNAME/invalid-conf.yaml"
  [ -f "$CONFIGURATION_FILE_PATH" ]
  # Results files
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]

  #when
  run java -Dposmulten.configuration.config.file.path="$CONFIGURATION_FILE_PATH" -Dposmulten.configuration.create.script.path="$BATS_TMPDIR/$TIMESTAMP/create_script.sql" -Dposmulten.configuration.drop.script.path="$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 1 ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]

  #Smoke tests for validation messages
  echo "$output" > "$BATS_TMPDIR/$TIMESTAMP/output"
  grep 'SEVERE: Posmulten invalid configuration' "$BATS_TMPDIR/$TIMESTAMP/output"
  grep 'SEVERE: Configuration error: There is mismatch between foreign keys column mapping' "$BATS_TMPDIR/$TIMESTAMP/output"
}

@test "Run executable jar file with passed java properties for invalid configuration file where extensions is not supported" {
  #given
  CONFIGURATION_FILE_PATH="$BATS_TEST_DIRNAME/unkonw-conf.nosuchext"
  [ -f "$CONFIGURATION_FILE_PATH" ]
  # Results files
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]

  #when
  run java -Dposmulten.configuration.config.file.path="$CONFIGURATION_FILE_PATH" -Dposmulten.configuration.create.script.path="$BATS_TMPDIR/$TIMESTAMP/create_script.sql" -Dposmulten.configuration.drop.script.path="$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 1 ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]

  #Smoke tests for validation messages
  echo "$output" > "$BATS_TMPDIR/$TIMESTAMP/output"
  grep 'SEVERE: Posmulten invalid configuration' "$BATS_TMPDIR/$TIMESTAMP/output"
  grep 'SEVERE: Configuration error: No supplier was found, able to handle file ' "$BATS_TMPDIR/$TIMESTAMP/output"
}

@test "The executable jar file should not log any content for silent mode for invalid configuration file" {
  #given
  CONFIGURATION_FILE_PATH="$CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH/invalid-list-nodes-blank-fields.yaml"
  [ -f "$CONFIGURATION_FILE_PATH" ]
  # Results files
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]
  unzip -p "$CONFIGURATION_JAR_NAME" silent-logging.properties >"$BATS_TMPDIR/$TIMESTAMP/silent-logging.properties"

  #when
  run java -Djava.util.logging.config.file="$BATS_TMPDIR/$TIMESTAMP/silent-logging.properties" -Dposmulten.configuration.config.file.path="$CONFIGURATION_FILE_PATH" -Dposmulten.configuration.create.script.path="$BATS_TMPDIR/$TIMESTAMP/create_script.sql" -Dposmulten.configuration.drop.script.path="$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 1 ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]

  #Smoke tests for validation messages
  echo "$output" > "$BATS_TMPDIR/$TIMESTAMP/output"
  #File is empty or blank
  ! grep -q '[^[:space:]]' < "$BATS_TMPDIR/$TIMESTAMP/output"
}

@test "The executable jar file should log content with a higher logging level for the invalid configuration file" {
  #given
  CONFIGURATION_FILE_PATH="$CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH/invalid-list-nodes-blank-fields.yaml"
  [ -f "$CONFIGURATION_FILE_PATH" ]
  # Results files
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]
  unzip -p "$CONFIGURATION_JAR_NAME" debug-logging.properties >"$BATS_TMPDIR/$TIMESTAMP/debug-logging.properties"

  #when
  run java -Djava.util.logging.config.file="$BATS_TMPDIR/$TIMESTAMP/debug-logging.properties" -Dposmulten.configuration.config.file.path="$CONFIGURATION_FILE_PATH" -Dposmulten.configuration.create.script.path="$BATS_TMPDIR/$TIMESTAMP/create_script.sql" -Dposmulten.configuration.drop.script.path="$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 1 ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ ! -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]

  #Smoke tests for validation messages
  echo "$output" > "$BATS_TMPDIR/$TIMESTAMP/output"
  #File is empty or blank
  grep 'TRACE:' "$BATS_TMPDIR/$TIMESTAMP/output"
  grep 'DEBUG:' "$BATS_TMPDIR/$TIMESTAMP/output"
  grep 'SEVERE:' "$BATS_TMPDIR/$TIMESTAMP/output"
}

@test "The executable jar should print correct version number" {
  #given
  #Resolve version
  EXPECTED_VERSION_NUMBER=`xmllint --xpath "string(/*[local-name()='project']/*[local-name()='parent']/*[local-name()='version'])" $CONFIGURATION_JAR_DIR/pom.xml`

  #when
  run java -Dposmulten.configuration.config.version.print="true" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "${lines[0]}" = "$EXPECTED_VERSION_NUMBER" ]
}

@test "The executable jar should print yaml syntax guide" {
  #given
  YAML_SYNTAX_GUIDE_PATH="${CONFIGURATION_JAR_DIR}/src/main/resources/configuration-yaml-interpreter-readme.txt"
  cp "$YAML_SYNTAX_GUIDE_PATH" "$BATS_TMPDIR/$TIMESTAMP/"
  YAML_SYNTAX_GUIDE_PATH="$BATS_TMPDIR/$TIMESTAMP/configuration-yaml-interpreter-readme.txt"
#  sed -e '$a\'  "${YAML_SYNTAX_GUIDE_PATH}"

  #when
  run java -Dposmulten.configuration.config.yaml.syntax.guide.print="true" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  echo -n "$output" > "$BATS_TMPDIR/$TIMESTAMP/output"
  [ "$status" -eq 0 ]
  cmp -b "$BATS_TMPDIR/$TIMESTAMP/output" "$YAML_SYNTAX_GUIDE_PATH"
}

function teardown {
  rm -rf "$BATS_TMPDIR/$TIMESTAMP"
}