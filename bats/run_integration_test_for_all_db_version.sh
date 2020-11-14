#!/usr/bin/env bash

set -e
DIRNAME="$(dirname $0)"
echo "Running integration tests for postgres with version 10"
"$DIRNAME"/run_integration_tests.sh --postgres_docker_version 10.14
echo "Running integration tests for postgres with version 11"
"$DIRNAME"/run_integration_tests.sh --postgres_docker_version 11.9
echo "Running integration tests for postgres with version 12"
"$DIRNAME"/run_integration_tests.sh --postgres_docker_version 12.4
echo "Running integration tests for postgres with version 13"
"$DIRNAME"/run_integration_tests.sh --postgres_docker_version 13.0