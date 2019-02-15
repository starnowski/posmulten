#!/bin/bash
set -e

function shutdownDockerContainer {
    lastCommandStatus="$?"
    echo "Shutting down docker container"
    sudo docker stop test-postgres
    exit $lastCommandStatus
}
trap shutdownDockerContainer EXIT SIGINT

export DATABASE_PORT=5432

sudo docker run --rm --name test-postgres -e POSTGRES_PASSWORD=postgres -d postgres:9.6

export PGPASSWORD=postgres
psql -qtAX -U postgres -p $DATABASE_PORT --host=localhost -f "../db_scripts/prepare_postgresql-core_db.sql"


#Run test
bats -r .


#
# TIPS!
# psql:
# - To quit command line console (no-interactive mode) pass '\q' then press ENTER
# - If docker container is still working, you can login to database by executing below commands:
#   export PGPASSWORD=postgres
#   psql -d postgres -U postgres -p 5432 --host=localhost
#