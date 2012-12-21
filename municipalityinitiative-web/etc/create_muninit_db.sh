# Usage: sh ./create_db.sh <locale> <password for initiative>
# NOTE: Set passwords in ~/.pgpass, e.g.
# localhost:5432:*:postgres:postgres
# localhost:5432:*:municipalityinitiative:Eskim0

# Verify user dir
if [ ! -f muninit_schema/01_schema.sql ]
then
  echo "ERROR: muninit_schema/01_schema.sql not found in the working directory."
  exit 1
fi

# Check parameter count
if [ "$#" -eq 2 ]
then 
  DBPWD=$2
elif [ "$#" -eq 1 ]
then 
  read -p "Password for municipalityinitiative: " DBPWD
else
  echo "USAGE: $0 <locale> <password for municipalityinitiative>"
  exit 2
fi

# Confirm drop/create
read -p "Drop and create database (yes/no)? " confirmation
confirmation="$(echo ${confirmation} | tr 'A-Z' 'a-z')"

if [[ $confirmation =~ ^(y|yes)$ ]]
then
     echo "== create_db.sh =="
else
     echo "Aborted create_db.sh!"
     exit 3
fi


# Drop/create database and schema

export PGCLIENTENCODING="UTF8"

# Create database as superuser
psql -U postgres <<EOF
DROP DATABASE IF EXISTS muninitdb;
DROP USER IF EXISTS municipalityinitiative;

CREATE DATABASE muninitdb ENCODING 'UTF8' LC_COLLATE '$1' LC_CTYPE '$1' TEMPLATE template0;
CREATE USER municipalityinitiative WITH PASSWORD '$DBPWD';
\q
EOF

# Create schema 
psql -U postgres -d muninitdb <<EOF
CREATE SCHEMA municipalityinitiative;
\q
EOF

# Execute schema files
export PGOPTIONS='--client-min-messages=warning --search-path=municipalityinitiative'

ls muninit_schema/*.sql | sort -f |
  while read file
  do
    echo "-- $file"
    psql -U postgres -d muninitdb --single-transaction -f "$file"
  done

# Grant required rights
psql -U postgres -d muninitdb <<EOF
GRANT CONNECT, TEMP ON DATABASE muninitdb TO municipalityinitiative;
GRANT USAGE ON SCHEMA municipalityinitiative TO municipalityinitiative;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA municipalityinitiative TO municipalityinitiative;
GRANT ALL ON ALL SEQUENCES IN SCHEMA  municipalityinitiative TO municipalityinitiative;
\q
EOF
