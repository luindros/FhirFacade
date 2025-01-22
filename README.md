FHIR Facade for data stored in a PostgreSQL database organized in columns and rows. It retrieves data from the database and transforms it into FHIR-compliant resources.

<img width="173" alt="image" src="https://github.com/user-attachments/assets/9bb57def-2367-4f06-87d8-6705889dcef0" />


<img width="233" alt="image" src="https://github.com/user-attachments/assets/ec1cc0a0-fe21-4ee3-9465-d9c5e9ff0e3c" />


Run Postgres DB in docker:
1) docker compose up
2) psql -h localhost -U postgres
3) Password: postgres
4) Run command by command the seed_databased.sql
  <img width="354" alt="image" src="https://github.com/user-attachments/assets/b62f9037-20d4-4d9e-bf7b-48135c49b2c2" />

5) Make sure you have data in the DB running some select statements:
<img width="245" alt="image" src="https://github.com/user-attachments/assets/5e48a141-f27b-49fb-93f2-8829ce901617" 

6) mvn clean install
<img width="533" alt="image" src="https://github.com/user-attachments/assets/0c370c08-c417-4b97-803a-c4aeecc236b6" />

   
7) java -jar .\target\fhir-bootcamp-1.0-SNAPSHOT.jar

   <img width="700" alt="image" src="https://github.com/user-attachments/assets/51a94704-5b86-4157-a203-00685ff92182" />

8) Query the data using your browser or any other rest client like Postman or Insomnia:

    
   <img width="342" alt="image" src="https://github.com/user-attachments/assets/6aee90f9-8aa0-45dc-b448-9db18cffd43c" />
   

   <img width="365" alt="image" src="https://github.com/user-attachments/assets/f27ba1c2-6a58-4326-81a4-a77b8a76a7d7" />
   

   <img width="519" alt="image" src="https://github.com/user-attachments/assets/c574eac2-2efa-410a-a4a0-2b6468480385" />
   

   <img width="562" alt="image" src="https://github.com/user-attachments/assets/3c51805b-0103-47f6-84a2-7ec532c90f78" />




