#!/usr/bin/env bash

function resolveScriptDirectory {
    echo "$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
}

function waitUntilDockerContainerIsReady {
    checkCount=1
    timeoutInSeconds=180
    while : ; do
        set +e
        results=`psql -qtAX -U postgres -p $DATABASE_PORT --host="$DOCKER_DB_IP" -c "SELECT 1;"`
        [[ "$?" -ne 0 && $checkCount -ne $timeoutInSeconds ]] || break
        checkCount=$(( checkCount+1 ))
        echo "Waiting $checkCount seconds for database to start"
        sleep 1
    done
    set -e
}

function shutdownDockerContainer {
    lastCommandStatus="$?"
    echo "Shutting down docker container"
    sudo docker rm -f -v test-postgres
    exit $lastCommandStatus
}

function exportScriptDirEnvironment {
    SCRIPT_DIR=`resolveScriptDirectory`
    export SCRIPT_DIR="$SCRIPT_DIR"
}

function startPostgresDockerContainer {
    sudo docker run --rm --name test-postgres -e POSTGRES_PASSWORD=postgres_posmulten -p 127.0.0.1:$DATABASE_PORT:5432/tcp -d postgres:$POSTGRES_DOCKER_VERSION
}

function preparePostgresDatabase {
    psql -qtAX -U postgres -p $DATABASE_PORT --host="$DOCKER_DB_IP" -f "$SCRIPT_DIR/../db_scripts/prepare_postgresql-core_db.sql"
}