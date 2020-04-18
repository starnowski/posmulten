
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
}

@test "Database table 'users' should not have enabled row security level" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT pc.relrowsecurity FROM pg_class pc, pg_catalog.pg_namespace pg WHERE pc.relname = 'users' AND pc.relnamespace = pg.oid AND pg.nspname = '$DATABASE_TESTS_SCHEMA_NAME';" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "f" ]
}

@test "Database table 'groups' should not have enabled row security level" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT pc.relrowsecurity FROM pg_class pc, pg_catalog.pg_namespace pg WHERE pc.relname = 'groups' AND pc.relnamespace = pg.oid AND pg.nspname = '$DATABASE_TESTS_SCHEMA_NAME';" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "f" ]
}

@test "Database table 'users_groups' should not have enabled row security level" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT pc.relrowsecurity FROM pg_class pc, pg_catalog.pg_namespace pg WHERE pc.relname = 'users_groups' AND pc.relnamespace = pg.oid AND pg.nspname = '$DATABASE_TESTS_SCHEMA_NAME';" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "f" ]
}

@test "Database table 'posts' should not have enabled row security level" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT pc.relrowsecurity FROM pg_class pc, pg_catalog.pg_namespace pg WHERE pc.relname = 'posts' AND pc.relnamespace = pg.oid AND pg.nspname = '$DATABASE_TESTS_SCHEMA_NAME';" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "f" ]
}

@test "Database table 'users' should not have forced row security level" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT pc.relforcerowsecurity FROM pg_class pc, pg_catalog.pg_namespace pg WHERE pc.relname = 'users' AND pc.relnamespace = pg.oid AND pg.nspname = '$DATABASE_TESTS_SCHEMA_NAME';" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "f" ]
}

@test "Database table 'groups' should not have forced row security level" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT pc.relforcerowsecurity FROM pg_class pc, pg_catalog.pg_namespace pg WHERE pc.relname = 'groups' AND pc.relnamespace = pg.oid AND pg.nspname = '$DATABASE_TESTS_SCHEMA_NAME';" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "f" ]
}

function teardown {
  #Restore previous password
  PGPASSWORD="$PREVIOUS_PGPASSWORD"
}