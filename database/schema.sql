DROP TABLE IF EXISTS ticket CASCADE;
DROP TABLE IF EXISTS "session" CASCADE;
DROP TABLE IF EXISTS hall CASCADE;
DROP TABLE IF EXISTS movie_actor CASCADE;
DROP TABLE IF EXISTS actor CASCADE;
DROP TABLE IF EXISTS movie CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;

CREATE TABLE movie (
                       movie_id SERIAL PRIMARY KEY,
                       title VARCHAR(200) NOT NULL,
                       description TEXT,
                       duration_minutes INTEGER NOT NULL,
                       release_year INTEGER
);

CREATE TABLE actor (
                       actor_id SERIAL PRIMARY KEY,
                       full_name VARCHAR(150) NOT NULL,
                       country VARCHAR(100)
);

CREATE TABLE movie_actor (
                             movie_id INTEGER REFERENCES movie(movie_id) ON DELETE CASCADE,
                             actor_id INTEGER REFERENCES actor(actor_id) ON DELETE CASCADE,
                             character_name VARCHAR(200),
                             PRIMARY KEY (movie_id, actor_id)
);

CREATE TABLE hall (
                      hall_id SERIAL PRIMARY KEY,
                      hall_number INTEGER NOT NULL,
                      hall_type INTEGER NOT NULL
);

CREATE TABLE "session" (
                           session_id SERIAL PRIMARY KEY,
                           movie_id INTEGER REFERENCES movie(movie_id) ON DELETE CASCADE,
                           hall_id INTEGER REFERENCES hall(hall_id) ON DELETE CASCADE,
                           start_time TIME NOT NULL,
                           session_date DATE NOT NULL,
                           price DECIMAL(10,2) NOT NULL,
                           UNIQUE(hall_id, session_date, start_time)
);

CREATE TABLE "user" (
                        user_id SERIAL PRIMARY KEY,
                        email VARCHAR(100) UNIQUE NOT NULL,
                        full_name VARCHAR(150) NOT NULL,
                        passport VARCHAR(9) UNIQUE NOT NULL,
                        phone VARCHAR(20)
);

CREATE TABLE ticket (
                        ticket_id SERIAL PRIMARY KEY,
                        session_id INTEGER REFERENCES "session"(session_id) ON DELETE CASCADE,
                        user_id INTEGER REFERENCES "user"(user_id) ON DELETE CASCADE,
                        seat_number INTEGER NOT NULL,
                        purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        UNIQUE(session_id, seat_number)
);