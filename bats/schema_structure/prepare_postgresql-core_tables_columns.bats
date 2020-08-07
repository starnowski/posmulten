
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
}

@test "Database table 'users' should have column 'name' with data type 'character varying' and maximal length '255'" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'users' AND column_name = 'name' AND data_type = 'character varying' AND character_maximum_length = 255);" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'groups' should have column 'name' with data type 'character varying' and maximal length '255" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'groups' and column_name = 'name' AND data_type = 'character varying' AND character_maximum_length = 255);" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'posts' should have column 'text' with data type 'text' and without maximal length" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'posts' and column_name = 'text' AND data_type = 'text' AND character_maximum_length IS NULL);" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'users' should have column 'tenant_id' with data type 'character varying' and maximal length '255'" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'users' AND column_name = 'tenant_id' AND data_type = 'character varying' AND character_maximum_length = 255);" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'groups' should have column 'tenant_id' with data type 'character varying' and maximal length '255" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'groups' and column_name = 'tenant_id' AND data_type = 'character varying' AND character_maximum_length = 255);" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'posts' should have column 'tenant_id' with data type 'character varying' and maximal length '255" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'posts' and column_name = 'tenant_id' AND data_type = 'character varying' AND character_maximum_length = 255);" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'users_groups' should have column 'tenant_id' with data type 'character varying' and maximal length '255" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'users_groups' and column_name = 'tenant_id' AND data_type = 'character varying' AND character_maximum_length = 255);" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'comments' should have column 'tenant' with data type 'character varying' and maximal length '255" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'comments' and column_name = 'tenant' AND data_type = 'character varying' AND character_maximum_length = 255);" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'comments' should have column 'text' with data type 'text' and without maximal length" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'comments' and column_name = 'text' AND data_type = 'text' AND character_maximum_length IS NULL);" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'notifications' should have column 'title' with data type 'character varying' and maximal length '255" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'notifications' and column_name = 'title' AND data_type = 'character varying' AND character_maximum_length = 255);" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'notifications' should have column 'content' with data type 'text' and without maximal length" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'notifications' and column_name = 'content' AND data_type = 'text' AND character_maximum_length IS NULL);" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Database table 'notifications' should not have column 'tenant_id'" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE  table_schema = '$DATABASE_TESTS_SCHEMA_NAME' AND table_name = 'notifications' and column_name = 'tenant_id');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

function teardown {
  #Restore previous password
  PGPASSWORD="$PREVIOUS_PGPASSWORD"
}