#!/bin/bash
set -e
mvn clean verify
hg push