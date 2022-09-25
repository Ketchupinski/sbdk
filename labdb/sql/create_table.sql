DROP TABLE IF EXISTS doctor_visits;
DROP TABLE IF EXISTS diagnoses;
DROP TABLE IF EXISTS visits;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS specializations;



CREATE TABLE IF NOT EXISTS patients
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS visits
(
    id          SERIAL PRIMARY KEY,
    patient_id  INT          NOT NULL,
    date        DATE         NOT NULL,
    description VARCHAR(255) NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS doctors
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    specialization VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS doctor_visits
(
    id        SERIAL PRIMARY KEY,
    doctor_id INT NOT NULL,
    visit_id  INT NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctors (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (visit_id) REFERENCES visits (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS diagnoses
(
    id       SERIAL PRIMARY KEY,
    visit_id INT          NOT NULL,
    name     VARCHAR(255) NOT NULL,
    FOREIGN KEY (visit_id) REFERENCES visits (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO patients (name)
VALUES ('John Smith'),
       ('Donald Trump'),
       ('Barak Obama');

INSERT INTO doctors (name, specialization)
VALUES ('Dr. House', 'Cardiologist'),
       ('Dr. Strange', 'Neurologist'),
       ('Dr. Who', 'Therapist');

INSERT INTO visits (patient_id, date, description)
VALUES (1, '2017-01-01', 'Fever'),
       (2, '2017-01-02', 'Cough'),
       (3, '2017-01-03', 'Broken leg');

INSERT INTO doctor_visits (doctor_id, visit_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);

INSERT INTO diagnoses (visit_id, name)
VALUES (1, 'Flu'),
       (2, 'Cancer'),
       (3, 'Broken leg');

