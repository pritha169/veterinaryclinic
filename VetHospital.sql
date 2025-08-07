create database hospital;
use hospital;

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

insert into admin(
admin_id, username,name, age, phone,city,email,gender,password) values("558574","admin","Pritha","22","01234567891","Narayanganj","pritha@gmail.com","female","admin");

SELECT * FROM admin WHERE username='admin' AND password='admin'

CREATE TABLE doctor (
    doctor_id VARCHAR(6) PRIMARY KEY,
    name VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    specialization VARCHAR(50),
    phone VARCHAR(11),
    email VARCHAR(50),
    city VARCHAR(50)
);

INSERT INTO doctor (doctor_id, name, age, gender, specialization, phone, email, city)
VALUES 
('D001', 'Dr. Ayesha Rahman', '35', 'Female', 'Cardiologist', '01712345678', 'ayesha@example.com', 'Dhaka'),
('D002', 'Dr. Tanvir Hossain', '40', 'Male', 'Dermatologist', '01987654321', 'tanvir@example.com', 'Chittagong'),
('D003', 'Dr. Nusrat Jahan', '30', 'Female', 'Pediatrician', '01811223344', 'nusrat@example.com', 'Khulna');


select * from doctor;

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

CREATE TABLE appointment (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id VARCHAR(6),
    doctor_id VARCHAR(6),
    appointment_date DATE,
    appointment_time TIME,  -- new, for precise time
    is_emergency BOOLEAN DEFAULT FALSE,
    pet_category VARCHAR(50),
    type VARCHAR(20),
    reminder BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);

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

CREATE TABLE Orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id VARCHAR(50),
    order_date DATE,
    total_amount DECIMAL(10, 2),
    status VARCHAR(20)
);
select *from orders;
CREATE TABLE Order_Items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    medicine_name VARCHAR(100),
    quantity INT,
    price DECIMAL(10, 2),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);
select *from Order_Items;





ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Sumit123!';
FLUSH PRIVILEGES;

SELECT user, host, plugin FROM mysql.user WHERE user='root';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'Sumit123!';
FLUSH PRIVILEGES;
