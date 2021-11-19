 sudo apt-get update
 sudo apt install docker -y      
 curl -sSL https://get.docker.com/ | sh
 sudo service docker start
 sudo usermod -a -G docker ubuntu
