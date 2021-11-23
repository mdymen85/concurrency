provider "aws" {
    region = "us-east-1"
}

#variable "ec2_names" {
#    type = list(string)
#    default = ["nginx","server1","server2","database","redis"]
#}

locals {

     ec2_instances_servers = [
       {
            name = "server1",
            ip_address = "172.31.16.5",
            depends_on = [],
            script = <<-EOF
                        #!/bin/bash
                        sudo apt-get update 
                        sudo apt install docker -y       
                        curl -sSL https://get.docker.com/ | sh 
                        sudo service docker start 
                        sudo usermod -a -G docker ubuntu                        
                        sudo docker run -p 8081:8080 --name concurrency -e MYSQL_HOST=172.31.16.7 -e REDIS_HOST=172.31.16.8 -d mdymen85/concurrency:latest                        
                     EOF      
       },
       {
            name = "server2",
            ip_address = "172.31.16.6",
            script = <<-EOF
                        #!/bin/bash                     
                        sudo apt-get update 
                        sudo apt install docker -y       
                        curl -sSL https://get.docker.com/ | sh 
                        sudo service docker start 
                        sudo usermod -a -G docker ubuntu                        
                        sudo docker run -p 8081:8080 --name concurrency -e MYSQL_HOST=172.31.16.7 -e REDIS_HOST=172.31.16.8 -d mdymen85/concurrency:latest                        
                     EOF           
       }
     ]
     ec2_instances = [
       {
            name = "nginx",
            ip_address = "172.31.16.4",
            depends_on = [],
            script = <<-EOF
                        #!/bin/bash
                        sudo apt-get update 
                        sudo apt install docker -y       
                        curl -sSL https://get.docker.com/ | sh 
                        sudo service docker start 
                        sudo usermod -a -G docker ubuntu
                     EOF
            
       },
       {
            name = "database",
            ip_address = "172.31.16.7",
            script = <<-EOF
                        #!/bin/bash
                        wget https://mdymen-cloudformation-bucket.s3.amazonaws.com/data_concurrency.sql
                        sudo apt-get update 
                        sudo apt install docker -y       
                        curl -sSL https://get.docker.com/ | sh 
                        sudo service docker start 
                        sudo usermod -a -G docker ubuntu
                        sudo docker run --name concurrency-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mdymen_pass -d mysql:latest
                        sleep 30
                        sudo docker exec -i concurrency-mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < /data_concurrency.sql                           
                     EOF      
       },
       {
            name = "redis",
            ip_address = "172.31.16.8",
            script = <<-EOF
                        #!/bin/bash
                        sudo apt-get update 
                        sudo apt install docker -y       
                        curl -sSL https://get.docker.com/ | sh 
                        sudo service docker start 
                        sudo usermod -a -G docker ubuntu
                        sudo docker run --name redis -d -p 6379:6379 redis
                     EOF            
       }                     
     ]

}

resource "aws_security_group" "terraform_segurity_group" {
     vpc_id = "vpc-06540f5331e97d85a"
     ingress {
       description      = "SSH"
       from_port        = 22
       to_port          = 22
       protocol         = "tcp"
       cidr_blocks      = ["0.0.0.0/0"]
     }   
     ingress {
       description      = "8081"
       from_port        = 8081
       to_port          = 8081
       protocol         = "tcp"
       cidr_blocks      = ["0.0.0.0/0"]
     }  
     ingress {
       description      = "8080"
       from_port        = 8080
       to_port          = 8080
       protocol         = "tcp"
       cidr_blocks      = ["0.0.0.0/0"]
     }  
     ingress {
       description      = "mysql"
       from_port        = 3306
       to_port          = 3306
       protocol         = "tcp"
       cidr_blocks      = ["0.0.0.0/0"]
     }             
     ingress {
       description      = "redis"
       from_port        = 6379
       to_port          = 6379
       protocol         = "tcp"
       cidr_blocks      = ["0.0.0.0/0"]
     }       
     egress {
       from_port        = 0
       to_port          = 0
       protocol         = "-1"
       cidr_blocks      = ["0.0.0.0/0"]
       ipv6_cidr_blocks = ["::/0"]
     }                   
     tags = {
        Name = "security_group_tf"
     }
}

resource "aws_instance" "terraform_ec2_servers" {
    #for_each = toset(var.ec2_names)
    for_each = {
       for index, vm in local.ec2_instances_servers:
       index => vm
    }
    
    ami = "ami-083654bd07b5da81d"
    instance_type = "t2.micro"
    private_ip = each.value.ip_address
    vpc_security_group_ids = [
         aws_security_group.terraform_segurity_group.id
    ]
 #   provisioner "file" {
 #        source = "install-docker.sh"
 #        destination = "/tmp/install-docker.sh"
 #   }
    key_name   = "mdymen"    
    subnet_id = "subnet-056ceb1856e2d326e"
    tags = {
        Name = each.value.name
    }
    depends_on = [aws_instance.terraform_ec2_example]
    user_data = each.value.script  
}

resource "aws_instance" "terraform_ec2_example" {
    #for_each = toset(var.ec2_names)
    for_each = {
       for index, vm in local.ec2_instances:
       index => vm
    }
    
    ami = "ami-083654bd07b5da81d"
    instance_type = "t2.micro"
    private_ip = each.value.ip_address
    vpc_security_group_ids = [
         aws_security_group.terraform_segurity_group.id
    ]
 #   provisioner "file" {
 #        source = "install-docker.sh"
 #        destination = "/tmp/install-docker.sh"
 #   }
    key_name   = "mdymen"    
    subnet_id = "subnet-056ceb1856e2d326e"
    tags = {
        Name = each.value.name
    }
  	
    user_data = each.value.script  
}

resource "aws_elb" "elb" {
  name               = "terraform-elb"
  availability_zones = ["us-east-1a", "us-east-1b", "us-east-1c", "us-east-1d", "us-east-1e", "us-east-1f"]

  listener {
    instance_port     = 8001
    instance_protocol = "http"
    lb_port           = 8081
    lb_protocol       = "http"
  }

  health_check {
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 3
    target              = "HTTP:8081/health"
    interval            = 30
  }

  instances                   = [aws_instance.terraform_ec2_servers.id]
#  cross_zone_load_balancing   = true
#  idle_timeout                = 400
#  connection_draining         = true
#  connection_draining_timeout = 400

  tags = {
    Name = "terraform-elb"
  }
}


