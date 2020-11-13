#!/usr/bin/env bash

function resolveScriptDirectory {
    echo "$( cd "$( dirname "${0}" )" >/dev/null 2>&1 && pwd )"
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

