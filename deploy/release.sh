#!/bin/bash

DIRNAME="$(dirname $0)"

if [[ $TRAVIS_PULL_REQUEST == "false" ]]; then
    echo "Deploying tag $TRAVIS_TAG"

    # Setting gpg directory path
    export GPG_DIR="$(dirname $0)"

    # Decrypting key files
    openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in $GPG_DIR/pubring.gpg.enc -out $GPG_DIR/pubring.gpg -d
    openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -in $GPG_DIR/secring.gpg.enc -out $GPG_DIR/secring.gpg -d
    "${DIRNAME}/../mvnw" deploy --settings $GPG_DIR/settings.xml -DperformRelease=true -DskipTests=true -P maven-central-deploy
    exit $?
fi