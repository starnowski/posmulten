
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
  export TIMESTAMP=`date +%s`
  export TMP_SQL_FILE="$TIMESTAMP_timestamp.sql"
}

@test "Create rls policy with permission commands ALL and USING expression" {
  #given
  export PGPASSWORD=postgres_posmulten
  export TEST_SCRIPT_DATABASE_USER='"postgresql-core-user"'
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    CREATE POLICY users_policy ON
    ${DATABASE_TESTS_SCHEMA_NAME}.users FOR ALL TO ${TEST_SCRIPT_DATABASE_USER} USING (tenant_id = 'xxxx');
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"
  echo "output is --> $output <--"  >&3

  #then
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(1) FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = 'users_policy' AND pc.relname = 'users' AND pn.nspname = '$DATABASE_TESTS_SCHEMA_NAME' AND pg.polcmd = '*';" >&3

  [ "$status" -eq 0 ]
  [ "$output" = "1" ]
}

@test "Create rls policy with permission commands UPDATE and USING expression" {
  #given
  export PGPASSWORD=postgres_posmulten
  export TEST_SCRIPT_DATABASE_USER='"postgresql-core-user"'
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    CREATE POLICY users_policy ON
    ${DATABASE_TESTS_SCHEMA_NAME}.users FOR UPDATE TO ${TEST_SCRIPT_DATABASE_USER} USING (tenant_id = 'xxxx');
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"
  echo "output is --> $output <--"  >&3

  #then
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(1) FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = 'users_policy' AND pc.relname = 'users' AND pn.nspname = '$DATABASE_TESTS_SCHEMA_NAME' AND pg.polcmd = 'w';" >&3

  [ "$status" -eq 0 ]
  [ "$output" = "1" ]
}

@test "Create rls policy with permission commands SELECT and USING expression" {
  #given
  export PGPASSWORD=postgres_posmulten
  export TEST_SCRIPT_DATABASE_USER='"postgresql-core-user"'
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    CREATE POLICY users_policy ON
    ${DATABASE_TESTS_SCHEMA_NAME}.users FOR SELECT TO ${TEST_SCRIPT_DATABASE_USER} USING (tenant_id = 'xxxx');
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"
  echo "output is --> $output <--"  >&3

  #then
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(1) FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = 'users_policy' AND pc.relname = 'users' AND pn.nspname = '$DATABASE_TESTS_SCHEMA_NAME' AND pg.polcmd = 'r';" >&3

  [ "$status" -eq 0 ]
  [ "$output" = "1" ]
}

@test "Create rls policy with permission commands DELETE and USING expression" {
  #given
  export PGPASSWORD=postgres_posmulten
  export TEST_SCRIPT_DATABASE_USER='"postgresql-core-user"'
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    CREATE POLICY users_policy ON
    ${DATABASE_TESTS_SCHEMA_NAME}.users FOR DELETE TO ${TEST_SCRIPT_DATABASE_USER} USING (tenant_id = 'xxxx');
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"
  echo "output is --> $output <--"  >&3

  #then
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(1) FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = 'users_policy' AND pc.relname = 'users' AND pn.nspname = '$DATABASE_TESTS_SCHEMA_NAME' AND pg.polcmd = 'd';" >&3

  [ "$status" -eq 0 ]
  [ "$output" = "1" ]
}

@test "should not be able to create rls policy with permission commands INSERT and USING expression" {
  #given
  export PGPASSWORD=postgres_posmulten
  export TEST_SCRIPT_DATABASE_USER='"postgresql-core-user"'
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    CREATE POLICY users_policy ON
    ${DATABASE_TESTS_SCHEMA_NAME}.users FOR INSERT TO ${TEST_SCRIPT_DATABASE_USER} USING (tenant_id = 'xxxx');
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"
  echo "output is --> $output <--"  >&3

  #then
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(1) FROM pg_catalog.pg_policy pg, pg_class pc, pg_catalog.pg_namespace pn WHERE pg.polrelid = pc.oid AND pc.relnamespace = pn.oid AND pg.polname = 'users_policy' AND pc.relname = 'users' AND pn.nspname = '$DATABASE_TESTS_SCHEMA_NAME' AND pg.polcmd = 'a';" >&3

  [ "$status" -eq 0 ]
  [ "$output" = "0" ]
}

@test "Create rls policy with permission commands ALL and WITH CHECK expression" {
  #given
  export PGPASSWORD=postgres_posmulten
  export TEST_SCRIPT_DATABASE_USER='"postgresql-core-user"'
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    CREATE POLICY users_policy ON
    ${DATABASE_TESTS_SCHEMA_NAME}.users FOR ALL TO ${TEST_SCRIPT_DATABASE_USER} WITH CHECK (tenant_id = 'xxxx');
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