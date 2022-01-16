#!/bin/bash

DIRNAME="$(dirname $0)"

# Setting gpg directory path
export GPG_DIR="$(dirname $0)"

# Decrypting key files
openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -md md5 -in $GPG_DIR/pubring.gpg.enc -out $GPG_DIR/pubring.gpg -d
openssl aes-256-cbc -pass pass:$ENCRYPTION_PASSWORD -md md5 -in $GPG_DIR/secring.gpg.enc -out $GPG_DIR/secring.gpg -d
gpg --batch --yes --pinentry-mode loopback --import $GPG_DIR/secring.gpg
#"${DIRNAME}/../mvnw" deploy --settings $GPG_DIR/settings.xml -DperformRelease=true -DskipTests=true -P maven-central-deploy
"${DIRNAME}/../mvnw" clean install -DperformRelease=true -DskipTests=true -P maven-central-deploy
exit $?