

function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
}

@test "Database user 'postgresql-core-owner' should be able to login to database 'postgresql_core' with password 'owner123'" {
  export PGPASSWORD=owner123
  run psql -qtAX -d postgresql_core -U "postgresql-core-owner" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT 'user postgresql-core-owner logged';" >&3
  [ "$status" -eq 0 ]
  echo "output is --> $output <--"
  [ "$output" = "user postgresql-core-owner logged" ]
}

@test "Database user 'postgresql-core-user' should be able to login to database 'postgresql_core' with password 'user123'" {
  export PGPASSWORD=user123
  run psql -qtAX -d postgresql_core -U "postgresql-core-user" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT 'user postgresql-core-user logged';" >&3
  [ "$status" -eq 0 ]
  echo "output is --> $output <--"
  [ "$output" = "user postgresql-core-user logged" ]
}

function teardown {
  #Restore previous password
  PGPASSWORD="$PREVIOUS_PGPASSWORD"
}