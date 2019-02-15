
function setup {
    DATABASE_PORT=5432
}

@test "Database user 'postgresql-core-owner' should be able to login to database 'postgresql_core' with password 'owner123'" {
  run psql -qtAX -d postgresql_core -U "postgresql-core-owner" -p $DATABASE_PORT -c "SELECT 1 FROM DUAL;"
  [ "$status" -eq 0 ]
  [ "$output" = "foo: no such file 'nonexistent_filename'" ]
}