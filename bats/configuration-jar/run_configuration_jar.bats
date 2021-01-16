
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
  export TIMESTAMP=`date +%s`
  export CONFIGURATION_JAR_TARGET_DIR="$BATS_TEST_DIRNAME/../../configuration-parent/configuration-jar/target"
  export CONFIGURATION_JAR_NAME=`find '$CONFIGURATION_JAR_TARGET_DIR' -name '*-jar-with-dependencies.jar'`
}

@test "Relation between 'users'(id) and 'users_groups'(user_id) should be one-to-many." {
  #given
  echo "CONFIGURATION_JAR_NAME is $CONFIGURATION_JAR_NAME"  >&3

  #when
  run java -jar "$CONFIGURATION_JAR_TARGET_DIR/configuration-jar-0.3.0-SNAPSHOT-jar-with-dependencies.jar"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

function teardown {
  rm "$BATS_TMPDIR/$TMP_SQL_FILE"
}