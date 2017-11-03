#!/usr/bin/env bash

cd $(dirname $0)

dev_build() {
  mvn package
}

dev_run() {
  # Do what you need to run your app in the foreground
  java -jar target/BookingCalendarGenerator-1.0-SNAPSHOT-jar-with-dependencies.jar $*
}


usage() {
  cat <<EOF
Usage:
  $0 <command> <args>
Local machine commands:
  dev_build        : builds and packages your app
  dev_run <file>   : starts your app in the foreground
EOF
}

action=$1
action=${action:-"usage"}
action=${action/help/usage}
shift
if type -t $action >/dev/null; then
  echo "Invoking: $action"
  $action $*
else
  echo "Unknown action: $action"
  usage
  exit 1
fi
