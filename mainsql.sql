create database hospital_1;
use hospital_1; 
SHOW TABLES;


create table admin(
admin_id varchar(6) primary key not null,
username varchar(50),
name varchar(50),
age varchar(50),
phone varchar(11),
city varchar(50),
email varchar(50),
gender varchar(50),
password varchar(50));

INSERT INTO admin (admin_id, username, name, age, phone, city, email, gender, password)
VALUES ("558574", "admin", "Pritha", "22", "01234567891", "Narayanganj", "pritha@gmail.com", "female", "admin");

SELECT * FROM admin WHERE username='admin' AND password='admin';

select *from admin;

CREATE TABLE doctor (
    doctor_id VARCHAR(6) PRIMARY KEY,
    name VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    specialization VARCHAR(50),
    phone VARCHAR(11),
    email VARCHAR(50),
    city VARCHAR(50),
    password VARCHAR(20)
);



INSERT INTO doctor (doctor_id, name, age, gender, specialization, phone, email, city, password)
VALUES 
('D001', 'Dr. Ayesha Rahman', '35', 'Female', 'Cardiologist', '01712345678', 'ayesha@example.com', 'Dhaka', 'D001ar'),
('D002', 'Dr. Tanvir Hossain', '40', 'Male', 'Dermatologist', '01987654321', 'tanvir@example.com', 'Chittagong', 'D002th'),
('D003', 'Dr. Nusrat Jahan', '30', 'Female', 'Pediatrician', '01811223344', 'nusrat@example.com', 'Khulna', 'D003nj');
ALTER TABLE appointment ADD COLUMN first_visit BOOLEAN NOT NULL DEFAULT TRUE;


ALTER TABLE doctor
ADD COLUMN fees DECIMAL(10,2) NOT NULL DEFAULT 0;

UPDATE doctor SET fees = 1000.00 WHERE doctor_id = 'D001';
UPDATE doctor SET fees = 1200.00 WHERE doctor_id = 'D002';
UPDATE doctor SET fees = 900.00 WHERE doctor_id = 'D003';

select * from doctor;
DROP TABLE  doctor;

CREATE TABLE patient (
    patient_id VARCHAR(6) PRIMARY KEY,
    name VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    phone VARCHAR(11),
    email VARCHAR(50),
    address VARCHAR(100),
    password VARCHAR(20)
);
select *from patient;

CREATE TABLE IF NOT EXISTS appointment (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(6),
    doctor_id VARCHAR(6),
    appointment_date DATE,
    appointment_time TIME,
    is_emergency BOOLEAN DEFAULT FALSE,
    pet_category VARCHAR(50),
    type VARCHAR(20),
    reminder BOOLEAN DEFAULT FALSE,
    status VARCHAR(20) DEFAULT 'pending', -- New column to track completion
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);

ALTER TABLE appointment ADD COLUMN final_fee DECIMAL(10,2);


SET SQL_SAFE_UPDATES = 0;


UPDATE appointment 
SET status = 'completed' 
WHERE appointment_date < CURDATE() AND appointment_id IS NOT NULL;


UPDATE appointment 
SET status = 'completed' 
WHERE appointment_date < CURDATE() 
   OR (appointment_date = CURDATE() AND appointment_time < CURTIME());



SET SQL_SAFE_UPDATES = 1;



select *from appointment;
drop table appointment;

CREATE TABLE medicine (
    medicine_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100),
    type VARCHAR(50),
    price DECIMAL(10,2),
    description TEXT,
    quantity INT,
    expiry_date DATE
);
INSERT INTO medicine (medicine_id, name, type, price, description, quantity, expiry_date) VALUES
('M001', 'Petcal', 'Tablet', 15.50, 'Calcium supplement for pets', 100, '2026-12-31'),
('M002', 'CanineCough Syrup', 'Syrup', 25.00, 'Relieves cough in dogs', 50, '2025-06-30'),
('M003', 'FeliVax', 'Injection', 100.00, 'Vaccine for cats', 20, '2027-03-15');
select *from medicine;

CREATE TABLE medicine_orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(50),
    medicine_name VARCHAR(100),
    type VARCHAR(50),
    quantity INT,
    price_per_unit DECIMAL(10,2),
    subtotal DECIMAL(10,2),
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP
);



