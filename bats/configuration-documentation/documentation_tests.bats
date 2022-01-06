
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
  export TIMESTAMP=`date +%s`
  export CONFIGURATION_JAR_TARGET_DIR="$BATS_TEST_DIRNAME/../../configuration-parent/configuration-jar/target"
  export CONFIGURATION_JAR_NAME=`find "$CONFIGURATION_JAR_TARGET_DIR" -name '*-jar-with-dependencies.jar'`
  export CONFIGURATION_YAML_TEST_RESOURCES_DIR_PATH="$BATS_TEST_DIRNAME/../../configuration-parent/configuration-yaml-interpreter/src/test/resources/com/github/starnowski/posmulten/configuration/yaml"
  mkdir -p "$BATS_TMPDIR/$TIMESTAMP"
  export CONFIGURATION_YAML_INTERPRETER_README_CONVERTER="$BATS_TEST_DIRNAME/../../configuration-parent/configuration-yaml-interpreter-readme-converter"
  export CONFIGURATION_YAML_INTERPRETER="$BATS_TEST_DIRNAME/../../configuration-parent/configuration-yaml-interpreter"
}

@test "The executable jar should print updated yaml syntax guide" {
  #given
  pushd "$CONFIGURATION_YAML_INTERPRETER_README_CONVERTER"
  node convert.js "$CONFIGURATION_YAML_INTERPRETER/README.md" "$BATS_TMPDIR/$TIMESTAMP/generated_output"
  popd

  #when
  run java -Dposmulten.configuration.config.yaml.syntax.guide.print="true" -jar "$CONFIGURATION_JAR_NAME"

  #then
  echo "output is --> $output <--"  >&3
  echo -n "$output" > "$BATS_TMPDIR/$TIMESTAMP/output"
  [ "$status" -eq 0 ]
  cmp -b "$BATS_TMPDIR/$TIMESTAMP/output" "$BATS_TMPDIR/$TIMESTAMP/generated_output"
}

function teardown {
  rm -rf "$BATS_TMPDIR/$TIMESTAMP"
}