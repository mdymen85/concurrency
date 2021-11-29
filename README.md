
### Introduction: Concurrency

This project aims to run an application with **redis** for distributed lock, in aws using **IaC** with **terraform**. The terraform file executes in an AWS environment, and creates an Auto-Scalling Group with two instances by default **EC2**. 

### Entities

Exist two classes, **Client** that represents the checking accounts with an attribute that saves the balance. And every time a new transaction arrives to that account, updates the account balance. Transactinons are saved in the table **Transaction**, so when it comes one transaction, the account must be locked to avoid concurrency problems.

### Distributed Lock

I use **Redisson** library for locking

```
		<dependency>
		    <groupId>org.redisson</groupId>
		    <artifactId>redisson</artifactId>
		    <version>3.16.4</version>
		</dependency>
```

I developed a class called **RedissonLockManager** that receives a consumer that executes the method that need the lock. In this case i  



docker network create concurrency-network

docker run --net concurrency-network --name concurrency-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mdymen_pass -d mysql:latest

sleep 10

docker exec -i concurrency-mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < /mysql/data.sql

docker run --name redis -d -p 6379:6379 redis

sleep 10

docker run --net concurrency-network -p 8081:8080 --name concurrency -e MYSQL_HOST=<<MYSQL_HOST>> -e REDIS_HOST=<<REDIS_HOST>> -d mdymen85/concurrency:latest
