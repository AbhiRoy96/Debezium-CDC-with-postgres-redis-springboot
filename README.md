To Run this
---

1. docker exec -it postgres psql -U postgres -d inventory

2. CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO customers (first_name, last_name, email)
VALUES
  ('Abhishek', 'Roy', 'abhishek@example.com'),
  ('Ravi', 'Kumar', 'ravi@example.com');

3. curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" \
  localhost:8083/connectors/ -d @connect/postgres-source.json

4. curl -s localhost:8083/connectors/inventory-connector/status | jq

5. To build the Java app run: mvn clean package -DskipTests 
