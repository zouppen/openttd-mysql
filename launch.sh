#!/bin/bash -e
#
# Starts openttd-mysql. All configurable variables are in launch.conf.
# A sample configuration is at launch.conf.example.
# Modify this only if you know what you are doing.

source launch.conf

OWN_DIR=$(readlink -f "$(dirname "$0")")
export CLASSPATH=.:$CONNECTOR
cd "$OWN_DIR"

tail -n 0 -f "$LOG"|java LogReader
