

function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
  export TIMESTAMP=`date +%s`
  export TMP_SQL_FILE="$TIMESTAMP_timestamp.sql"
}

@test "Database user 'postgresql-core-user' should have privilege 'USAGE' for 'public' schema" {
  #given
  export PGPASSWORD=postgres_posmulten
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
   SELECT pg_catalog.has_schema_privilege('postgresql-core-user', 'public', 'USAGE') AS USER_HAS_PRIVILEGE;
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database user 'postgresql-core-user' should have privilege 'CREATE' for 'public' schema" {
  #given
  export PGPASSWORD=postgres_posmulten
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
   SELECT pg_catalog.has_schema_privilege('postgresql-core-user', 'public', 'CREATE') AS USER_HAS_PRIVILEGE;
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database user 'postgresql-core-user' should have not privilege 'USAGE' for 'non_public_schema' schema" {
  #given
  export PGPASSWORD=postgres_posmulten
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
   SELECT pg_catalog.has_schema_privilege('postgresql-core-user', 'non_public_schema', 'USAGE') AS USER_HAS_PRIVILEGE;
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "f" ]
}

@test "Database user 'postgresql-core-user' should not have privilege 'CREATE' for 'non_public_schema' schema" {
  #given
  export PGPASSWORD=postgres_posmulten
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
   SELECT pg_catalog.has_schema_privilege('postgresql-core-user', 'non_public_schema', 'CREATE') AS USER_HAS_PRIVILEGE;
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "f" ]
}

function teardown {
  #Restore previous password
  PGPASSWORD="$PREVIOUS_PGPASSWORD"
}