#!/bin/sh

# This is a Gradle wrapper startup script for Unix/Linux/macOS

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

# Execute Gradle
cd "$(dirname "$0")"
exec "$JAVACMD" -Xmx64m -Xms64m -classpath "gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
