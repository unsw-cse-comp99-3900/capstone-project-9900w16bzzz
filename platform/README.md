## How to use init.sql to init local database

@author Zuoming yan @date 2024/07/01

#### 1. startup docker container mysql

Start the mysql container with the following command: 

```
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=admin123 -e MYSQL_DATABASE=mydb -e MYSQL_USER=myuser -e MYSQL_PASSWORD=admin123 -p 3306:3306 -d mysql:latest

```

When the docker mysql container starts normally you can see the following:

```
(base) yanzuoming@Johns-MacBook-Pro ~ % docker ps
CONTAINER ID   IMAGE     COMMAND                  CREATED       STATUS         PORTS                               NAMES
3fb55d0ab501   mysql     "docker-entrypoint.s…"   11 days ago   Up 5 seconds   0.0.0.0:3306->3306/tcp, 33060/tcp   mysql-server
```

Ensure that when 3306 -> 3306 exists in the PORTS column, this proves that the container's internal port is successfully mapped to the local port and the service can be accessed via localhost:3306.

#### 2. Copy local files inside the container

```
docker cp your_init_sql_file_path your_container_name:/init.sql
```

Above that `your_init_sql_file_path` fill in the absolute path to the V1__init_database.sql file, and fill in your container names in the `your_container_name` position, which is in the last column of the docker ps display

#### 3. Go inside the container

```
(base) yanzuoming@Johns-MacBook-Pro ~ % docker exec -it mysql-server bash
bash-5.1# mysql -u root -p
Enter password: 
```

and then enter the password 'admin123'

```
Enter password: 
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 12
Server version: 8.4.0 MySQL Community Server - GPL

Copyright (c) 2000, 2024, Oracle and/or its affiliates.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> 


```

then execute: 

```
mysql> source init.sql
Query OK, 4 rows affected (0.10 sec)

Query OK, 1 row affected (0.01 sec)

Database changed
Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected, 1 warning (0.00 sec)

Query OK, 0 rows affected, 3 warnings (0.01 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected, 1 warning (0.00 sec)

Query OK, 0 rows affected, 5 warnings (0.01 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected, 1 warning (0.00 sec)

Query OK, 0 rows affected, 1 warning (0.02 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

Query OK, 0 rows affected (0.00 sec)

```

If you see the above output, the database has been initialized successfully, and you can start the platform service after you have configured the idea project environment!
