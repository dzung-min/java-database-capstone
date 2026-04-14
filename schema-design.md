# Schema Design

## MySQL Database Design

### Table: patients
- id: INT, Primary Key, Auto Increment
- name: VARCHAR, Not Null
- email: VARCHAR, Not Null, Unique
- password: VARCHAR, Not Null
- phone: VARCHAR, Not Null
- address: VARCHAR, Not Null

## Table: doctors
- id: INT, Primary Key, Auto Increment
- name: VARCHAR, Not Null
- email: VARCHAR, Not Null, Unique
- password: VARCHAR, Not Null
- specialty: VARCHAR, Not Null
- phone: VARCHAR, Not Null
- address: VARCHAR, Not Null
- availableTimes 

### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: admin
- id: INT, Primary Key, Auto Increment
- name: VARCHAR, Not Null
- password: VARCHAR, Not Null

## MongoDB Collection Design

