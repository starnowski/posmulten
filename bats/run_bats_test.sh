#!/bin/bash
set -e

function waitUntilDockerContainerIsReady {
    checkCount=1
    timeoutInSecond=180
    while : ; do
        set +e
        results=`psql -qtAX -U postgres -p $DATABASE_PORT --host="$DOCKER_DB_IP" -c "SELECT 1;"`
        [[ "$?" -ne 0 && $checkCount -ne $timeoutInSecond ]] || break
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
trap shutdownDockerContainer EXIT SIGINT

export DATABASE_PORT=15432

sudo docker run --rm --name test-postgres -e POSTGRES_PASSWORD=postgres_posmulten -p 127.0.0.1:15432:5432/tcp -d postgres:9.6.12
export DOCKER_DB_IP="127.0.0.1"

export PGPASSWORD=postgres_posmulten
waitUntilDockerContainerIsReady
psql -qtAX -U postgres -p $DATABASE_PORT --host="$DOCKER_DB_IP" -f "../db_scripts/prepare_postgresql-core_db.sql"


#Run test
bats -rt .


#
# TIPS!
# psql:
# - To quit command line console (no-interactive mode) pass '\q' then press ENTER
# - If docker container is still working, you can login to database by executing below commands:
#   export PGPASSWORD=postgres
#   psql -d postgres -U postgres -p 5432 --host=localhost
#
# In case problem with error "/usr/local/lib/libldap_r-2.4.so.2: no version information available (required by /usr/lib/x86_64-linux-gnu/libpq.so.5)"
# try https://www.dangtrinh.com/2017/04/how-to-fix-usrlocalliblibldapr-24so2-no.html and execute:
# sudo ln -fs /usr/lib/liblber-2.4.so.2 /usr/local/lib/
# sudo ln -fs /usr/lib/libldap_r-2.4.so.2 /usr/local/lib/
#
