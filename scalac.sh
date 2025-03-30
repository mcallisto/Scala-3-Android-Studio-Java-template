#!/bin/bash
# Wrapper script for Scala compiler using jars from buildSrc/lib

# Set up the classpath with all the Scala compiler jars
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SCALA_LIB_DIR="$SCRIPT_DIR/buildSrc/lib"
SCALA_CP=""

# Build the classpath with all the jars in the lib directory
for jar in "$SCALA_LIB_DIR"/*.jar; do
  if [ -n "$SCALA_CP" ]; then
    SCALA_CP="$SCALA_CP:$jar"
  else
    SCALA_CP="$jar"
  fi
done

# Execute the Scala compiler with the classpath
exec java -cp "$SCALA_CP" dotty.tools.dotc.Main "$@" 