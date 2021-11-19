# concurrency
docker network create concurrency-network

docker run --net concurrency-network --name concurrency-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mdymen_pass -d mysql:latest

sleep 10

docker exec -i concurrency-mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < /mysql/data.sql

docker run --name redis -d -p 6379:6379 redis

sleep 10

docker run --net concurrency-network -p 8081:8080 --name concurrency -e MYSQL_HOST=<<MYSQL_HOST>> -e REDIS_HOST=<<REDIS_HOST>> -d mdymen85/concurrency:latest
