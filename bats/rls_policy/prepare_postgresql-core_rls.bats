
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
  export TIMESTAMP=`date +%s`
  export TMP_SQL_FILE="$TIMESTAMP_timestamp.sql"
}

@test "Create rls policy with command ALL and USING" {
  #given
  export PGPASSWORD=postgres_posmulten
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    CREATE POLICY users_policy ON
    ${DATABASE_TESTS_SCHEMA_NAME}.users FOR ALL TO postgresql-core-user USING tenant_id = 'xxxx'';
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"
  echo "output is --> $output <--"  >&3

  #then
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(1) FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = 'users_policy' AND pc.relname = 'users' AND pn.nspname = '$DATABASE_TESTS_SCHEMA_NAME' AND pg.polcmd = '*';" >&3

  [ "$status" -eq 0 ]
  [ "$output" = "1" ]
}

function teardown {
cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    DROP POLICY IF EXISTS users_policy ON ${DATABASE_TESTS_SCHEMA_NAME}.users;
SQL
  psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"
  #Restore previous password
  PGPASSWORD="$PREVIOUS_PGPASSWORD"
}