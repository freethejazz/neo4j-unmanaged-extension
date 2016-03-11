#! /bin/bash

mvn clean install
cp target/unmanaged-extension-template-1.0.jar infra/plugins/
