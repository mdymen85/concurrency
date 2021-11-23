#!/bin/bash

container=0

until [ $container == healthy ]
do
container=$(docker inspect --format='{{.State.Health.Status}}' concurrency)
echo "${container}"
sleep 5
done


