
#!/usr/bin/env bash

# Demarrage de PostgreSQL
set -euo pipefail

echo ">> Demarrage de PostgreSQL"
sudo systemctl start postgresql.service

echo ">> Attente du serveur"
for i in $(seq 1 15); do
    pg_isready -h localhost -p 5432 && break
    sleep 1
done

pg_isready -h localhost -p 5432

echo ">> Creation de la base tp"
sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'postgres';"
sudo -u postgres createdb tp

echo ">> Creation de la table students"
PGPASSWORD=postgres psql -h localhost -U postgres -d tp \
    -v ON_ERROR_STOP=1 -f "$(dirname "$0")/init.sql"
