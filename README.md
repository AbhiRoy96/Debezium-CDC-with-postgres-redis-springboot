# Debezium CDC with Postgres, Redis & Spring Boot

A sample project demonstrating change-data-capture (CDC) from PostgreSQL using Debezium and propagating those changes into Redis via a Spring Boot application.

This repository contains a Java (Spring Boot) service that listens to database changes (via Debezium / Kafka Connect) and keeps Redis in sync with the latest row state. The repository also includes Docker artifacts to run the stack locally.

Key ideas
- Capture row-level changes from PostgreSQL (insert/update/delete).
- Debezium (running as Kafka Connect) reads Postgres WAL and emits change events.
- A Spring Boot microservice consumes change events and persists or removes entries in Redis.
- Redis acts as a fast read cache / materialized view of the Postgres data.

---

Features
- Row-level CDC for a Postgres table using Debezium
- Consume Debezium events with Spring (Kafka consumer or Debezium Embedded)
- Sync create/update/delete events to Redis
- Example Docker Compose to bootstrap Postgres, Kafka, Zookeeper, Debezium connector and Redis
- Sample REST endpoints to exercise the database (if provided in the project)

---

Architecture (high level)
1. PostgreSQL (with wal2json / logical replication enabled)
2. Debezium (Kafka Connect) captures WAL changes and publishes to Kafka topics
3. Spring Boot application consumes Kafka topics (or uses embedded Debezium) and updates Redis
4. Redis holds materialized view / cache of table rows for fast reads

---

Quickstart (recommended)

The easiest way to run the this stack is: 

0. You need to clone this repo and run ```docker compose up -d```.

1. Then Login to the Docker container ```docker exec -it postgres psql -U postgres -d inventory```

2. Create required tables

``` 

CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO customers (first_name, last_name, email)
VALUES
  ('Abhishek', 'Roy', 'abhishek@example.com'),
  ('Adam', 'Smith', 'adam@example.com');

```

3. Register Debezium 

``` 
  
  curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" \
  localhost:8083/connectors/ -d @connect/postgres-source.json

```

4. Check the connector has been registered successfully or not ``` curl -s localhost:8083/connectors/inventory-connector/status | jq ```

5. You stack is now ready. You can connect to JetBrains DataSpell and add Datasources(Postgres and Redis). The momment you add new Data to your customer table changes should reflect in your Redis cache.


[OPTIONAL] To build the Java app run: ``` mvn clean package -DskipTests ```

---

Troubleshooting
- No events in Kafka:
  - Verify Postgres logical replication is enabled (wal_level >= logical), and user has REPLICATION privileges.
  - Ensure the Debezium connector uses the correct database name/server.name/table list.
  - Check Connect logs (docker logs <connect-container>).
- Spring app not consuming:
  - Verify Kafka bootstrap servers and topic names.
  - Verify group id and consumer configuration (offsets).
- Redis not updating:
  - Check the Spring application's Redis connection properties.
  - Check for exceptions in application logs.

---

Useful commands
- View Kafka Connect connectors:
  curl http://localhost:8083/connectors
- See connector status:
  curl http://localhost:8083/connectors/inventory-connector/status
- View Kafka topics (using kafka-topics tool inside Kafka image) or via a UI like Kafdrop.

---

Contact / Author
Repository: AbhiRoy96/Debezium-CDC-with-postgres-redis-springboot
Author: AbhiRoy96