#!/usr/bin/env bash

set -e
DIRNAME="$(dirname $0)"
echo "Running tests for postgres with version 9.6"
"$DIRNAME"/run_bats_test.sh
echo "Running tests for postgres with version 10"
"$DIRNAME"/run_bats_test.sh --postgres_docker_version 10.14
echo "Running tests for postgres with version 11"
"$DIRNAME"/run_bats_test.sh --postgres_docker_version 11.9
echo "Running tests for postgres with version 12"
"$DIRNAME"/run_bats_test.sh --postgres_docker_version 12.4
echo "Running tests for postgres with version 13"
"$DIRNAME"/run_bats_test.sh --postgres_docker_version 13.0