#!/bin/sh
set -eu

# Parse host and port from JDBC URL if provided, otherwise fallback to defaults
DB_URL=${DATABASE_URL:-}

if [ -n "$DB_URL" ]; then
  # Expect format jdbc:mysql://host:port/dbname?params
  HOST=$(echo "$DB_URL" | sed -E 's#^jdbc:mysql://([^:/?#]+).*$#\1#')
  PORT=$(echo "$DB_URL" | sed -E 's#^jdbc:mysql://[^:/?#]+:([0-9]+).*$#\1#') || true
fi

: ${HOST:=mysql.railway.internal}
: ${PORT:=3306}

echo "Waiting for database ${HOST}:${PORT}..."

# Wait until TCP port is open
while ! nc -z "$HOST" "$PORT" >/dev/null 2>&1; do
  echo "Waiting for ${HOST}:${PORT}..."
  sleep 1
done

echo "Database is up, starting application"
exec java -jar /app.jar
