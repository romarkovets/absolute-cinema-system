DROP TABLE IF EXISTS tickets CASCADE;
DROP TABLE IF EXISTS sessions CASCADE;
DROP TABLE IF EXISTS halls CASCADE;
DROP TABLE IF EXISTS movie_actors CASCADE;
DROP TABLE IF EXISTS actors CASCADE;
DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE movies (
                        movie_id SERIAL PRIMARY KEY,
                        title VARCHAR(200) NOT NULL,
                        description TEXT,
                        duration_minutes INTEGER NOT NULL,
                        release_year INTEGER
);

CREATE TABLE actors (
                        actor_id SERIAL PRIMARY KEY,
                        full_name VARCHAR(150) NOT NULL,
                        country VARCHAR(100)
);

CREATE TABLE movie_actors (
                              movie_id INTEGER REFERENCES movies(movie_id) ON DELETE CASCADE,
                              actor_id INTEGER REFERENCES actors(actor_id) ON DELETE CASCADE,
                              character_name VARCHAR(200),
                              PRIMARY KEY (movie_id, actor_id)
);

CREATE TABLE halls (
                       hall_id SERIAL PRIMARY KEY,
                       hall_name VARCHAR(50) NOT NULL,
                       seat_count INTEGER NOT NULL
);

CREATE TABLE sessions (
                          session_id SERIAL PRIMARY KEY,
                          movie_id INTEGER REFERENCES movies(movie_id) ON DELETE CASCADE,
                          hall_id INTEGER REFERENCES halls(hall_id) ON DELETE CASCADE,
                          start_time TIMESTAMP NOT NULL,
                          price DECIMAL(10,2) NOT NULL,
                          UNIQUE(hall_id, start_time)
);

CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       full_name VARCHAR(150) NOT NULL,
                       phone VARCHAR(20)
);

CREATE TABLE tickets (
                         ticket_id SERIAL PRIMARY KEY,
                         session_id INTEGER REFERENCES sessions(session_id) ON DELETE CASCADE,
                         user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
                         seat_number INTEGER NOT NULL,
                         purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         UNIQUE(session_id, seat_number)
);