# NOTE: Set passwords in ~/.pgpass, e.g.
# localhost:5432:*:postgres:postgres
# localhost:5432:*:muninitest:1nitest

if [ ! -f muninit_schema/01_schema.sql ]
then
  echo "schema/01_schema.sql not found. Execute this script in it's own folder!"
  exit 0
fi

echo "== create_test_schema.sh =="

export PGCLIENTENCODING="UTF8"

# Drop/create schema 
psql -h localhost -U postgres -d muninitdb <<EOF
DROP SCHEMA IF EXISTS muninitest CASCADE;
DROP USER IF EXISTS muninitest;

CREATE SCHEMA muninitest;
CREATE USER muninitest WITH PASSWORD '1nitest';
\q
EOF

# Create tables 
export PGOPTIONS='--client-min-messages=warning --search-path=muninitest'

ls muninit_schema/*.sql | sort -f |
  while read file
  do
    echo "-- $file"
    psql -h localhost -U postgres -d muninitdb --single-transaction -f "$file"
  done

# Grant required rights
psql -h localhost -U postgres -d muninitdb <<EOF
GRANT CONNECT, TEMP ON DATABASE muninitdb TO muninitest;
GRANT USAGE ON SCHEMA muninitest TO muninitest;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA muninitest TO muninitest;
GRANT ALL ON ALL SEQUENCES IN SCHEMA  muninitest TO muninitest;
\q
EOF
