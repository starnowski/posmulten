
function setup {
  #Save previous password
  PREVIOUS_PGPASSWORD="$PGPASSWORD"
  export TIMESTAMP=`date +%s`
  export TMP_SQL_FILE="$TIMESTAMP_timestamp.sql"
}

@test "Table 'users' should have primary key for column 'id'" {
  #given
  export PGPASSWORD=postgres_posmulten
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    SELECT EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE U
    INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS R
        ON R.CONSTRAINT_NAME = U.CONSTRAINT_NAME
		AND R.TABLE_SCHEMA = U.TABLE_SCHEMA
		AND R.TABLE_NAME = U.TABLE_NAME
		AND R.TABLE_CATALOG = U.TABLE_CATALOG
	WHERE R.CONSTRAINT_TYPE = 'PRIMARY KEY'
    	AND U.COLUMN_NAME = 'id'
    	AND U.TABLE_NAME = 'users'
    	AND U.TABLE_SCHEMA = 'public'
    );
SQL


  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Table 'groups' should have primary key for column 'uuid'" {
  #given
  export PGPASSWORD=postgres_posmulten
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    SELECT EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE U
    INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS R
        ON R.CONSTRAINT_NAME = U.CONSTRAINT_NAME
		AND R.TABLE_SCHEMA = U.TABLE_SCHEMA
		AND R.TABLE_NAME = U.TABLE_NAME
		AND R.TABLE_CATALOG = U.TABLE_CATALOG
	WHERE R.CONSTRAINT_TYPE = 'PRIMARY KEY'
    	AND U.COLUMN_NAME = 'uuid'
    	AND U.TABLE_NAME = 'groups'
    	AND U.TABLE_SCHEMA = 'public'
    );
SQL


  #when
  run psql -qtAX -d postgresql_core -U "postgres" --host="$DOCKER_DB_IP" -p $DATABASE_PORT -f "$BATS_TMPDIR/$TMP_SQL_FILE"

  #then
  echo "output is --> $output <--"  >&3
  [ "$status" -eq 0 ]
  [ "$output" = "t" ]
}

@test "Table 'posts' should have primary key for column 'id'" {
  #given
  export PGPASSWORD=postgres_posmulten
  cat << SQL > "$BATS_TMPDIR/$TMP_SQL_FILE"
    SELECT EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE U
    INNER JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS R
        ON R.CONSTRAINT_NAME = U.CONSTRAINT_NAME
		AND R.TABLE_SCHEMA = U.TABLE_SCHEMA
		AND R.TABLE_NAME = U.TABLE_NAME
		AND R.TABLE_CATALOG = U.TABLE_CATALOG
	WHERE R.CONSTRAINT_TYPE = 'PRIMARY KEY'
    	AND U.COLUMN_NAME = 'id'
    	AND U.TABLE_NAME = 'posts'
    	AND U.TABLE_SCHEMA = 'public'
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