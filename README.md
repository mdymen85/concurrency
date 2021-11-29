
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

I developed a class called **RedissonLockManager** that receives a consumer who will execute the method that need the lock. 


![](https://github.com/mdymen85/concurrency/blob/main/diagram1.png)

![](https://github.com/mdymen85/concurrency/blob/main/diagram2.png)

```
curl --location --request POST 'localhost:8081/api/v1/transaction' \
--header 'Content-Type: application/json' \
--data-raw '{
    "account": "00454",
    "type": "CREDIT",
    "deal": "1"
}'
```

```
sudo docker run -p 8081:8080 --name concurrency -e MYSQL_HOST=172.31.16.7 -e REDIS_HOST=172.31.16.8 -d mdymen85/concurrency:latest 
```

```
sudo docker run --name concurrency-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mdymen_pass -d mysql:latest
sudo docker exec -i concurrency-mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < /data_concurrency.sql  
```

```
sudo docker run --name redis -d -p 6379:6379 redis
```
