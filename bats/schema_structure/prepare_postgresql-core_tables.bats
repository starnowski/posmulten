
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
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.tables t JOIN pg_catalog.pg_class c ON (t.table_name = c.relname) JOIN pg_catalog.pg_user u ON (c.relowner = u.usesysid) WHERE t.table_schema='$DATABASE_TESTS_SCHEMA_NAME' AND t.table_name='users' AND u.usename = 'postgresql-core-owner');" >&3

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
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.tables t JOIN pg_catalog.pg_class c ON (t.table_name = c.relname) JOIN pg_catalog.pg_user u ON (c.relowner = u.usesysid) WHERE t.table_schema='$DATABASE_TESTS_SCHEMA_NAME' AND t.table_name='groups' AND u.usename = 'postgresql-core-owner');" >&3

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
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.tables t JOIN pg_catalog.pg_class c ON (t.table_name = c.relname) JOIN pg_catalog.pg_user u ON (c.relowner = u.usesysid) WHERE t.table_schema='$DATABASE_TESTS_SCHEMA_NAME' AND t.table_name='users_groups' AND u.usename = 'postgresql-core-owner');" >&3

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
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.tables t JOIN pg_catalog.pg_class c ON (t.table_name = c.relname) JOIN pg_catalog.pg_user u ON (c.relowner = u.usesysid) WHERE t.table_schema='$DATABASE_TESTS_SCHEMA_NAME' AND t.table_name='posts' AND u.usename = 'postgresql-core-owner');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

function teardown {
  #Restore previous password
  PGPASSWORD="$PREVIOUS_PGPASSWORD"
}