#!/bin/bash
# This script builds and runs the demo, by using a single script the local mvn repo is preserved so
# the demo is able to use the artifact produced by the add-on project install.
mvn install -DskipTests -Pproduction
cd vcf-toolbar-layout-flow-demo
mvn jetty:run