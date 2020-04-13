
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
}

@test "Schema should contains sequence 'primary_sequence'" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.sequences WHERE sequence_name = 'primary_sequence' AND sequence_schema = '$DATABASE_TESTS_SCHEMA_NAME');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "The 'postgresql-core-user' user should not have privileges for sequence 'primary_sequence'" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM   information_schema.usage_privileges WHERE  grantee = 'postgresql-core-user' AND object_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND object_name = 'primary_sequence' AND object_type = 'SEQUENCE' AND privilege_type = 'USAGE');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "f" ]
}

function teardown {
  #Restore previous password
  PGPASSWORD="$PREVIOUS_PGPASSWORD"
}