-- ============================================
-- Inventory Service Database Initialization
-- ============================================

-- Create the inventory_service database
CREATE DATABASE inventory_service;

-- Create admin user (if not exists)
DO
    $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'admin') THEN
CREATE USER admin WITH PASSWORD 'password';
END IF;
END
$$;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE inventory_service TO admin;

-- Log success
DO
    $$
BEGIN
    RAISE NOTICE 'Database inventory_service initialized successfully';
END $$;