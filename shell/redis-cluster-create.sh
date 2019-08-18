#!/bin/bash
for port in `seq 7001 7006`; do 
  docker run --net=host --name=redis-${port} -p ${port}:${port} -v /home/kixs/docker-space/redis/${port}/conf/redis.conf:/usr/local/etc/redis/redis.conf -v /home/kixs/docker-space/redis/${port}/data:/data -d redis:latest redis-server /usr/local/etc/redis/redis.conf;
done
