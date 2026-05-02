#!/bin/sh
set -eu

# Use internal Railway network hostname for MySQL service
# Railway services communicate via hostname.railway.internal within same environment
HOST=${DATABASE_HOST:-mysql.railway.internal}
PORT=${DATABASE_PORT:-3306}

echo "Waiting for MySQL at ${HOST}:${PORT}..."

# Wait until TCP port is open (max 60 seconds)
COUNTER=0
while ! nc -z "$HOST" "$PORT" >/dev/null 2>&1; do
  COUNTER=$((COUNTER + 1))
  if [ $COUNTER -gt 60 ]; then
    echo "ERROR: MySQL not ready after 60 seconds at ${HOST}:${PORT}"
    exit 1
  fi
  echo "Attempting ${COUNTER}/60: Waiting for ${HOST}:${PORT}..."
  sleep 1
done

echo "MySQL is ready! Starting Spring Boot application..."
exec java -jar /app/app.jar
