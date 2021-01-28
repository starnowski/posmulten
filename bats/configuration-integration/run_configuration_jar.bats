
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
  export TIMESTAMP=`date +%s`
  export CONFIGURATION_JAR_TARGET_DIR="$BATS_TEST_DIRNAME/../../configuration-parent/configuration-jar/target"
  export CONFIGURATION_JAR_DIR="$BATS_TEST_DIRNAME/../../configuration-parent/configuration-jar"
  export CONFIGURATION_JAR_NAME=`find "$CONFIGURATION_JAR_TARGET_DIR" -name '*-jar-with-dependencies.jar'`
  #TODO directory with tests configuration
  export CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH="$BATS_TEST_DIRNAME/../../configuration-parent/configuration-yaml-interpreter/src/test/resources/com/github/starnowski/posmulten/configuration/yaml"
  mkdir -p "$BATS_TMPDIR/$TIMESTAMP"
}

@test "Apply and drop shared schema via database scripts created based on yaml configuration" {
  #given
  export DOCKER_DB_IP="${DOCKER_DB_IP:-127.0.0.1}"
  export PGPASSWORD="${PGPASSWORD:-owner123}"
  export DATABASE_PORT="${DATABASE_PORT:-owner123}"
  CONFIGURATION_FILE_PATH="$CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH/integration-tests-configuration.yaml"
  [ -f "$CONFIGURATION_FILE_PATH" ]
  # Results files
  java -Dposmulten.configuration.config.file.path="$CONFIGURATION_FILE_PATH" -Dposmulten.configuration.create.script.path="$BATS_TMPDIR/$TIMESTAMP/create_script.sql" -Dposmulten.configuration.drop.script.path="$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" -jar "$CONFIGURATION_JAR_NAME"
  [ -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" ]
  [ -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" ]

  #when - apply shared schema
  echo "create_script:" >&3
  cat "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" >&3
  run psql -f "$BATS_TMPDIR/$TIMESTAMP/create_script.sql" -U "postgresql-core-owner" -d postgresql_core --host="$DOCKER_DB_IP" -p $DATABASE_PORT -v "ON_ERROR_STOP=1" >&3
  create_script_status="$status"
  echo "output for creating shared schema is --> $output <--"  >&3

  #when - dropping shared schema
  echo "drop_script:" >&3
  cat "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" >&3
  run psql -f "$BATS_TMPDIR/$TIMESTAMP/drop_script.sql" -U "postgresql-core-owner" -d postgresql_core --host="$DOCKER_DB_IP" -p $DATABASE_PORT -v "ON_ERROR_STOP=1" >&3
  drop_script_status="$status"
  echo "output for dropping shared schema is --> $output <--"  >&3

  #then
  [ "$create_script_status" -eq 0 ]
  [ "$drop_script_status" -eq 0 ]
}

function teardown {
  rm -rf "$BATS_TMPDIR/$TIMESTAMP"
}