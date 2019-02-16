

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

  #when usesuper
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(*) FROM pg_user WHERE usename = 'postgresql-core-superuser' AND usesuper IS true;" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "1" ]
}

@test "Database user 'postgresql-core-owner' should not have super user privilege" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when usesuper
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(*) FROM pg_user WHERE usename = 'postgresql-core-owner' AND usesuper IS true;" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "0" ]
}

@test "Database user 'postgresql-core-user' should not have super user privilege" {
  #given
  export PGPASSWORD=postgres_posmulten

  #when usesuper
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT COUNT(*) FROM pg_user WHERE usename = 'postgresql-core-user' AND usesuper IS true;" >&3

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "0" ]
}

function teardown {
  #Restore previous password
  PGPASSWORD="$PREVIOUS_PGPASSWORD"
}