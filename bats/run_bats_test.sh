#!/bin/bash
set -e

export DATABASE_PORT=5432

sudo docker run -d --rm --name test-postgres postgres:9.6 -e POSTGRES_PASSWORD=postgres -d postgres

psql -qtAX -U postgres -p $DATABASE_PORT -f ../db_scripts/prepare_postgresql-core_db.sql

#Run test
bats -r .
