
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
  export TIMESTAMP=`date +%s`
  export TMP_SQL_FILE="$TIMESTAMP_timestamp.sql"
}

@test "Relation between 'users'(id) and 'users_groups'(user_id) should be one-to-many." {
  #given
  export PGPASSWORD=postgres_posmulten
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    SELECT EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE u
    INNER JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS FK
        on U.CONSTRAINT_CATALOG = FK.UNIQUE_CONSTRAINT_CATALOG
        and U.CONSTRAINT_SCHEMA = FK.UNIQUE_CONSTRAINT_SCHEMA
        and U.CONSTRAINT_NAME = FK.UNIQUE_CONSTRAINT_NAME
    INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE R
        ON R.CONSTRAINT_CATALOG = FK.CONSTRAINT_CATALOG
        AND R.CONSTRAINT_SCHEMA = FK.CONSTRAINT_SCHEMA
        AND R.CONSTRAINT_NAME = FK.CONSTRAINT_NAME
    WHERE
        R.table_name = 'users_groups'
        AND R.column_name = 'user_id'
        AND R.table_schema = 'public'
        AND U.table_name = 'users'
        AND U.column_name = 'id'
        AND U.table_schema = 'public'
    );
SQL


  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Relation between 'groups'(uuid) and 'users_groups'(group_id) should be one-to-many." {
  #given
  export PGPASSWORD=postgres_posmulten
    cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    SELECT EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE u
    INNER JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS FK
        on U.CONSTRAINT_CATALOG = FK.UNIQUE_CONSTRAINT_CATALOG
        and U.CONSTRAINT_SCHEMA = FK.UNIQUE_CONSTRAINT_SCHEMA
        and U.CONSTRAINT_NAME = FK.UNIQUE_CONSTRAINT_NAME
    INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE R
        ON R.CONSTRAINT_CATALOG = FK.CONSTRAINT_CATALOG
        AND R.CONSTRAINT_SCHEMA = FK.CONSTRAINT_SCHEMA
        AND R.CONSTRAINT_NAME = FK.CONSTRAINT_NAME
    WHERE
        R.table_name = 'users_groups'
        AND R.column_name = 'group_id'
        AND R.table_schema = 'public'
        AND U.table_name = 'groups'
        AND U.column_name = 'uuid'
        AND U.table_schema = 'public'
    );
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Relation between 'users'(id) and 'posts'(user_id) should be one-to-many." {
  #given
  export PGPASSWORD=postgres_posmulten
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    SELECT EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE u
    INNER JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS FK
        on U.CONSTRAINT_CATALOG = FK.UNIQUE_CONSTRAINT_CATALOG
        and U.CONSTRAINT_SCHEMA = FK.UNIQUE_CONSTRAINT_SCHEMA
        and U.CONSTRAINT_NAME = FK.UNIQUE_CONSTRAINT_NAME
    INNER JOIN INFORMATION_SCHEMA.KEY_COLUMN_USAGE R
        ON R.CONSTRAINT_CATALOG = FK.CONSTRAINT_CATALOG
        AND R.CONSTRAINT_SCHEMA = FK.CONSTRAINT_SCHEMA
        AND R.CONSTRAINT_NAME = FK.CONSTRAINT_NAME
    WHERE
        R.table_name = 'posts'
        AND R.column_name = 'user_id'
        AND R.table_schema = 'public'
        AND U.table_name = 'users'
        AND U.column_name = 'id'
        AND U.table_schema = 'public'
    );
SQL

  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

function teardown {
  #Restore previous password
  PGPASSWORD="$PREVIOUS_PGPASSWORD"
  rm "$BATS_TMPDIR/$TMP_SQL_FILE"
}