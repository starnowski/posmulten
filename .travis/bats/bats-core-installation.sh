#!/bin/bash

wget https://github.com/starnowski/bats-core/archive/v1.1.0.zip
unzip v1.1.0.zip
mv bats-core-1.1.0/ "$1/bats-core"
rm v1.1.0.zip