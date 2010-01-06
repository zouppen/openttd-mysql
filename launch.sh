#!/bin/bash -e
#
# Starts openttd-mysql. All configurable variables are in launch.conf.
# A sample configuration is at launch.conf.example.
# Modify this only if you know what you are doing.

OWN_DIR=$(readlink -f "$(dirname "$0")")
cd "$OWN_DIR"
source launch.conf
export CLASSPATH=.:$CONNECTOR

tail -n 0 -f "$LOG"|java LogReader