select *from medicine_orders;
drop table medicine_orders;



CREATE TABLE medical_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(20),
    doctor_name VARCHAR(100),
    pet_name VARCHAR(100),
    pet_category VARCHAR(50),
    diagnosis TEXT,
    prescription TEXT,
    date DATE
); 

Drop Table medical_records;

select *from medical_records;

SHOW PLUGINS;

ALTER USER 'root'@'localhost' IDENTIFIED BY 'Sumit123!';
FLUSH PRIVILEGES;

INSTALL PLUGIN mysql_native_password SONAME 'authentication_mysql_native_password';


ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Sumit123!';
FLUSH PRIVILEGES;

SELECT user, host, plugin FROM mysql.user WHERE user='root';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Sumit123!';
FLUSH PRIVILEGES;


-- Drop dependent table FIRST
DROP TABLE IF EXISTS appointment;

-- Then drop doctor and patient tables
DROP TABLE IF EXISTS doctor;
DROP TABLE IF EXISTS patient;

-- Recreate doctor table
CREATE TABLE doctor (
    doctor_id VARCHAR(6) PRIMARY KEY,
    name VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    specialization VARCHAR(50),
    phone VARCHAR(11),
    email VARCHAR(50),
    city VARCHAR(50),
    password VARCHAR(20),
    fees DECIMAL(10,2) NOT NULL DEFAULT 0
);

INSERT INTO doctor (doctor_id, name, age, gender, specialization, phone, email, city, password, fees)
VALUES 
('D001', 'Dr. Ayesha Rahman', 35, 'Female', 'Cardiologist', '01712345678', 'ayesha@example.com', 'Dhaka', 'D001ar', 1000.00),
('D002', 'Dr. Tanvir Hossain', 40, 'Male', 'Dermatologist', '01987654321', 'tanvir@example.com', 'Chittagong', 'D002th', 1200.00),
('D003', 'Dr. Nusrat Jahan', 30, 'Female', 'Pediatrician', '01811223344', 'nusrat@example.com', 'Khulna', 'D003nj', 900.00);

select *from doctor;
-- Recreate patient table
CREATE TABLE patient (
    patient_id VARCHAR(6) PRIMARY KEY,
    name VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    phone VARCHAR(11),
    email VARCHAR(50),
    address VARCHAR(100),
    password VARCHAR(20)
);
select *from patient;

-- Recreate appointment table
CREATE TABLE appointment (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(6),
    doctor_id VARCHAR(6),
    appointment_date DATE,
    appointment_time TIME,
    is_emergency BOOLEAN DEFAULT FALSE,
    pet_category VARCHAR(50),
    type VARCHAR(20),
    reminder BOOLEAN DEFAULT FALSE,
    status VARCHAR(20) DEFAULT 'pending',
    first_visit BOOLEAN NOT NULL DEFAULT TRUE,
    final_fee DECIMAL(10,2),
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);
select *from appointment;

CREATE TABLE home_service_appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(50) NOT NULL,
    doctor_id VARCHAR(50) NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    pet_category VARCHAR(50),
    fee INT NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    reminder BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (doctor_id, appointment_date, appointment_time) -- to avoid double booking same doctor/time
);
select *from home_service_appointments;


drop table medical_records;
CREATE TABLE medical_records (
    id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT,
    patient_id VARCHAR(20),
    doctor_id VARCHAR(20),
    advice TEXT,
    prescription TEXT,
    pet_category VARCHAR(50),
    type VARCHAR(20),
    record_date DATE
);
select *from medical_records;

UPDATE medical_records
SET advice = REPLACE(advice, 'Write advice here...', '')
WHERE advice LIKE 'Write advice here...%' AND id > 0;


UPDATE medical_records
SET prescription = REPLACE(prescription, 'Write prescription here...', '')
WHERE prescription LIKE 'Write prescription here...%' AND id > 0;




UPDATE medical_records
SET patient_id = TRIM(patient_id)
WHERE id > 0;





SELECT mr.*, p.name AS patient_name
FROM medical_records mr
JOIN patient p ON mr.patient_id = p.patient_id;









ALTER TABLE medical_records
DROP COLUMN patient_name;
