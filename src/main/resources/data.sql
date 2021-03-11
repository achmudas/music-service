CREATE TABLE artist (
  id INT,
  name VARCHAR(250) NOT NULL,
  PRIMARY KEY(id)
);

CREATE TABLE album (
    id INT PRIMARY KEY,
    artist_id INT,
    CONSTRAINT fk_album_artist
          FOREIGN KEY(artist_id)
    	  REFERENCES artist(id)
);

CREATE TABLE "USER" (
    id INT,
    first_name VARCHAR(250),
    last_name VARCHAR(250),
    artist_id INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_artist
          FOREIGN KEY(artist_id)
          REFERENCES artist(id)
);