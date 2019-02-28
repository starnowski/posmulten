

function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
}

@test "Database user 'postgresql-core-owner' should be able to login to database 'postgresql_core' with password 'owner123'" {
  #given
  export PGPASSWORD=owner123

  #when
  run psql -qtAX -d postgresql_core -U "postgresql-core-owner" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT 'user postgresql-core-owner logged';" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "user postgresql-core-owner logged" ]
}

@test "Database user 'postgresql-core-user' should be able to login to database 'postgresql_core' with password 'user123'" {
  #given
  export PGPASSWORD=user123

  #when
  run psql -qtAX -d postgresql_core -U "postgresql-core-user" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT 'user postgresql-core-user logged';" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "user postgresql-core-user logged" ]
}

@test "Database user 'postgresql-core-superuser' should be able to login to database 'postgresql_core' with password 'superuser123'" {
  #given
  export PGPASSWORD=superuser123

  #when
  run psql -qtAX -d postgresql_core -U "postgresql-core-superuser" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT 'user postgresql-core-superuser logged';" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "user postgresql-core-superuser logged" ]
}

@test "Database user 'postgresql-core-superuser' should have super user privilege" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(*) FROM pg_user WHERE usename = 'postgresql-core-superuser' AND usesuper IS true;" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "1" ]
}

@test "Database user 'postgresql-core-owner' should not have super user privilege" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(*) FROM pg_user WHERE usename = 'postgresql-core-owner' AND usesuper IS true;" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "0" ]
}

@test "Database user 'postgresql-core-user' should not have super user privilege" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(*) FROM pg_user WHERE usename = 'postgresql-core-user' AND usesuper IS true;" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "0" ]
}

@test "Database table 'users' should exists" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM   information_schema.tables WHERE  table_schema = 'public' AND table_name = 'users');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "true" ]
}

@test "The user 'postgresql-core-owner' should be an owner of 'users' table" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT EXISTS (SELECT 1 FROM information_schema.tables t JOIN pg_catalog.pg_class c ON (t.table_name = c.relname) JOIN pg_catalog.pg_user u ON (c.relowner = u.usesysid) WHERE t.table_schema='public' AND t.table_name='users' AND u.usename = 'postgresql-core-owner');" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "true" ]
}

function teardown {
  #Restore previous password
  PGPASSWORD="$PREVIOUS_PGPASSWORD"
}