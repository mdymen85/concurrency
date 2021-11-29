# Concurrency

## Problem

Is not a strange situation in microservice environment to need to lock some code that lives in many instances. 

## My solution

This project aims to run an application with **redis** for distributed lock, in aws using **IaC** with **terraform**. The terraform file ***terraform-ec2.tf*** executes in an AWS environment, and creates an Auto-Scalling Group with two instances by default **EC2**. For run the terraform file, must config de connection to AWS adding the **AWS_ACCESS_KEY** and **AWS_SECRET_ACCESS_KEY**, and run this commands:

```
terraform init
terraform apply
```

And for destroy the infraestructure:

```
terraform destroy
```


## Entities

There are two classes, **Client** that represents the checking accounts with an attribute that saves the balance. And every time a new transaction arrives to that account, updates the account balance. Transactinons are saved in the table **Transaction**, so when it comes one transaction, the account must be locked to avoid concurrency problems updating the balance account.

The request in the API is like the following curl:

```
curl --location --request POST 'localhost:8081/api/v1/transaction' \
--header 'Content-Type: application/json' \
--data-raw '{
    "account": "00454",
    "type": "CREDIT",
    "deal": "1"
}'
```

## Distributed Lock

I use **Redisson** library for locking

```
		<dependency>
		    <groupId>org.redisson</groupId>
		    <artifactId>redisson</artifactId>
		    <version>3.16.4</version>
		</dependency>
```

I developed a class called **RedissonLockManager** that receives a consumer who will execute the method that need the lock. After that, it will be executed the transaction method that saves the transaction and update the balance. After all this operation ends, the locked it will be released, as it shows in the next image:

![](https://github.com/mdymen85/concurrency/blob/main/diagram2.png)

## Infraestructure

As i wrote above, the terraform file will create an ELB with and a Auto-Scalling Group two instances as default an instance for the mysql database, and an instance for redis, as the image shows below:

![](https://github.com/mdymen85/concurrency/blob/main/diagram1.png)


In all instances, it will be created docker images to host the containers.

EC2 instances will **pull images for my docker hub** tu run de application. Just need to configure the MYSQL_HOST and the REDS_HOST.

```
sudo docker run -p 8081:8080 --name concurrency -e MYSQL_HOST=172.31.16.7 -e REDIS_HOST=172.31.16.8 -d mdymen85/concurrency:latest 
```

For run the **mysql database**, will run the following command.
```
sudo docker run --name concurrency-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mdymen_pass -d mysql:latest
sudo docker exec -i concurrency-mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < /data_concurrency.sql  
```

For run **redis**, it will create the container and redis image.
```
sudo docker run --name redis -d -p 6379:6379 redis

## Testing

I tested the implementation using **Jmeter**. This allowed me to run 15 threads simultaneously, with 1000 requests to the same account. The goal was to request to the ELB, and this instance send the request between the instances in the Auto-Scalling Group. At the end of the process, the number of transaction and the balance value was perfect! This meant that the lock worked! Then i disabled the lock, and the system had a strange behaivor, the account balance and transaction numbers were incorrect.
```
