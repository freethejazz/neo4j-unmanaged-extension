#! /bin/bash

mvn clean install
mkdir -p infra/plugins
cp target/unmanaged-extension-template-1.0.jar infra/plugins/
