CREATE TABLE file_metadata (
    id VARCHAR(36) NOT NULL,          -- Assuming UUIDs for IDs
    original_name VARCHAR(255),
    file_name VARCHAR(255),
    file_size BIGINT,
    sign VARCHAR(255),               -- Assuming it's a string that holds some kind of signature
    properties TEXT,                 -- Properties can be quite large, so TEXT is used here
    create_time DATETIME,
    update_time DATETIME,
    deleted BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE cluster_node (
    id VARCHAR(50) PRIMARY KEY ,
    host VARCHAR(500) NOT NULL,
    heartbeat_time bigint NOT NULL
);
