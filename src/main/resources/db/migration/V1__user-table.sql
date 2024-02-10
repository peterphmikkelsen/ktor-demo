CREATE TABLE users (
    id uuid PRIMARY KEY,
    name varchar(255) NOT NULL,
    age int NOT NULL,
    email varchar(255) NOT NULL,
    phone_number varchar(20),
    status varchar(30) NOT NULL,
    created_at timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamptz DEFAULT CURRENT_TIMESTAMP
);