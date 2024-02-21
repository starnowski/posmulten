#!/bin/bash
set -e

DIRNAME="$(dirname $0)"
. "$DIRNAME"/utils.sh

export POSTGRES_DOCKER_VERSION="9.6.12"

# Call getopt to validate the provided input.
options=$(getopt -o "" --long postgres_docker_version: -- "$@")
[ $? -eq 0 ] || {
    echo "Incorrect options provided"
    exit 1
}
eval set -- "$options"
while true; do
    case "$1" in
    --postgres_docker_version)
        shift; # The arg is next in position args
        export POSTGRES_DOCKER_VERSION="$1"
        ;;
    --)
        shift
        break
        ;;
    esac
    shift
done

echo "Running test for postgres docker image with version $POSTGRES_DOCKER_VERSION"

trap shutdownDockerContainer EXIT SIGINT

export DATABASE_PORT=15432

startPostgresDockerContainer

export DOCKER_DB_IP="127.0.0.1"
export PGPASSWORD=postgres_posmulten

waitUntilDockerContainerIsReady

exportScriptDirEnvironment
preparePostgresDatabase


#Run test
set +e
bats -rt "$SCRIPT_DIR/prepare_postgresql-core_db"
DATABASE_TESTS_RESULT="$?"

echo "Running schema structure tests for 'public' schema"
export DATABASE_TESTS_SCHEMA_NAME="public"
bats -rt "$SCRIPT_DIR/schema_structure"
DATABASE_TESTS_PUBLIC_SCHEMA_RESULT="$?"

echo "Running schema structure tests for 'non_public_schema' schema"
export DATABASE_TESTS_SCHEMA_NAME="non_public_schema"
bats -rt "$SCRIPT_DIR/schema_structure"
DATABASE_TESTS_NON_PUBLIC_SCHEMA_RESULT="$?"

echo "Running schema structure tests for 'non_public_schema_with_composite_key' schema"
export DATABASE_TESTS_SCHEMA_NAME="non_public_schema_with_composite_key"
bats --exclude "prepare_postgresql-core_primary_keys.bats" --exclude "prepare_postgresql-core_foreign_keys.bats" -rt "$SCRIPT_DIR/schema_structure"
DATABASE_TESTS_NON_PUBLIC_SCHEMA_COMPOSITE_KEY_RESULT="$?"

echo "Running rls policy tests for 'public' schema"
export DATABASE_TESTS_SCHEMA_NAME="public"
bats -rt "$SCRIPT_DIR/rls_policy"
DATABASE_TESTS_PUBLIC_RLS_RESULT="$?"

echo "Running rls policy tests for 'non_public_schema' schema"
export DATABASE_TESTS_SCHEMA_NAME="non_public_schema"
bats -rt "$SCRIPT_DIR/rls_policy"
DATABASE_TESTS_NON_PUBLIC_RLS_RESULT="$?"

echo "Running rls policy tests for 'non_public_schema_with_composite_key' schema"
export DATABASE_TESTS_SCHEMA_NAME="non_public_schema_with_composite_key"
bats -rt "$SCRIPT_DIR/rls_policy"
DATABASE_TESTS_NON_PUBLIC_SCHEMA_COMPOSITE_KEY_RLS_RESULT="$?"

[[ $DATABASE_TESTS_RESULT -eq 0 ]] && [[ $DATABASE_TESTS_PUBLIC_SCHEMA_RESULT -eq 0 ]] && [[ $DATABASE_TESTS_NON_PUBLIC_SCHEMA_RESULT -eq 0 ]] && [[ $DATABASE_TESTS_PUBLIC_RLS_RESULT -eq 0 ]] && [[ $DATABASE_TESTS_NON_PUBLIC_RLS_RESULT -eq 0 ]] && [[ $DATABASE_TESTS_NON_PUBLIC_SCHEMA_COMPOSITE_KEY_RESULT -eq 0 ]] && [[ $DATABASE_TESTS_NON_PUBLIC_SCHEMA_COMPOSITE_KEY_RLS_RESULT -eq 0 ]]

#
# TIPS!
# psql:
# - To quit command line console (no-interactive mode) pass '\q' then press ENTER
# - If docker container is still working, you can login to database by executing below commands:
#   export PGPASSWORD=postgres
#   psql -d postgres -U postgres -p 15432 --host=127.0.0.1
#
# In case problem with error "/usr/local/lib/libldap_r-2.4.so.2: no version information available (required by /usr/lib/x86_64-linux-gnu/libpq.so.5)"
# try https://www.dangtrinh.com/2017/04/how-to-fix-usrlocalliblibldapr-24so2-no.html and execute:
# sudo ln -fs /usr/lib/liblber-2.4.so.2 /usr/local/lib/
# sudo ln -fs /usr/lib/libldap_r-2.4.so.2 /usr/local/lib/
#
