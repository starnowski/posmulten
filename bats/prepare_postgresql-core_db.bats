

@test "Database user 'postgresql-core-owner' should be able to login to database 'postgresql_core' with password 'owner123'" {
  export PGPASSWORD=owner123
  run psql -qtAX -d postgresql_core -U "postgresql-core-owner" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -c "SELECT 'user postgresql-core-owner logged';" >&3
  [ "$status" -eq 0 ]
  echo "output is --> $output <--"
  [ "$output" = "user postgresql-core-owner logged" ]
}