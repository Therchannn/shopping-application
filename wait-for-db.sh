#!/bin/sh
set -e

# Use internal Railway network hostname for MySQL service
# Railway services communicate via hostname.railway.internal within same environment
HOST=${DATABASE_HOST:-mysql.railway.internal}
PORT=${DATABASE_PORT:-3306}

echo "[$(date +'%Y-%m-%d %H:%M:%S')] Waiting for MySQL at ${HOST}:${PORT}..."
echo "[$(date +'%Y-%m-%d %H:%M:%S')] This script will give MySQL 90 seconds to be fully ready"

# Wait until TCP port is open (max 90 seconds with incremental delays)
COUNTER=0
MAX_ATTEMPTS=90
while [ $COUNTER -lt $MAX_ATTEMPTS ]; do
  if nc -z "$HOST" "$PORT" >/dev/null 2>&1; then
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] MySQL port is open at ${HOST}:${PORT}"
    break
  fi
  COUNTER=$((COUNTER + 1))
  if [ $((COUNTER % 10)) -eq 0 ]; then
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] Waiting for ${HOST}:${PORT} (${COUNTER}/${MAX_ATTEMPTS})..."
  fi
  sleep 1
done

if [ $COUNTER -ge $MAX_ATTEMPTS ]; then
  echo "[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: MySQL port still closed after ${MAX_ATTEMPTS} seconds"
  exit 1
fi

echo "[$(date +'%Y-%m-%d %H:%M:%S')] MySQL port is open. Waiting 30 seconds for full initialization..."
sleep 30

echo "[$(date +'%Y-%m-%d %H:%M:%S')] Starting Spring Boot application..."
exec java -jar /app/app.jar
