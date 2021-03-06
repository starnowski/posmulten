
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
}

@test "Database table 'users' should exists" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'users');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "The user 'postgresql-core-owner' should be an owner of 'users' table" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM pg_catalog.pg_namespace sch JOIN pg_catalog.pg_class tab ON ( sch.oid = tab.relnamespace ) JOIN pg_catalog.pg_user us ON ( tab.relowner = us.usesysid ) WHERE sch.nspname='$DATABASE_TESTS_SCHEMA_NAME' AND tab.relname='users' AND us.usename = 'postgresql-core-owner');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'groups' should exists" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'groups');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "The user 'postgresql-core-owner' should be an owner of 'groups' table" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM pg_catalog.pg_namespace sch JOIN pg_catalog.pg_class tab ON ( sch.oid = tab.relnamespace ) JOIN pg_catalog.pg_user us ON ( tab.relowner = us.usesysid ) WHERE sch.nspname='$DATABASE_TESTS_SCHEMA_NAME' AND tab.relname='groups' AND us.usename = 'postgresql-core-owner');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'users_groups' should exists" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'users_groups');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "The user 'postgresql-core-owner' should be an owner of 'users_groups' table" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM pg_catalog.pg_namespace sch JOIN pg_catalog.pg_class tab ON ( sch.oid = tab.relnamespace ) JOIN pg_catalog.pg_user us ON ( tab.relowner = us.usesysid ) WHERE sch.nspname='$DATABASE_TESTS_SCHEMA_NAME' AND tab.relname='users_groups' AND us.usename = 'postgresql-core-owner');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'posts' should exists" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'posts');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "The user 'postgresql-core-owner' should be an owner of 'posts' table" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM pg_catalog.pg_namespace sch JOIN pg_catalog.pg_class tab ON ( sch.oid = tab.relnamespace ) JOIN pg_catalog.pg_user us ON ( tab.relowner = us.usesysid ) WHERE sch.nspname='$DATABASE_TESTS_SCHEMA_NAME' AND tab.relname='posts' AND us.usename = 'postgresql-core-owner');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'comments' should exists" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'comments');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "The user 'postgresql-core-owner' should be an owner of 'comments' table" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM pg_catalog.pg_namespace sch JOIN pg_catalog.pg_class tab ON ( sch.oid = tab.relnamespace ) JOIN pg_catalog.pg_user us ON ( tab.relowner = us.usesysid ) WHERE sch.nspname='$DATABASE_TESTS_SCHEMA_NAME' AND tab.relname='comments' AND us.usename = 'postgresql-core-owner');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'notifications' should exists" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'notifications');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "The user 'postgresql-core-owner' should be an owner of 'notifications' table" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM pg_catalog.pg_namespace sch JOIN pg_catalog.pg_class tab ON ( sch.oid = tab.relnamespace ) JOIN pg_catalog.pg_user us ON ( tab.relowner = us.usesysid ) WHERE sch.nspname='$DATABASE_TESTS_SCHEMA_NAME' AND tab.relname='notifications' AND us.usename = 'postgresql-core-owner');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

function teardown {
  #Restore previous password
  PGPASSWORD="$PREVIOUS_PGPASSWORD"
}