#!/usr/bin/env bash

DIRNAME="$(dirname $0)"
"$DIRNAME"/run_bats_test.sh
"$DIRNAME"/run_bats_test.sh --postgres_docker_version 10.14
"$DIRNAME"/run_bats_test.sh --postgres_docker_version 11.9
"$DIRNAME"/run_bats_test.sh --postgres_docker_version 12.4
"$DIRNAME"/run_bats_test.sh --postgres_docker_version 13.0