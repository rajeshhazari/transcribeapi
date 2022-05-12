-- Run these commands to create the default database.

-- Note: CREATE DATABASE needs to be executed individually.

DO
$do$
BEGIN
   IF NOT EXISTS (
      SELECT                       -- SELECT list can stay empty for this
      FROM   pg_catalog.pg_roles
      WHERE  rolname = 'devuser') THEN

      CREATE ROLE devuser LOGIN PASSWORD 'devuser321' CREATEDB;
   END IF;
END
$do$;

